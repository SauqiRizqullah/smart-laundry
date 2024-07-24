//package com.gruptiga.smartlaundry.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.gruptiga.smartlaundry.constant.ConstantTable;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.GenericGenerator;
//
//@Setter
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Builder
//@Table(name = ConstantTable.TRANSACTION_DETAIL)
//public class TransactionDetail {
//
//    @Id
//    @Column(name = "trx_detail_id")
//    private String trxDetailId;
//
//
//    @ManyToOne
//    @JoinColumn(name = "service_type_id")
//    private ServiceType serviceType;
//
//    @Column(name = "qty")
//    private Integer qty;
//
//    @Column(name = "price")
//    private Long price;
//}
