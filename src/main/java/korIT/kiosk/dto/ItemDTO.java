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
    private String price;
    private String isSoldOut; // 품절상태(true, DB값 : 1) 판매중(false, DB값 : 0)
    private String itemImg;
    private int memberId;

    //테스트용 생성자
   public ItemDTO(String itemName, String price, int memberId) {
        this.itemName = itemName;
        this.price = price;
        this.memberId = memberId;
    }
}
