package com.lumen.www.controller;

import com.lumen.www.dto.JoinListDTO;
import com.lumen.www.dto.JoinSearchDTO;
import com.lumen.www.dto.JsonResult;
import com.lumen.www.dto.PromotionsDTO;
import com.lumen.www.files.ImageUploader;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.service.AdminService;
import com.lumen.www.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AdminController {


    private final AdminService adminService;
    private final ImageUploader imageUploader;
    private final JwtTokenProvider jwtTokenProvider;
    // 에디터 이미지 저장
    @PostMapping("image/upload")
    public ModelAndView uploadImage(MultipartHttpServletRequest request) throws Exception {
        return imageUploader.uploadImage(request);
    }

    // 프로모션 등록
    @PostMapping("add-promotions")
    public JsonResult addPromotions(@RequestBody PromotionsDTO promotionsDTO) {
        return adminService.addPromotions(promotionsDTO);
    }

    // 가입자 관리
    @PostMapping("join")
    public JsonResult adminJoin(@RequestBody JoinSearchDTO joinSearchDTO){
        return adminService.getJoinList(joinSearchDTO);
    }
    




}
