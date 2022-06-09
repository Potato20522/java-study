package org.example;

import org.junit.jupiter.api.Test;
import wiki.xsx.core.pdf.component.XEasyPdfComponent;
import wiki.xsx.core.pdf.component.table.XEasyPdfCell;
import wiki.xsx.core.pdf.component.table.XEasyPdfRow;
import wiki.xsx.core.pdf.component.table.XEasyPdfTable;
import wiki.xsx.core.pdf.component.text.XEasyPdfText;
import wiki.xsx.core.pdf.doc.XEasyPdfDocument;
import wiki.xsx.core.pdf.doc.XEasyPdfPage;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TableDemo {
    @Test
    void length() {
        System.out.println("内容".length());
    }
    @Test
    void demo1() {
        List<XEasyPdfRow> rows = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<XEasyPdfCell> cells = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                XEasyPdfText text = XEasyPdfHandler.Text.build("内容");
                XEasyPdfCell cell = XEasyPdfHandler.Table.Row.Cell.build(text.getFontSize()*3).addContent(text);
                cells.add(cell);
            }
            XEasyPdfRow row = XEasyPdfHandler.Table.Row.build(cells);
            rows.add(row);
        }
        XEasyPdfTable table = XEasyPdfHandler.Table.build(rows)
                .enableCenterStyle().setMarginLeft(20F);

        List<XEasyPdfComponent> components = new ArrayList<>();
        components.add(table);
        XEasyPdfPage page = XEasyPdfHandler.Page.build(components);

        XEasyPdfDocument document = XEasyPdfHandler.Document.build(page);
        document.save("./target/TableDemo1.pdf").close();
    }

    @Test
    void simpleTable() {
        List<Person> persons = mockPerson();
        List<XEasyPdfRow> rows = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<XEasyPdfCell> cells = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                XEasyPdfText text = XEasyPdfHandler.Text.build("内容");
                XEasyPdfCell cell = XEasyPdfHandler.Table.Row.Cell.build(text.getFontSize()*3).addContent(text);
                cells.add(cell);
            }
            XEasyPdfRow row = XEasyPdfHandler.Table.Row.build(cells);
            rows.add(row);
        }
        XEasyPdfTable table = XEasyPdfHandler.Table.build(rows)
                .enableCenterStyle().setMarginLeft(20F);

        List<XEasyPdfComponent> components = new ArrayList<>();
        components.add(table);
        XEasyPdfPage page = XEasyPdfHandler.Page.build(components);

        XEasyPdfDocument document = XEasyPdfHandler.Document.build(page);
        document.save("./target/TableDemo1.pdf").close();
    }


    private List<Person> mockPerson() {
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(Randoms.nextObject(Person.class));
        }
        return list;
    }
}
