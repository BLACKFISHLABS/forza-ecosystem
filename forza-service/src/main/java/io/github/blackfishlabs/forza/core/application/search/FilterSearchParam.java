package io.github.blackfishlabs.forza.core.application.search;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class FilterSearchParam {

    @Size(min = 1)
    private String status;

    private String description;
}
