package com.example.domains.event;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EntityChangedEventAspect {
	Logger log = LoggerFactory.getLogger(EntityChangedEventAspect.class);
	
	@Autowired
	private ApplicationEventPublisher publisher;

	private void publish(@NonNull EntityChangedEvent event) {
		publisher.publishEvent(event);
	}
	@Pointcut("execution(* com.example.domains.contracts.repositories..*.*(..))")
	public void repositorios() {}

	@Pointcut("execution(* com.example.domains.services..*.*(..))")
	public void servicios() {}
	
	@Pointcut("this(com.example.domains.core.contracts.services.DomainService)") 
	public void domainService() {}

	@Pointcut("execution(* *.add(..))")
	public void metodosAdd() {}

	@Pointcut("execution(* *.modify(..))")
	public void metodosModify() {}

	@Pointcut("execution(* *.delete(..))")
	public void metodosDelete() {}

	@Pointcut("execution(* *.deleteById(..))")
	public void metodosDeleteById() {}

	@AfterReturning(pointcut="domainService() && metodosAdd()", returning="retVal")
	public void add(JoinPoint jp, Object retVal) {
		var event = EntityChangedEvent.asAdd(retVal);
		publish(event);
		log.warn("Raise " + event);
	}
	
	@AfterReturning(pointcut="domainService() && metodosModify()", returning="retVal")
	public void modify(JoinPoint jp, Object retVal) {
		var event = EntityChangedEvent.asModify(retVal);
		publish(event);
		log.warn("Raise " + event);
	}
	
	@After("domainService() && metodosDelete()")
	public void delete(JoinPoint jp, Object retVal) {
		var event = EntityChangedEvent.asRemove(jp.getArgs()[0]);
		publish(event);
		log.warn("Raise " + event);
	}

	@After("(domainService() && metodosDeleteById()) || @annotation(com.example.domains.event.EmitEntityDeleted)")
	public void deleteById(JoinPoint jp) {
		var name = jp.getTarget().getClass().getSimpleName().replace("ServiceImpl", "");
		var anotacion = jp.getTarget().getClass().getAnnotation(EntityName.class);
		if(anotacion != null)
			name = anotacion.name();
		var metodo = BeanUtils.resolveSignature(jp.getSignature().getName() + jp.getSignature().toLongString().split(jp.getSignature().getName())[1], jp.getTarget().getClass());
		if(metodo != null && metodo.getAnnotation(EmitEntityDeleted.class) != null)
			name = metodo.getAnnotation(EmitEntityDeleted.class).entityName();
		var event = EntityChangedEvent.asRemove(name, jp.getArgs()[0]);
		publish(event);
		log.warn("Raise " + event);
	}
}