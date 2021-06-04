package korIT.kiosk.dto;

import korIT.kiosk.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO{
    private int id;
    private String username;
    private String password;
    private String pwd_confirm;
    private String location;
    private String role;
    private String reg_date;
    private String img;


    //Test용 생성자
    public MemberDTO(String username, String password, String pwd_confirm, String role, String reg_date) {
        this.username = username;
        this.password = password;
        this.pwd_confirm = pwd_confirm;
        this.location = location;
        this.role = role;
        this.reg_date = reg_date;
    }
}

