package io.github.blackfishlabs.forza.domain.pojo;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({OrderStatus.STATUS_CREATED, OrderStatus.STATUS_MODIFIED, OrderStatus.STATUS_SYNCED, OrderStatus.STATUS_CANCELLED, OrderStatus.STATUS_INVOICED})
public @interface OrderStatus {

    int STATUS_CREATED = 0;
    int STATUS_MODIFIED = 1;
    int STATUS_SYNCED = 2;
    int STATUS_CANCELLED = 3;
    int STATUS_INVOICED = 5;
}
