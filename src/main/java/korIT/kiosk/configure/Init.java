package korIT.kiosk.configure;

import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Init {

    private final MemberService memberService;

    @PostConstruct
    public void init() {
        List<MemberDTO> supervisorList = memberService.findByRole("ROLE_SUPERVISOR");
        if (supervisorList.isEmpty()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String regDate = sdf.format(timestamp);
            MemberDTO member = new MemberDTO("ROOT", "1234", "1234", "ROLE_SUPERVISOR", regDate);
            memberService.join(member);
        }
    }
}
