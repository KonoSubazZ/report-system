package com.novo.report.utils;

import com.swetake.util.Qrcode;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class CreateQRCode {
    //    public static String createQRCode(String content, String logo) {
//        String binary = null;
//        try {
//            Qrcode qrcodeHandler = new Qrcode();
//            qrcodeHandler.setQrcodeErrorCorrect('M');
//            qrcodeHandler.setQrcodeEncodeMode('B');
//            qrcodeHandler.setQrcodeVersion(7);
//            // System.out.println(content);
//            byte[] contentBytes = content.getBytes("gb2312");
//            //构造一个BufferedImage对象 设置宽、高
//            BufferedImage bufImg = new BufferedImage(140, 140, BufferedImage.TYPE_INT_RGB);
//            Graphics2D gs = bufImg.createGraphics();
//            gs.setBackground(Color.WHITE);
//            gs.clearRect(0, 0, 140, 140);
//            // 设定图像颜色 > BLACK
////            gs.setColor(Color.black);
//            gs.setColor(new Color(97,150,76));
//            // 设置偏移量 不设置可能导致解析出错
//            int pixoff = 2;
//            // 输出内容 > 二维码
//            if (contentBytes.length > 0 && contentBytes.length < 120) {
//                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
//                for (int i = 0; i < codeOut.length; i++) {
//                    for (int j = 0; j < codeOut.length; j++) {
//                        if (codeOut[j][i]) {
//                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
//                        }
//                    }
//                }
//            } else {
//                System.err.println("QRCode content bytes length = "+ contentBytes.length + " not in [ 0,120 ]. ");
//                return binary;
//            }
//            Image img = ImageIO.read(new File(logo));//实例化一个Image对象。
//            gs.drawImage(img, 55, 55, 30, 30, null);
//            gs.dispose();
//            bufImg.flush();
//            // 生成二维码QRCode图片
////            File imgFile = new File(imgPath);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            ImageIO.write(bufImg, "png", out);
//            byte[] bytes = out.toByteArray();
//
//            BASE64Encoder encoder = new BASE64Encoder();
//            binary = encoder.encodeBuffer(bytes).trim();
//        }catch (Exception e){
//            e.printStackTrace();
//            return binary;
//        }
//        return binary;
//    }
    public static String createQRCode(String content, String logo) {
        String binary = null;
        try {
            // 创建二维码生成器对象并设置参数
            Qrcode qrcodeHandler = createQRCodeHandler();

            // 将二维码内容转为字节数组
            byte[] contentBytes = content.getBytes("gb2312");

            // 创建二维码图像
            BufferedImage bufImg = createQRCodeImage(qrcodeHandler, contentBytes);

            // 插入 logo 到二维码图像中
            insertLogo(bufImg, logo);

            // 将二维码图像转换为 Base64 编码的字符串
            binary = encodeToBase64(bufImg);

        } catch (Exception e) {
            // 输出异常信息
            e.printStackTrace();
            return null;
        }
        return binary;
    }

    // 创建二维码生成器并设置相关参数
    private static Qrcode createQRCodeHandler() {
        Qrcode qrcodeHandler = new Qrcode();
        qrcodeHandler.setQrcodeErrorCorrect('M'); // 设置错误纠正级别
        qrcodeHandler.setQrcodeEncodeMode('B');   // 设置编码模式
        qrcodeHandler.setQrcodeVersion(7);        // 设置二维码版本
        return qrcodeHandler;
    }

    // 创建二维码图像
    private static BufferedImage createQRCodeImage(Qrcode qrcodeHandler, byte[] contentBytes) throws Exception {
        if (contentBytes.length <= 0 || contentBytes.length >= 120) {
            throw new IllegalArgumentException("二维码内容字节长度必须在 [0, 120] 范围内");
        }

        BufferedImage bufImg = new BufferedImage(140, 140, BufferedImage.TYPE_INT_RGB);
        // 创建 Graphics2D 对象
        Graphics2D gs = bufImg.createGraphics();
        try {
            gs.setBackground(Color.WHITE); // 设置背景颜色为白色
            gs.clearRect(0, 0, 140, 140);  // 清空画布区域
            gs.setColor(new Color(97, 150, 76)); // 设置二维码颜色

            int pixoff = 2; // 偏移量，避免二维码解析出错

            // 生成二维码内容并绘制
            boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
            for (int i = 0; i < codeOut.length; i++) {
                for (int j = 0; j < codeOut.length; j++) {
                    if (codeOut[j][i]) {
                        // 如果该位置为黑色，绘制一个小矩形
                        gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                    }
                }
            }
        } finally {
            // 确保关闭 Graphics2D 对象
            if (gs != null) {
                gs.dispose();
            }
        }
        return bufImg;
    }

    // 插入 logo 到二维码图像
    private static void insertLogo(BufferedImage bufImg, String logo) throws Exception {
        Image img = ImageIO.read(new File(logo));
        Graphics2D gs = bufImg.createGraphics();
        gs.drawImage(img, 55, 55, 30, 30, null); // 将 logo 绘制到二维码中心
        gs.dispose();
    }

    // 将二维码图像转换为 Base64 编码
    private static String encodeToBase64(BufferedImage bufImg) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(bufImg, "png", out);
            byte[] bytes = out.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encodeBuffer(bytes).trim();
        }
    }

}
