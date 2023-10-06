package com.servicedata.servicelogs.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class MachineFilterData {
    private Long machineId;
    private Integer registrationNumber;
    private Integer serialNumber;
	private String producerName;
	private String machineType;
}