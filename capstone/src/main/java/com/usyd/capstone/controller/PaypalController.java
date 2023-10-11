package com.usyd.capstone.controller;

import com.alibaba.fastjson.JSON;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.usyd.capstone.common.util.URLUtils;
import com.usyd.capstone.service.PaypalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/paypal")
@Slf4j
public class PaypalController {

    public static final String PAYPAL_SUCCESS_URL = "/success";
    public static final String PAYPAL_CANCEL_URL = "/cancel";

    @Resource
    private PaypalService paypalService;

    /**
     * 开始交易
     * @param request
     * @return
     */
    @PostMapping("/pay")
    public String pay(@RequestParam(name="taskId", required = false, defaultValue = "77") Integer taskId, HttpServletRequest request){
        //
        String cancelUrl = URLUtils.getBaseURl(request) + "/paypal" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + "/paypal" + PAYPAL_SUCCESS_URL;
        System.out.println(taskId);

        //调用交易方法
        Payment payment = paypalService.createPayment(taskId, cancelUrl, successUrl);
        //log.info("执行结果： payment:{}", JSON.toJSONString(payment));
        //交易成功后，跳转反馈地址
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
        return "cancel";
    }

    /**
     * 交易成功,并执行交易(相当于提交事务)
     * @param paymentId
     * @param payerId
     * @return
     */
    @GetMapping(PAYPAL_SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        Payment payment = paypalService.executePayment(paymentId, payerId);
        if(payment.getState().equals("approved")){
            return "success";
        }
        return "redirect:/";
    }

}
