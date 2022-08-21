package com.gitee.potato20522.spi;

import java.util.ServiceLoader;

public class TestSpi {
    public static void main(String[] args) {
        ServiceLoader<UploadCDN> uploadCDN = ServiceLoader.load(UploadCDN.class);
        for (UploadCDN u : uploadCDN) {
            u.upload("filePath");
        }
    }
}
