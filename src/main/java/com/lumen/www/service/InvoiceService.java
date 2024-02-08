package com.lumen.www.service;

import com.lumen.www.dto.invoice.InvoiceDTO;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


public interface InvoiceService {


    /**
     * 주어진 InvoiceDTO를 사용하여 송장 이메일을 발송합니다.
     *
     * @param invoiceDTO 송장 정보를 담은 InvoiceDTO 객체
     * @return 이메일 발송 결과를 나타내는 ResponseEntity
     */
    ResponseEntity<String> invoiceEmailShipment(InvoiceDTO invoiceDTO);

    /**
     * 주어진 수신자 이메일 주소에 송장을 첨부하여 이메일을 발송합니다.
     *
     * @param recipientEmail 수신자 이메일 주소
     * @param invoiceDTO     송장 정보를 담은 InvoiceDTO 객체
     * @throws IOException        파일 생성 중 발생하는 입출력 예외
     * @throws MessagingException 이메일 발송 중 발생하는 메시징 예외
     */
    void sendInvoiceAsEmail(String recipientEmail, InvoiceDTO invoiceDTO) throws IOException, MessagingException;


}
