package com.gz.design.panda.biz.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangpeng on 2018/02/01.
 */
public class PdfUtils {
    // 利用模板生成pdf  
    public static void pdfout(Map<String,String> inMap,String templatePath,String newPDFPath) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font FontChinese = new Font(bf, 5, Font.NORMAL);
        FileOutputStream  out = new FileOutputStream(newPDFPath);// 输出流
        PdfReader reader = new PdfReader(templatePath);// 读取pdf模板
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, bos);
        AcroFields form = stamper.getAcroFields();
        //文字类的内容处理
        form.addSubstitutionFont(bf);
        for(String key : inMap.keySet()){
            String value = inMap.get(key);
            form.setField(key,value);
        }
        stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
        stamper.close();
        Document doc = new Document();
        Font font = new Font(bf, 32);
        PdfCopy copy = new PdfCopy(doc, out);
        doc.open();
        PdfImportedPage importPage = null;
        ///循环是处理成品只显示一页的问题
        for (int i=1;i<=reader.getNumberOfPages();i++) {
            importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
            copy.addPage(importPage);
        }
        doc.close();
    }

    public static void main(String[] args) {
        String templatePath = "C:\\Users\\9188\\Desktop\\exportPDF\\BorrowerAccidentInsurancetemplate.pdf";
        String newPDFPath =  "C:\\Users\\9188\\Desktop\\exportPDF\\PDFTest\\123.pdf";
        Map<String,String> map = new HashMap();
        map.put("name","张三张三");
        map.put("id","411326199212161518");
        map.put("amount","198");
        map.put("year","2018");
        map.put("month","12");
        map.put("day","26");
    try {
        pdfout(map,templatePath,newPDFPath);
    } catch (IOException e) {
        e.printStackTrace();
    } catch (DocumentException e) {
        e.printStackTrace();
    }


    }
}