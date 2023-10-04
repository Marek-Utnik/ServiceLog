package com.servicedata.servicelogs.models;

import com.servicedata.servicelogs.enums.AuthorityName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Authority {

    @Id
    @GeneratedValue
    private Long authorityId;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    AuthorityName name;

    public Authority(AuthorityName name) {
        this.name = name;
    }
}