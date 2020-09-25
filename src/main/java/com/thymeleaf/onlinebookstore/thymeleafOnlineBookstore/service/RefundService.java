package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Refund;

import java.util.List;

public interface RefundService {
    List<Refund> getAllRefundreq();
    void saveRefund(Refund refund);
}
