package com.example.orderdemo.shipping.cmd;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class ShipmentPreparedEvt {

    UUID shipmentId;

    String destination;

}
