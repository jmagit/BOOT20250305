package com.example.domains.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;

import com.example.domains.core.entities.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * The persistent class for the film database table.
 * 
 */
@Entity
@Table(name = "film")
@NamedQuery(name = "Film.findAll", query = "SELECT f FROM Film f")
public class Film extends AbstractEntity<Film> implements Serializable {
	private static final long serialVersionUID = 1L;

	public static enum Rating {
		GENERAL_AUDIENCES("G"), PARENTAL_GUIDANCE_SUGGESTED("PG"), PARENTS_STRONGLY_CAUTIONED("PG-13"), RESTRICTED("R"),
		ADULTS_ONLY("NC-17");

		String value;

		Rating(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static Rating getEnum(String value) {
			switch (value) {
			case "G":
				return Rating.GENERAL_AUDIENCES;
			case "PG":
				return Rating.PARENTAL_GUIDANCE_SUGGESTED;
			case "PG-13":
				return Rating.PARENTS_STRONGLY_CAUTIONED;
			case "R":
				return Rating.RESTRICTED;
			case "NC-17":
				return Rating.ADULTS_ONLY;
			case "":
				return null;
			default:
				throw new IllegalArgumentException("Unexpected value: " + value);
			}
		}

		public static final String[] VALUES = { "G", "PG", "PG-13", "R", "NC-17" };
	}
	
	public static enum SpecialFeature {
	    Trailers("Trailers"),
	    Commentaries("Commentaries"),
	    DeletedScenes("Deleted Scenes"),
	    BehindTheScenes("Behind the Scenes");

	    String value;

	    SpecialFeature(String value) {
	        this.value = value;
	    }

		public String getValue() {
			return value;
		}

	    public static SpecialFeature getEnum(String specialFeature) {
	        return Stream.of(SpecialFeature.values())
	                .filter(p -> p.getValue().equals(specialFeature))
	                .findFirst()
	                .orElseThrow(IllegalArgumentException::new);
	    }
	}
	
	@Converter
	private static class RatingConverter implements AttributeConverter<Rating, String> {
		@Override
		public String convertToDatabaseColumn(Rating rating) {
			return rating == null ? null : rating.getValue();
		}

		@Override
		public Rating convertToEntityAttribute(String value) {
			return value == null ? null : Rating.getEnum(value);
		}
	}
	
	@Converter
	private static class SpecialFeatureConverter implements AttributeConverter<Set<SpecialFeature>, String> {
	    @Override
	    public String convertToDatabaseColumn(Set<SpecialFeature> attribute) {
	        if (attribute == null || attribute.size() == 0) {
	            return null;
	        }
	        return attribute.stream()
	                .map(SpecialFeature::getValue)
	                .collect(Collectors.joining(","));
	    }

	    @Override
	    public Set<SpecialFeature> convertToEntityAttribute(String value) {
	        if (value == null) {
	            return EnumSet.noneOf(SpecialFeature.class);
	        }
	        return Arrays.stream(value.split(","))
	                .map(SpecialFeature::getEnum)
	                .collect(Collectors.toCollection(() -> EnumSet.noneOf(SpecialFeature.class)));
	    }
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "film_id", unique = true, nullable = false)
	private int filmId;

	@Lob
	private String description;

	@Column(name = "last_update", insertable = false, updatable = false, nullable = false)
	private Date lastUpdate;

	@Positive
	private Integer length;

	@Convert(converter = RatingConverter.class)
	private Rating rating;

	// @Temporal(TemporalType.DATE)
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
	@Min(1901)
	@Max(2155)
	@Column(name = "release_year")
	private Short releaseYear;

	@NotNull
	@Positive
	@Column(name = "rental_duration", nullable = false)
	private byte rentalDuration;

	@NotNull
	@Digits(integer = 2, fraction = 2)
	@DecimalMin(value = "0.0", inclusive = false)
	@Column(name = "rental_rate", nullable = false, precision = 10, scale = 2)
	private BigDecimal rentalRate;

	@NotNull
	@Digits(integer = 3, fraction = 2)
	@DecimalMin(value = "0.0", inclusive = false)
	@Column(name = "replacement_cost", nullable = false, precision = 10, scale = 2)
	private BigDecimal replacementCost;

	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String title;
	
	@Column(name = "special_features")
	@Convert(converter = SpecialFeatureConverter.class)
	private Set<SpecialFeature> specialFeatures = EnumSet.noneOf(SpecialFeature.class);

	// bi-directional many-to-one association to Language
	@ManyToOne
	@JoinColumn(name = "language_id")
	@NotNull
	@JsonManagedReference
	private Language language;

	// bi-directional many-to-one association to Language
	@ManyToOne
	@JoinColumn(name = "original_language_id")
	@JsonManagedReference
	private Language languageVO;

	// bi-directional many-to-one association to FilmActor
	@OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonBackReference
	private List<FilmActor> filmActors = new ArrayList<FilmActor>();

	// bi-directional many-to-one association to FilmCategory
	@OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonBackReference
	private List<FilmCategory> filmCategories = new ArrayList<FilmCategory>();

	public Film() {
	}

	public Film(int filmId) {
		this.filmId = filmId;
	}

	public Film(@NotBlank @Size(max = 128) String title, @NotNull Language language, @Positive byte rentalDuration,
			@Positive @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 2, fraction = 2) BigDecimal rentalRate,
			@DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 3, fraction = 2) BigDecimal replacementCost) {
		super();
		this.title = title;
		this.language = language;
		this.rentalDuration = rentalDuration;
		this.rentalRate = rentalRate;
		this.replacementCost = replacementCost;
	}

	public Film(int filmId, @NotBlank @Size(max = 128) String title, @NotNull Language language,
			@NotNull @Positive byte rentalDuration,
			@NotNull @Digits(integer = 2, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) BigDecimal rentalRate,
			@NotNull @Digits(integer = 3, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) BigDecimal replacementCost) {
		super();
		this.filmId = filmId;
		this.title = title;
		this.language = language;
		this.rentalDuration = rentalDuration;
		this.rentalRate = rentalRate;
		this.replacementCost = replacementCost;
	}

	public Film(int filmId, @NotBlank @Size(max = 128) String title, String description, @Min(1895) Short releaseYear,
			@NotNull Language language, Language languageVO, @Positive byte rentalDuration,
			@Positive @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 2, fraction = 2) BigDecimal rentalRate,
			@Positive Integer length,
			@DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 3, fraction = 2) BigDecimal replacementCost,
			Rating rating) {
		super();
		this.filmId = filmId;
		this.title = title;
		this.description = description;
		this.releaseYear = releaseYear;
		this.language = language;
		this.languageVO = languageVO;
		this.rentalDuration = rentalDuration;
		this.rentalRate = rentalRate;
		this.length = length;
		this.replacementCost = replacementCost;
		this.rating = rating;
	}

	public int getFilmId() {
		return this.filmId;
	}

	public void setFilmId(int filmId) {
		this.filmId = filmId;
		if (filmActors != null && filmActors.size() > 0)
			filmActors.forEach(item -> {
				if (item.getId().getFilmId() != filmId)
					item.getId().setFilmId(filmId);
			});
		if (filmCategories != null && filmCategories.size() > 0)
			filmCategories.forEach(item -> {
				if (item.getId().getFilmId() != filmId)
					item.getId().setFilmId(filmId);
			});
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Integer getLength() {
		return this.length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Rating getRating() {
		return this.rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public Short getReleaseYear() {
		return this.releaseYear;
	}

	public void setReleaseYear(Short releaseYear) {
		this.releaseYear = releaseYear;
	}

	public byte getRentalDuration() {
		return this.rentalDuration;
	}

	public void setRentalDuration(byte rentalDuration) {
		this.rentalDuration = rentalDuration;
	}

	public BigDecimal getRentalRate() {
		return this.rentalRate;
	}

	public void setRentalRate(BigDecimal rentalRate) {
		this.rentalRate = rentalRate;
	}

	public BigDecimal getReplacementCost() {
		return this.replacementCost;
	}

	public void setReplacementCost(BigDecimal replacementCost) {
		this.replacementCost = replacementCost;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Language getLanguageVO() {
		return this.languageVO;
	}

	public void setLanguageVO(Language languageVO) {
		this.languageVO = languageVO;
	}

	// Gestión de actores

	public List<Actor> getActors() {
		return this.filmActors.stream().map(item -> item.getActor()).toList();
	}

	public void setActors(List<Actor> source) {
		if (filmActors == null || !filmActors.isEmpty())
			clearActors();
		source.forEach(item -> addActor(item));
	}

	public void clearActors() {
		filmActors = new ArrayList<FilmActor>();
	}

	public void addActor(Actor actor) {
		FilmActor filmActor = new FilmActor(this, actor);
		filmActors.add(filmActor);
	}

	public void addActor(int actorId) {
		addActor(new Actor(actorId));
	}

	public void removeActor(Actor actor) {
		var filmActor = filmActors.stream().filter(item -> item.getActor().equals(actor)).findFirst();
		if (filmActor.isEmpty())
			return;
		filmActors.remove(filmActor.get());
	}

	public void removeActor(int actorId) {
		removeActor(new Actor(actorId));
	}

	// Gestión de categorias

	public List<Category> getCategories() {
		return this.filmCategories.stream().map(item -> item.getCategory()).toList();
	}

	public void setCategories(List<Category> source) {
		if (filmCategories == null || !filmCategories.isEmpty())
			clearCategories();
		source.forEach(item -> addCategory(item));
	}

	public void clearCategories() {
		filmCategories = new ArrayList<FilmCategory>();
	}

	public void addCategory(Category item) {
		FilmCategory filmCategory = new FilmCategory(this, item);
		filmCategories.add(filmCategory);
	}

	public void addCategory(int id) {
		addCategory(new Category(id));
	}

	public void removeCategory(Category ele) {
		var filmCategory = filmCategories.stream().filter(item -> item.getCategory().equals(ele)).findFirst();
		if (filmCategory.isEmpty())
			return;
		filmCategories.remove(filmCategory.get());
	}

	public void removeCategory(int id) {
		removeCategory(new Category(id));
	}

	// Special Features
	public List<SpecialFeature> getSpecialFeatures() {
		return specialFeatures.stream().toList();
	}

	public void addSpecialFeatures(SpecialFeature specialFeatures) {
		this.specialFeatures.add(specialFeatures);
	}

	public void removeSpecialFeatures(SpecialFeature specialFeatures) {
		this.specialFeatures.remove(specialFeatures);
	}

	@Override
	public int hashCode() {
		return Objects.hash(filmId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Film o)
			return filmId == o.filmId;
		else
			return false;
	}

	@Override
	public String toString() {
		return "Film [filmId=" + filmId + ", title=" + title + ", rentalDuration=" + rentalDuration + ", rentalRate="
				+ rentalRate + ", replacementCost=" + replacementCost + ", lastUpdate=" + lastUpdate + ", description="
				+ description + ", length=" + length + ", rating=" + rating + ", releaseYear=" + releaseYear
				+ ", language=" + language + ", languageVO=" + languageVO + "]";
	}

	public Film merge(Film target) {
//		BeanUtils.copyProperties(this, target, "filmId" , "filmActors", "filmCategories");
		target.title = title;
		target.description = description;
		target.releaseYear = releaseYear;
		target.language = language;
		target.languageVO = languageVO;
		target.rentalDuration = rentalDuration;
		target.rentalRate = rentalRate;
		target.length = length;
		target.replacementCost = replacementCost;
		target.rating = rating;
		target.specialFeatures = EnumSet.copyOf(specialFeatures);
		// Borra los actores que sobran
		target.getActors().stream().filter(item -> !getActors().contains(item))
				.forEach(item -> target.removeActor(item));
		// Añade los actores que faltan
		getActors().stream().filter(item -> !target.getActors().contains(item)).forEach(item -> target.addActor(item));
		// Borra las categorias que sobran
		target.getCategories().stream().filter(item -> !getCategories().contains(item))
				.forEach(item -> target.removeCategory(item));
		// Añade las categorias que faltan
		getCategories().stream().filter(item -> !target.getCategories().contains(item))
				.forEach(item -> target.addCategory(item));
		
		// Bug de Hibernate
		target.filmActors.forEach(o -> o.prePersiste());
		target.filmCategories.forEach(o -> o.prePersiste());
		
		return target;
	}
	
	// Bug de Hibernate
	@PostPersist
	@PostUpdate
	public void prePersiste() {
		System.err.println("prePersiste(): Bug Hibernate");
		filmActors.forEach(o -> o.prePersiste());
		filmCategories.forEach(o -> o.prePersiste());
	}

}