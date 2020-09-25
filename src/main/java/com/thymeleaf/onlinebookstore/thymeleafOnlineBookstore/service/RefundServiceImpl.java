package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Refund;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RefundServiceImpl implements RefundService{

    @Autowired
    private RefundRepository refundRepository;

    @Override
    public List<Refund> getAllRefundreq() {
        return refundRepository.findAll();
    }

    @Override
    public void saveRefund(Refund refund) {
        this.refundRepository.save(refund);
    }
}
