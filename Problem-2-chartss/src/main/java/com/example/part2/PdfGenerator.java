package com.example.part2;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
//import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PdfGenerator {
    public static void generatePdf(List<ExcelData> data, String outputPath) throws IOException {
        try (OutputStream fos = new FileOutputStream(outputPath);
             PdfWriter writer = new PdfWriter(fos);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument)) {



            JFreeChart chart3 = ChartGenerator.createBarChart(data);

            // Convert JFreeChart to BufferedImage
            int width3 = 600; // set width of the image
            int height3 = 700; // set height of the image
            BufferedImage bufferedImage3 = chart3.createBufferedImage(width3, height3);

            // Convert BufferedImage to iTextPDF Image
            Image itextImage3 = new Image(ImageDataFactory.create(bufferedImage3, null));

            JFreeChart chart4 = ChartGenerator.top3SkillsPieChart();

            // Convert JFreeChart to BufferedImage
            int width4 = 600; // set width of the image
            int height4 = 700; // set height of the image
            BufferedImage bufferedImage4 = chart4.createBufferedImage(width4, height4);

            // Convert BufferedImage to iTextPDF Image
            Image itextImage4 = new Image(ImageDataFactory.create(bufferedImage4, null));

            JFreeChart chart5 = ChartGenerator.top3SkillsPeakTimePieChart();

            // Convert JFreeChart to BufferedImage
            int width5 = 600; // set width of the image
            int height5 = 700; // set height of the image
            BufferedImage bufferedImage5 = chart5.createBufferedImage(width5, height5);

            // Convert BufferedImage to iTextPDF Image
            Image itextImage5 = new Image(ImageDataFactory.create(bufferedImage5, null));

            JFreeChart chart6 = ChartGenerator.maxInterviewBarChart();

            // Convert JFreeChart to BufferedImage
            int width6= 600; // set width of the image
            int height6 = 700; // set height of the image
            BufferedImage bufferedImage6 = chart6.createBufferedImage(width6, height6);

            // Convert BufferedImage to iTextPDF Image
            Image itextImage6 = new Image(ImageDataFactory.create(bufferedImage6, null));

            JFreeChart chart7 = ChartGenerator.minInterviewBarChart();

            // Convert JFreeChart to BufferedImage
            int width7= 600; // set width of the image
            int height7 = 700; // set height of the image
            BufferedImage bufferedImage7 = chart7.createBufferedImage(width7, height7);

            // Convert BufferedImage to iTextPDF Image
            Image itextImage7 = new Image(ImageDataFactory.create(bufferedImage7, null));


            document.add(itextImage6);
            document.add(itextImage7);
            document.add(itextImage3);
            document.add(itextImage4);
            document.add(itextImage5);
        }
    }
}