package vikinglab.lumen.vo;

import lombok.Data;

import java.util.Date;

@Data
public class QnaVO {
    private String user;
    private String inquiryDate;
    private String inquiryDetails;
    private String subscriptionPlan;
    private String answerStatus;
}
