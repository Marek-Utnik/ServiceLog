package com.servicedata.servicelogs.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompanyFilterData {
    private Long companyId;
	private String companyName;
	private String companyAddress;
	
}