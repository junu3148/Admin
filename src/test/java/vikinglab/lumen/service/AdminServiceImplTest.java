package vikinglab.lumen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vikinglab.lumen.dao.AdminRepository;
import vikinglab.lumen.json.JsonResult;
import vikinglab.lumen.vo.AdminUser;
import vikinglab.lumen.vo.CurrentSituationVO;
import vikinglab.lumen.vo.MonthVO;
import vikinglab.lumen.vo.QnaVO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAdminLogin() {
        AdminUser inputUser = new AdminUser();
        inputUser.setId("test");
        inputUser.setPassword("123");

        AdminUser expectedUser = new AdminUser();
        expectedUser.setId("test");
        expectedUser.setPassword("123");
        expectedUser.setIsAdmin(0);

        when(adminRepository.adminLogin(inputUser)).thenReturn(expectedUser);

        JsonResult result = adminService.adminLogin(inputUser);

        verify(adminRepository, times(1)).adminLogin(inputUser);
        assertEquals(expectedUser.getIsAdmin(), result.getData());
    }

    @Test
    public void testAdminLoginCk() {
        AdminUser inputUser = new AdminUser();
        inputUser.setAccountName("AccountName");
        inputUser.setAccountNumber("AccountNumber");

        AdminUser expectedUser = new AdminUser();
        expectedUser.setUserName("김동규");
        expectedUser.setId("test");
        expectedUser.setIsAdmin(1);

        when(adminRepository.adminLoginCk(inputUser)).thenReturn(expectedUser);

        AdminUser result = adminService.adminLoginCk(inputUser);

        verify(adminRepository, times(1)).adminLoginCk(inputUser);
        assertEquals(expectedUser.getUserName(), result.getUserName());
    }

    @Test
    public void testSubscriberCount() {
        int expectedSubscriberCount = 165;

        when(adminRepository.subscriberCount()).thenReturn(expectedSubscriberCount);

        JsonResult result = adminService.subscriberCount();

        verify(adminRepository, times(1)).subscriberCount();
        assertEquals(expectedSubscriberCount, result.getData());
    }

    @Test
    public void testMonthlySalesChart() {
        MonthVO expectedMonthVO = new MonthVO();

        when(adminRepository.getMonthlySalesChart()).thenReturn(expectedMonthVO);

        JsonResult result = adminService.getMonthlySalesChart();

        verify(adminRepository, times(1)).getMonthlySalesChart();
        assertEquals(expectedMonthVO, result.getData());
    }

    @Test
    public void testCurrentSituation() {
        List<CurrentSituationVO> expectedList = new ArrayList<>();
        CurrentSituationVO situation1 = new CurrentSituationVO();
        situation1.setUserCount(124);
        situation1.setUpDown("up");
        situation1.setPercentage("4%");
        expectedList.add(situation1);

        when(adminRepository.getCurrentSituation()).thenReturn(expectedList);

        JsonResult result = adminService.getCurrentSituation();

        verify(adminRepository, times(1)).getCurrentSituation();
        assertEquals(expectedList, result.getData());
    }

    @Test
    public void testMainInquiryList() {
        List<QnaVO> expectedList = new ArrayList<>();
        QnaVO qna1 = new QnaVO();
        qna1.setInquiryDate("2024.01.01");
        qna1.setInquiryDetails("로그인이 안돼요1");
        qna1.setAnswerStatus("답변대기");
        qna1.setSubscriptionPlan("pro");
        expectedList.add(qna1);

        when(adminRepository.getMainInquiryList()).thenReturn(expectedList);

        JsonResult result = adminService.getMainInquiryList();

        verify(adminRepository, times(1)).getMainInquiryList();
        assertEquals(expectedList, result.getData());
    }
}
