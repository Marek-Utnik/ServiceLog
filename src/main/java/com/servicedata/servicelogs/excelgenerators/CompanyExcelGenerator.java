package com.servicedata.servicelogs.excelgenerators;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.servicedata.servicelogs.models.Company;

public class CompanyExcelGenerator {
	
    private XSSFWorkbook workbook;
    private Company company;
	
	public CompanyExcelGenerator(Company company) {
		workbook = new XSSFWorkbook();
		
	}
	
	
}
