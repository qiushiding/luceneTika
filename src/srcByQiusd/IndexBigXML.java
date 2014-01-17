package srcByQiusd;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import lucene.IndexCreateProcess;
import divideXML.FileProcess;

public class IndexBigXML {
	/**
	 *把一个大的xml文件分割成小的xml，然后做索引
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) {
		Date d1 = new Date();
		long l1 = System.currentTimeMillis();
		FileProcess fp = new FileProcess();
		IndexCreateProcess indexProcess = 
				new IndexCreateProcess("F:\\百度云\\研究方向\\关键词抽取\\索引测试\\indexTest");
		File [] fs ;
		try{
			//获取某个文件夹下面的xml文件，然后分割成小的xml，然后 再索引
			fs = fp.getXMLFiles("F:\\百度云\\研究方向\\关键词抽取\\索引测试\\xmlFile");
			if( fs != null && fs.length>0)
				for (File fTemp : fs){
					//把一个大的xml文件分成小的
					File [] smallXMLs = fp.divide(fTemp.getAbsolutePath());
					System.out.println("divide finished");
					//把生成的小文件加入到索引中，然后删除
					for (int i = 0; i<smallXMLs.length ; i++){
						indexProcess.addToIndex(smallXMLs[i] );
//						fp.deleteFile(smallXMLs[i]);
						System.out.println("add " + smallXMLs[i].toString());
					}
					//删除分割大文件时生成的文件夹
					try {
						fp.closeAll();
					} catch (IOException e) {
						e.printStackTrace();
					}
					indexProcess.closeWriter();
					//删除文件夹
					for (int j = 0;j<3;j++){
						fp.deleteDir(smallXMLs[0].getParentFile());
						fp.deleteDir(smallXMLs[0].getParentFile());
					}
				}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try {
				fp.closeAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
			indexProcess.closeWriter();
		}
//		fp.deleteDirOfSameName(new File("D:\\test\\new  2.xml"));
//		//测试删除文件夹
//		dts.deleteDir(new File ("c:/aaa"));
		Date d2 = new Date();
		long l2 = System.currentTimeMillis();
		System.out.println("cost time:");
		System.out.println(l2-l1 );
		System.out.println( d2.getTime() - d1.getTime() );
	}

}
