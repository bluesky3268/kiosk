package korIT.kiosk.service;

import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.dto.MemberLoginDTO;
import korIT.kiosk.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.springframework.util.ObjectUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberMapper memberMapper;

    // 가게 이름 중복 확인
    public boolean duplicateShopCheck(String shopName) {
        MemberDTO findShop = memberMapper.findByShop(shopName);
        if (isEmpty(findShop)) {
            return true;
        }
        return false;
    }

    //회원가입
    public void join(MemberDTO member) {
        memberMapper.insertMember(member);
    }

    // 로그인
    public String login(MemberLoginDTO loginDTO) {
        log.info("[ loginDTO : " + loginDTO + " ]");

        MemberDTO findMember = memberMapper.findByShop(loginDTO.getShop());
        log.info("[ findMember : " + findMember + " ]");

        if (findMember != null && (loginDTO.getShop().equals(findMember.getShop()))) {
            if (loginDTO.getPwd().equals(findMember.getPwd())) {
                // 로그인 성공
                log.info("로그인 성공 : " + findMember.getShop());
                return "0";
            } else{
                // 비밀번호 틀림
                log.info("올바르지 않은 비밀번호");
                return "1";
            }
        }else{
            // 아이디가 없음
            log.info("해당 아이디 없음");
            return "2";
        }
    }





}