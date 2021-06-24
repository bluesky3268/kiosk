package korIT.kiosk.dto;

import lombok.Data;

@Data
public class Criteria {
    private int pageNum;
    private int amount;
    private String memberId;
    private String role;

    public Criteria() {
        this(1, 5);
    }

    public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }
//
//    public Criteria(int pageNum, int amount, String memberId) {
//       this.pageNum = pageNum;
//       this.amount = amount;
//       this.memberId = memberId;
//    }

}
