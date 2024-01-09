package com.lumen.www.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
    private int pageNum, amount; // 현재 페이지, 페이지 당 보여질 게시물 갯수
    private int showStat;
    private int keyword;
    private String keyword2;
    private String type; // 검색 키워드, 검색 타입
    // private String[] typeArr; //검색 타입 배열

    // 기본 생성자 -> 기본세팅 : pageNum=1, amount=10
    public Criteria() {
        this(1, 10, 1);
    }

    public Criteria(int pageNum, int amount, String keyword2) {
        super();
        this.pageNum = pageNum;
        this.amount = amount;
        this.keyword2 = keyword2;
    }

    // 생성자 -> 원하는 pageNum, 원하는 amount
    public Criteria(int pageNum, int amount, int keyword) {
        super();
        this.pageNum = pageNum;
        this.amount = amount;
        this.keyword = keyword;
    }

}