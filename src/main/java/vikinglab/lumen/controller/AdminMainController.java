package vikinglab.lumen.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vikinglab.lumen.json.JsonResult;

@RestController
@RequiredArgsConstructor
public class AdminMainController {


    @GetMapping("/adminmain")
    public String adminMain(HttpSession session) {
        System.out.println("adminmain()");

        String s = (String) session.getAttribute("accessToken");

        System.out.println(s);


        return "adminMain";
    }

    @GetMapping("/subscribercount")
    public JsonResult subscribercount(){

       int  subscriber = 1;

        JsonResult jsonResult = new JsonResult();

        jsonResult.success(subscriber);

        return jsonResult;
    }


}
