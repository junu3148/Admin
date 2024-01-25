package com.lumen.www.controller;

import com.lumen.www.dto.JoinSearchDTO;
import com.lumen.www.dto.JsonResult;
import com.lumen.www.dto.PromotionsDTO;
import com.lumen.www.files.ImageUploader;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AdminController {


    private final AdminService adminService;
    private final ImageUploader imageUploader;
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
