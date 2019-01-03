package com.example.orderdemo.ordering.saga;

import com.example.orderdemo.ordering.cmd.OrderPlacedEvt;
import com.example.orderdemo.ordering.cmd.RegisterShipmentForOrderArrivedCmd;
import com.example.orderdemo.ordering.cmd.RegisterShipmentForOrderPreparedCmd;
import com.example.orderdemo.shipping.cmd.PrepareShipmentCmd;
import com.example.orderdemo.shipping.cmd.ShipmentArrivedEvt;
import com.example.orderdemo.shipping.cmd.ShipmentPreparedEvt;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

import static org.axonframework.modelling.saga.SagaLifecycle.associateWith;

@Saga
@Slf4j
@Profile("coordinator")
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    private UUID orderId;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderPlacedEvt evt) {
        log.debug("handling {}", evt);
        orderId = evt.getOrderId();
        UUID shipmentId = UUID.randomUUID();
        associateWith("shipmentId", shipmentId.toString());
        commandGateway.send(new PrepareShipmentCmd(shipmentId, evt.getDestination()));
    }

    @SagaEventHandler(associationProperty = "shipmentId")
    public void on(ShipmentPreparedEvt evt) {
        log.debug("handling {}", evt);
        log.debug("orderId: {}", orderId);
        commandGateway.send(new RegisterShipmentForOrderPreparedCmd(orderId, evt.getShipmentId()));
    }

    @SagaEventHandler(associationProperty = "shipmentId")
    @EndSaga
    public void on(ShipmentArrivedEvt evt) {
        log.debug("handling {}", evt);
        log.debug("orderId: {}", orderId);
        commandGateway.send(new RegisterShipmentForOrderArrivedCmd(orderId, evt.getShipmentId()));
    }
}
