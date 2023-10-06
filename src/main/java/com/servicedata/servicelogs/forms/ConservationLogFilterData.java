package com.servicedata.servicelogs.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConservationLogFilterData {
    private Long systemUserId;
    private LocalDate publicationDateStart;
    private LocalDate publicationDateEnd;
	private String conservationDescription;
}