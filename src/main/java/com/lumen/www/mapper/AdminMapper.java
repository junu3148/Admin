package com.lumen.www.mapper;

import com.lumen.www.vo.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

    AdminUser getAdminUser(AdminUser adminUser);

}
