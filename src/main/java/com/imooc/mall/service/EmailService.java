package com.imooc.mall.service;

/**
 *  描述：     邮件Service
 */
public interface EmailService {
    void sendSimpleMessage(String to,String  subject,String text);

    Boolean saveEmailTORedis(String emailAddress, String verificationCode);
}
