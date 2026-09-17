package com.warehouse.demo.util.info;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Department {
    public static final String WAREHOUSE_EMPLOYEES_DEPARTMENT = "Warehouse Employees Department";
    public static final String AUXILIARY_EMPLOYEES_DEPARTMENT = "Auxiliary Employees Department";
    public static final String WAREHOUSE_SERVICE_DEPARTMENT = "Warehouse Service Department";
    public static final String HIGH_LEVEL_STAFF_DEPARTMENT = "High-level Staff Department";
    public static final String HR_DEPARTMENT = "HR Department";
    public static final String MANAGEMENT_DEPARTMENT = "Management Department";
    public static final String LABOR_PROTECTION_DEPARTMENT = "Labor Protection Department";
    public static final String ACCOUNTANCY_DEPARTMENT = "Accountancy Department";
    public static final String IT_DEPARTMENT = "IT Department";
    public static final String SERVICE_DEPARTMENT = "Service Department";

    public static final Map<String, Integer> WAREHOUSE_FLOOR_DEPARTMENT = Map.of(   // Integer = priority
        WAREHOUSE_EMPLOYEES_DEPARTMENT, 30,
        AUXILIARY_EMPLOYEES_DEPARTMENT, 20,
        WAREHOUSE_SERVICE_DEPARTMENT, 30
    );
    public static final Map<String, Integer> OFFICE_DEPARTMENT = Map.of(
        HIGH_LEVEL_STAFF_DEPARTMENT, 10,
        HR_DEPARTMENT, 20,
        MANAGEMENT_DEPARTMENT, 20,
        LABOR_PROTECTION_DEPARTMENT, 20,
        ACCOUNTANCY_DEPARTMENT, 20
    );
    public static final Map<String, Integer> HYBRID_DEPARTMENT = Map.of(
        IT_DEPARTMENT, 10,
        SERVICE_DEPARTMENT, 30
    );

    public static final Map<String, Integer> PRIORITIES = Stream.of(
        WAREHOUSE_FLOOR_DEPARTMENT, 
        OFFICE_DEPARTMENT, 
        HYBRID_DEPARTMENT
    )
        .flatMap(m -> m.entrySet().stream())
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
}
