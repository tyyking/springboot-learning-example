package org.spring.springboot.utils.excel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 图片拆分
 */
public class Test2 {

    public static void main(String[] agrs) throws IOException {
        String originalImg = "D:\\1\\test.jpg";
        // 读入大图
        File file = new File(originalImg);
        FileInputStream fis = new FileInputStream(file);
        System.out.println(file.exists());
        BufferedImage image = ImageIO.read(fis);

        // 分割成3*3(9)个小图
        // 分割成2*1(2)个小图 todo
        int rows = 2;
        int cols = 1;
        int chunks = rows * cols;

        // 计算每个小图的宽度和高度
        int chunkWidth = image.getWidth() / cols;
        int chunkHeight = image.getHeight() / rows;
        System.out.println("图片的宽度为:" + chunkWidth * rows + "图片的高度为:" + chunkHeight * cols);//230,278
        //大图中的一部分
        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //设置小图的大小和类型
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                //写入图像内容
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                System.out.println("源矩阵第一个角的坐标" + chunkWidth * y + "+" + chunkHeight * x + "源矩阵第二个角的坐标" + chunkWidth * (y + 1) + "+" + chunkHeight * (x + 1));
                gr.dispose();
                /*
                 源矩阵第一个角的坐标0+0源矩阵第二个角的坐标77+92
                 源矩阵第一个角的坐标77+0源矩阵第二个角的坐标154+92
                 源矩阵第一个角的坐标154+0源矩阵第二个角的坐标231+92
                 源矩阵第一个角的坐标0+92源矩阵第二个角的坐标77+184
                 源矩阵第一个角的坐标77+92源矩阵第二个角的坐标154+184
                 源矩阵第一个角的坐标154+92源矩阵第二个角的坐标231+184
                 源矩阵第一个角的坐标0+184源矩阵第二个角的坐标77+276
                 源矩阵第一个角的坐标77+184源矩阵第二个角的坐标154+276
                 源矩阵第一个角的坐标154+184源矩阵第二个角的坐标231+276
                */
            }
        }
        // 输出小图
        for (int i = 0; i < imgs.length; i++) {
            //ImageIO.write(imgs[i], "jpg", new File("C:\\img\\split\\img" + i + ".jpg"));
            ImageIO.write(imgs[i], "jpg", new File("D:\\1\\22\\" + i + ".jpg"));
            System.out.println(i);
        }

        System.out.println("完成分割！");
    }
}