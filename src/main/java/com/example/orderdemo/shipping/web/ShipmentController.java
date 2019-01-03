package com.example.orderdemo.shipping.web;

import com.example.orderdemo.shipping.cmd.RegisterShipmentArrivalCmd;
import com.example.orderdemo.shipping.status.ListAll;
import com.example.orderdemo.shipping.status.OpenShipment;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shipments")
@RequiredArgsConstructor
@Profile("shipments")
public class ShipmentController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @RequestMapping(path = "/open", method = RequestMethod.GET)
    public List<OpenShipment> findOpen() {
        return queryGateway.query(new ListAll(), ResponseTypes.multipleInstancesOf(OpenShipment.class))
                .join();
    }

    @RequestMapping(path = "/registerArrival", method = RequestMethod.POST)
    public void registerArrival(String shipmentId) {
        commandGateway.sendAndWait(new RegisterShipmentArrivalCmd(UUID.fromString(shipmentId)));
    }

}
