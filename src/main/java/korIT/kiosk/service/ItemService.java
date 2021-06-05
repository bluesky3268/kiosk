package korIT.kiosk.service;

import korIT.kiosk.dto.ItemDTO;
import korIT.kiosk.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;

    // 상품 이미지 파일 경로 저장
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/itemImg";

    /**
     * 등록, 수정, 삭제
     */

    // 상품 등록
    public void insertItem(ItemDTO item, MultipartFile img) {

        setImgFile(item, img);
        setSoldOut(item);

        log.info("저장될 상품 : " + item);
        // item 저장
        itemMapper.insertItem(item);
    }

    // 상품 정보 수정
    public void updateItem(ItemDTO item, MultipartFile img) {
        log.info("수정할 item : " + item);

        if (img != null) {
            setImgFile(item, img);
        }

        setSoldOut(item);
        itemMapper.updateItem(item);

        log.info("수정 성공 : " + item);
    }

    // 상품 삭제
    public void deleteItem(int itemId) {
        itemMapper.deleteItem(itemId);
        log.info(itemId + "번 상품 삭제 성공");
    }

    // 이미지 파일 설정
    private void setImgFile(ItemDTO item, MultipartFile img) {
        String fileName = UUID.randomUUID().toString() + "."
                + img.getOriginalFilename().substring(img.getOriginalFilename().indexOf(".")+1);
        Path itemImgPath = Paths.get(uploadDir, fileName);

        try {
            Files.write(itemImgPath, img.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        item.setItemImg(fileName);
    }

    // 품절 상태 설정
    public void setSoldOut(ItemDTO itemDTO) {
        if (itemDTO.getIsSoldOut().equals("true")) {
            // 품절
            itemDTO.setIsSoldOut("1");
        }else{
            // 판매중
            itemDTO.setIsSoldOut("0");
        }
    }


    /**
     * 조회
     */

    // 가게별 상품 조회
    public List<ItemDTO> findItemsByMemberId(String memberId) {
        List<ItemDTO> itemList = itemMapper.findItemsByMemberId(memberId);
        return itemList;
    }


    public ItemDTO findByItemName(String itemName) {
        ItemDTO findItem = itemMapper.findByItemName(itemName);
        return findItem;
    }

    public ItemDTO findByItemId(int itemId) {
        ItemDTO findItem = itemMapper.findByItemId(itemId);
        return findItem;
    }

    public List<ItemDTO> findItems() {
        List<ItemDTO> items = itemMapper.findItems();
        return items;
    }

}
