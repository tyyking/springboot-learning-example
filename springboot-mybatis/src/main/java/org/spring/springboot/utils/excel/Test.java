package org.spring.springboot.utils.excel;

/**
 * 多张图片转成pdf
 */
public class Test {
    public static void main(String[] args) {
        String[] fileList = FileUtil.getFileList("D:\\1\\22");
        String target = "D:/1/0.pdf";
        ImgPDF.ImgChangePDF(fileList,target);
    }
}
