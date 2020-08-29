package io.github.blackfishlabs.forza.domain.pojo;

import io.github.blackfishlabs.forza.domain.dto.OrderItemDto;

import static io.github.blackfishlabs.forza.helper.CurrencyUtils.round;

public class OrderItem {

    private Integer id;

    private Integer orderItemId;

    private PriceTableItem item;

    private Float quantity;

    private Double salesPrice;

    private Double subTotal;

    private String lastChangeTime;

    private String tempId;

    public Integer getId() {
        return id;
    }

    public OrderItem withId(final Integer id) {
        this.id = id;
        return this;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public OrderItem withOrderItemId(final Integer orderItemId) {
        this.orderItemId = orderItemId;
        return this;
    }

    public PriceTableItem getItem() {
        return item;
    }

    public OrderItem withItem(final PriceTableItem item) {
        this.item = item;
        return this;
    }

    public Float getQuantity() {
        return quantity;
    }

    public OrderItem withQuantity(final Float quantity) {
        this.quantity = quantity;
        calculateSubTotal();
        return this;
    }

    public OrderItem addQuantity(final float quantity) {
        if (this.quantity == null) {
            this.quantity = quantity;
        } else {
            this.quantity += quantity;
        }
        calculateSubTotal();
        return this;
    }

    public OrderItem removeOneFromQuantity() {
        if (quantity != null && quantity >= 1) {
            --quantity;
            calculateSubTotal();
        }
        return this;
    }

    private void calculateSubTotal() {
        subTotal = this.quantity * this.salesPrice;
    }

    public Double getSubTotal() {
        return round(subTotal);
    }

    public OrderItem withSubTotal(final Double subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public OrderItem withSalesPrice(final Double salesPrice) {
        this.salesPrice = salesPrice;
        return this;
    }

    public String getLastChangeTime() {
        return lastChangeTime;
    }

    public OrderItem withLastChangeTime(final String lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
        return this;
    }

    public String getTempId() {
        return tempId;
    }

    public OrderItem withTempId(final String tempId) {
        this.tempId = tempId;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderItem item = (OrderItem) o;

        return getId() != null ?
                getId().equals(item.getId()) : getTempId().equals(item.getTempId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : getTempId().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderItem{");
        sb.append("id=").append(id);
        sb.append(", orderItemId=").append(orderItemId);
        sb.append(", item=").append(item);
        sb.append(", quantity=").append(quantity);
        sb.append(", subTotal=").append(subTotal);
        sb.append(", lastChangeTime='").append(lastChangeTime).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public OrderItemDto createPostOrderItem() {
        return new OrderItemDto(
                getId(),
                getItem().getProduct().getProductId(),
                getQuantity(),
                getSubTotal(),
                getSalesPrice()
        );
    }
}
