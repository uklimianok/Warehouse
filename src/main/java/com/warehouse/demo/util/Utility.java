package com.warehouse.demo.util;

public class Utility {
    public static String getOutputMessage(EntityName entityName, OutputMessage message) {
        return entityName.getName() + " " + message.getMessage();
    }

    public static String getOutputMessage(EntityName entityName, String message) {
        return entityName.getName() + " " + message;
    }
}
