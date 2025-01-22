package com.auth.licoflix.core.domain.dto.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FilterDTO {
    private Long id;
    private String description;
}