package com.myblog92.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private List<PostDto> dto;
    private int pageSize;
    private int pageNo;
    private boolean lastPage;
    private int totalPage;

}
