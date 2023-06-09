package com.techstore.service.mail;

import com.techstore.model.dto.EmailDto;
import com.techstore.model.dto.OrderDto;
import com.techstore.model.dto.QuickOrderDto;

public interface IMailSenderService {
    void sendRegistrationMailConfirmation(String email, String token, long limitTimeMs);

    void sendForgottenPasswordMail(EmailDto emailDto);

    void sendQuickOrderConfirmationMail(String cartKey, QuickOrderDto quickOrderDto);

    void sendOrderConfirmationMail(OrderDto orderDto);
}
