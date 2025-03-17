package com.example.domains.event;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonPointcuts {
	@Pointcut("execution(public * com.example..*.*(..))")
	public void cualquierMetodoPublico() {}

	@Pointcut("this(com.example.domains.contracts.repositories)")
	public void repositorios() {}

	@Pointcut("this(com.example.domains.services)") // execution(* com.example.domains.services..*.modify(..))
	public void servicios() {}

	@Pointcut("execution(* *.add(..))")
	public void metodosAdd() {}

	@Pointcut("execution(* *.modify(..))")
	public void metodosModify() {}

	@Pointcut("execution(* com.example..*.delete(..))")
	public void metodosDelete() {}

	@Pointcut("execution(* com.example..*.deleteById(int))")
	public void metodosDeleteById() {}

	@Pointcut("(metodosAdd() || metodosSave() || metodosDelete()) && (repositorios() || servicios())")
	public void requiredAuthentication() {}

}