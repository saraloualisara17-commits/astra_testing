package com.wash.laundry_app.command;

public enum CommandeStatus {
    PENDING_PICKUP,
    PICKED_UP,
    IN_PROCESS,
    READY_FOR_DELIVERY,
    DELIVERED,
    CANCELLED,
    PICKUP_FAILED,
    DELIVERY_FAILED
}
