package com.pashonokk.dvdrental.mapper.impl;

import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.pageFeature.GeneralPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PageMapperImpl implements PageMapper {
    @Override
    public <T> GeneralPageResponse<T> toGeneralResponse(Page<T> page) {
        return new GeneralPageResponse<>(page);
    }
}
