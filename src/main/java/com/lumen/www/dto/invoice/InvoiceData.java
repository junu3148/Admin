package com.lumen.www.dto.invoice;

import lombok.Data;

@Data
public class InvoiceData {
    private String invoiceNumber;
    private String dateOfIssue;
    private String dateDue;
    private String billToName;
    private String shipToName;
    private String subTotal;
    private String tax;
    private String total;
}
