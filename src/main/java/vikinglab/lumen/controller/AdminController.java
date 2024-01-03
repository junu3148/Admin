package vikinglab.lumen.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vikinglab.lumen.dto.JwtToken;
import vikinglab.lumen.json.JsonResult;
import vikinglab.lumen.service.AdminServiceImpl;
import vikinglab.lumen.vo.AdminUser;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceImpl adminService;


    @GetMapping("/adminlogin")
    public String adminloginform() {
        System.out.println("adminlogin()");


        return "adminloginform";
    }

    @PostMapping("/adminlogin")
    @ResponseBody
    public JsonResult adminLogin2(@RequestBody AdminUser adminUser) {
        System.out.println("adminlogin()");

        AdminUser adminUser1 = adminService.login(adminUser);


        JsonResult jsonResult = new JsonResult();
        if (adminUser1 != null) {
            jsonResult.success(adminUser1);

        } else {

            jsonResult.fail("실패");
        }

        return jsonResult;
    }


    /*    @PostMapping("/adminloginck")
        @ResponseBody
        public JsonResult adminLoginck(@RequestBody AdminUser adminUser) {
            System.out.println("adminloginck()");


            JwtToken jwtToken = adminService.loginck(adminUser);

            System.out.println(jwtToken);

            JsonResult jsonResult = new JsonResult().result(jwtToken).orElseFail("실패");

            System.out.println(jsonResult);

            return jsonResult;
        }*/
    @PostMapping("/adminloginck")
    @ResponseBody
    public JsonResult adminLoginck(@RequestBody AdminUser adminUser) {
        System.out.println("adminloginck()");


        AdminUser adminUser1 = adminService.loginck(adminUser);
        System.out.println(adminUser1);

        JsonResult jsonResult = new JsonResult().result(adminUser1).orElseFail("실패");

        System.out.println(jsonResult);
        return jsonResult;
    }

    // ------------------- 로그아웃
    @GetMapping("/logout")
    @ResponseBody
    public JsonResult logout(HttpSession session) {
        System.out.println("logout()");

        session.removeAttribute("authUser");
        session.invalidate();

        return new JsonResult();

    }

}
