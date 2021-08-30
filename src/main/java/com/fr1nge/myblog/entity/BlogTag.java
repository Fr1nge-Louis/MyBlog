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
public class BlogTag extends Model<BlogTag> {

    private static final long serialVersionUID = 1L;

    /**
     * 标签表主键id
     */
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Integer tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 是否删除 0=否 1=是
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.tagId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
