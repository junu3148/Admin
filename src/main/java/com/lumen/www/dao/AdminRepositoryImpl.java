package com.lumen.www.dao;

import com.lumen.www.dto.common.SearchDTO;
import com.lumen.www.dto.faq.FaqDTO;
import com.lumen.www.dto.inquiry.InquiryDTO;
import com.lumen.www.dto.inquiry.InquiryListDTO;
import com.lumen.www.dto.invoice.InvoiceDTO;
import com.lumen.www.dto.invoice.InvoiceListDTO;
import com.lumen.www.dto.main.UserActivityDTO;
import com.lumen.www.dto.notice.NoticeDTO;
import com.lumen.www.dto.notice.NoticeListDTO;
import com.lumen.www.dto.payment.PayListDTO;
import com.lumen.www.dto.pricing.PriceListDTO;
import com.lumen.www.dto.pricing.PriceSearchDTO;
import com.lumen.www.dto.terms.TermsDTO;
import com.lumen.www.dto.user.AdminDTO;
import com.lumen.www.dto.user.AdminUser;
import com.lumen.www.dto.user.JoinListDTO;
import com.lumen.www.dto.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final SqlSession sqlSession;

    // 아이디 확인
    @Override
    public Optional<AdminUser> findByUsername(String adminId) {
        return Optional.ofNullable(sqlSession.selectOne("admin.findByUsername", adminId));
    }

    // 관리자 권한
    @Override
    public int getRole(String adminId) {
        return sqlSession.selectOne("admin.getRole", adminId);
    }

    // 2차 로그인
    @Override
    public Optional<AdminUser> adminLoginCk(AdminUser adminUser) {
        return Optional.ofNullable(sqlSession.selectOne("admin.adminLoginCk", adminUser));
    }

    // 관리자 세부 정보
    @Override
    public AdminDTO getAdminUser(String adminId) {
        return sqlSession.selectOne("admin.getAdminUser", adminId);
    }

    // 관리자 정부 수정
    @Override
    public int updateAdminUser(AdminUser adminUser) {
        return sqlSession.update("admin.updateAdminUser", adminUser);
    }

    // 가입자 현황
    @Override
    public int subscriberCount() {
        return sqlSession.selectOne("admin.subscriberCount");
    }

    // 메인페이지 월별가입자 그래프
    @Override
    public List<Map<String, Object>> getMonthlySubscriber() {
        return sqlSession.selectList("admin.getMonthlySubscriber");
    }

    // 메인페이지 현황지표
    @Override
    public List<UserActivityDTO> getUserActivity() {
        return sqlSession.selectList("admin.getUserActivity");
    }

    // 문의 리스트
    @Override
    public List<InquiryDTO> getInquiryList() {
        return sqlSession.selectList("admin.getInquiryList");
    }

    // 가입자리스트
    @Override
    public List<JoinListDTO> getJoinList(SearchDTO searchDTO) {
        return sqlSession.selectList("admin.getJoinList", searchDTO);
    }

    // 가입자 세부 정보
    @Override
    public UserDTO getUserDetails(UserDTO userDTO) {
        return sqlSession.selectOne("admin.getUserDetails", userDTO);
    }

    // 가입자 강제 탈퇴
    @Override
    public int adminJoinUserDelete(UserDTO userDTO) {
        return sqlSession.update("admin.adminJoinUserDelete", userDTO);
    }

    // 탈퇴회원 30일 후 데이터 삭제
    @Override
    public void deleteUsersWithStatusOlderThanOneMonth() {
        sqlSession.delete("admin.deleteUsersWithStatusOlderThanOneMonth");
    }

    // 프로모션 동의 메일 조회
    @Override
    public List<String> getPromotionsAccept() {
        return sqlSession.selectList("admin.getPromotionsAccept");
    }

    // 미결제 회원 리스트
    @Override
    public List<PriceListDTO> getPriceList(PriceSearchDTO priceSearchDTO) {
        return sqlSession.selectList("admin.getPriceList", priceSearchDTO);
    }

    // 회원 상태 변경
    @Override
    public int updateUserStatus(UserDTO userDTO) {
        return sqlSession.update("admin.updateUserStatus", userDTO);
    }

    // 청약철회 현황
    @Override
    public List<PayListDTO> getSubscriptionEndList(SearchDTO searchDTO) {
        return sqlSession.selectList("admin.getSubscriptionEndList", searchDTO);
    }

    // 인보이스 리스트
    @Override
    public List<InvoiceListDTO> getInvoiceList(SearchDTO searchDTO) {
        return sqlSession.selectList("admin.getInvoiceList", searchDTO);
    }

    // 인보이스 세부 정보
    @Override
    public InvoiceDTO getInvoiceDetails(InvoiceDTO invoiceDTO) {
        return sqlSession.selectOne("admin.getInvoiceDetails", invoiceDTO);
    }

    // 1:1 문의 현황
    @Override
    public List<InquiryListDTO> getInquiryList(SearchDTO searchDTO) {
        return sqlSession.selectList("admin.getInquiryList2", searchDTO);
    }

    // 1:1 문의 세부 정보
    @Override
    public InquiryDTO getInquiryDetails(InquiryDTO inquiryDTO) {
        return sqlSession.selectOne("admin.getInquiryDetails", inquiryDTO);
    }

    // 1:1 문의 답변 등록
    @Override
    public int insertInquiryAnswer(InquiryDTO inquiryDTO) {
        return sqlSession.update("admin.insertInquiryAnswer", inquiryDTO);
    }

    // 공지사항 현황
    @Override
    public List<NoticeListDTO> getNoticeList(SearchDTO searchDTO) {
        return sqlSession.selectList("admin.getNoticeList", searchDTO);
    }

    // 공지사항 세부 정보
    @Override
    public NoticeDTO getNoticeDetails(NoticeDTO noticeDTO) {
        return sqlSession.selectOne("admin.getNoticeDetails", noticeDTO);
    }

    // 공지사항 등록
    @Override
    public int insertNotice(NoticeDTO noticeDTO) {
        return sqlSession.insert("admin.insertNotice", noticeDTO);
    }

    // 공지사항 수정
    @Override
    public int updateNotice(NoticeDTO noticeDTO) {
        return sqlSession.update("admin.updateNotice", noticeDTO);
    }

    // 공지사항 삭제
    @Override
    public int deleteNotice(NoticeDTO noticeDTO) {
        return sqlSession.delete("admin.deleteNotice", noticeDTO);
    }

    // FAQ 현황
    @Override
    public List<FaqDTO> getFaqList(SearchDTO searchDTO) {
        return sqlSession.selectList("admin.getFaqList", searchDTO);
    }

    // FAQ 세부 정보
    @Override
    public FaqDTO getFaq(FaqDTO faqDTO) {
        return sqlSession.selectOne("admin.getFaq", faqDTO);
    }

    // FAQ 등록
    @Override
    public int insertFaq(FaqDTO faqDTO) {
        return sqlSession.insert("admin.insertFaq", faqDTO);
    }

    // FAQ 수정
    @Override
    public int updateFaq(FaqDTO faqDTO) {
        return sqlSession.update("admin.updateFaq", faqDTO);
    }

    // FAQ 삭제
    @Override
    public int deleteFaq(FaqDTO faqDTO) {
        return sqlSession.delete("admin.deleteFaq", faqDTO);
    }

    // Terms 정보
    @Override
    public TermsDTO getTerms() {
        return sqlSession.selectOne("admin.getTerms");
    }

    // Terms 수정
    @Override
    public int updateTerms(TermsDTO termsDTO) {
        return sqlSession.update("admin.updateTerms", termsDTO);
    }



}
