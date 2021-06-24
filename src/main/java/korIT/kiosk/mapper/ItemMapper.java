package korIT.kiosk.mapper;

import korIT.kiosk.dto.Criteria;
import korIT.kiosk.dto.ItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {

    int insertItem(ItemDTO itemDTO);

    void updateItem(ItemDTO itemDTO);

    void deleteItem(int itemId);

    ItemDTO findByItemId(int itemId);

    ItemDTO findByItemName(String ItemName);

    List<ItemDTO> findItemsByMemberId(String memberId);

    List<ItemDTO> findItems();


    // 페이징 처리 메서드

    List<ItemDTO> getItemListWithPaging(Criteria criteria);

    int countItems(String memberId);

    List<ItemDTO> getItemList();

    int getItemListCnt();


}
