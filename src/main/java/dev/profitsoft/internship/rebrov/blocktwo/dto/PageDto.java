package dev.profitsoft.internship.rebrov.blocktwo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageDto<T> {
    private List<T> list;
    private Integer totalPages;
    private Long totalElements;
}