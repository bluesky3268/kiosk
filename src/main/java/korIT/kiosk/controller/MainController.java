package korIT.kiosk.controller;

import korIT.kiosk.dto.Criteria;
import korIT.kiosk.dto.ItemDTO;
import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.dto.PageDTO;
import korIT.kiosk.service.ItemService;
import korIT.kiosk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/")
    public String main(Model model, Criteria cri) {

        List<MemberDTO> memberList = memberService.findByRole("ROLE_MANAGER");
        String memberId = String.valueOf(memberList.get(0).getId());
        List<ItemDTO> itemList = itemService.findItemsByMemberId(memberId);

        model.addAttribute("memberList", memberList);
        model.addAttribute("itemList", itemList);

        log.info("memberList : " + memberList);
        return "main";
    }

}
