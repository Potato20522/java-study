package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import wiki.xsx.core.pdf.component.XEasyPdfComponent;
import wiki.xsx.core.pdf.component.table.XEasyPdfCell;
import wiki.xsx.core.pdf.component.table.XEasyPdfRow;
import wiki.xsx.core.pdf.component.table.XEasyPdfTable;
import wiki.xsx.core.pdf.doc.XEasyPdfDefaultFontStyle;
import wiki.xsx.core.pdf.doc.XEasyPdfDocument;
import wiki.xsx.core.pdf.doc.XEasyPdfPage;
import wiki.xsx.core.pdf.doc.XEasyPdfPositionStyle;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;

public class TestDemo {
    PDRectangle rectangle =  new PDRectangle( 841.8898F,595.27563F);
    public static void main(String[] args) {
        testWritePdf();
    }

    public static void testWritePdf() {
        List<PdfModel<?>> list = new ArrayList<>();
        PdfModel<WbDTO2> table = new PdfModel<>();
        table.setType(PdfContentType.TABLE);
        List<WbDTO2> dataList = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            dataList.add(WbDTO2.builder().deviceId("deviceId-" + i).deviceName("设备" + (i + 1))
                    .localName("南山区金证大楼/" + (i + 1) + "楼").type("虚拟设备").productKey("kt")
                    .productId("productId-" + i).build());
        }
        table.setTableData(dataList);
        list.add(table);
        write(list, PDRectangle.A4);
    }

    /**
     * 导出PDF
     *
     * @param dataList  数据
     * @param rectangle 纸张大小，默认A4
     * @author 杨昌海
     * @date 2022/5/19
     */
    public static void write(List<PdfModel<?>> dataList, PDRectangle rectangle) {
        //todo 支持指定换页
        rectangle = rectangle == null ? PDRectangle.A4 : rectangle;
        List<XEasyPdfComponent> components = new ArrayList<>();
        for (PdfModel<?> pdfModel : dataList) {
            XEasyPdfComponent component = null;
            switch (pdfModel.getType()) {
                case TABLE:
                    component = createTable(pdfModel.getTableData(), rectangle);
                    break;
                default:
                    break;
            }
            if (component != null) {
                components.add(component);
            }
        }
        XEasyPdfPage page = XEasyPdfHandler.Page.build(rectangle, components);
        XEasyPdfDocument document = XEasyPdfHandler.Document.build(page);
        document.save("./utilsPdf.pdf").close();
    }

    private static <T> XEasyPdfTable createTable(List<T> data, PDRectangle rectangle) {
        List<XEasyPdfRow> rows = new ArrayList<>();
        List<XEasyPdfCell> header = new ArrayList<>();
        int column = 0;
        float columnWith = 0;
        for (T datum : data) {
            Class<?> aClass = datum.getClass();
            Field[] fields = aClass.getDeclaredFields();
            if (column == 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    PdfTableProperty annotation = field.getAnnotation(PdfTableProperty.class);
                    if (annotation != null) {
                        column++;
                    }
                }
                columnWith = rectangle.getWidth() / column;
                for (Field field : fields) {
                    field.setAccessible(true);
                    PdfTableProperty annotation = field.getAnnotation(PdfTableProperty.class);
                    if (annotation != null) {
                        String title = annotation.title();
                        header.add(XEasyPdfHandler.Table.Row.Cell.build(columnWith)
                                .addContent(XEasyPdfHandler.Text.build(title))
                                .setFontSize(16F)
                                .setDefaultFontStyle(XEasyPdfDefaultFontStyle.BOLD)
                                .setHorizontalStyle(XEasyPdfPositionStyle.LEFT)
                        );
                    }
                }
                //表头
                rows.add(XEasyPdfHandler.Table.Row.build(header));
            }
            List<XEasyPdfCell> cells = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(datum);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                value = value == null ? "" : value;
                cells.add(XEasyPdfHandler.Table.Row.Cell.build(columnWith)
                        .addContent(XEasyPdfHandler.Text.build(value.toString()))
                        .setHorizontalStyle(XEasyPdfPositionStyle.LEFT)
                );
            }
            rows.add(XEasyPdfHandler.Table.Row.build(cells));
        }
        return XEasyPdfHandler.Table.build(rows);
    }


}








