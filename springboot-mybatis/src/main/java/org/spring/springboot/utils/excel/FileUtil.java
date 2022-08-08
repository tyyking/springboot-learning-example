package org.spring.springboot.utils.excel;

import java.io.File;

/**
 * 文件处理工具类
 */
public class FileUtil {
    private static int index = 0;  //存储数组
    /**
     * 截取文件后缀名
     * @param file  文件类
     * @return
     */
    public static String fileLastName(File file) {
        if (file == null) {
        		return null;
        	}
        String filename = file.getName();
        String[] split = filename.split("\\.");
        if (split.length > 1) {
            return split[split.length - 1];
        } else {
            return "";
        }
    }

    /**
     * 返回列表文件
     * @param filePath  字符串 文件的路径 比如 G:/HTML/testData
     * @return
     */
    public static String[] getFileList(String filePath){
        File file = new File(filePath); //获得文件路径
        File []fileList = file.listFiles(); //获得下属列表
        String[] strList = new String[1024];    //数组存储
        for(File f :fileList){
            //只要图片
            if(fileLastName(f) == "jpg" || fileLastName(f).equals("jpg") || fileLastName(f) == "png" || fileLastName(f).equals("png")){
                strList[index++] = f.toString();         //存储进去
            }
        }
        return strList;
    }
}
