package com.larasierra.movietickets.shopping.domain;

import com.larasierra.movietickets.shared.domain.BaseEntity;
import com.larasierra.movietickets.shared.validation.ValidId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@ToString(exclude = "items")
@Getter @Setter
@Table(name = "orders")
@Entity
public class Order extends BaseEntity<String> {
    @ValidId
    @Id
    @Column(name = "order_id")
    private String orderId;

    @ValidId
    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotBlank
    @NotNull
    @Column(name = "purchase_token")
    private String purchaseToken;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @Convert(converter = OrderStatusConverter.class)
    @NotNull
    @Column(name = "status")
    private OrderStatus status;

    @NotNull
    @Column(name = "cancel")
    private Boolean cancel;

    @Min(1)
    @NotNull
    @Column(name = "total_cents")
    private Long totalCents;

    @Column(name = "order_code")
    private UUID orderCode;

    @NotNull
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public void addItem(@NotNull OrderItem orderItem) {
        orderItem.setOrder(this);
        items.add(orderItem);
    }

    public void addItems(@NotNull List<OrderItem> items) {
        items.forEach(this::addItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String getId() {
        return orderId;
    }
}
