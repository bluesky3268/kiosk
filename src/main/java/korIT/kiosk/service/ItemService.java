package korIT.kiosk.service;

import korIT.kiosk.dto.ItemDTO;
import korIT.kiosk.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;

    public void insertItem(ItemDTO item) {
      itemMapper.insertItem(item);
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
