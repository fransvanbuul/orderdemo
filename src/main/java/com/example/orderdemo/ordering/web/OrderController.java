package com.example.orderdemo.ordering.web;

import com.example.orderdemo.ordering.cmd.PlaceOrderCmd;
import com.example.orderdemo.ordering.status.FindStatus;
import com.example.orderdemo.ordering.status.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Profile("orders")
public class OrderController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @RequestMapping(path = "/placeOrder", method = RequestMethod.POST)
    public UUID placeOrder(String goods, String destination) {
        return commandGateway.sendAndWait(new PlaceOrderCmd(UUID.randomUUID(), goods, destination));
    }

    @RequestMapping(path = "/findStatus", method = RequestMethod.POST)
    public OrderStatus findStatus(String orderId) {
        return queryGateway.query(new FindStatus(UUID.fromString(orderId)), ResponseTypes.instanceOf(OrderStatus.class))
                .join();
    }
}
