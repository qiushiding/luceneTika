package srcByQiusd;

import java.io.File;
import divideXML.FileProcess;

public class DivideXML {

	/**
	 * 把一个大的xml分割成小的xml
	 * @param args
	 * @throws Exception
	 */
	public static void main (String [] args ) throws Exception{
		System.out.println("Divide Begin.");
		FileProcess fp = new FileProcess();
		File [] fs ;
		fs = fp.getXMLFiles("F:\\百度云\\研究方向\\关键词抽取\\patent\\ipg130115");
		if( fs != null && fs.length>0) {
			for (File fTemp : fs){
				File [] smallXMLs = fp.divide(fTemp.getAbsolutePath());
			}
		}
		System.out.println("Divide Finished.");
	}

}
