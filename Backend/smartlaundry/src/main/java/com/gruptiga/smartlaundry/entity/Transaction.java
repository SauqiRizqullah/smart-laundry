package com.gruptiga.smartlaundry.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gruptiga.smartlaundry.constant.ConstantTable;
import com.gruptiga.smartlaundry.constant.Payment;
import com.gruptiga.smartlaundry.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

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

}
