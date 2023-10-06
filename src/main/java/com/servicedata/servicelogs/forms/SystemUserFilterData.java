package com.servicedata.servicelogs.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemUserFilterData {
    private Long systemUserId;
    private String username;
    private String name;
	private String surname;
}