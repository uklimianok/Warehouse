package com.warehouse.demo.configuration.aspect;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.Identifiable;
import com.warehouse.demo.event.service.ActionLogEvent;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class ActionLoggingAspect {
    private final KafkaTemplate<String, ActionLogEvent> kafkaTemplate;

    @AfterReturning(
        pointcut = "execution(* com.warehouse.demo.service..*.create(..)) || execution(* com.warehouse.demo.service..*.update(..))", 
        returning = "result"
    )
    public void logSave(JoinPoint joinPoint, Object result) {
        if (result instanceof Identifiable identifiable) {
            ActionLogEvent actionLogEvent = new ActionLogEvent();
            actionLogEvent.setEmployeeNumber(SecurityContextHolder.getContext().getAuthentication().getName());
            actionLogEvent.setProceededAt(LocalDateTime.now());
            actionLogEvent.setEntityId(identifiable.getId());
            actionLogEvent.setEntityType(result.getClass().getSimpleName());
            actionLogEvent.setAction(joinPoint.getSignature().getName());

            kafkaTemplate.send("action-log-event", actionLogEvent);
        }
    }

    @AfterReturning("execution(* com.warehouse.demo.service..*.delete(..))")
    public void logDelete(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] instanceof Long id) {
            ActionLogEvent actionLogEvent = new ActionLogEvent();
            actionLogEvent.setEmployeeNumber(SecurityContextHolder.getContext().getAuthentication().getName());
            actionLogEvent.setProceededAt(LocalDateTime.now());
            actionLogEvent.setEntityId(id);
            actionLogEvent.setEntityType(joinPoint.getTarget().getClass().getSimpleName().replace("ServiceImpl", ""));
            actionLogEvent.setAction(joinPoint.getSignature().getName());

            kafkaTemplate.send("action-log-event", actionLogEvent);
        }
    }
}
