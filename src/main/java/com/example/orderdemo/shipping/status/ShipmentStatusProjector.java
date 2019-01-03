package com.example.orderdemo.shipping.status;

import com.example.orderdemo.shipping.cmd.ShipmentArrivedEvt;
import com.example.orderdemo.shipping.cmd.ShipmentPreparedEvt;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("shipments")
public class ShipmentStatusProjector {

    private final EntityManager entityManager;

    @EventHandler
    public void on(ShipmentPreparedEvt evt, @Timestamp Instant timestamp) {
        entityManager.persist(OpenShipment.builder()
                .shipmentId(evt.getShipmentId())
                .destination(evt.getDestination())
                .registeredOn(timestamp)
                .build());
    }

    @EventHandler
    public void on(ShipmentArrivedEvt evt) {
        OpenShipment openShipment = entityManager.find(OpenShipment.class, evt.getShipmentId());
        entityManager.remove(openShipment);
    }

    @QueryHandler
    public List<OpenShipment> handle(ListAll query) {
        return entityManager.createQuery(
                "SELECT e FROM OpenShipment e ORDER BY registeredOn ASC",
                OpenShipment.class
        ).getResultList();
    }

}
