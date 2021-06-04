package korIT.kiosk.mapper;

import korIT.kiosk.dto.ItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {

    int insertItem(ItemDTO itemDTO);

    ItemDTO findByItemId(int itemId);

    ItemDTO findByItemName(String ItemName);

//    List<ItemDTO> findItemsByMemberId(String memberId);

    List<ItemDTO> findItems();

    int checkSaleStatus(int itemId);

    int changeToSoldOut(int itemId);

    int changeToForSale(int itemId);




}
