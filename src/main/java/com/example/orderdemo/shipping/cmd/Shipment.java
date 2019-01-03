package com.example.orderdemo.shipping.cmd;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@Aggregate
@NoArgsConstructor
@Profile("shipments")
class Shipment {

    @AggregateIdentifier
    private UUID shipmentId;

    @CommandHandler
    Shipment(PrepareShipmentCmd cmd) {
        apply(new ShipmentPreparedEvt(cmd.getShipmentId(), cmd.getDestination()));
    }

    @CommandHandler
    void handle(RegisterShipmentArrivalCmd cmd) {
        apply(new ShipmentArrivedEvt(shipmentId));
    }

    @EventSourcingHandler
    void on(ShipmentPreparedEvt evt) {
        shipmentId = evt.getShipmentId();
    }

    @EventSourcingHandler
    void on(ShipmentArrivedEvt evt) {
        markDeleted();
    }

}
