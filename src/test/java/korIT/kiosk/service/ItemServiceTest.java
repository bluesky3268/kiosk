package korIT.kiosk.service;

import korIT.kiosk.dto.ItemDTO;
import korIT.kiosk.mapper.ItemMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemMapper itemMapper;



    @Test
    @Transactional
    @Rollback(value=true)
    public void 아이템추가() {
        ItemDTO itemDTO = new ItemDTO("국수", 3000, "이미지", "4");

        itemMapper.insertItem(itemDTO);

        ItemDTO findItem = itemMapper.findByItemName("국수");
        System.out.println("findItem : " + findItem);

        Assertions.assertThat(3000).isEqualTo(findItem.getPrice());

    }

}