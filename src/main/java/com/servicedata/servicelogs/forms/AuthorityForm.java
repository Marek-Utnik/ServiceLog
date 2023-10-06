package com.servicedata.servicelogs.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorityForm {

    private Boolean Serviceman = false;
    private Boolean Companyuser = false;
    private Boolean Admin = false;
    private Long SystemUserId;
}