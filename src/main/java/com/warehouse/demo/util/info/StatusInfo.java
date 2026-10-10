package com.warehouse.demo.util.info;

import java.util.Set;

public class StatusInfo {
    public static final String PRODUCT_PALLET_ORDERED = "Ordered";
    public static final String PRODUCT_PALLET_UNLOADED = "Unloaded";
    public static final String PRODUCT_PALLET_STORED = "Stored";
    public static final String PRODUCT_PALLET_ACTIVE = "Active";
    public static final String PRODUCT_PALLET_OUT_OF_USE = "Out-of-use";
    public static final String ORDER_ACCEPTED = "Accepted";
    public static final String ORDER_PROCESSING = "Processing";
    public static final String ORDER_INCOMPLETED = "Incompleted";
    public static final String ORDER_COMPLETED = "Completed";
    public static final String ORDER_SENT = "Sent";
    public static final String ORDER_PALLET_PICKING = "Picking";
    public static final String ORDER_PALLET_PICKED = "Picked";
    public static final String ORDER_PALLET_EXPORTING = "Exporting";
    public static final String ORDER_PALLET_LOADING = "Loading";
    public static final String ORDER_PALLET_SENT = "Sent";

    public static final Set<String> PRODUCT_PALLET = Set.of(
        PRODUCT_PALLET_ORDERED,
        PRODUCT_PALLET_UNLOADED,
        PRODUCT_PALLET_STORED,
        PRODUCT_PALLET_ACTIVE,
        PRODUCT_PALLET_OUT_OF_USE
    );
    public static final Set<String> ORDER = Set.of(
        ORDER_ACCEPTED,
        ORDER_PROCESSING,
        ORDER_INCOMPLETED,
        ORDER_COMPLETED,
        ORDER_SENT
    );
    public static final Set<String> ORDER_PALLET = Set.of(
        ORDER_PALLET_PICKING,
        ORDER_PALLET_PICKED,
        ORDER_PALLET_EXPORTING,
        ORDER_PALLET_LOADING,
        ORDER_PALLET_SENT
    );
}
