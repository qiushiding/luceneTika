package TestCode;

import java.io.File;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import XMLProcess.XMLParse;


public class XMLParseTest {

	public static void main(String[] args) throws Exception {
		
		XMLParse xmlParse = new XMLParse(new File("C:\\Users\\qiusd\\Desktop\\patent\\ipg130108_40.xml"));
		
		String string = xmlParse.GetFirstElememt("doc-number");
		
		System.out.println(string);
		
	}

}
