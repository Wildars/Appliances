package com.example.appliances.service.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.twilio.example.ValidationExample.ACCOUNT_SID;
import static com.twilio.example.ValidationExample.AUTH_TOKEN;

@Service
public class TwilioService {
    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }
    private static final String accountSid = "ACc23e43e03fa2097505ff167fcb5238d0";
    private static final String authToken = "6d61a8bb2f91684553d40b3a7ac9ecf5";
    private static final String twilioPhoneNumber = "+14173872053";

    public void sendSms(String toPhoneNumber, String messageBody) {
        Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                messageBody
        ).create();
    }
}

