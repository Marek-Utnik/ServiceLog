package com.servicedata.servicelogs.excelgenerators;

import java.io.IOException;
import java.util.List;
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
    private Company company;
    private SortedMap<Machine, List<ConservationLog>> logs;
    private int rowCount;
	
	public CompanyExcelGenerator(Company company, SortedMap<Machine, List<ConservationLog>> logs) {

		workbook = new XSSFWorkbook();
		this.company = company;
		this.logs = logs;
		this.rowCount = 0;
	}
	
	private void fillWorkbook() {
        sheet = workbook.createSheet("Service Logs");
		addCompany();
		for (Machine machine : logs.keySet()) {
			addMachine(machine);
			addConservationLogHeader();
			if (logs.get(machine)!=null) {
				for (ConservationLog log : logs.get(machine)) {
					addConservationLog(log);
				}	
			}
		}
	}
	
	private void addConservationLog(ConservationLog log) {
        Row row = sheet.createRow(rowCount++);
        createCell(row, 1, log.getConservationLogId());
        createCell(row, 2, log.getSystemUser().getUsername()+" "+log.getSystemUser().getName()+" "+log.getSystemUser().getSurname());
        createCell(row, 3, log.getPublicationDate().toString());
        createCell(row, 4, log.getConservationDescription());
	}

	private void addConservationLogHeader() {
        Row row = sheet.createRow(rowCount++);
        createCell(row, 1, "Log ID");
        createCell(row, 2, "Serviceman");
        createCell(row, 3, "Date");
        createCell(row, 4, "Service Description");
	}

	private void addMachine(Machine machine) {
        Row row = sheet.createRow(rowCount++);
        createCell(row, 0, "Machine ID");
        createCell(row, 1, machine.getMachineId());
        row = sheet.createRow(rowCount++);
        createCell(row, 0, "Registration Number");
        createCell(row, 1, machine.getRegistrationNumber());
        row = sheet.createRow(rowCount++);
        createCell(row, 0, "Serial Number");
        createCell(row, 1, machine.getSerialNumber());
        row = sheet.createRow(rowCount++);
        createCell(row, 0, "Producer Name");
        createCell(row, 1, machine.getProducerName());
        row = sheet.createRow(rowCount++);
        createCell(row, 0, "Machine Type");
        createCell(row, 1, machine.getMachineType());
	}

	private void addCompany() {
        Row row = sheet.createRow(rowCount++);
        createCell(row, 0, "Company ID");
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
        }
    }
	
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        fillWorkbook();
    	ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
        outputStream.close();
    }
	
}
