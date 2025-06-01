package Pendulum;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelWriter {

    private String filePath;

    private Sheet sheet;

    private Workbook workbook;

    private ArrayList<Double> theta = new ArrayList<>();
    private ArrayList<Double> thetaDot = new ArrayList<>();
    private ArrayList<Double> energy = new ArrayList<>();
    private ArrayList<Double> dt = new ArrayList<>();
    private ArrayList<Double> posY = new ArrayList<>();

    public ExcelWriter(String filePath) {
        this.filePath = filePath;

        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Data");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Theta");
        header.createCell(1).setCellValue("Theta Dot");
        header.createCell(2).setCellValue("Energy");
        header.createCell(3).setCellValue("Time");
        header.createCell(4).setCellValue("Position Y");
    }

    public void writeDataToExcel(int n) throws IOException {
        for(int i = 0; i < theta.size() && i < n; i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(theta.get(i));
            row.createCell(1).setCellValue(thetaDot.get(i));
            row.createCell(2).setCellValue(energy.get(i));
            row.createCell(3).setCellValue(dt.get(i));
            row.createCell(4).setCellValue(posY.get(i));
        }

        for(int col = 0; col < 5; col++) {
            sheet.autoSizeColumn(col);
        }
        try(FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
        System.out.println("Data written to: " + filePath);
    }

    public synchronized void addValues(double theta, double thetaDot, double energy, double dt, double positionY) {
        this.theta.add(theta);
        this.thetaDot.add(thetaDot);
        this.energy.add(energy);
        this.dt.add(dt);
        this.posY.add(positionY);
    }

}
