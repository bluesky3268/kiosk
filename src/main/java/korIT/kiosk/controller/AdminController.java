package korIT.kiosk.controller;

import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.dto.MemberLoginDTO;
import korIT.kiosk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Member;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/main")
    public String main() {
        return "admin/admin_main";
    }

    @GetMapping("/join")
    public String join() {
        return "admin/join";
    }

    @PostMapping("/join")
    public String join(MemberDTO memberDTO) {
        log.info("[ memberDTO params : " + memberDTO + " ]");
        memberService.join(memberDTO);
        log.info("등록 성공");
        return "/admin/login";
    }

    @PostMapping(value = "/duplicateCheck")
    @ResponseBody
    public String duplicateCheck(@RequestBody String shop) {
        String shopName = shop.trim();
        boolean duplicateCheck = memberService.duplicateShopCheck(shopName);
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

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(MemberLoginDTO loginDTO, HttpSession session) {
        log.info("[ params for login = " + loginDTO + " ]");

        String login = memberService.login(loginDTO);
        if (login.equals("0")) {
            session.setAttribute("shop", loginDTO.getShop());
            return "redirect:/admin/main";
        } else if (login.equals("1")) {
            return "/admin/login";
        } else {
            return "/admin/login";
        }
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session, ModelAndView mv) {
        if (session != null) {
            session.invalidate();
            mv.addObject("msg", "로그아웃되었습니다.");
            mv.setViewName("redirect:/admin/login");
            log.info("로그아웃 성공");
            return mv;
        } else {
            mv.addObject("msg", "로그인이 먼저 필요합니다.");
            mv.setViewName("redirect:/admin/login");
            return mv;
        }
    }
}
