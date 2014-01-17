package srcByQiusd;

import java.io.File;
import java.io.IOException;

import lucene.IndexCreateProcess;
import divideXML.FileProcess;

public class IndexSmallXMLs {
	/**
	 * 测试单单从test\22 文件夹获取几个小的xml文件来建索引
   * 直接对已经分割好的小的xml做索引
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Date d1 = new Date();
		long l1 = System.currentTimeMillis();
		FileProcess fp = new FileProcess();
		IndexCreateProcess indexProcess = new IndexCreateProcess("d:/wikiTest/index");
		try{
			//把一个大的xml文件分成小的
			File [] smallXMLs = fp.getXMLFiles("D:\\test\\22");
			//把生成的小文件加入到索引中，然后删除
			for (int i = 0; i<smallXMLs.length ; i++){
				indexProcess.addToIndex(smallXMLs[i] );
//						fp.deleteFile(smallXMLs[i]);
			}
			//删除分割大文件时生成的文件夹
			try {
				fp.closeAll();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			indexProcess.closeWriter();
			//删除文件夹
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try {
				fp.closeAll();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			indexProcess.closeWriter();
		}
//		fp.deleteDirOfSameName(new File("D:\\test\\new  2.xml"));
//		//测试删除文件夹
//		dts.deleteDir(new File ("c:/aaa"));
//		Date d2 = new Date();
		long l2 = System.currentTimeMillis();
		System.out.println(l2-l1 );
//		System.out.println( d2.getTime() - d1.getTime() );
	}
}
