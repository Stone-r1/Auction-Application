package org.example.shared.domain;


public record PageQuery(
        int pageNumber,
        int pageSize
) {
    public PageQuery {
        if (pageSize < 1) {
            throw new IllegalArgumentException("pageSize must be >= 1");
        }
    }
}

