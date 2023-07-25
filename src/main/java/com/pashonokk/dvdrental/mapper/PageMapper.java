package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.endpoint.PageResponse;
import org.springframework.data.domain.Page;

public interface PageMapper{
    <T> PageResponse<T> toPageResponse(Page<T> page);
}
