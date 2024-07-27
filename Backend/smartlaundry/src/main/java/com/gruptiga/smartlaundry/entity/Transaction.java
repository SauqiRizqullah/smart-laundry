package com.gruptiga.smartlaundry.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gruptiga.smartlaundry.constant.ConstantTable;
import com.gruptiga.smartlaundry.constant.Payment;
import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.TRANSACTION)
public class Transaction {

    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.gruptiga.smartlaundry.utils.TransactionCustomId")
    @Column(name = "trx_id")
    private String trxId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "service_type_id")
    private String serviceTypeId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status; //enum

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "payment")
    @Enumerated(EnumType.STRING)
    private Payment payment; //enum

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "status_pembayaran")
    private STATUS_PEMBAYARAN statusPembayaran;
}
