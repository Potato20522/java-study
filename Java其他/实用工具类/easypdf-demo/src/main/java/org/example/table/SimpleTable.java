package org.example.table;

import lombok.SneakyThrows;
import wiki.xsx.core.pdf.component.table.XEasyPdfCell;
import wiki.xsx.core.pdf.component.table.XEasyPdfRow;
import wiki.xsx.core.pdf.component.table.XEasyPdfTable;
import wiki.xsx.core.pdf.component.text.XEasyPdfText;
import wiki.xsx.core.pdf.doc.XEasyPdfDefaultFontStyle;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//
public class SimpleTable {
    Map<String, Float> headerWidth = new HashMap<>();//Key:headerName,value:改单元格宽度
    Map<Field, String> headerFieldMap = new HashMap<>();//Key:属性名,value:headerName

    /**
     * 第一行是表头
     *
     * @param <T>
     */
    public <T> XEasyPdfRow firstRowHeader() {
        XEasyPdfRow row = XEasyPdfHandler.Table.Row.build();

        return row;
    }

    /**
     * 第一列是表头
     *
     * @param <T>
     */
    public <T> void firstColHeader() {

    }

    public <T> XEasyPdfTable buildVertical(List<T> datas) {
        return buildVertical(datas, false);
    }

    /**
     * 表头在第一行
     */
    @SneakyThrows
    public <T> XEasyPdfTable buildVertical(List<T> datas, boolean needHeader) {
        XEasyPdfTable table = XEasyPdfHandler.Table.build().enableCenterStyle();
        if (datas == null || datas.isEmpty()) {
            return table;
        }
        Class<?> clazz = datas.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            return table;
        }
        //表头
        List<XEasyPdfCell> header = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            TableProperty annotation = field.getAnnotation(TableProperty.class);
            if (isCollection(field) || (annotation != null && annotation.ignore())) {
                continue;
            }
            String headerName = decideHeaderName(annotation, field);
            XEasyPdfText text = XEasyPdfHandler.Text.build(headerName).setDefaultFontStyle(XEasyPdfDefaultFontStyle.BOLD);
            float width = decideHeaderCellWidth(headerName, annotation, text.getFontSize());
            headerWidth.put(headerName, width);
            headerFieldMap.put(field, headerName);
            XEasyPdfCell cell = XEasyPdfHandler.Table.Row.Cell.build(width).addContent(text);
            header.add(cell);
        }
        XEasyPdfRow headerRow = XEasyPdfHandler.Table.Row.build(header);
        table.setTileRow(headerRow);

        for (T obj : datas) {
            List<XEasyPdfCell> cells = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                if (isCollection(field)) {
                    continue;
                }
                Float width = headerWidth.get(headerFieldMap.get(field));
                Object filedValue = field.get(obj);
                String value = String.valueOf(filedValue);
                XEasyPdfText text = XEasyPdfHandler.Text.build(value);

                XEasyPdfCell cell = XEasyPdfHandler.Table.Row.Cell.build(width).addContent(text);
                cells.add(cell);
            }
            table.addRow(XEasyPdfHandler.Table.Row.build(cells));
        }
        return table;
    }

    private String decideHeaderName(TableProperty annotation, Field field) {
        if (annotation != null) {
            return annotation.value();
        } else {
            return field.getName();
        }
    }

    private float decideHeaderCellWidth(String headerName, TableProperty annotation, float fontSize) {
        if (annotation != null && annotation.width() > 0) {
            return annotation.width();
        }
        return (headerName.length() + 2) * fontSize;
    }

    /**
     * 表头在第一列,第二列就是数据，不居中(默认靠左)
     */
    public <T> XEasyPdfTable buildHorizontal(T data) {
        return buildHorizontal(data, false);
    }

    /**
     * 表头在第一列,第二列就是数据，是否居中
     */
    public <T> XEasyPdfTable buildHorizontal(T data, boolean center) {
        XEasyPdfTable table = XEasyPdfHandler.Table.build();
        if (center) {
            table.enableCenterStyle();
        }
        if (data == null) {
            return table;
        }
        Field[] fields = data.getClass().getDeclaredFields();
        if (fields.length == 0) {
            return table;
        }
        for (Field field : fields) {
            TableProperty annotation = field.getAnnotation(TableProperty.class);
            String headerName = decideHeaderName(annotation, field);

        }

        return table;
    }

    /**
     * 只有N条数据行，没有表头
     */
    @SneakyThrows
    public <T> XEasyPdfTable onlyDataRowTable(List<T> datas, Map<String, Float> headerWidth) {
        if (datas == null || datas.isEmpty()) {
            throw new TableException("no data,can not create table");
        }
        XEasyPdfTable table = XEasyPdfHandler.Table.build();
        Class<?> clazz = datas.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (T obj : datas) {
            List<XEasyPdfCell> cells = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                if (isCollection(field)) {
                    continue;
                }
                Float width = headerWidth.get(headerFieldMap.get(field));
                Object filedValue = field.get(obj);
                String value = String.valueOf(filedValue);
                XEasyPdfText text = XEasyPdfHandler.Text.build(value);

                XEasyPdfCell cell = XEasyPdfHandler.Table.Row.Cell.build(width).addContent(text);
                cells.add(cell);
            }
            table.addRow(XEasyPdfHandler.Table.Row.build(cells));
        }
        return null;
    }

    private boolean isCollection(Field field) {
        Class<?> type = field.getType();
        return (type.equals(List.class) || type.equals(ArrayList.class) ||
                type.equals(Map.class) || type.equals(HashMap.class));
    }

}
