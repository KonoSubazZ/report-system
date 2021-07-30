package com.novo.report.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.apache.axis.encoding.Base64;
import org.apache.commons.net.ftp.FTPClient;

public class YSImg {
    /** 
     * 采用指定宽度、高度或压缩比例 的方式对图片进行压缩 
     * @param imgsrc 源图片地址 
     * @param imgdist 目标图片地址 
     * @param widthdist 压缩后图片宽度（当rate==null时，必传） 
     * @param heightdist 压缩后图片高度（当rate==null时，必传） 
     * @param rate 压缩比例  
     */  
    public static void reduceImg(String imgsrc, String imgdist, int widthdist,  
            int heightdist, Float rate) {  
        try {  
            File srcfile = new File(imgsrc);  
            // 检查文件是否存在  
            if (!srcfile.exists()) {  
                return;  
            }  
            System.out.println("文件存在 开始压缩");
            // 如果rate不为空说明是按比例压缩  
            if (rate != null && rate > 0) {  
                // 获取文件高度和宽度  
                int[] results = getImgWidth(srcfile);  
                if (results == null || results[0] == 0 || results[1] == 0) {  
                    return;  
                } else {  
                    widthdist = (int) (results[0] * rate);  
                    heightdist = (int) (results[1] * rate);  
                }  
            }  
            // 开始读取文件并进行压缩  
            Image src = javax.imageio.ImageIO.read(srcfile);  
            //Image src = ImageIO.read(srcfile);  
            BufferedImage tag = new BufferedImage((int) widthdist,  
                    (int) heightdist, BufferedImage.TYPE_INT_RGB);  
  
            tag.getGraphics().drawImage(  
                    src.getScaledInstance(widthdist, heightdist,  
                            Image.SCALE_SMOOTH), 0, 0, null);  
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
            //encoder.encode(tag);  
            FileOutputStream out = new FileOutputStream(imgdist);  
            ImageIO.write(tag, "jpeg", out);
            out.close();  
            System.out.println("压缩成功");
  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }  
    /** 
     * 获取图片宽度 
     *  
     * @param file 
     *            图片文件 
     * @return 宽度 
     */  
    public static int[] getImgWidth(File file) {  
        InputStream is = null;  
        BufferedImage src = null;  
        int result[] = { 0, 0 };  
        try {  
            is = new FileInputStream(file);  
            src = javax.imageio.ImageIO.read(is);  
            result[0] = src.getWidth(null); // 得到源图宽  
            result[1] = src.getHeight(null); // 得到源图高  
            is.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
    
    public static String getCompressBase64FromUrl(InputStream retrieveFileStream, int width, int height) throws Exception {
        Image img = ImageIO.read(retrieveFileStream);
        
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的优先级比速度高 生成的图片质量比较好 但速度慢
        tag.getGraphics().drawImage(img.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, null);
     
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(tag, "jpg", baos); // 这里也可以传 FileOutputStream 写进文件里
        return new String(Base64.encode(baos.toByteArray()));
    }
    public static boolean compressPic(String srcFilePath, String descFilePath)  {
        File file = null;
        BufferedImage src = null;
        FileOutputStream out = null;
        ImageWriter imgWrier;
        ImageWriteParam imgWriteParams;
 
        try {
			// 指定写图片的方式为 jpg
			imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
			imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
			        null);
			// 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
			imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
			// 这里指定压缩的程度，参数qality是取值0~1范围内，
			imgWriteParams.setCompressionQuality((float) 0.1);
			imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
			ColorModel colorModel =ImageIO.read(new File(srcFilePath)).getColorModel();// ColorModel.getRGBdefault();
			// 指定压缩时使用的色彩模式
//        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
//                colorModel, colorModel.createCompatibleSampleModel(16, 16)));
			imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
			        colorModel, colorModel.createCompatibleSampleModel(16, 16)));
			 try {
		            if (isBlank(srcFilePath)) {
		                return false;
		            } else {
		                file = new File(srcFilePath);System.out.println(file.length());
		                src = ImageIO.read(file);
		                out = new FileOutputStream(descFilePath);
		 
		                imgWrier.reset();
		                // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
		                // OutputStream构造
		                imgWrier.setOutput(ImageIO.createImageOutputStream(out));
		                // 调用write方法，就可以向输入流写图片
		                imgWrier.write(null, new IIOImage(src, null, null),
		                        imgWriteParams);
		                out.flush();
		                out.close();
		            }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 
       
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0 || string.trim().equals("")) {
            return true;
        }
        return false;
    }
}
