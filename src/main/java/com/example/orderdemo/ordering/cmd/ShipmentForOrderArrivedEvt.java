package com.example.orderdemo.ordering.cmd;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class ShipmentForOrderArrivedEvt {

    @TargetAggregateIdentifier
    UUID orderId;

    UUID shipmentId;

}
