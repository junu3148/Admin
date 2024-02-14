package com.lumen.www.service;

import com.lumen.www.dto.invoice.InvoiceDTO;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


public interface InvoiceService {

    /**
     * 지정된 송장 정보를 이메일로 전송합니다.
     * <p>
     * 이 메서드는 입력받은 송장 정보({@code InvoiceDTO})를 사용하여 해당 송장 내용을
     * 이메일로 전송하는 기능을 수행합니다. 이메일 전송이 성공적으로 완료되면, 성공 메시지를 담은
     * {@code ResponseEntity<String>}을 반환합니다. 이메일 전송 과정에서 오류가 발생한 경우,
     * 오류 메시지와 함께 적절한 HTTP 오류 응답을 반환합니다.
     *
     * @param invoiceDTO 이메일로 전송할 송장 정보를 담고 있는 {@code InvoiceDTO} 객체.
     * @return 이메일 전송 성공 여부를 나타내는 메시지를 담고 있는 {@code ResponseEntity<String>} 객체.
     *         전송 성공 시, {@code HttpStatus.OK} 상태와 함께 성공 메시지를 반환하며, 실패 시 오류 메시지와 상태를 반환합니다.
     */

    ResponseEntity<String> invoiceEmailShipment(InvoiceDTO invoiceDTO);

    /**
     * 지정된 이메일 주소로 송장 정보를 포함한 이메일을 전송합니다.
     * <p>
     * 이 메서드는 입력받은 수신자 이메일 주소({@code String})와 송장 정보({@code InvoiceDTO})를 사용하여
     * 송장 내용을 이메일로 전송하는 기능을 수행합니다. 이메일 전송은 {@code IOException} 또는 {@code MessagingException}을
     * 발생시킬 수 있으며, 이 경우 해당 예외가 메서드를 호출한 측으로 전파됩니다. 메서드 호출자는 이 예외를 적절히 처리하여
     * 사용자에게 오류 상황을 알려야 합니다.
     *
     * @param recipientEmail 송장 이메일을 수신할 사용자의 이메일 주소를 나타내는 {@code String}.
     * @param invoiceDTO 이메일로 전송할 송장 정보를 담고 있는 {@code InvoiceDTO} 객체.
     * @throws IOException 이메일 전송 과정에서 입출력 오류가 발생한 경우 발생합니다.
     * @throws MessagingException 이메일 메시지 구성 또는 전송 과정에서 문제가 발생한 경우 발생합니다.
     */

    void sendInvoiceAsEmail(String recipientEmail, InvoiceDTO invoiceDTO) throws IOException, MessagingException;


}
