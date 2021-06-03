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
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final MemberService memberService;


    @GetMapping("/main")
    public String main() {
        return "administration/admin_main";
    }

    @GetMapping("/join")
    public String join() {
        return "administration/join";
    }

    @PostMapping("/join")
    public String join(MemberDTO memberDTO) {
        log.info("[ memberDTO params : " + memberDTO + " ]");
        memberService.join(memberDTO);
        log.info("등록 성공");
        return "redirect:/admin/main";
    }

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

    @GetMapping("/supervisor")
    public String supervisor() {
        return "administration/supervisor";
    }

    @GetMapping("/manager")
    public String manager() {
        return "administration/manager";
    }

    @GetMapping("/shopList")
    public String shopList(Model model) {
        List<MemberDTO> members = memberService.getMemberList();
        model.addAttribute("members", members);
        log.info("members : " + members);
        return "administration/shopList";
    }
}
