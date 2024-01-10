package com.lumen.www.mapper;

import com.lumen.www.dto.AdminUser;
import org.apache.ibatis.annotations.Mapper;


// 사실상 사용 안될것 같은데; 메서드 위에 @select 달고 사용할것 아니면;
@Mapper
public interface AdminMapper {

    AdminUser getAdminUser(AdminUser adminUser);

}
