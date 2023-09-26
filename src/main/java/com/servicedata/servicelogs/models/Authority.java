package com.servicedata.servicelogs.models;

import com.servicedata.servicelogs.enums.AuthorityName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Authority{

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