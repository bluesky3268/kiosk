package korIT.kiosk.configure;

import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class Init {

    private final MemberService memberService;

    // 실행 시 role을 검색해서 supervisor가 없으면 root로 등록
    @PostConstruct
    public void init() {
        List<MemberDTO> supervisorList = memberService.findByRole("ROLE_SUPERVISOR");
        if (supervisorList.isEmpty()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String regDate = sdf.format(timestamp);
            MemberDTO member = new MemberDTO("ROOT", "1234", "1234", "ROLE_SUPERVISOR", regDate);

            memberService.joinInit(member);
        }
    }
}
