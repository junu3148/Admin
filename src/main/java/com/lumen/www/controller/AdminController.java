package com.lumen.www.controller;

import com.lumen.www.dto.JoinListDTO;
import com.lumen.www.dto.JoinSearchDTO;
import com.lumen.www.dto.JsonResult;
import com.lumen.www.dto.PromotionsDTO;
import com.lumen.www.files.ImageUploader;
import com.lumen.www.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;
    private final ImageUploader imageUploader;
    private static final Logger logger = LoggerFactory.getLogger(AdminSignController.class);

    // 에디터 이미지 저장
    @PostMapping("/image/upload")
    public ModelAndView uploadImage(MultipartHttpServletRequest request) throws Exception {
        return imageUploader.uploadImage(request);
    }

    // 가입자 관리
    @PostMapping("/admin-join")
    public JsonResult adminJoin(@RequestBody JoinSearchDTO joinSearchDTO){
        return adminService.getJoinList(joinSearchDTO);
    }
    
    // 프로모션 등록
    @PostMapping("/add-promotions")
    public JsonResult addPromotions(@RequestBody PromotionsDTO promotionsDTO) {
        return adminService.addPromotions(promotionsDTO);
    }




}
