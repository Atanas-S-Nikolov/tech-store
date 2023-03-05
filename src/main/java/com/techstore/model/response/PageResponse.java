package com.techstore.model.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class PageResponse<T> {
    @Getter
    private final Long totalItems;

    @Getter
    private final Integer totalPages;

    @Getter
    private final Integer currentPage;

    @Getter
    private final Collection<T> items;
}
