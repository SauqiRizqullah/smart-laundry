package com.gruptiga.smartlaundry.entity;

import com.gruptiga.smartlaundry.constant.ConstantTable;
import com.gruptiga.smartlaundry.constant.Detail;
import com.gruptiga.smartlaundry.constant.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.SERVICETYPE)
public class ServiceType {

    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.gruptiga.smartlaundry.utils.ServiceTypeCustomId")
    @Column(name = "service_type_id")
    private String serviceTypeId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type; //enum

    @Column(name = "service")
    private String service;

    @Column(name = "price")
    private Long price;

    @Column(name = "detail")
    @Enumerated(EnumType.STRING)
    private Detail detail; //enum
}
