package com.cy.recruit.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;
import java.util.Random;

import static javax.swing.UIManager.getColor;

/**
 * 验证码生成器
 *
 */
@Slf4j
public class SCaptchaUtils {
    // 图片的宽度。
    private int width = 140;
    // 图片的高度。
    private int height = 40;
    // 验证码字符个数
    private int codeCount = 4;
    // 验证码干扰线数
    private int lineCount = 3;
    // 验证码
    private String code = null;
    // 验证码图片Buffer
    private BufferedImage buffImg = null;

    private char[] codeSequence = {'a', 'b', 'c', 'd', 'e', 'f', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'M', 'N', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };
    // 生成随机数
    private Random random = new Random();

    public SCaptchaUtils() {
        this.createCode();
    }

    /**
     *
     * @param width 图片宽
     * @param height 图片高
     */
    public SCaptchaUtils(int width, int height) {
        this.width = width;
        this.height = height;
        this.createCode();
    }

    /**
     *
     * @param width 图片宽
     * @param height 图片高
     * @param codeCount 字符个数
     * @param lineCount 干扰线条数
     */
    public SCaptchaUtils(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        this.createCode();
    }

    public void createCode() {
        int codeX = 0;
        int fontHeight = 0;
        fontHeight = height - 5;// 字体的高度
        codeX = width / (codeCount + 2);// 每个字符的宽度

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 创建字体
        Font font = getRandomFont("ttf/font"+ getRandomNumber(1,5) +".ttf", fontHeight);

        g.setFont(font);

        StringBuffer randomCode = new StringBuffer();
        // 随机产生验证码字符
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 设置字体颜色
            g.setColor(getRandomColor());
            int h = getRandomNumber(25, 35);
            int w = (i + 1) * codeX;
            double rate = Math.PI * (getRandomNumber(-30, 30)/180D);
            g.rotate(-rate, w, h/2);
            // 设置字体位置
            g.drawString(strRand, w, h);
            g.rotate(rate, w, h/2);
            randomCode.append(strRand);
        }
        createRandomLine(width,height,lineCount, g, 255);
        code = randomCode.toString();
    }

    private void createRandomLine(int width,int height,int minMany,Graphics g,int alpha) {  // 随机产生干扰线条
        for (int i = 0; i < getRandomNumber(minMany, minMany + 5); i++) {
            int x1 = getRandomNumber(0, (int) (width * 0.6));
            int y1 = getRandomNumber(0, (int) (height * 0.6));
            int x2 = getRandomNumber((int) (width * 0.4), width);
            int y2 = getRandomNumber((int) (height * 0.2), height);
            g.setColor(getColor(alpha));
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /** 获取随机颜色 */
    private Color getRandomColor() {
        int r = getRandomNumber(220,256);//颜色偏亮色
        int g = getRandomNumber(256);
        int b = getRandomNumber(256);
        return new Color(r, g, b);
    }

    /** 获取随机数 */
    private int getRandomNumber(int number) {
        return random.nextInt(number);
    }

    private int getRandomNumber(int min, int max){
        return random.nextInt(max-min) + min;
    }

    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {
        return code;
    }

    private Font getRandomFont(String fontFilename, int fontHeight){
        try{
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(fontFilename);
            Font actionJson = Font.createFont(Font.TRUETYPE_FONT,is);//返回一个指定字体类型和输入数据的font
            Font actionJsonBase = actionJson.deriveFont(Font.BOLD,fontHeight);
            return actionJsonBase;
        }catch (Exception e){
            return new Font("Verdana", Font.BOLD, fontHeight);
        }
    }
}