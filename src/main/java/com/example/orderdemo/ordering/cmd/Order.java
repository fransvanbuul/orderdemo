package com.example.orderdemo.ordering.cmd;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

import java.util.*;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
@Profile("orders")
public class Order {

    @AggregateIdentifier
    private UUID orderId;

    private Set<UUID> undeliveredShipments;

    @CommandHandler
    Order(PlaceOrderCmd cmd) {
        apply(new OrderPlacedEvt(cmd.getOrderId(), cmd.getGoods(), cmd.getDestination()));
    }

    @CommandHandler
    void handle(RegisterShipmentForOrderPreparedCmd cmd) {
        if(!undeliveredShipments.contains(cmd.getShipmentId())) {
            apply(new ShipmentForOrderPreparedEvt(orderId, cmd.getShipmentId()));
        }
    }

    @CommandHandler
    void handle(RegisterShipmentForOrderArrivedCmd cmd) {
        if(undeliveredShipments.contains(cmd.getShipmentId())) {
            apply(new ShipmentForOrderArrivedEvt(orderId, cmd.getShipmentId()));
        }
    }

    @EventSourcingHandler
    void on(OrderPlacedEvt evt) {
        orderId = evt.getOrderId();
        undeliveredShipments = new HashSet<>();
    }

    @EventSourcingHandler
    void on(ShipmentForOrderPreparedEvt evt) {
        undeliveredShipments.add(evt.getShipmentId());
    }

    @EventSourcingHandler
    void on(ShipmentForOrderArrivedEvt evt) {
        undeliveredShipments.remove(evt.getShipmentId());
    }
}
