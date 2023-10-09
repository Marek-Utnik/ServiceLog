package com.servicedata.servicelogs.excelgenerators;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class CompanyExcelGenerator {
	
    private XSSFWorkbook workbook;
    private Company company;
    private SortedMap<Machine, ArrayList<ConservationLog>> logs;
	
	public CompanyExcelGenerator(Company company, SortedMap<Machine, ArrayList<ConservationLog>> logs) {

		workbook = new XSSFWorkbook();
		this.company = company;
		this.logs = logs;
	}
	
	private void fillWorkbook() {
		addCompany(company);
		for (Machine machine : logs.keySet()) {
			addMachine(machine);
			addConservationLogDescription();
			for (ConservationLog log : logs.get(machine)) {
				addConservationLogDescription();
			}	
		}
	}
	
	private void addConservationLogDescription() {
		// TODO Auto-generated method stub
		
	}

	private void addMachine(Machine machine) {
		// TODO Auto-generated method stub
		
	}

	private void addCompany(Company company) {
		
	}
	
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        fillWorkbook();
    	ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
	
}
