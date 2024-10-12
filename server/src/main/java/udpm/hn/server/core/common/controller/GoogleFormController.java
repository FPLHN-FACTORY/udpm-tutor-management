//package udpm.hn.server.core.common.controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import udpm.hn.server.infrastructure.constant.MappingConstants;
//
//@RestController
//@RequestMapping(MappingConstants.API_CREATE)
//public class GoogleFormController {
//
//    @GetMapping
//    public String createGoogleForm() {
//        String url = "https://script.google.com/macros/s/AKfycbxDxpf-3IA8V9cjhYCLgVAhG0vcXH_xqQKosqJ9eF8V2Wn-2EEDzicFbVXrjcY0N7tP6A/exec-";
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject(url, String.class);
//        return response;  // Trả về link Google Form
//    }
//}
