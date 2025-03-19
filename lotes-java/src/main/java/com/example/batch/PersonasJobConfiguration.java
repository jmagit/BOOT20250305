package com.example.batch;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.models.Persona;
import com.example.models.PersonaDTO;
import com.example.models.PhotoDTO;
import com.thoughtworks.xstream.security.AnyTypePermission;

@Configuration
public class PersonasJobConfiguration {
	@Autowired
	JobRepository jobRepository;
	@Autowired
	PlatformTransactionManager transactionManager;

	// CSV to DB

	public FlatFileItemReader<PersonaDTO> personaCSVItemReader(String fname) {
		return new FlatFileItemReaderBuilder<PersonaDTO>()
				.name("personaCSVItemReader")
				.resource(new FileSystemResource(fname))
				.linesToSkip(1)
				.delimited()
				.names(new String[] { "id", "nombre", "apellidos", "correo", "sexo", "ip" })
				.fieldSetMapper(new BeanWrapperFieldSetMapper<PersonaDTO>() { {
						setTargetType(PersonaDTO.class);
					}})
				.build();
	}

	@Autowired
	public PersonaItemProcessor personaItemProcessor;

	@Bean
    @DependsOnDatabaseInitialization
	JdbcBatchItemWriter<Persona> personaDBItemWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Persona>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO personas VALUES (:id,:nombre,:correo,:ip)")
				.dataSource(dataSource)
				.build();
	}

	Step importCSV2DBStep(int index, String file, JdbcBatchItemWriter<Persona> toDB) {
		return new StepBuilder("importCSV2DBStep" + index, jobRepository)
				.<PersonaDTO, Persona>chunk(10, transactionManager)
				.reader(personaCSVItemReader(file))
				.processor(personaItemProcessor)
				.writer(toDB)
				.build();
	}

	// BD to CSV

	@Bean
	JdbcCursorItemReader<Persona> personaDBItemReader(DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<Persona>().name("personaDBItemReader")
				.sql("SELECT id, nombre, correo, ip FROM personas")
				.dataSource(dataSource)
				.rowMapper(new BeanPropertyRowMapper<>(Persona.class))
				.build();
	}

    @Bean
    FlatFileItemWriter<Persona> personaCSVItemWriter() {
		return new FlatFileItemWriterBuilder<Persona>().name("personaCSVItemWriter")
				.resource(new FileSystemResource("output/outputData.csv"))
				.lineAggregator(new DelimitedLineAggregator<Persona>() {
					{ // anonymous class constructor
						setDelimiter(",");
						setFieldExtractor(new BeanWrapperFieldExtractor<Persona>() {
							{ // anonymous class constructor
								setNames(new String[] { "id", "nombre", "correo", "ip" });
							}
						});
					}
				}).build();
	}

    @Bean
    Step exportDB2CSVStep(JdbcCursorItemReader<Persona> personaDBItemReader) {
		return new StepBuilder("exportDB2CSVStep", jobRepository)
				.<Persona, Persona>chunk(100, transactionManager)
				.reader(personaDBItemReader)
				.writer(personaCSVItemWriter())
				.build();
	}
    
	// XML a DB
	
	public StaxEventItemReader<PersonaDTO> personaXMLItemReader() {
		XStreamMarshaller marshaller = new XStreamMarshaller();
		Map<String, Class> aliases = new HashMap<>();
		aliases.put("Persona", PersonaDTO.class);
		marshaller.setAliases(aliases);
		marshaller.setTypePermissions(AnyTypePermission.ANY);
		return new StaxEventItemReaderBuilder<PersonaDTO>()
				.name("personaXMLItemReader")
				.resource(new FileSystemResource("input/Personas.xml"))
				.addFragmentRootElements("Persona")
				.unmarshaller(marshaller).build();
	}	
	
	@Bean
	Step importXML2DBStep1(JdbcBatchItemWriter<Persona> personaDBItemWriter) {
		return new StepBuilder("importXML2DBStep1", jobRepository)
				.<PersonaDTO, Persona>chunk(10, transactionManager)
				.reader(personaXMLItemReader())
				.processor(personaItemProcessor)
				.writer(personaDBItemWriter).build();
	}

	// DB a XML
	
	public StaxEventItemWriter<Persona> personaXMLItemWriter() {
		XStreamMarshaller marshaller = new XStreamMarshaller();
		Map<String, Class> aliases = new HashMap<>();
		aliases.put("Persona", Persona.class);
		marshaller.setAliases(aliases);
		return new StaxEventItemWriterBuilder<Persona>()
				.name("personaXMLItemWriter")
				.resource(new FileSystemResource("output/outputData.xml"))
				.marshaller(marshaller)
				.rootTagName("Personas")
				.overwriteOutput(true)
				.build();
	}
	
	@Bean
	Step exportDB2XMLStep(JdbcCursorItemReader<Persona> personaDBItemReader) {
		return new StepBuilder("exportDB2XMLStep", jobRepository)
				.<Persona, Persona>chunk(100, transactionManager)
				.reader(personaDBItemReader)
				.writer(personaXMLItemWriter())
				.build();
	}
	
	

    // Tasklet
    
    @Bean
    FTPLoadTasklet ftpLoadTasklet(@Value("${input.dir.name:./ftp}") String dir) {
		FTPLoadTasklet tasklet = new FTPLoadTasklet();
		tasklet.setDirectoryResource(new FileSystemResource(dir));
		return tasklet;
	}

    @Bean
    Step copyFilesInDir(FTPLoadTasklet ftpLoadTasklet) {
	        return new StepBuilder("copyFilesInDir", jobRepository)
	            .tasklet(ftpLoadTasklet, transactionManager)
	            .build();
	}
	

	
	// Job
	
//	@Bean
	public Job personasJob(PersonasJobListener listener, JdbcBatchItemWriter<Persona> personaDBItemWriter, Step exportDB2CSVStep,
			Step importXML2DBStep1, Step exportDB2XMLStep, 
			Step copyFilesInDir) {
//		return new JobBuilder("personasJob", jobRepository)
//				.incrementer(new RunIdIncrementer())
//				.listener(listener)
//				.start(importCSV2DBStep(1, "input/personas-1.csv", personaDBItemWriter)) // CSV to DB
//				.next(importXML2DBStep1) // XML to DB
//				.next(exportDB2CSVStep) // DB to CSV
//				.next(exportDB2XMLStep) // DB to XML
//				.build();
		var job = new JobBuilder("personasJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(copyFilesInDir);
		for(int i = 1; i <= 3; i++)
			job = job.next(importCSV2DBStep(i, "input/personas-" + i +".csv", personaDBItemWriter));
		job = job.next(exportDB2CSVStep);
		return job.build();
	}

	// Custom ItemStream

	@Bean
	Job photoJob(PhotoRestItemReader photoRestItemReader, JdbcCursorItemReader<Persona> personaDBItemReader) {
		String[] headers = new String[] { "id", "author", "width", "height", "url", "download_url" };
		
		return new JobBuilder("photoJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(new StepBuilder("photoStep1", jobRepository)
						.<PhotoDTO, PhotoDTO>chunk(100, transactionManager)
						.reader(photoRestItemReader)
						.writer(new FlatFileItemWriterBuilder<PhotoDTO>().name("photoCSVItemWriter")
								.resource(new FileSystemResource("output/photoData.csv"))
								.headerCallback(new FlatFileHeaderCallback() {
									public void writeHeader(Writer writer) throws IOException {
										writer.write(String.join(",", headers));
									}
								}).lineAggregator(new DelimitedLineAggregator<PhotoDTO>() {
									{
										setDelimiter(",");
										setFieldExtractor(new BeanWrapperFieldExtractor<PhotoDTO>() {
											{
												setNames(headers);
											}
										});
									}
								}).build())
						.build())
				.build();
	}

}
