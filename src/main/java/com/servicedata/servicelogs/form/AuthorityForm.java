package com.servicedata.servicelogs.form;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorityForm{

	Boolean Serviceman = false;
	Boolean Companyuser = false;
	Boolean Admin = false;
	Long SystemUserId;

}