package com.pashonokk.dvdrental.endpoint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
@Setter
@Getter
@ToString
public class PageResponse<T> {
    private HttpStatus status;
    private String message;
    private Long pageNumber;
    private int pageSize;
    private Long totalRecords;
    private Long totalPages;
    @Setter(AccessLevel.PRIVATE)
    private List<T> records;

    public PageResponse(Page<T> page){
        Pageable pageable = page.getPageable();
        this.status = HttpStatus.OK;
        this.message = "processed successfully";
        this.pageNumber = (long) pageable.getPageNumber();
        this.pageSize = pageable.getPageSize();
        this.totalRecords = (long) page.getContent().size();
        this.totalPages = (long) page.getTotalPages();
        this.records = page.getContent();
    }


}
