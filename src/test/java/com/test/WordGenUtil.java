//package com.test;
//
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.deepoove.poi.XWPFTemplate;
//import com.deepoove.poi.data.RowRenderData;
//import com.deepoove.poi.data.Rows;
//import com.deepoove.poi.data.Tables;
//import com.deepoove.poi.data.Texts;
//import com.deepoove.poi.util.PoitlIOUtils;
//
//public class WordGenUtil {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		XWPFTemplate template = XWPFTemplate.compile("d:\\test.docx");
//		Map<String, Object> map = new HashMap<String, Object>();
//
//
//		map.put("name", "Sayi");
////		map.put("author", new TextRenderData("000000", "Sayi"));
////		map.put("link", new HyperlinkTextRenderData("website", "http://deepoove.com"));
////		map.put("anchor", new HyperlinkTextRenderData("anchortxt", "anchor:appendix1"));
//
//		map.put("author", Texts.of("Sayi").color("000000").create());
//		map.put("link", Texts.of("website").link("http://deepoove.com").create());
//		map.put("anchor", Texts.of("anchortxt").anchor("appendix1").create());
//
//		// 第0行居中且背景为蓝色的表格
//		RowRenderData row0 = Rows.of("姓名", "学历").textColor("FFFFFF")
//		      .bgColor("4472C4").center().create();
//		RowRenderData row1 = Rows.create("李四", "博士");
//		map.put("table1", Tables.create(row0, row1));
//		try {
//			template.render(map);
//			template.write(new FileOutputStream("d:\\test.docx"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally {
//			PoitlIOUtils.closeQuietlyMulti(template);
//		}
//	}
//
//}
