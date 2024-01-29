package com.lumen.www.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.lumen.www.dto.*;
import com.lumen.www.files.ImageUploader;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
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
    public JsonResult adminJoin(@RequestBody JoinSearchDTO joinSearchDTO) {
        return adminService.getJoinList(joinSearchDTO);
    }

    // 가입자 세부 정보
    @PostMapping("join/details")
    public JsonResult adminJoinDetails(@RequestBody UserDTO userDTO) {
        return adminService.getUserDetails(userDTO);
    }

    // 가입자 비밀번호 초기화
    @PostMapping("join/details/pw-reset")
    public JsonResult adminJoinPWReset(@RequestBody UserDTO userDTO) {
        return adminService.adminJoinPWReset(userDTO);
    }

    // 가입자 강제 탈퇴
    @PostMapping("join/details/user-delete")
    public ResponseEntity<Integer> adminJoinUserDelete(@RequestBody UserDTO userDTO) {
        return adminService.adminJoinUserDelete(userDTO);
    }

}
