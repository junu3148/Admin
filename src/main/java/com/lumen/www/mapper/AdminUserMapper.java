package com.lumen.www.mapper;

import com.lumen.www.vo.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminUserMapper {

    AdminUser getAdminUser(AdminUser adminUser);

}
