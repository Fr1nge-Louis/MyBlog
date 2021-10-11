package com.fr1nge.myblog.controller.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BlogDetailVO {
    private Long blogId;

    private String blogTitle;

    private Integer blogCategoryId;

    private Integer commentCount;

    private String blogCategoryIcon;

    private String blogCategoryName;

    private String blogCoverImage;

    private Long blogViews;

    private List<String> blogTags;

    private String blogContent;//blog内容

    private Integer enableComment;//是否允许评论

    private Date createTime;
}
