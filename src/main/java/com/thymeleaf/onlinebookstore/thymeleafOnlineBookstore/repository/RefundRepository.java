package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Refund;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    @Query("SELECT i from Refund i where i.refund_Id = ?1")
    Refund findRefundById(Long refund_id);

    @Query("SELECT i from Refund i where i.Respond is NULL")
    List<Refund> getAllNotResponedRefundReq();

    @Query("SELECT  i from  Refund i where i.customer_orders.status = 'Refunded'")
    List<Refund> getAllRefundedReqs();

    @Query("SELECT i from Refund i where i.customer_orders.status = 'Rejected'")
    List<Refund> getAllRejectedRefundReqs();
}
