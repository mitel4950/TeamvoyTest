package com.example.teamvoytest.config;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  private static final Map<Class<?>, Logger> LOGGERS = new HashMap<>();


  @Around("execution(public * com.example.teamvoytest.controller..*(..)) || execution(public * com.example.teamvoytest.domain.repository..*(..))")
  public Object logControllersAndRepositoriesCall(ProceedingJoinPoint joinPoint) throws Throwable {
    Logger logger = getLogger(joinPoint);

    logMethodNameAndParams(logger, joinPoint);
    Object res = joinPoint.proceed();
    if (res != null) {
      logMethodNameAndReturnValue(res, logger, joinPoint);
    }
    return res;
  }


  @Around("execution(public * com.example.teamvoytest..*(..)) && !execution(public * com.example.teamvoytest.controller..*(..)) && !execution(public * com.example.teamvoytest.domain.repository..*(..))")
  public Object logMethodsCall(ProceedingJoinPoint joinPoint) throws Throwable {
    Logger logger = getLogger(joinPoint);

    logMethodNameAndParams(logger, joinPoint);
    return joinPoint.proceed();
  }


  private Logger getLogger(ProceedingJoinPoint joinPoint) {
    Class<?> clazz = joinPoint.getTarget().getClass();
    return LOGGERS.computeIfAbsent(clazz, LoggerFactory::getLogger);
  }

  private void logMethodNameAndParams(Logger logger, ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();

    String[] parameterNames = signature.getParameterNames();
    Object[] args = joinPoint.getArgs();
    String params = IntStream.range(0, args.length)
        .mapToObj(i -> parameterNames[i] + "=" + args[i])
        .collect(Collectors.joining(",\n\t\t\t"));

    if (!params.isEmpty()) {
      params = "\n\t\t\t" + params + "\n\t\t\t";
    }

    logger.info("{}.{}({})",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                params);
  }

  private void logMethodNameAndReturnValue(Object res,
                                           Logger logger,
                                           ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();

    logger.info("{}.{} = {}",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                res);
  }

}
