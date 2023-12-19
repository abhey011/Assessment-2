package com.example.part2;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.JFreeChart;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.poi.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Calendar.DATE;
import static java.util.Calendar.getInstance;
import java.sql.*;

@SpringBootApplication
public class Part2Application {
	private static final String URL = "jdbc:mysql://localhost:3306/charts";
	private static final String USER = "root";
	private static final String PASSWORD = "Aj@123456";

	private static BasicDataSource dataSource;

	static {
		dataSource = new BasicDataSource();
		dataSource.setUrl(URL);
		dataSource.setUsername(USER);
		dataSource.setPassword(PASSWORD);
		dataSource.setMinIdle(5);
		dataSource.setMaxIdle(10);
		dataSource.setMaxOpenPreparedStatements(1000);
	}

	public static void insertData(List<ExcelData> dataList) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			String sql = "INSERT INTO interviewTable (date, month, team, panelName, round, skill, time,candidateCurrentLoc, candidatePreferredLoc, candidateName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try	(PreparedStatement statement = connection.prepareStatement(sql)) {
				dataList.stream()
						.forEach(data -> {
							try {
								statement.setString(1, data.getDate());
								statement.setString(2, data.getMonth());
								statement.setString(3, data.getTeam());
								statement.setString(4, data.getPanel());
								statement.setString(5, data.getRound());
								statement.setString(6, data.getSkill());
								statement.setString(7, data.getTime());
								statement.setString(8, data.getCurrLoc());
								statement.setString(9, data.getPreLoc());
								statement.setString(10, data.getName());

								statement.executeUpdate();
							} catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<ExcelData> parseExcel(String filePath) {
		List<ExcelData> dataList = new ArrayList<>();

		try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
			 Workbook workbook = new XSSFWorkbook(fileInputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if(row.getRowNum() == 0) {
					continue;
				}

				ExcelData excelData = new ExcelData();
				excelData.setDate(getDateCellValue(row.getCell(0)));
				excelData.setMonth(getMonthCellValue(row.getCell(1)));
				excelData.setTeam(getCellValue(row.getCell(2)));
				excelData.setPanel(getCellValue(row.getCell(3)));
				excelData.setRound(getCellValue(row.getCell(4)));
				excelData.setSkill(getCellValue(row.getCell(5)));
				excelData.setTime(getTimeCellValue(row.getCell(6)));
				excelData.setCurrLoc(getCellValue(row.getCell(7)));
				excelData.setPreLoc(getCellValue(row.getCell(8)));
				excelData.setName(getCellValue(row.getCell(9)));

				dataList.add(excelData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataList;
	}

	private static String getDateCellValue(Cell cell) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("d-MMM-yy");
			return sdf.format(cell.getDateCellValue());
		} catch (Exception e) {
			return null;
		}
	}

	private static String getMonthCellValue(Cell cell) {
		return cell.getStringCellValue() != null ? cell.getStringCellValue() : null;
	}
	private static String getTimeCellValue(Cell cell) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a");
			return cell.getLocalDateTimeCellValue().format(formatter);
		} catch (Exception e) {
			return null;
		}
	}

	private static String getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				return String.valueOf(cell.getNumericCellValue());
			default:
				return null;
		}
	}

	public static void main(String[] args) throws SQLException, IOException {
		try {
			SpringApplication.run(Part2Application.class, args);

			String filePath = "C:\\Assessmentt-2\\Problem-2-chartss\\src\\main\\resources\\FinalAccolite.xlsx";
			List<ExcelData> dataList = parseExcel(filePath);

			insertData(dataList);

			// Step 5: Generate PDF and embed charts
			PdfGenerator.generatePdf(dataList, "C:\\Assessmentt-2\\Problem-2-chartss\\src\\main\\resources\\Output.pdf");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
