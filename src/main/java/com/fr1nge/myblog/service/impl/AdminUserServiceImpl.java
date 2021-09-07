package com.fr1nge.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fr1nge.myblog.dao.AdminUserMapper;
import com.fr1nge.myblog.entity.AdminUser;
import com.fr1nge.myblog.service.AdminUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

}
