package com.fr1nge.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.alibaba.fastjson.JSON;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2021-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlogComment extends Model<BlogComment> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     * 关联的blog主键
     */
    private Long blogId;

    /**
     * 评论者名称
     */
    private String commentator;

    /**
     * 评论人的邮箱
     */
    private String email;

    /**
     * 网址
     */
    private String websiteUrl;

    /**
     * 评论内容
     */
    private String commentBody;

    /**
     * 评论提交时间
     */
    private Date commentCreateTime;

    /**
     * 评论时的ip地址
     */
    private String commentatorIp;

    /**
     * 回复内容
     */
    private String replyBody;

    /**
     * 回复时间
     */
    private Date replyCreateTime;

    /**
     * 是否审核通过 0-未审核 1-审核通过
     */
    private Integer commentStatus;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer isDeleted;


    @Override
    protected Serializable pkVal() {
        return this.commentId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
