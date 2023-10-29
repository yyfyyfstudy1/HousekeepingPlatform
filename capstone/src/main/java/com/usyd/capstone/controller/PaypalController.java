package com.usyd.capstone.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.usyd.capstone.common.util.URLUtils;
import com.usyd.capstone.entity.VO.TaskIdVO;
import com.usyd.capstone.service.PaypalService;
import com.usyd.capstone.service.TaskOngoingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;


@RestController
@RequestMapping("/paypal")
@Slf4j
public class PaypalController {

    public static final String PAYPAL_SUCCESS_URL = "/success";
    public static final String PAYPAL_CANCEL_URL = "/cancel";

    @Resource
    private PaypalService paypalService;

    @Resource
    private TaskOngoingService taskOngoingService;

    /**
     * 开始交易
     * @param request
     * @return
     */
    @PostMapping("/pay")
    public String pay(@RequestBody TaskIdVO taskIdVO, HttpServletRequest request){

        Integer taskId = taskIdVO.getTaskId();
        Long taskDuration = taskIdVO.getTaskDuration();

        String cancelUrl = URLUtils.getBaseURl(request) + "/paypal" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + "/paypal" + PAYPAL_SUCCESS_URL + "?taskId=" + taskId;

        //调用交易方法
        Payment payment = paypalService.createPayment(taskId, cancelUrl, successUrl, taskDuration);
        //log.info("执行结果： payment:{}", JSON.toJSONString(payment));

        for(Links links : payment.getLinks()){
            if(links.getRel().equals("approval_url")){
                return  links.getHref();
            }
        }
        return "redirect:/";
    }

    /**
     * 交易取消
     * @return
     */
    @GetMapping(PAYPAL_CANCEL_URL)
    public String cancelPay(HttpServletResponse response){
        return "payment canceled";
    }

    /**
     * 交易成功,并执行交易(相当于提交事务)
     * @param paymentId
     * @param payerId
     * @return
     */

    @Value("${base.Url}") // 从配置文件获取上传目录
    private String baseUrl;
    @GetMapping(PAYPAL_SUCCESS_URL)
    public ResponseEntity<Void>  successPay(@RequestParam("paymentId") String paymentId,
                                            @RequestParam("PayerID") String payerId,
                                            @RequestParam("taskId") Integer taskId){

        System.out.println("这是任务id" + taskId);

        taskOngoingService.employerPaymentSuccessful(taskId);
        Payment payment = paypalService.executePayment(paymentId, payerId);
        URI redirectUri;
        if(payment.getState().equals("approved")){
            redirectUri = URI.create(baseUrl+ "/pay/payment-success");
        } else {
            redirectUri = URI.create(baseUrl + "localhost:8080/");
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

}
