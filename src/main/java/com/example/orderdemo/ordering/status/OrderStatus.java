package com.example.orderdemo.ordering.status;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Profile;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Profile("orders")
public class OrderStatus {

    @Id
    UUID orderId;

    UUID shipmentId;

    boolean delivered;

}
