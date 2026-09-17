package com.warehouse.demo.util;

import org.junit.jupiter.api.Test;

import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilityTest {  // JUnit 5 tests for light (simple) objects
    @Test
    void getOutputMessage_productAndNotFoundParams_returnsString() {
        String expected = "Product not found.";
        String actual = Utility.getOutputMessage(Entity.PRODUCT, OutputMessage.NOT_FOUND);
        assertEquals(expected, actual);
    }

    @Test
    void getOutputMessage_barcodeNumberAndExistsParams_returnsString() {
        String expected = "Barcode number already exists.";
        String actual = Utility.getOutputMessage(Entity.BARCODE_NUMBER, OutputMessage.EXISTS);
        assertEquals(expected, actual);
    }

    @Test
    void getOutputMessage_employeeAndCustomParams_returnsString() {
        String expected = "Employee ....";
        String actual = Utility.getOutputMessage(Entity.EMPLOYEE, "....");
        assertEquals(expected, actual);
    }

    @Test
    void getOutputMessage_employeeAndEmptyParams_returnsString() {
        String expected = "Cannot perform operation on Employee.";
        String actual = Utility.getOutputMessage(Entity.EMPLOYEE, "");
        assertEquals(expected, actual);
    }

    @Test
    void valueOf_invalidConstantName_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Utility.getOutputMessage(Entity.valueOf("NONE"), OutputMessage.NOT_FOUND);
        });
        String expected = "NONE";
        String actual = exception.getMessage();
        assertTrue(actual.contains(expected));
    }
}
