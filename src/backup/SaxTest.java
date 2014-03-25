/**
 * Copyright 1998-2013,2013 南方人才网
 * 文件名： WikiTest.java
 * JDK版本： 1.6.0_10
 * 作者： yf
 * 日期： 下午02:25:00
 * 描述：账号Service类
 * 
 */
package backup;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.ant.DocumentHandlerException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

// From chapter 7
public class SaxTest extends DefaultHandler {

	private StringBuilder elementBuffer = new StringBuilder();
	private Map<String, String> attributeMap = new HashMap<String, String>();

	private Document doc;
	IndexWriter writer = null;

	public Document getDocument(InputStream is) // #1
			throws DocumentHandlerException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser parser = spf.newSAXParser();
			parser.parse(is, this);
		} catch (Exception e) {
			throw new DocumentHandlerException(e);
			// "Cannot parse XML document", e);
		}
		return doc;
	}

	public void startDocument() {
		doc = new Document();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		elementBuffer.setLength(0);
		attributeMap.clear();
		int numAtts = atts.getLength();
		if (numAtts > 0) {
			for (int i = 0; i < numAtts; i++) {
				attributeMap.put(atts.getQName(i), atts.getValue(i));
			}
		}
	}

	public void characters(char[] text, int start, int length) { // #4
		elementBuffer.append(text, start, length);
	}

	public void endElement(String uri, String localName, String qName) // #5
			throws SAXException {
		if (qName.equals("us-patent-grant")) {
			return;
		} else if (qName.equals("contact")) {
			for (Entry<String, String> attribute : attributeMap.entrySet()) {
				String attName = attribute.getKey();
				String attValue = attribute.getValue();
				doc.add(new Field(attName, attValue, Field.Store.YES,
						Field.Index.NOT_ANALYZED));
			}
		} else {
			doc.add(new Field(qName, elementBuffer.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
	}

	public void addToIndex(File xmlFile) {
	}

	// 生成indexWriter
	public IndexWriter getWriter(String path) throws Exception {
		Directory dir = FSDirectory.open(new File(path));
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_36,
				new StandardAnalyzer(Version.LUCENE_36));
		IndexWriter iWriter = new IndexWriter(dir, iwConfig);
		// dir 的关闭
		return iWriter;
	}
}
