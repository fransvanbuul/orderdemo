package com.example.orderdemo.ordering.status;

import com.example.orderdemo.ordering.cmd.OrderPlacedEvt;
import com.example.orderdemo.ordering.cmd.ShipmentForOrderArrivedEvt;
import com.example.orderdemo.ordering.cmd.ShipmentForOrderPreparedEvt;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
@Profile("orders")
public class OrderStatusProjector {

    private final EntityManager entityManager;

    @EventHandler
    public void on(OrderPlacedEvt evt) {
        entityManager.persist(OrderStatus.builder()
                .orderId(evt.getOrderId())
                .shipmentId(null)
                .delivered(false)
                .build());
    }

    @EventHandler
    public void on(ShipmentForOrderPreparedEvt evt) {
        OrderStatus status = entityManager.find(OrderStatus.class, evt.getOrderId());
        status.setShipmentId(evt.getShipmentId());
    }

    @EventHandler
    public void on(ShipmentForOrderArrivedEvt evt) {
        OrderStatus status = entityManager.find(OrderStatus.class, evt.getOrderId());
        status.setDelivered(true);
    }

    @QueryHandler
    public OrderStatus handle(FindStatus query) {
        return entityManager.find(OrderStatus.class, query.getOrderId());
    }

}
