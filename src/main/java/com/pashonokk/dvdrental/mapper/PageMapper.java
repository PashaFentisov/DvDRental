package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.pageFeature.GeneralPageResponse;
import org.springframework.data.domain.Page;

public interface PageMapper{
    <T> GeneralPageResponse<T> toGeneralResponse(Page<T> page);
}
