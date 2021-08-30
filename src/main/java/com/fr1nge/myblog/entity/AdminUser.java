package com.fr1nge.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class AdminUser extends Model<AdminUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员id
     */
    @TableId(value = "admin_user_id", type = IdType.AUTO)
    private Integer adminUserId;

    /**
     * 管理员登陆名称
     */
    private String loginUserName;

    /**
     * 管理员登陆密码
     */
    private String loginPassword;

    /**
     * 管理员显示昵称
     */
    private String nickName;

    /**
     * 是否锁定 0未锁定 1已锁定无法登陆
     */
    private Integer locked;


    @Override
    protected Serializable pkVal() {
        return this.adminUserId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
