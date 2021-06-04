package korIT.kiosk.service;

import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.dto.MemberLoginDTO;
import korIT.kiosk.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.ObjectUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;


    // 가게 이름 중복 확인
    public boolean duplicateShopCheck(String username) {
        MemberDTO findShop = memberMapper.findByUsername(username);
        log.info("shopName duplicateCheck : " + findShop);
        if (isEmpty(findShop)) {
            return true;
        }
        return false;
    }

    //회원가입
    public void join(MemberDTO member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        if(member.getRole().equals("manager")){
            member.setRole("ROLE_MANAGER");
        }else{
            member.setRole("ROLE_SUPERVISOR");
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(timestamp);
        member.setReg_date(format);

        log.info("join_memberDTO : " + member);
        memberMapper.insertMember(member);
    }

    // 가게 리스트
    public List<MemberDTO> getMemberList() {
        List<MemberDTO> members = memberMapper.findMembers();
        return members;
    }

//    // 로그인
//    public String login(MemberLoginDTO loginDTO) {
//        log.info("[ loginDTO : " + loginDTO + " ]");
//
//        MemberDTO findMember = memberMapper.findByUsername(loginDTO.getUsername());
//        log.info("[ findMember : " + findMember + " ]");
//
//        if (findMember != null && (loginDTO.getUsername().equals(findMember.getUsername()))) {
//            if (loginDTO.getPassword().equals(findMember.getPassword())) {
//                // 로그인 성공
//                log.info("로그인 성공 : " + findMember.getUsername());
//                return "0";
//            } else {
//                // 비밀번호 틀림
//                log.info("올바르지 않은 비밀번호");
//                return "1";
//            }
//        } else {
//            // 아이디가 없음
//            log.info("해당 아이디 없음");
//            return "2";
//        }
//    }

    // 권한 조회
    public String findRole(String shop) {
        MemberDTO member = memberMapper.findByUsername(shop);
        return member.getRole();

    }

    public List<MemberDTO> findByRole(String role) {
        List<MemberDTO> membersByRole = memberMapper.findByRole(role);
        return membersByRole;
    }


    // Security Session -> Authentication -> UserDetails(PrincipalDetails)
    // 값이 Authentication으로 리턴된다.
    // 그리고 Security session에는 Authentication이 저장된다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("param login shopName : " + username);
        MemberDTO member = memberMapper.findByUsername(username);

        log.info("login member : " + member);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole()));

        log.info("login memeber _ role" + authorities);

        return new User(member.getUsername(), member.getPassword(), authorities);
    }


}
