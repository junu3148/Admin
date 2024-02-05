package com.lumen.www.service;
// 기존의 import 문은 그대로 유지합니다.

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.lumen.www.dto.invoice.InvoiceDTO;
import com.lumen.www.dto.invoice.InvoiceData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        float pageWidth = pdf.getDefaultPageSize().getWidth();

        // 날짜 형식을 변환합니다.
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

        // invoiceData에서 날짜를 파싱
        LocalDate issuedDate = LocalDate.parse(invoiceData.getDateOfIssue(), inputFormatter);
        LocalDate dueDate = LocalDate.parse(invoiceData.getDateDue(), inputFormatter);
        LocalDate dueDatePlusOneYear = dueDate.plusYears(1);

        // 파싱된 날짜를 새로운 형식으로 변환
        String formattedIssueDate = issuedDate.format(outputFormatter);
        String formattedDueDate = dueDate.format(outputFormatter);
        String formattedDueDatePlusOneYear = dueDatePlusOneYear.format(outputFormatter);

        // 폰트 생성
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);


        float padding = 10; // 패딩 값 설정, 단위는 포인트(pt)


        // 1. 인보이스 헤더를 추가합니다.
        document.add(new Paragraph("Invoice").setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));


        // 'src/main/resources/static/logo.jpg' 경로에 있는 이미지를 로드
        ClassPathResource classPathResource = new ClassPathResource("static/logo.jpg");
        ImageData imageData = ImageDataFactory.create(classPathResource.getURL());
        Image image = new Image(imageData);

        // 이미지 크기 및 위치 조절 (옵션)
        image.scaleToFit(40, 40); // 예: 너비 50, 높이 50으로 크기 조절

        // 이미지를 오른쪽 맨 위에 배치
        float x = pageWidth - 40 - 30; // 페이지 너비에서 이미지 너비와 여백을 뺀 위치
        float y = pdf.getDefaultPageSize().getTop() - 40 - 30; // 상단 여백을 고려한 위치
        image.setFixedPosition(1, x, y); // 첫 번째 페이지에 이미지 위치 지정

        // 이미지를 문서에 추가

        // 이미지를 문서에 추가
        document.add(image);


        // 2. "Invoice Number", "Date of issue", "Date due"를 굵게 표시하고, 나머지는 기본 스타일을 사용
        Text invoiceNumberLabel = new Text("Invoice Number       ").setBold();
        Text dateOfIssueLabel = new Text("Date of issue           ").setBold();
        Text dateDueLabel = new Text("Date due                 ").setBold();

        // DTO에서 값 가져오기
        Text invoiceNumberValue = new Text(dto.getInvoiceCode() + "\n");
        Text dateOfIssueValue = new Text(formattedIssueDate + "\n");
        Text dateDueValue = new Text(formattedDueDate + "\n");


        // Paragraph 객체 생성 및 Text 객체들 추가, 여기서는 고정된 줄 간격을 설정합니다.
        Paragraph invoiceInfoParagraph = new Paragraph()
                .setFixedLeading(20) // 줄 간격을 14 포인트로 설정
                .add(invoiceNumberLabel)
                .add(invoiceNumberValue)
                .add(dateOfIssueLabel)
                .add(dateOfIssueValue)
                .add(dateDueLabel)
                .add(dateDueValue)
                .setFont(font); // 폰트 설정은 전체 Paragraph에 적용됩니다.

        document.add(invoiceInfoParagraph);


        document.add(new Paragraph("\n")); // 여기에 공백 추가


        // 3. 공급자 정보 테이블 생성
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{5, 4, 4})).useAllAvailableWidth();
        int fontSize1 = 9;

        // 'Lumen, Inc.' 제목과 상세 정보를 별도의 Paragraph로 추가
        Paragraph supplierTitle = new Paragraph(new Text("Lumen, Inc.").setBold()).setPaddingBottom(padding - 3);
        Paragraph supplierInfo = new Paragraph(new Text("1102-ho, B-dong,\nHanam-daero 947, Hanam-si,\nGyeonggido\nKorea\n+82 123 4567\nhelp@lumeninc.com").setFontSize(fontSize1));
        // Cell에 제목과 상세 정보 추가
        infoTable.addCell(new Cell().add(supplierTitle).add(supplierInfo).setFont(font).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));

        // 'Bill to' 제목과 상세 정보를 별도의 Paragraph로 추가
        Paragraph billToTitle = new Paragraph(new Text("Bill to").setBold()).setPaddingBottom(padding - 3);
        Paragraph billToInfo = new Paragraph(new Text("Company Name\nAddress 1\nAddress 2\nKorea\nZip Code\nEmail Address").setFontSize(fontSize1));
        // Cell에 제목과 상세 정보 추가
        infoTable.addCell(new Cell().add(billToTitle).add(billToInfo).setFont(font).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));

        // 'Ship to' 제목과 상세 정보를 별도의 Paragraph로 추가
        Paragraph shipToTitle = new Paragraph(new Text("Ship to").setBold()).setPaddingBottom(padding - 3);
        Paragraph shipToInfo = new Paragraph(new Text("Company Name\nAddress 1\nAddress 2\nKorea\nZip Code\nEmail Address").setFontSize(fontSize1));
        // Cell에 제목과 상세 정보 추가
        infoTable.addCell(new Cell().add(shipToTitle).add(shipToInfo).setFont(font).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));


        // 테이블을 문서에 추가
        document.add(infoTable);
        document.add(new Paragraph("\n")); // 여기에 공백 추가


        // 4.
        document.add(new Paragraph("$" + invoiceData.getTotal() + ".00 USD due " + formattedIssueDate).setBold().setFontSize(18).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("\n")); // 여기에 공백 추가


        // 5. 상품 정보를 테이블에 추가합니다.
        Table table = new Table(UnitValue.createPercentArray(new float[]{5, 1, 1, 1})).useAllAvailableWidth();

        // Define a solid border with 1 point thickness
        Border solidBorder = new SolidBorder(1);
        // Define a light gray solid border with 0.5 point thickness
        Border lightGrayBorder = new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f);


        // 헤더 셀에 선 제거 적용 및 굵게 설정
        table.addHeaderCell(new Cell().add(new Paragraph("Description")).setBorder(Border.NO_BORDER).setBorderBottom(solidBorder).setBold().setPaddingBottom(padding));
        table.addHeaderCell(new Cell().add(new Paragraph("Qty")).setBorder(Border.NO_BORDER).setBorderBottom(solidBorder).setBold().setPaddingBottom(padding));
        table.addHeaderCell(new Cell().add(new Paragraph("Unit price")).setBorder(Border.NO_BORDER).setBorderBottom(solidBorder).setBold().setPaddingBottom(padding));
        table.addHeaderCell(new Cell().add(new Paragraph("Amount").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER).setBorderBottom(solidBorder).setBold().setPaddingBottom(padding));

        String planType = (dto.getPlanType().equals("연간")) ? "annual" : "monthly";
        // 데이터 셀에 선 제거 적용
        table.addCell(new Cell().add(new Paragraph("LUMEN " + dto.getPlanName() + " (" + planType + ")\n" + formattedDueDate + " ~ " + formattedDueDatePlusOneYear + "\n\u00A0")).setBorder(Border.NO_BORDER).setPaddingTop(padding)).setBorderBottom(lightGrayBorder);
        table.addCell(new Cell().add(new Paragraph(String.valueOf(dto.getSubRound()))).setBorder(Border.NO_BORDER).setPaddingTop(padding)).setBorderBottom(lightGrayBorder);
        table.addCell(new Cell().add(new Paragraph("$" + dto.getPlanPrice())).setBorder(Border.NO_BORDER).setPaddingTop(padding)).setBorderBottom(lightGrayBorder);
        table.addCell(new Cell().add(new Paragraph("$" + dto.getPlanPrice()).setTextAlignment(TextAlignment.RIGHT).setPaddingTop(padding)).setBorder(Border.NO_BORDER));

        document.add(table);

        document.add(new Paragraph("\n")); // 여기에 공백 추가
        document.add(new Paragraph("\n")); // 여기에 공백 추가


        // 6. Create a table with four columns
        Table financialTable = new Table(UnitValue.createPercentArray(new float[]{5, 5, 3, 3})).useAllAvailableWidth();
        int fontSize2 = 10;

        // First two columns will be empty cells, the last two will contain the data
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().add(new Paragraph("Subtotal")).setFontSize(fontSize2).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));
        financialTable.addCell(new Cell().add(new Paragraph("$" + invoiceData.getSubTotal() + ".00")).setFontSize(fontSize2)
                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));

        // Repeat the same structure for other rows
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().add(new Paragraph("Total excluding tax")).setFontSize(fontSize2).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));
        financialTable.addCell(new Cell().add(new Paragraph("$" + invoiceData.getSubTotal() + ".00")).setFontSize(fontSize2)
                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));

        // Add a row for Tax
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().add(new Paragraph("Tax")).setFontSize(fontSize2).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));
        financialTable.addCell(new Cell().add(new Paragraph("$" + invoiceData.getTax() + ".00")).setFontSize(fontSize2)
                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));

        // Add a row for Total
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().add(new Paragraph("Total")).setFontSize(fontSize2).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));
        financialTable.addCell(new Cell().add(new Paragraph("$" + invoiceData.getTotal() + ".00")).setFontSize(fontSize2)
                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));

        // Add a bold row for Amount Due
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        financialTable.addCell(new Cell().add(new Paragraph("Amount due")).setBold().setFontSize(fontSize2).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));
        financialTable.addCell(new Cell().add(new Paragraph("$" + invoiceData.getTotal() + ".00 USD")).setFontSize(fontSize2)
                .setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderTop(lightGrayBorder).setPaddingTop(padding).setPaddingBottom(padding));


        document.add(financialTable);
        // 문서를 닫습니다.
        document.close();
    }


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
