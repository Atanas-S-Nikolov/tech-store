package com.techstore.service.mail;

import com.techstore.model.dto.QuickOrderDto;
import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.OrderEntity;

public interface IMailSenderService {
    void sendRegistrationMailConfirmation(String email, String token, long limitTimeMs);

    void sendForgottenPasswordMail(String email, String token, long limitTimeMs);

    void sendQuickOrderMail(CartEntity cart, QuickOrderDto quickOrderDto);

    void sendOrderMail(OrderEntity orderEntity);
}
