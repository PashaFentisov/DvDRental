package com.pashonokk.dvdrental.mapper.impl;

import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.mapper.PageMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PageMapperImpl implements PageMapper {
    @Override
    public <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(page);
    }
}
