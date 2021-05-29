package korIT.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private int id;
    private String shop;
    private String pwd;
    private String pwd_confirm;

    public MemberDTO(String shop, String pwd, String pwd_confirm) {
        this.shop = shop;
        this.pwd = pwd;
        this.pwd_confirm = pwd_confirm;
    }
}
