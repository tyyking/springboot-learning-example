package org.spring.springboot.utils.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 图片转换PDF类
 */
public class ImgPDF {
    /**
     * 将图片转换成PDF
     * @param source        文件路径的集合 可以调用 FileUtil.getFileList() 方法
     * @param target        PDF的名字和位置
     */
    public static void ImgChangePDF(String []source, String target) {
        //创建一个文档对象
        Document doc = new Document();
        try {
            //定义输出文件的位置
            PdfWriter.getInstance(doc, new FileOutputStream(target));
            //开启文档
            doc.open();
            // 循环获取图片文件夹内的图片
            for (int i = 0; i < source.length; i++) {
                if(source[i] == null){      //前面的方法默认了数组长度是1024，所以这里就让它提前退出循环
                    break;
                }
                //路径
                Image  img = Image.getInstance(source[i]);
                //获得宽高
                Float h = img.getHeight();
                Float w = img.getWidth();
                //统一压缩
                Integer percent = getPercent(h, w);
                //图片居中
                img.setAlignment(Image.MIDDLE);
                //百分比显示图
//                img.scalePercent(percent);
                img.scalePercent(30);
                //设置高和宽的比例
                doc.add(img);
            }
            // 关闭文档
            if(doc != null){
                doc.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    /**
     * 压缩
     * @param
     */
    public static Integer getPercent(Float h,Float w)
    {
        Integer g=0;
        Float g2=0.0f;
        g2=480/w*100;
        g=Math.round(g2);
        return g;
    }
}
