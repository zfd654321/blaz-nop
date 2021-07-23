package com.bl.nop.oms.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 描述：Excel写操作帮助类
 *
 * 
 */
public class ExcelUtil {

	//
	//
	public static boolean doExcel(String filePath, String sheetname, List<String> tittle, List<List<String>> list,
			Map<Integer, Integer> linewidth) {
		boolean result = true;
		try {
			File file = new File(filePath);
			File fileParent = file.getParentFile();  
			if(!fileParent.exists()){  
			    fileParent.mkdirs();  
			}  
			file.createNewFile(); 
			WritableWorkbook book = null;
			book = Workbook.createWorkbook(file);

			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet(sheetname, 0);
			// 设置页面默认格宽 度
			sheet.getSettings().setDefaultColumnWidth(10);
			// 设置单个列宽度
			for (Integer line : linewidth.keySet()) {
				sheet.setColumnView(line, linewidth.get(line));
			}
			// cellView.setAutosize(true);
			// sheet.getSettings().setFitHeight(10);
			// 设置标题行的样式
			WritableFont font1 = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD);
			// 设置标题行的字体颜色
			font1.setColour(Colour.WHITE);
			WritableCellFormat format1 = new WritableCellFormat(font1);
			// 设置标题行的背景颜色
			format1.setBackground(Colour.BLUE2);
			format1.setFont(font1);
			// 设置标题行边框;
			format1.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			// 设置标题行文字居中对齐方式;
			format1.setAlignment(Alignment.CENTRE);
			// 设置自动换行
			format1.setWrap(true);

			// 设置普通行的样式
			WritableFont font2 = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD);
			WritableCellFormat format2 = new WritableCellFormat(font2);
			// 设置普通行边框;
			format2.setBorder(Border.ALL, BorderLineStyle.THIN);
			// 设置标题行文字居中对齐方式;
			format2.setAlignment(Alignment.CENTRE);
			// 设置自动换行
			format2.setWrap(false);

			// 输入标题内容
			for (int i = 0; i < tittle.size(); i++) {
				sheet.addCell(new Label(i, 1, tittle.get(i), format1));

			}
			/*
			 * 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123
			 */
			// jxl.write.Number number = new jxl.write.Number(1, 0, 555.12541);
			// 输入List内容
			for (int i = 0; i < list.size(); i++) {
				List<String> p = list.get(i);
				for (int j = 0; j < p.size(); j++) {
					sheet.addCell(new Label(j, i + 2, p.get(j), format2));
				}

			}
			// 写入数据并关闭文件
			book.write();
			book.close();

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		// System.out.println(result);
		return result;
	}

	// main方法Demo示例
	public static void main(String[] args) {

		// 文件名
		String filePath = "G:\\文件名.xls";

		// 页名
		String sheetname = "Sheet1";

		// 标题行
		List<String> tittle = new ArrayList<>();
		tittle.add("标题列1");
		tittle.add("标题列2");
		tittle.add("标题列3");
		tittle.add("标题列4");
		tittle.add("标题列5");
		tittle.add("标题列6");

		// 行宽(可不设置，默认10)
		Map<Integer, Integer> linewidth = new HashMap<>();
		// 从第0行开始
		linewidth.put(0, 20);
		linewidth.put(2, 20);

		// 内容行
		List<List<String>> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			List<String> mlist = new ArrayList<>();
			mlist.add("XMS755XXXX7");
			mlist.add("2017-11-23");
			mlist.add("内容行" + i + "列3");
			mlist.add("内容行" + i + "列4");
			mlist.add("内容行" + i + "列5");
			mlist.add("内容行" + i + "列6");
			list.add(mlist);
		}
		doExcel(filePath, sheetname, tittle, list, linewidth);
		// System.out.println(result);
	}

}