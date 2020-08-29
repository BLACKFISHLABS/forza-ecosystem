package io.github.blackfishlabs.forza.data.order;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({OrderStatusSpecificationFilter.NONE, OrderStatusSpecificationFilter.NOT_CANCELLED, OrderStatusSpecificationFilter.CREATED_OR_MODIFIED, OrderStatusSpecificationFilter.SYNCED, OrderStatusSpecificationFilter.INVOICED})
public @interface OrderStatusSpecificationFilter {

    int NONE = 0;
    int NOT_CANCELLED = 1;
    int CREATED_OR_MODIFIED = 2;
    int SYNCED = 3;
    int INVOICED = 4;
}
