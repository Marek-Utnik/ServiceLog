package com.bazakonserwacji.zeszyt.form;


import java.util.List;
import java.util.SortedMap;

import com.bazakonserwacji.zeszyt.models.Authority;

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