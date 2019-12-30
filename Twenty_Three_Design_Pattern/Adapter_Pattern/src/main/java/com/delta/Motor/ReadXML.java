package com.delta.Motor;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;


public class ReadXML {
    public  static Object getObject(){
        try {
            DocumentBuilderFactory dFactury = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dFactury.newDocumentBuilder();
            Document doc;

            doc = documentBuilder.parse(new FileInputStream("E:\\work\\Twenty_Three_Design_Pattern\\Adapter_Pattern\\src\\main\\resources\\config.xml"));
            NodeList className = doc.getElementsByTagName("className");
            Node firstChild = className.item(0).getFirstChild();
            String cName ="com.delta.Motor." + firstChild.getNodeValue();
            //System.out.println(cName);

            Class<?> c = Class.forName(cName);
            Object o = c.newInstance();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
