package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WbDTO2 {

    @PdfTableProperty(title = "设备名称")
    private String deviceName;

    @PdfTableProperty(title = "设备id")
    private String deviceId;

    @PdfTableProperty(title = "产品名称")
    private String productName;

    @PdfTableProperty(title = "产品标识")
    private String productKey;

    @PdfTableProperty(title = "产品ID")
    private String productId;

    @PdfTableProperty(title = "设备位置")
    private String localName;

    @PdfTableProperty(title = "描述")
    private String type;
}
