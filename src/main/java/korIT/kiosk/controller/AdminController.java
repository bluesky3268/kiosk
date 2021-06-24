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

    /**
     * 메인페이지
     */

    // 관리자용 메인페이지
    @GetMapping("/main")
    public String main() {
        return "administration/admin_main";
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

    /**
     * 매장 매니저 사용 페이지들
     */

    // 매장 매니저용 페이지
    @GetMapping("/manager")
    public String manager() {
        return "administration/manager/manager";
    }

    // 상품 등록
    @GetMapping("/manager/itemAddForm")
    public String itemAddForm(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        return "administration/manager/itemAdd";
    }

    @PostMapping("/manager/itemAdd")
    public String itemAdd(ItemDTO itemDTO, @RequestParam("item_img") MultipartFile img, HttpSession session) {

        log.info("params itemDTO : " + itemDTO);
        log.info("MultipartFile : " + img);

        String name = (String) session.getAttribute("name");
        MemberDTO findMember = memberService.findByUsername(name);
        int id = findMember.getId();
        itemDTO.setMemberId(id);

        log.info("저장할 상품 : " + itemDTO + ", " + img);

        itemService.insertItem(itemDTO, img);

        log.info("상품등록 완료!");

        return "redirect:/admin/manager/itemList";
    }

    // 상품 목록
    @GetMapping("/manager/itemList")
    public String itemList(Criteria cri, Model model, HttpSession session) {

        // 세션에서 해당 매장 아이디 가져와서 id값으로 상품 검색
        String username = (String) session.getAttribute("name");
        MemberDTO findShop = memberService.findByUsername(username);
        String memberId = String.valueOf(findShop.getId());
        cri.setMemberId(memberId);

        log.info("username : " + username);
        log.info("memberId : " + memberId);
        log.info("parameter cri : " + cri);

        List<ItemDTO> itemListWithPaging = itemService.getItemListWithPaging(cri);

        PageDTO pageMaker = new PageDTO(cri, 123);

        model.addAttribute("username", username);
        model.addAttribute("memberId", memberId);
        model.addAttribute("list", itemListWithPaging);
        model.addAttribute("pageMaker", pageMaker);
        log.info("pages : " + pageMaker);



        return "administration/manager/itemList";
    }

    // 상품 수정
    @GetMapping({"/manager/itemEdit/{id}", "/supervisor/itemEdit/{id}"})
    public String itemEdit(@PathVariable("id") int id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("name");

        ItemDTO findItem = itemService.findByItemId(id);

        log.info("findItem : " + findItem);

        // 로그인한 사용자의 권한이 슈퍼바이져일 경우 슈퍼바이저의 이름이 상품 수정 폼의 가게 이름에 나타나게 되므로
        // session에 저장해 놓은 role의 value가 [ROLE_SUPERVISOR]라면
        // 수정할 상품에 해당하는 매장의 이름을 model에 추가해주고, 그렇지 않을 경우 session에 저장되어 있는 name을 그대로 사용한다.
        if (session.getAttribute("role").equals("[ROLE_SUPERVISOR]")) {
            MemberDTO findShop = memberService.findByMemberId(findItem.getMemberId());
            model.addAttribute("username", findShop.getUsername());
        }else{
            model.addAttribute("username", username);
        }
        model.addAttribute("item", findItem);
        session.setAttribute("itemImg", findItem.getItemImg());
        return "administration/manager/itemEdit";
    }

    @PostMapping("/manager/itemEdit/{id}")
    public String itemEdit(@PathVariable("id") int itemId, ItemDTO item, MultipartFile img, HttpSession session) {
        String itemImg = (String) session.getAttribute("itemImg");
        if (img == null) {
            item.setItemImg(itemImg);
        }

        String username = (String) session.getAttribute("name");
        MemberDTO findShop = memberService.findByUsername(username);
        item.setMemberId(findShop.getId());

        item.setItemId(itemId);

        itemService.updateItem(item, img);
        if(session.getAttribute("role").equals("[ROLE_SUPERVISOR]")){
            return "redirect:/admin/supervisor/itemList";
        }
        return "redirect:/admin/manager/itemList";
    }

    @GetMapping("/manager/itemDelete/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        itemService.deleteItem(id);
        return "redirect:/admin/manager/itemList";
    }


    /**
     * 푸드코트 슈퍼바이저 사용 페이지들
     */

    // 슈퍼바이저용 페이지
    @GetMapping("/supervisor/manageShop")
    public String manageShop_supervisor() {
        return "administration/supervisor/manageShop";
    }

    // 슈퍼바이저용 페이지
    @GetMapping("/supervisor/manageItem")
    public String manageItem_supervisor() {
        return "administration/supervisor/manageItem";
    }

    // 관리자 등록 폼 페이지
    @GetMapping("/supervisor/join")
    public String join() {
        return "administration/supervisor/join";
    }

    // 관리자 등록
    @PostMapping("/supervisor/join")
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

    // 매장 목록
    @GetMapping("/supervisor/shopList")
    public String shopList(Model model, Criteria cri) {

        cri.setRole("ROLE_MANAGER");
        List<MemberDTO> managers = memberService.findManagersWithPaging(cri);
        log.info("cri_parameter : " + cri);
        log.info("memberList : " + managers);

        int countManager = memberService.countManagers("ROLE_MANAGER");
        log.info("countManager : " + countManager);

        PageDTO pageMaker = new PageDTO(cri, countManager);
        log.info("startPage : " +  pageMaker.getStartPage() + ", endPage : " + pageMaker.getEndPage() + ", pageNum : " + cri.getPageNum());

        model.addAttribute("managerList", managers);
        model.addAttribute("pageMaker", pageMaker);
        return "administration/supervisor/shopList";
    }

    @GetMapping("/supervisor/itemList")
    public String itemList(Model model) {
        List<MemberDTO> memberList = memberService.findByRole("ROLE_MANAGER");


        model.addAttribute("memberList", memberList);

        return "administration/supervisor/itemList";
    }

    @GetMapping("/supervisor/itemList/detail")
    public String itemListByShop(@RequestParam("member") int id, Model model, Criteria cri) {
        String memberId = String.valueOf(id);
        MemberDTO member = memberService.findByMemberId(id);
        log.info("member : " + member);


        cri.setMemberId(memberId);
        List<ItemDTO> itemList = itemService.getItemListWithPaging(cri);
        log.info("itemList" + itemList);

        int countItems = itemService.countItems(memberId);

        PageDTO pageMaker = new PageDTO(cri, countItems);


        String username = member.getUsername();
        model.addAttribute("username", username);
        model.addAttribute("itemList", itemList);
        model.addAttribute("pageMaker", pageMaker);
        return "administration/supervisor/itemListDetail";
    }

}
