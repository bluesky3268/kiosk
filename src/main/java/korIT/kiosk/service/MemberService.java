package korIT.kiosk.service;

import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.springframework.util.ObjectUtils.*;

@Service
@RequiredArgsConstructor
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


}
