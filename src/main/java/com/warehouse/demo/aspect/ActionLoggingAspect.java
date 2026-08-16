package com.warehouse.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.Identifiable;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.warehouseService.ActionLogService;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class ActionLoggingAspect {
    private final ActionLogService actionLogService;

    @AfterReturning(
        pointcut = "execution(* com.warehouse.demo.service..*.create(..)) || execution(* com.warehouse.demo.service..*.update(..))", 
        returning = "result"
    )
    public void logSave(JoinPoint joinPoint, Object result) {
        if (result instanceof Identifiable identifiable) {
            Employee employee = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getEmployee();
            long entityId = identifiable.getId();
            String entityName = result.getClass().getSimpleName();
            String action = joinPoint.getSignature().getName();

            actionLogService.log(employee, entityName, entityId, action);
        }
    }

    @AfterReturning("execution(* com.warehouse.demo.service..*.delete(..))")
    public void logDelete(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] instanceof Long id) {
            Employee employee = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getEmployee();
            long entityId = id;
            String entityName = joinPoint.getTarget().getClass().getSimpleName().replace("ServiceImpl", "");
            String action = joinPoint.getSignature().getName();

            actionLogService.log(employee, entityName, entityId, action);
        }
    }
}
