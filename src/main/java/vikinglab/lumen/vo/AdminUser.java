package vikinglab.lumen.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminUser {

    private String token;
    private Long adminUserSq;
    private String id;
    private String password;
    private String userName;
    private String accountName;
    private String accountNumber;
    private int isAdmin;

}