package ahn.sungsin.service;

import ahn.sungsin.vo.CovidStatus;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;
import java.util.List;

public class PdfExporter {
    public static void exportToPdf(String date, List<CovidStatus> covidStatusList, String fileName) throws FileNotFoundException {

        try {
            // 한글 폰트 파일 경로를 지정합니다.
            String koreanFontPath = "NanumGothicLight.ttf";
            PdfFont koreanFont = PdfFontFactory.createFont(koreanFontPath, PdfEncodings.IDENTITY_H, true);

            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(fileName));
            Document doc = new Document(pdfDoc);

            // 제목 추가
            Paragraph title = new Paragraph("일일 코로나 바이러스 감염 현황 (" + date + ")");
            doc.add(title.setFont(koreanFont));

            // 테이블 생성 및 설정
            float[] columnWidths = {100, 50, 50, 50, 50, 50, 50};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            String[] headers = {"시도", "합계", "국내발생", "해외유입", "확진환자", "사망자", "발생률"};


            // 헤더 설정
            for (String header : headers) {
                Cell cell = new Cell();
                cell.add(new Paragraph(header).setFont(koreanFont)); // 한글 폰트 설정
                cell.setTextAlignment(TextAlignment.CENTER);
                table.addHeaderCell(cell);
            }

            // 데이터 셀 설정
            for (CovidStatus covidStatus : covidStatusList) {
                table.addCell(new Cell().add(new Paragraph(covidStatus.getRegion()).setFont(koreanFont)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(covidStatus.getTotal())).setFont(koreanFont)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(covidStatus.getDomestic())).setFont(koreanFont)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(covidStatus.getAbroad())).setFont(koreanFont)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(covidStatus.getConfirmed())).setFont(koreanFont)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(covidStatus.getDeaths())).setFont(koreanFont)));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", covidStatus.getRate())).setFont(koreanFont)));
            }

            // 테이블을 문서에 추가하고 문서를 닫습니다.
            doc.add(table);
            doc.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
