package com.potato;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class JFrameDemo2 {
    public static void main(String[] args) {
//        FlatLightLaf.setup();
        new MyJframe2().init();
    }
}
class MyJframe2 extends JFrame {
    public void init(){
        this.setVisible(true);
        this.setBounds(10,10,200,300);
        JLabel label = new JLabel("欢迎");
        this.add(label);
        label.setHorizontalAlignment(SwingConstants.CENTER);//设置水平对齐
        //获得一个容器
//        Container container = this.getContentPane();
//        container.setBackground(Color.BLUE);
    }
}