package com.servicedata.servicelogs.form;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorityForm{

	private Boolean Serviceman = false;
	private Boolean Companyuser = false;
	private Boolean Admin = false;
	private Long SystemUserId;
}