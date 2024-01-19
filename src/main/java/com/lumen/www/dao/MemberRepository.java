package com.lumen.www.dao;

import com.lumen.www.dto.AdminUser;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final SqlSession session;
    public AdminUser findByUsername(String adminId){

        System.out.println("오냐오냐");
        
       AdminUser d = session.selectOne("admin.findByUsername", adminId);

        System.out.println(d);

        return d;

    }

}
