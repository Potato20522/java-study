package org.example;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Dcm {
    public static void main(String[] args) throws IOException {
        DicomInputStream dis = new DicomInputStream(Files.newInputStream(Paths.get("E:\\杂项\\hangshengqi_20230311-135101-0039_135336\\gre_scout_101\\00000001.dcm")));
        Attributes attributes = new Attributes();
        attributes.setString(Tag.SOPInstanceUID, VR.UI);
        //读取预先传入的指定Tag，效率高，占用内存少
        dis.readNeedDataset(attributes);
        System.out.println(attributes.getString(Tag.SOPInstanceUID));
    }
}
