package io.github.blackfishlabs.forza.domain.pojo;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({CustomerStatus.STATUS_UNMODIFIED, CustomerStatus.STATUS_CREATED, CustomerStatus.STATUS_MODIFIED})
public @interface CustomerStatus {

    int STATUS_UNMODIFIED = 0;
    int STATUS_CREATED = 1;
    int STATUS_MODIFIED = 2;
}
