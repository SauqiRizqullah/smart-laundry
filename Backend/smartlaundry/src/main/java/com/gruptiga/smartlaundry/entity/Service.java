package com.gruptiga.smartlaundry.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gruptiga.smartlaundry.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.SERVICE)
public class Service {

    @Id
    @GeneratedValue(generator = "service-id")
    @GenericGenerator(name = "service-id", strategy = "com.gruptiga.smartlaundry.utils.ServiceCustomId")
    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<ServiceType> serviceTypes;
}