package local.warehouse.app.util.action;

import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;

public class MessageHandler {
    public static String getOutputMessage(Entity entityName, OutputMessage message) {
        return entityName.getEntity() + " " + message.getMessage();
    }

    public static String getOutputMessage(Entity entityName, String message) {
        return message.isEmpty() ? 
            "Cannot perform operation on " + entityName.getEntity() + "." :
            entityName.getEntity() + " " + message;
    }

    public static String getOutputMessage(OutputMessage message) {
        return message.getMessage();
    }

    public static String getOutputMessage(String message) {
        return message;
    }
}
