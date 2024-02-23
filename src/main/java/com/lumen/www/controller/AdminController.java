package com.lumen.www.controller;

import com.lumen.www.dto.common.JsonResult;
import com.lumen.www.dto.common.SearchDTO;
import com.lumen.www.dto.faq.FaqDTO;
import com.lumen.www.dto.inquiry.InquiryDTO;
import com.lumen.www.dto.invoice.InvoiceDTO;
import com.lumen.www.dto.notice.NoticeDTO;
import com.lumen.www.dto.pricing.PriceSearchDTO;
import com.lumen.www.dto.terms.TermsDTO;
import com.lumen.www.dto.user.UserDTO;
import com.lumen.www.files.ImageUploader;
import com.lumen.www.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AdminController {

    private final AdminService adminService;
    private final ImageUploader imageUploader;

    // 가입자 관리
    @GetMapping("join")
    public JsonResult getJoinList(@ModelAttribute SearchDTO searchDTO) {
        return adminService.getJoinList(searchDTO);
    }

    // 가입자 세부 정보
    @PostMapping("user/details")
    public JsonResult getUserDetails(@RequestBody UserDTO userDTO) {
        return adminService.getUserDetails(userDTO);
    }

    // 가입자 강제 탈퇴
    @PatchMapping("user")
    public ResponseEntity<String> deleteUser(@RequestBody UserDTO userDTO) {
        return adminService.deleteUser(userDTO);
    }

    // 미결제 관리
    @GetMapping("price")
    public JsonResult getPriceList(@ModelAttribute PriceSearchDTO priceSearchDTO) {
        return adminService.getPriceList(priceSearchDTO);
    }

    // 회원 활동 정지
    @PatchMapping("price")
    public ResponseEntity<String> updateUserStatus(@RequestBody UserDTO userDTO) {
        return adminService.updateUserStatus(userDTO);
    }

    // 청약철회 현황
    @GetMapping("pay")
    public JsonResult getPayList(@ModelAttribute SearchDTO searchDTO) {
        return adminService.getSubscriptionEndList(searchDTO);
    }

    // 청약철회 회원 세부 정보
    @PostMapping("pay/details")
    public JsonResult getPayDetails(@RequestBody UserDTO userDTO) {
        return adminService.getUserDetails(userDTO);
    }

    // 인보이스 현황
    @GetMapping("invoice")
    public JsonResult getInvoiceList(@ModelAttribute SearchDTO searchDTO) {
        return adminService.getInvoiceList(searchDTO);
    }

    // 인보이스 세부 정보
    @PostMapping("invoice/details")
    public JsonResult getInvoiceDetails(@RequestBody InvoiceDTO invoiceDTO) {
        return adminService.getInvoiceDetails(invoiceDTO);
    }

    // 1:1 문의 현황
    @GetMapping("inquiry")
    public JsonResult getInquiryList(@ModelAttribute SearchDTO searchDTO) {
        return adminService.getInquiryList(searchDTO);
    }

    // 1:1 문의 세부 정보
    @PostMapping("inquiry/details")
    public JsonResult getInquiryDetails(@RequestBody InquiryDTO inquiryDTO) {
        return adminService.getInquiryDetails(inquiryDTO);
    }

    // 1:1 문의 답변
    @PatchMapping("inquiry")
    public ResponseEntity<String> createInquiryAnswer(@RequestBody InquiryDTO inquiryDTO) {
        return adminService.insertInquiryAnswer(inquiryDTO);
    }

    // 공지사항 현황
    @GetMapping("notice")
    public JsonResult getNoticeList(@ModelAttribute SearchDTO searchDTO) {
        return adminService.getNoticeList(searchDTO);
    }

    // 공지사항 세부 정보
    @PostMapping("notice/details")
    public JsonResult getNoticeDetails(@RequestBody NoticeDTO noticeDTO) {
        return adminService.getNoticeDetails(noticeDTO);
    }

    // 공지사항 등록
    @PostMapping("notice")
    public ResponseEntity<String> createNotice(HttpServletRequest request, @RequestBody NoticeDTO noticeDTO) {
        return adminService.insertNotice(request, noticeDTO);
    }

    // 공지사항 수정
    @PatchMapping("notice")
    public ResponseEntity<String> updateNotice(@RequestBody NoticeDTO noticeDTO) {
        return adminService.updateNotice(noticeDTO);
    }

    // 공지사항 삭제
    @DeleteMapping("notice")
    public ResponseEntity<String> deleteNotice(@RequestBody NoticeDTO noticeDTO) {
        return adminService.deleteNotice(noticeDTO);
    }

    // FAQ 형황
    @GetMapping("faq")
    public JsonResult getFaqList(@ModelAttribute SearchDTO searchDTO) {
        return adminService.getFaqList(searchDTO);
    }

    // FAQ 세부 정보
    @PostMapping("faq/details")
    public JsonResult getFaqDetails(@RequestBody FaqDTO faqDTO) {
        return adminService.getFaqDetails(faqDTO);
    }

    // FAQ 등록
    @PostMapping("faq")
    public ResponseEntity<String> createFaq(HttpServletRequest request, @RequestBody FaqDTO faqDTO) {
        return adminService.insertFaq(request, faqDTO);
    }

    // FAQ 수정
    @PatchMapping("faq")
    public ResponseEntity<String> updateFaq(@RequestBody FaqDTO faqDTO) {
        return adminService.updateFaq(faqDTO);
    }

    // FAQ 삭제
    @DeleteMapping("faq")
    public ResponseEntity<String> deleteFaq(@RequestBody FaqDTO faqDTO) {
        return adminService.deleteFaq(faqDTO);
    }

    // Terms 정보
    @GetMapping("terms")
    public JsonResult getTermsDetails() {
        return adminService.getTermsDetails();
    }

    // Terms 수정
    @PatchMapping("terms")
    public ResponseEntity<String> updateTerms(@RequestBody TermsDTO termsDTO) {
        return adminService.updateTerms(termsDTO);
    }

    // 에디터 이미지 저장
    @PostMapping("image/upload")
    public ModelAndView uploadImage(MultipartHttpServletRequest request) {
        return imageUploader.uploadImage(request);
    }


}






















