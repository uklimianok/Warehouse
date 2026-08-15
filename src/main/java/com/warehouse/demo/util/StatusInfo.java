package com.warehouse.demo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class StatusInfo {
    @AllArgsConstructor
    @Getter
    public static enum ProductPalletStatus {
        ORDERED("Ordered"),
        UPLOADED("Uploaded"),
        STORED("Stored"),
        ACTIVE("Active"),
        OUT_OF_USE("Out-of-use");

        private final String name;
    }

    @AllArgsConstructor
    @Getter
    public static enum OrderStatus {
        ACCEPTED("Accepted"),
        STARTED("Started"),
        INCOMPLETE_INACTIVE("Incomplete-inactive"),
        INCOMPLETE_ACTIVE("Incomplete-active"),
        COMPLETE("Complete"),
        SENT("Sent");

        private final String name;
    }

    @AllArgsConstructor
    @Getter
    public static enum OrderPalletStatus {
        PICKING("Picking"),
        PICKED("Picked"),
        EXPORTING("Exporting"),
        LOADING("Loading"),
        SENT("Sent");

        private final String name;
    }
}
