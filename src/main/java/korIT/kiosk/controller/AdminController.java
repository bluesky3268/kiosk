package korIT.kiosk.controller;

import korIT.kiosk.dto.ItemDTO;
import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.service.ItemService;
import korIT.kiosk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final MemberService memberService;
    private final ItemService itemService;

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
    public String join(MemberDTO memberDTO, @RequestParam("shopImg") MultipartFile img) {
        log.info("[ memberDTO params : " + memberDTO + " ]" + "imageFile : " + img);
        memberService.join(memberDTO, img);
        log.info("등록 성공");
        return "redirect:/admin/main";
    }

    // 관리자 등록시 중복이름 체크
    @PostMapping("/duplicateCheck")
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

    // 매장 목록
    @GetMapping("/shopList")
    public String shopList(Model model) {

        List<MemberDTO> managerList = memberService.findByRole("ROLE_MANAGER");
        model.addAttribute("managerList", managerList);
        log.info("managerList : " + managerList);
        return "administration/shopList";
    }

    // 상품 등록
    @GetMapping("/itemAddForm")
    public String itemAddForm(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        return "administration/itemAdd";
    }

    @PostMapping("/itemAdd")
    public String itemAdd(ItemDTO itemDTO, @RequestParam("item_img") MultipartFile img, HttpSession session) {

        log.info("params itemDTO : " + itemDTO);
        log.info("MultipartFile : " + img);

        String name = (String) session.getAttribute("name");
        MemberDTO findMember = memberService.findByUsername(name);
        int id = findMember.getId();
        itemDTO.setMemberId(id);

        log.info("저장할 상품 : " + itemDTO + ", " + img);

        itemService.insertItem(itemDTO, img);

        return "redirect:/admin/itemList";
    }

    // 상품 목록
    @GetMapping("/itemList")
    public String itemList(Model model, HttpSession session) {

        String username = (String) session.getAttribute("name");
        MemberDTO findShop = memberService.findByUsername(username);
        String findShopId = String.valueOf(findShop.getId());
        List<ItemDTO> itemList = itemService.findItemsByMemberId(findShopId);

        log.info("itemList : " + itemList);

        model.addAttribute("username", username);
        model.addAttribute("itemList", itemList);
        return "administration/itemList";
    }

    // 상품 수정
    @GetMapping("/itemEdit/{id}")
    public String itemEdit(@PathVariable("id") int id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("name");
        MemberDTO findShop = memberService.findByUsername(username);

        ItemDTO findItem = itemService.findByItemId(id);

        log.info("findItem : " + findItem);

        session.setAttribute("itemImg", findItem.getItemImg());
        model.addAttribute("username", username);
        model.addAttribute("item", findItem);
        return "administration/itemEdit";
    }

    @PostMapping("/itemEdit/{id}")
    public String itemEdit(@PathVariable("id") int id, ItemDTO item, MultipartFile img, HttpSession session) {
        String itemImg = (String)session.getAttribute("itemImg");
        if (img == null) {
            item.setItemImg(itemImg);
        }

        String username = (String) session.getAttribute("name");
        MemberDTO findShop = memberService.findByUsername(username);
        item.setMemberId(findShop.getId());

        item.setItemId(id);

        itemService.updateItem(item, img);
        return "redirect:/admin/itemList";
    }

    @GetMapping("/itemDelete/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        itemService.deleteItem(id);
        return "redirect:/admin/itemList";
    }

}
