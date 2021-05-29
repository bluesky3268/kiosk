package korIT.kiosk.controller;

import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final MemberService memberService;

    @GetMapping("")
    public String main() {
        return "admin/admin_main";
    }

    @GetMapping("/join")
    public String join() {
        return "admin/join";
    }

    @PostMapping("/join")
    @ResponseBody
    public String join(@RequestBody MemberDTO memberDTO) {
        memberService.join(memberDTO);
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
        return "/admin/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(ModelAndView modelAndView) {
        return "로그인 성공";
    }
}
