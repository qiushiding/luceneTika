package XMLProcess;

import java.io.*;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.*;

public class XMLParse {

	String xmlString = null;
	SAXBuilder sBuilder;
	Document doc;

	/**
	 * 构造函数 输入XML文件
	 * 
	 * @param xmlFile
	 */
	public XMLParse(File xmlFile) throws JDOMException, Exception {
		BufferedReader br = new BufferedReader(new FileReader(xmlFile));
		StringBuilder sb = new StringBuilder();
		sb.capacity();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		br.close();
		xmlString = sb.toString();
		sBuilder = new SAXBuilder();
		doc = sBuilder.build(new StringReader(xmlString));
	}

	/**
	 * 构造函数 输入XML的String
	 * 
	 * @param xmlString
	 */
	public XMLParse(String xmlString) throws Exception {
		this.xmlString = xmlString;
		sBuilder = new SAXBuilder();
		doc = sBuilder.build(new StringReader(this.xmlString));
	}

	private void TraverseXML() {

	}

	public String GetFirstElememt(String targetName) throws Exception {
		Element root = doc.getRootElement();
		Element element = TraverseForFirstElement(root, targetName);
		if (element == null)
			return "not found";
		return element.getValue();
	}

	public Element TraverseForFirstElement(Element rootElement,
			String targetName) throws Exception {
		if (rootElement.getName() == targetName) {
			return rootElement;
		}
		List<Element> elements = rootElement.getChildren();
		for (int i = 0; i < elements.size(); i++) {
			Element element = TraverseForFirstElement(elements.get(i),
					targetName);
			if (element != null) {
				return element;
			}
		}
		return null;
	}

}