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
@Table(name = ConstantTable.TYPE)
public class Type {
    @Id
    @GeneratedValue(generator = "type-id")
    @GenericGenerator(name = "type-id", strategy = "com.gruptiga.smartlaundry.utils.TypeCustomId")
    @Column(name = "type_id", nullable = false, updatable = false)
    private String typeId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<ServiceType> serviceTypes;
}
