package vikinglab.lumen.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vikinglab.lumen.vo.Criteria;

@Getter
@Setter
@ToString
public class PageMakerDTO {
    private int startPage, endPage, total;    //시작, 끝 페이지, 전체 게시물
    private boolean prev, next;        //이전, 다음 페이지 유무
    private Criteria cri;            //현재 페이지, 페이지당 게시물 표시수 정보

    public PageMakerDTO(int total, Criteria cri) {
        this.total = total; // 전체 게시물 수를 인자로 받아 멤버 변수에 저장
        this.cri = cri; // Criteria 객체를 인자로 받아 멤버 변수에 저장

        /* 마지막 페이지 계산 */
        this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0)) * 10;

        /* 시작 페이지 계산 */
        this.startPage = this.endPage - 9;

        /* 전체 마지막 페이지 계산 */
        int realEnd = (int) (Math.ceil(total * 1.0 / cri.getAmount()));

        /* 전체 마지막 페이지(realEnd)가 화면에 보이는 마지막 페이지(endPage)보다 작은 경우, 보이는 페이지(endPage) 값을 조정 */
        if (realEnd < this.endPage) {
            this.endPage = realEnd;
        }

        /* 시작 페이지(startPage) 값이 1보다 큰 경우 true */
        this.prev = this.startPage > 1;

        /* 마지막 페이지(endPage) 값이 전체 마지막 페이지(realEnd)보다 작은 경우 true */
        this.next = this.endPage < realEnd;
    }

}