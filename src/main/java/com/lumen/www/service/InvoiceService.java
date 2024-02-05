package com.lumen.www.service;
// 기존의 import 문은 그대로 유지합니다.

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.lumen.www.dto.invoice.InvoiceDTO;
import com.lumen.www.dto.invoice.InvoiceData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;


    // InvoiceDTO를 받아서 PDF 문서를 생성하고 이메일로 보내는 메서드
    public void sendInvoiceAsEmail(String recipientEmail, InvoiceDTO invoiceDTO) throws IOException, MessagingException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // PDF 문서 생성
        createPdf(outputStream, invoiceDTO);

        // 이메일 발송
        sendEmailWithAttachment(recipientEmail, outputStream.toByteArray(), "Invoice_" + invoiceDTO.getInvoiceCode() + ".pdf");
    }

    private InvoiceData setUpInvoiceDataFromDTO(InvoiceDTO dto) {
        InvoiceData data = new InvoiceData();
        data.setInvoiceNumber(dto.getInvoiceCode());
        // Date of issue와 Date due는 동일하게 설정 (변경 필요 시 수정)
        data.setDateOfIssue(dto.getAccessionDate());
        data.setDateDue(dto.getAccessionDate());
        data.setBillToName(dto.getUserName() + "\n" + dto.getPhoneNumber() + "\n" + dto.getCityProvince() + "\n" + dto.getBasicAddress() + "\n" + dto.getDetailedAddress());
        // Ship to는 예시에서 Bill to와 동일하게 설정 (변경 필요 시 수정)
        data.setShipToName(dto.getUserName() + "\n" + dto.getPhoneNumber() + "\n" + dto.getCityProvince() + "\n" + dto.getBasicAddress() + "\n" + dto.getDetailedAddress());
        // SubTotal, Tax, Total 계산 방식에 따라 설정 필요
        data.setSubTotal(String.valueOf(dto.getPlanPrice()));
        data.setTax("0"); // 세금 계산 로직이 필요함
        // Total은 SubTotal과 Tax를 합한 값 (변경 필요 시 수정)
        data.setTotal(String.valueOf(dto.getPlanPrice())); // 세금을 더하는 로직이 필요함
        // 나머지 필드도 적절히 설정
        // ...
        return data;
    }

    /// PDF 문서 생성 메서드
    private void createPdf(ByteArrayOutputStream outputStream, InvoiceDTO dto) throws IOException {
        // InvoiceDTO에서 필요한 데이터를 추출하여 InvoiceData 객체를 생성합니다.
        InvoiceData invoiceData = setUpInvoiceDataFromDTO(dto);

        // PDF 문서를 만드는 과정입니다.
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 폰트 생성
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);

        // 한글 폰트 설정
        //PdfFont font = PdfFontFactory.createFont("resources/fonts/NanumGothic.ttf", PdfEncodings.IDENTITY_H, true);


        // 인보이스 헤더를 추가합니다.
        document.add(new Paragraph("Invoice").setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(25));


        // 날짜 형식을 변환합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        LocalDate issuedDate = LocalDate.parse(invoiceData.getDateOfIssue(), formatter);
        LocalDate dueDate = LocalDate.parse(invoiceData.getDateDue(), formatter);

        // 인보이스 번호, 발행일, 만기일
        document.add(new Paragraph(String.format("Invoice Number: %s\nDate of issue: %s\nDate due: %s",
                dto.getInvoiceCode(), dto.getIssueDate(), dto.getIssueDate())).setFont(font));

        document.add(new Paragraph("\n")); // 여기에 공백 추가

        // 공급자 정보
          Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                .useAllAvailableWidth();

        infoTable.addCell(new Cell().add(new Paragraph("Lumen, Inc.\n1102-ho, B-dong,\nHanam-daero 947, Hanam-si,\nGyeonggido\nKorea\n+82 123 4567\nhelp@lumeninc.com"))
                .setFont(font).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Bill to\nCompany Name\nAddress 1\nAddress 2\nKorea\nZip Code\nEmail Address"))
                .setFont(font).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Ship to\nCompany Name\nAddress 1\nAddress 2\nKorea\nZip Code\nEmail Address"))
                .setFont(font).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

        document.add(infoTable);

        document.add(new Paragraph("\n")); // 여기에 공백 추가


        // 상품 정보를 테이블에 추가합니다.
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 5, 2, 2}))
                .useAllAvailableWidth();

        // 헤더 셀에 선 제거 적용
        table.addHeaderCell(new Cell().add(new Paragraph("Description")).setBorder(Border.NO_BORDER));
        table.addHeaderCell(new Cell().add(new Paragraph("Qty")).setBorder(Border.NO_BORDER));
        table.addHeaderCell(new Cell().add(new Paragraph("Unit price")).setBorder(Border.NO_BORDER));
        table.addHeaderCell(new Cell().add(new Paragraph("Amount")).setBorder(Border.NO_BORDER));

        // 데이터 셀에 선 제거 적용
        table.addCell(new Cell().add(new Paragraph(dto.getPlanName() + " (" + dto.getPlanType() + ")")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(dto.getSubRound()))).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("$" + dto.getPlanPrice())).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("$" + dto.getPlanPrice())).setBorder(Border.NO_BORDER));

        document.add(table);

        document.add(new Paragraph("\n")); // 여기에 공백 추가



        // 소계, 세금, 총계 및 최종 금액을 오른쪽 정렬로 표시하기 위한 테이블 생성
        Table financialTable = new Table(UnitValue.createPercentArray(new float[]{1, 2})).useAllAvailableWidth();
        // financialTable에 선을 추가합니다.
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("-------------------------------------------------")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("Subtotal                                       $" + invoiceData.getSubTotal())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("-------------------------------------------------")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("Total excluding tax                      $" + invoiceData.getSubTotal())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("-------------------------------------------------")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("Tax                                                $" + invoiceData.getTax())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("-------------------------------------------------")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("Total                                            $" + invoiceData.getTotal())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("-------------------------------------------------")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell(1, 2).add(new Paragraph("Amount due                        $" + invoiceData.getTotal() + " USD")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

        document.add(financialTable);



        // 문서를 닫습니다.
        document.close();
    }


/*

    /// PDF 문서 생성 메서드
    private void createPdf(ByteArrayOutputStream outputStream, InvoiceDTO dto) throws IOException {
        // InvoiceDTO에서 필요한 데이터를 추출하여 InvoiceData 객체를 생성합니다.
        InvoiceData invoiceData = setUpInvoiceDataFromDTO(dto);

        // PDF 문서를 만드는 과정입니다.
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 인보이스 헤더를 추가합니다.
        document.add(new Paragraph("Invoice")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(14));


        // 날짜 형식을 변환합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        LocalDate issuedDate = LocalDate.parse(invoiceData.getDateOfIssue(), formatter);
        LocalDate dueDate = LocalDate.parse(invoiceData.getDateDue(), formatter);

        // 인보이스 번호, 발행일, 만기일을 문서에 추가합니다.
        document.add(new Paragraph(String.format("Invoice Number: %s\nDate of issue: %s\nDate due: %s",
                invoiceData.getInvoiceNumber(), issuedDate, dueDate)));
        // 공급자 정보를 문서에 추가하는 코드 부분
        List supplierList = new List()
                .setSymbolIndent(12)
                .setListSymbol("\u2022");

        // 폰트 생성
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);

        // 리스트 아이템에 폰트 적용
        supplierList.setFont(font);

        supplierList.add(new ListItem("Lumen, Inc."))
                .add(new ListItem("1102-ho, B-dong, Hanam-daero 947, Hanam-si, Gyeonggido"))
                .add(new ListItem("Korea"))
                .add(new ListItem("+82 123 4567"))
                .add(new ListItem("help@lumeninc.com"));

        document.add(supplierList);

        // 청구 정보를 문서에 추가합니다.
        Table billToTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth();

        billToTable.addCell(new Cell().add(new Paragraph("Bill to"))
                .setTextAlignment(TextAlignment.CENTER));
        billToTable.addCell(new Cell().add(new Paragraph("Ship to"))
                .setTextAlignment(TextAlignment.CENTER));
        billToTable.addCell(new Paragraph(invoiceData.getBillToName()));
        billToTable.addCell(new Paragraph(invoiceData.getShipToName()));

        document.add(billToTable);

        // 상품 정보를 테이블에 추가합니다.
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 5, 2, 2}))
                .useAllAvailableWidth();

        table.addHeaderCell("Description");
        table.addHeaderCell("Qty");
        table.addHeaderCell("Unit price");
        table.addHeaderCell("Amount");

        // 상품 이름, 수량, 단가, 금액을 테이블에 추가합니다.
        table.addCell(dto.getPlanName() + " (" + dto.getPlanType() + ")");
        table.addCell(String.valueOf(dto.getSubRound()));
        table.addCell("$" + dto.getPlanPrice());
        table.addCell("$" + dto.getPlanPrice());

        document.add(table);

        // 소계, 세금, 총계를 문서에 추가합니다.
        document.add(new Paragraph(String.format("Subtotal: $%s\nTax: $%s\nTotal: $%s",
                invoiceData.getSubTotal(), invoiceData.getTax(), invoiceData.getTotal())));

        // 금액 표시를 문서에 추가합니다.
        document.add(new Paragraph("Amount due: $" + invoiceData.getTotal() + " USD")
                .setTextAlignment(TextAlignment.RIGHT));

        // 문서를 닫습니다.
        document.close();
    }


*/


    // 이메일 발송 메서드
    private void sendEmailWithAttachment(String recipientEmail, byte[] attachment, String filename) throws MessagingException {

        // MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        // MimeMessageHelper를 사용하여 멀티파트 메시지로 설정
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true로 멀티파트 활성화

        // 이메일 수신자, 제목, 본문 등 설정
        helper.setTo(recipientEmail);
        helper.setSubject("Invoice: " + filename);
        helper.setText("Please find attached your invoice.");
        helper.setFrom(email);

        // 첨부 파일 추가
        helper.addAttachment(filename, new ByteArrayResource(attachment));

        // 이메일 발송
        javaMailSender.send(message);
    }

}
