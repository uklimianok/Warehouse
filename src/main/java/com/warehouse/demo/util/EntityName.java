package com.warehouse.demo.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EntityName {
    POSITION("Position"),
    ORGANIZATION_TYPE("Organization type"),
    ORGANIZATION("Organization"),
    SHIFT("Shift"),
    EMPLOYEE("Employee"),
    PRODUCT("Product"),
    PRODUCT_PACKAGE("Package"),
    PALLET("Pallet"),
    STATUS("Status"),
    WORKSHOP("Workshop"),
    WORK_STATION("Work station"),
    PRODUCT_PALLET("Product pallet"),
    GATE("Gate"),
    TRACK("Track"),
    ORDERED_PRODUCT("Ordered product"),
    ORDER_PALLET("Order pallet"),
    PAPER_CARD("Paper card"),
    RETURN_PRODUCT("Return product"),
    PICKED_PRODUCT("Picked product"),
    ACTION_LOG("Action log"),
    USER("User"),
    BARCODE_NUMBER("Barcode number");

    private final String name;
}
