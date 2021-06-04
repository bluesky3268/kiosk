package korIT.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO {
    private int itemId;
    private String itemName;
    private int price;
    private String itemImg;
    private String memberId;
    private boolean isSoldOut; // 품절상태(true, DB값 : 1) 판매중(false, DB값 : 0)


    //테스트용 생성자
   public ItemDTO(String itemName, int price, String img, String memberId) {
        this.itemName = itemName;
        this.price = price;
        this.itemImg = img;
        this.memberId = memberId;
    }
}
