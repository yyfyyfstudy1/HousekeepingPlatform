package com.usyd.capstone.service.impl;

import com.alibaba.fastjson.JSON;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.usyd.capstone.common.Enums.PaypalPaymentIntent;
import com.usyd.capstone.common.Enums.PaypalPaymentMethod;
import com.usyd.capstone.mapper.TasksMapper;
import com.usyd.capstone.service.PaypalService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class PaypalServiceImpl implements PaypalService {

    //注入认证信息bean
    @Resource
    private APIContext apiContext;
    @Resource
    private TasksMapper tasksMapper;

    @Override
    public Payment createPayment(Integer taskId, String cancelUrl, String successUrl) {
        //1、根据taskId查询task信息，获取task 金额
        //total应为查询出来的task金额，此处仅为示例，防止编译报错
        BigDecimal total = new BigDecimal("");
        //todo
        if(tasksMapper.findSalaryById(taskId)!=null){
        total = tasksMapper.findSalaryById(taskId);
        }
        //2、执行创建支付
        Payment payment = createPayment(total, "USD", cancelUrl, successUrl);
        log.info("支付完成:{}", JSON.toJSONString(payment));
        //3、更新task状态 为“已完结”
        //todo

        return payment;
    }

    /**
     * 执行支付的方法(相当于提交事务)
     * @param paymentId 支付ID
     * @param payerId 支付人ID
     */
    @SneakyThrows
    @Override
    public Payment executePayment(String paymentId, String payerId)  {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }


    /**
     * 支付方法
     * @param total 交易金额
     * @param currency 货币类型
     * @param cancelUrl 交易取消后跳转url
     * @param successUrl 交易成功后跳转url
     * @return
     */
    @SneakyThrows
    private Payment createPayment(
            BigDecimal total,
            String currency,
            String cancelUrl,
            String successUrl) {

        Payment payment = new Payment();
        //正常应该要根据官方文档，确定必传的有哪些参数

        //交易类型  ： 固定为 sale类型
        payment.setIntent(PaypalPaymentIntent.sale.name());

        //支付方式：此处固定为 paypal
        Payer payer = new Payer();
        payer.setPaymentMethod(PaypalPaymentMethod.paypal.name());
        payment.setPayer(payer);

        //交易金额信息
        List<Transaction> transactions = new ArrayList<>();
        //此处仅单个金额信息
        Transaction transaction = new Transaction();
        //金额信息
        Amount amountDetails = new Amount();
        amountDetails.setCurrency(currency);
        amountDetails.setTotal(String.format("%.2f", total));
        transaction.setAmount(amountDetails);
        payment.setTransactions(transactions);


        //加入反馈对象
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(successUrl);
        redirectUrls.setCancelUrl(cancelUrl);
        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }

}