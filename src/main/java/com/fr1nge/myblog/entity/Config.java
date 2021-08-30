package com.fr1nge.myblog.entity;

import java.util.Date;
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
public class Config extends Model<Config> {

    private static final long serialVersionUID = 1L;

    /**
     * 配置项的名称
     */
    private String configName;

    /**
     * 配置项的值
     */
    private String configValue;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.configName;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
