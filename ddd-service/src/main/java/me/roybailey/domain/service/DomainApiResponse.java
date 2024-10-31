package me.roybailey.domain.service;

import java.util.List;

public record DomainApiResponse<T>(
        String client,
        List<T> results
) {
}
