package com.lumen.www.controller;


import com.lumen.www.dto.invoice.InvoiceDTO;
import com.lumen.www.dto.promotion.PromotionsDTO;
import com.lumen.www.dto.user.UserDTO;
import com.lumen.www.service.EmailService;
import com.lumen.www.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class EmailController {


    private final EmailService emailService;
    private final InvoiceService invoiceService;

    // 가입자 비밀번호 초기화
    @PostMapping("user")
    public String adminJoinPWReset(@RequestBody UserDTO userDTO) {
        return emailService.sendMailPWReset(userDTO);
    }

    // 프로모션 메일 발송
    @PostMapping("send/promotions")
    public ResponseEntity<String> addPromotions(@RequestBody PromotionsDTO promotionsDTO) {
        return emailService.sendMailPromo(promotionsDTO);
    }

    // 인보이스 메일 발송
    @PostMapping("invoice/email")
    public ResponseEntity<String> invoiceEmailShipment(@RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.invoiceEmailShipment(invoiceDTO);
    }

}
