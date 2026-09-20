package com.warehouse.demo.util.info;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Entity {
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
    ORDER("Order"),
    ACTION_LOG("Action log"),
    USER("User"),
    BARCODE_NUMBER("Barcode number"),
    DEPARTMENT("Department"),
    NEXT_WORK_STATION("Next work station");

    private final String entity;
}
