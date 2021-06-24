package korIT.kiosk.dto;

import lombok.Data;

@Data
public class PageDTO {
    private int startPage;
    private int endPage;
    private boolean prev, next;
    private int total;
    private Criteria cri;

    public PageDTO(Criteria cri, int total) {
        this.total = total;
        this.cri = cri;

        this.endPage = (int)(Math.ceil(cri.getPageNum()/5.0))*5; // 1페이지를 불러올 때 5페이지까지 표시
        this.startPage = this.endPage - 4;

        int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount())); // 총 게시물 개수가 11이면 게시판의 실제 마지막 페이지는 3
        if (realEnd < this.endPage) {
            this.endPage = realEnd;
        }
        this.prev = this.startPage > 1;
        this.next = this.endPage < realEnd;

        // 페이지 게시물 수 11개 -> 전체 페이지 수 3
        // 시작페이지 1 마지막 페이지 3
        // endPage = 2 realEnd = 3

    }
}
