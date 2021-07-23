package com.bl.nop.common.util;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class XmlUtil {
	public static Map<String, String> buildXmlToMap(String resultXml) {
		Map<String, String> map = null;
		if (null == resultXml || "".equals(resultXml)) {
			return null;
		} else {
			try {
				DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document = documentBuilder.parse(new ByteArrayInputStream(resultXml.getBytes()));
	            Element element = document.getDocumentElement();
	            NodeList nodeList = element.getChildNodes();
	            map = new LinkedHashMap<String, String>();
	            for(int i=0;i<nodeList.getLength();i++){
	                Element e = (Element) nodeList.item(i);
	                map.put(e.getNodeName(),e.getTextContent());
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return map;
		}
		
	}
}
