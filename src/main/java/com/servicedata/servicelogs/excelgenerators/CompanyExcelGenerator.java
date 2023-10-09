package com.servicedata.servicelogs.excelgenerators;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class CompanyExcelGenerator {
	
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private String name;
    private Company company;
    private SortedMap<Machine, ArrayList<ConservationLog>> logs;
    private int rowCount;
	
	public CompanyExcelGenerator(String name, Company company, SortedMap<Machine, ArrayList<ConservationLog>> logs) {

		workbook = new XSSFWorkbook();
		this.company = company;
		this.logs = logs;
		this.name = name;
		this.rowCount = 0;
	}
	
	private void fillWorkbook() {
        sheet = workbook.createSheet("ServiceLogs");
		addCompany();
		for (Machine machine : logs.keySet()) {
			addMachine(machine);
			addConservationLogDescription();
			for (ConservationLog log : logs.get(machine)) {
				addConservationLog();
			}	
		}
	}
	
	private void addConservationLog() {
		// TODO Auto-generated method stub
		
	}

	private void addConservationLogDescription() {
		// TODO Auto-generated method stub
		
	}

	private void addMachine(Machine machine) {
		// TODO Auto-generated method stub
		
	}

	private void addCompany() {
        Row row = sheet.createRow(rowCount++);
        createCell(row, 0, "ID");
        createCell(row, 1, company.getCompanyId());
        row = sheet.createRow(rowCount++);
        createCell(row, 0, "Company Name");
        createCell(row, 1, company.getCompanyName());
        row = sheet.createRow(rowCount++);
        createCell(row, 0, "Company Address");
        createCell(row, 1, company.getCompanyAddress());
	}
	
    private void createCell(Row row, int columnCount, Object valueOfCell) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
    }
	
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        fillWorkbook();
    	ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
	
}
