package com.gruptiga.smartlaundry.entity;

import com.gruptiga.smartlaundry.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ConstantTable.BLACKLIST_TOKEN)
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blacklist_token_id")
    private Long blacklistTokenId;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "expiry_date")
    private Instant expiryDate;
}
