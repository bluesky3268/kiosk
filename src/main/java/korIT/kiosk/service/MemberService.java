package korIT.kiosk.service;

import korIT.kiosk.dto.Criteria;
import korIT.kiosk.dto.MemberDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.util.ObjectUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    // 이미지 파일 업로드 디렉토리 설정
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/shopImg";


    // 가게 이름 중복 확인
    public boolean duplicateShopCheck(String username) {
        MemberDTO findShop = memberMapper.findByUsername(username);
        log.info("shopName duplicateCheck : " + findShop);
        if (isEmpty(findShop)) {
            return true;
        }
        return false;
    }

    // 초기 슈퍼바이저 등록
    public void joinInit(MemberDTO memberDTO) {

        // 비밀번호 암호화
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        // 권한 설정
        checkRole(memberDTO);
        // 등록일자 설정
        String format = getDate();
        memberDTO.setRegDate(format);

        log.info("join_memberDTO : " + memberDTO);
        memberMapper.insertMember(memberDTO);
    }

    // 관리자 등록
    public void join(MemberDTO member, MultipartFile img) {

        // 이미지 파일 설정
        String fileName = setImgFile(img);
        member.setImg(fileName);
        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        // 권한 설정
        checkRole(member);
        // 등록일자 설정
        String format = getDate();
        member.setRegDate(format);

        log.info("join_memberDTO : " + member);
        memberMapper.insertMember(member);
    }

    /**
     * join()에 사용하는 메서드
     */
    // 권한 확인 후 설정
    private void checkRole(MemberDTO member) {
        if (member.getRole().equals("manager")) {
            member.setRole("ROLE_MANAGER");
        } else {
            member.setRole("ROLE_SUPERVISOR");
        }
    }
    // 날짜 설정
    private String getDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(timestamp);
    }
    // 이미지 파일 설정
    private String setImgFile(MultipartFile img) {
        String fileName = UUID.randomUUID().toString() + "."
                + img.getOriginalFilename().substring(img.getOriginalFilename().indexOf(".") + 1);
        log.info("img.getOriginalFilename : " + img.getOriginalFilename());
        Path imgPath = Paths.get(uploadDir, fileName);
        try {
            Files.write(imgPath, img.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 조회
     */
    // 매장 리스트 조회
    public List<MemberDTO> getMemberList() {
        List<MemberDTO> members = memberMapper.findMembers();
        return members;
    }

    // 권한 조회
    public String findRole(String shop) {
        MemberDTO member = memberMapper.findByUsername(shop);
        return member.getRole();

    }

    public List<MemberDTO> findByRole(String role) {
        List<MemberDTO> membersByRole = memberMapper.findByRole(role);
        return membersByRole;
    }

    public MemberDTO findByUsername(String username) {
        MemberDTO member = memberMapper.findByUsername(username);
        return member;
    }

    public MemberDTO findByMemberId(int memberId) {
        MemberDTO member = memberMapper.findByMemberId(memberId);
        return member;
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

        log.info("login member _ role" + authorities);

        return new User(member.getUsername(), member.getPassword(), authorities);
    }

    // 페이징 처리
    public List<MemberDTO> findManagersWithPaging(Criteria cri) {
       return memberMapper.findManagersWithPaging(cri);
    }

    public int countManagers(String role) {
        return memberMapper.countManagers(role);
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

}
