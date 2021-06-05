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

    // 상품 등록
    public void insertItem(ItemDTO item, MultipartFile img) {

        // 상품 이미지 파일 설정
        String fileName = UUID.randomUUID().toString() + "."
                + img.getOriginalFilename().substring(img.getOriginalFilename().indexOf(".")+1);
        Path itemImgPath = Paths.get(uploadDir, fileName);

        try {
            Files.write(itemImgPath, img.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        item.setItemImg(fileName);

        setSoldOut(item);

        log.info("저장될 상품 : " + item);
        // item 저장
        itemMapper.insertItem(item);
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
