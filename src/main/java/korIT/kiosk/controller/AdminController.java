package korIT.kiosk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.dto.MemberLoginDTO;
import korIT.kiosk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final MemberService memberService;

    // 관리자용 메인페이지
    @GetMapping("/main")
    public String main() {
        return "administration/admin_main";
    }

    // 관리자 등록 폼 페이지
    @GetMapping("/join")
    public String join() {
        return "administration/join";
    }

    // 관리자 등록
    @PostMapping("/join")
    public String join(MemberDTO memberDTO, @RequestParam("thumbImg")MultipartFile img) {
        log.info("[ memberDTO params : " + memberDTO + " ]" + "imageFile : " + img);
        memberService.join(memberDTO, img);
        log.info("등록 성공");
        return "redirect:/admin/main";
    }

    // 관리자 등록시 중복이름 체크
    @PostMapping(value = "/duplicateCheck")
    @ResponseBody
    public String duplicateCheck(@RequestBody HashMap<String, Object> shop) {

        String checkName = String.valueOf(shop.get("shop"));
        boolean duplicateCheck = memberService.duplicateShopCheck(checkName);
        if (duplicateCheck) {
            // 아이디 사용가능
            log.info("아이디 사용가능");
            return "0";
        } else {
            // 중복된 아이디 있음
            log.info("아이디 중복");
            return "1";
        }
    }

    //로그인 폼 페이지
    @GetMapping("/loginForm")
    public String loginForm() {
        return "administration/loginForm";
    }

    //    @PostMapping("/login")
//    public String login(MemberLoginDTO loginDTO, HttpSession session) {
//        log.info("[ params for login = " + loginDTO + " ]");
//
//        String login = memberService.login(loginDTO);
//        if (login.equals("0")) {
//            session.setAttribute("username", loginDTO.getUsername());
//            log.info("로그인 성공");
//            return "redirect:/admin/main";
//        } else if (login.equals("1")) {
//            return "redirect:/admin/loginForm";
//        } else {
//            return "redirect:/admin/loginForm";
//        }
//    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
            log.info("로그아웃 성공");
            return "redirect:/admin/main";
        } else {
            return "redirect:/admin/login";
        }
    }
    // 매장 매니저용 페이지
    @GetMapping("/manager")
    public String manager() {
        return "administration/manager";
    }

    // 슈퍼바이저용 페이지
    @GetMapping("/supervisor")
    public String supervisor() {
        return "administration/supervisor";
    }

    //
    @GetMapping("/shopList")
    public String shopList(Model model) {

        List<MemberDTO> managerList = memberService.findByRole("ROLE_MANAGER");
        model.addAttribute("managerList", managerList);
        log.info("managerList : " + managerList);
        return "administration/shopList";
    }
}
