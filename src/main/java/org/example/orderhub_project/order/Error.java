package org.example.orderhub_project.order;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Error(
        String code,
        Object details
) {
}
