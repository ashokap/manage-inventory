package com.example.manageinventory.models;

public enum IndentStatus {
    ORDER_RECEIVED, // Default status for outgoing indent
    ORDER_PLACED,  // Default status for incoming indent
    DISPATCHED,  // Updatable status for Outgoing indent
    DELETED, // Status marking deletion of any kind of indent
    PROCESSED // Updatable status for incoming indent
}
