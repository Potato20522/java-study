package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfModel<T> {
    /**
     * 内容类型，默认文本
     */
    private PdfContentType type = PdfContentType.TEXT;

    /**
     * type为TABLE表格的业务数据
     */
    private List<T> tableData;
}