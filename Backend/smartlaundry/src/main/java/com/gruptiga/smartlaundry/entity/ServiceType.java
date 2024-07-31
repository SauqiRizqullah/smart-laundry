package com.gruptiga.smartlaundry.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gruptiga.smartlaundry.constant.ConstantTable;
import com.gruptiga.smartlaundry.constant.Detail;
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
    @JsonBackReference
    private Account account;

    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonManagedReference
    private Type type;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonManagedReference
    private Service service;

    @Column(name = "price")
    private Long price;

    @Column(name = "detail")
    @Enumerated(EnumType.STRING)
    private Detail detail;

    @Column(name = "image_path")
    private String imagePath;
}
