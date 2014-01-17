package demo;

import java.io.File;
import java.io.IOException;

import lucene.IndexCreateProcess;

import divideXML.FileProcess;

public class Test1 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

//		FileProcess fileProcess = new FileProcess();
//		fileProcess.divide("D:\\test\\test\\ipg110104.xml");
		
		
		
		IndexCreateProcess indexProcess = new IndexCreateProcess("D:\\wikiTest\\index");
		indexProcess.addToIndex(new File("D:\\test\\test\\ipg110104_1.xml"));
	}

}
