package com.lumen.www.controller;

import com.lumen.www.dto.*;
import com.lumen.www.files.ImageUploader;
import com.lumen.www.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AdminController {


    private final AdminService adminService;
    private final ImageUploader imageUploader;

    // 가입자 관리
    @PostMapping("join")
    public JsonResult adminJoin(@RequestBody SearchDTO searchDTO) {
        return adminService.getJoinList(searchDTO);
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
    public ResponseEntity<?> adminJoinUserDelete(@RequestBody UserDTO userDTO) {
        return adminService.adminJoinUserDelete(userDTO);
    }


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

    // 미결제 관리
    @PostMapping("price")
    public JsonResult adminPrice(@RequestBody PriceSearchDTO priceSearchDTO) {
        return adminService.getPriceList(priceSearchDTO);
    }

    // 회원 활동 정지
    @PostMapping("price/stop")
    public ResponseEntity<?> adminPriceUserStop(@RequestBody UserDTO userDTO) {
        System.out.println("호출호출");
        return adminService.updateUserStatus(userDTO);
    }

    // 청약철회 현황
    @PostMapping("pay")
    public JsonResult adminPay(@RequestBody SearchDTO searchDTO) {
        return adminService.getSubscriptionEndList(searchDTO);
    }

    // 청약철회 회원 정보
    @PostMapping("pay/details")
    public JsonResult adminPayDetails(@RequestBody UserDTO userDTO) {
        return adminService.getUserDetails(userDTO);
    }

    // 인보이스 현황
    @PostMapping("invoice")
    public JsonResult adminInvoice(@RequestBody SearchDTO searchDTO) {
        return adminService.getInvoiceList(searchDTO);
    }

    // 인보이스 세부 정보
    @PostMapping("invoice/details")
    public  JsonResult adminInvoiceDetails(@RequestBody InvoiceDTO invoiceDTO ){

        return adminService.getInvoiceDetails(invoiceDTO);
    }
}
