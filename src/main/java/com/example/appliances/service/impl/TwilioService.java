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
    private static final String accountSid = "AC863e0d1d3b305423234644cba57c4a1d";
    private static final String authToken = "22ccfd6a4f0ecbcb24d61fe15eb79b49";
    private static final String twilioPhoneNumber = "+13149168597";

    public void sendSms(String toPhoneNumber, String messageBody) {
        Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                messageBody
        ).create();
    }
}

