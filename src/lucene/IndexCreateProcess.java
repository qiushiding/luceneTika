package lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.ant.DocumentHandlerException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import saxAPI.WikiTest;

public class IndexCreateProcess extends DefaultHandler {
	
	private StringBuilder elementBuffer = new StringBuilder();
	private Map<String, String> attributeMap = new HashMap<String, String>();
	private Document doc;
	IndexWriter writer=null;

	/**
	 * 构造函数，初始化索引存储的目标文件夹
	 * @param indexDir 目标文件夹
	 */
	public IndexCreateProcess (String indexDir){
		try {
			writer = this.getWriter(indexDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 解析xml文件的入口，并添加到lucene的Document中
	 * @param xmlFileName xml文件
	 * @return lucene的Document对象
	 * @throws DocumentHandlerException
	 * @throws FileNotFoundException
	 */
	public Document getDocument( File xmlFileName)
			throws DocumentHandlerException, FileNotFoundException {
		InputStream is = new FileInputStream (xmlFileName);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser parser = spf.newSAXParser();
			parser.parse(is, this);
		} catch (Exception e) {
			throw new DocumentHandlerException(e);
			// "Cannot parse XML document", e);
		}
		doc.add(new Field("fileName", xmlFileName.getName(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
		return doc;
	}

	public void startDocument() {
		doc = new Document();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName,
			String qName, Attributes atts) throws SAXException {
		elementBuffer.setLength(0);
		attributeMap.clear();
		int numAtts = atts.getLength();
		if (numAtts > 0) {
			for (int i = 0; i < numAtts; i++) {
				attributeMap.put(atts.getQName(i), atts.getValue(i));
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] text, int start, int length) {
		elementBuffer.append(text, start, length);
	}

	/* 
	　* 需要在此设置根属性
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("us-patent-grant")) {
			return;
		}else {
			doc.add(new Field(qName, elementBuffer.toString(), Field.Store.NO,
					Field.Index.ANALYZED));
		}
	}

	/**
	 * 把某个文件加入索引
	 * @param xmlFile 目标文件
	 */
	public void addToIndex(File xmlFile){
		try {
//			doc = this.getDocument(new FileInputStream (xmlFile));
			doc = this.getDocument(xmlFile);
			writer.addDocument(doc);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成indexWriter
	 * @param path 索引保存的文件夹
	 * @return indexWriter对象
	 * @throws Exception
	 */
	public IndexWriter getWriter(String path) throws Exception{
		Directory dir=FSDirectory.open(new File(path));
		IndexWriterConfig iwConfig=new IndexWriterConfig(Version.LUCENE_36,
				new StandardAnalyzer(Version.LUCENE_36));
		IndexWriter iWriter=new IndexWriter(dir, iwConfig);
		//dir  的关闭
		return iWriter;
	}//

	/**
	 * 关闭写索引的资源
	 */
	public void closeWriter(){
		try {
			if(this.writer != null)
				this.writer.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]) throws Exception {
		WikiTest handler = new WikiTest();
		Document doc = handler.getDocument(new FileInputStream(new File(
				"C:\\Users\\qiusd\\Desktop\\temp\\ipg130101_1.xml")));
		// new FileInputStream(new File(args[0])));
		System.out.println(doc);
	}
}
