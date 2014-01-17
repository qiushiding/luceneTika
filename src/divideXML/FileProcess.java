/**
 * Copyright 1998-2013,2013 南方人才网
 * 文件名： DivideToSmall.java
 * JDK版本： 1.6.0_10
 * 作者： yf
 * 日期： 上午08:41:35
 * 描述：账号Service类
 * 
 */

package divideXML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import lucene.IndexCreateProcess;


public class FileProcess {

	BufferedReader br ;
	BufferedWriter bw ;
	
	/**
	 * 把一个包含很多个xml文件 完整内容的大的xml文件分单独的xml文件，保存在文件名同名的文件夹下面
	 * @param XMLPath 目标xml文件的完整路径名
	 * @return 分割完后的各个小的xml文件名
	 * @throws IOException
	 */
	public File [] divide(String XMLPath) throws IOException{
		File f = new File(XMLPath);
		ArrayList<File> fileList = new ArrayList<File>();
		br = new BufferedReader(new FileReader(f));
		
		File dirOfDate = new File(XMLPath.substring(0,XMLPath.lastIndexOf(".")));
		dirOfDate.mkdirs();
		
		String line ;
		int dida = 0;
		String headLine = new String ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		File fOut ;
//		bw = null;//= new BufferedWriter ( new FileWriter(fOut ) );
		while ( ( line = br.readLine() ) != null){
			if ( line.contains(headLine)){
				if( dida >0){
					bw.flush();
				}
				++dida;
				fOut = new File(dirOfDate.getAbsolutePath()+"/"+XMLPath.substring(XMLPath.lastIndexOf('\\'), XMLPath.lastIndexOf('.')) + "_"+dida+".xml");
				bw = new BufferedWriter ( new FileWriter(fOut ) );
				fileList.add( fOut );
			}
			bw.write(line+"\n"); 
		}
		bw.flush();
		return fileList.toArray(new File[0]);
	}
	
	/**
	 * 关闭缓存读取和写入资源
	 * @throws IOException
	 */
	public void closeAll () throws IOException{
		if( br != null)
			br.close();
		if (bw != null)
			bw.close();
	}
	
	/**
	 * 返回某个文件夹下面的所有xml文件
	 * @param dirPath 目标文件夹
	 * @return xml文件数组
	 * @throws Exception
	 */
	public File [] getXMLFiles (String dirPath) throws Exception{
		File fatherDir = new File( dirPath);
		if ( ! fatherDir.exists() || !fatherDir.isDirectory())
			throw new Exception("输入文件夹路径错误");
		//筛选xml文件
//		File [] xmlFiles = fatherDir.listFiles(
		return fatherDir.listFiles(
				new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
				            File file = new File(dir, name);
				            boolean accept = false;
				              
				            if (name.toLowerCase().endsWith("." + "xml")) {
			                    accept = true;
			                }
						return accept;
					}
				});//xml筛选完毕
	}
	
	/**
	 * 删除文件
	 * @param file 文件
	 */
	public void deleteFile (File file ){
		if (file.exists() && file.isFile()){
			try{
				System.gc();
				boolean ab = file.delete();
				//System.out.println(ab);
			}catch (Exception e1){
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 删除某个文件夹及其下面的内容
	 * @param dir 目标文件夹
	 */
	public void deleteDir (File dir){
		//先清空文件夹
		if (dir.exists() && dir.isDirectory()){
			File [] subFiles = dir.listFiles();
			if ( subFiles != null && subFiles.length>0 ){
				for ( File fTemp : subFiles){
					if (fTemp.isFile())
						deleteFile(fTemp);
					else 
						deleteDir(fTemp);
				}
			}
		}
		System.out.println("删除全部文件完成");
		//再删除自己
		System.gc();
		dir.delete();
		System.out.println("删除文件夹完成");
	}
	
	public void deleteDirOfSameName(File file){
		File dir = null;
		if ( file.getAbsolutePath().lastIndexOf('.') >0 )
			dir = new File ( file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf('.')));
		if ( dir.exists() && dir.isDirectory())
			this.deleteDir(dir);
	}
	
	/**
	 * 这个函数似乎没有用
	 * 无法起到分割xml的作用
	 * @param XMLPath
	 * @throws IOException
	 */
	public void  divide2(String XMLPath) throws IOException{
		File f = new File(XMLPath);
		br = new BufferedReader(new FileReader(f));
		//创建输出的文件夹
		File dirOfDate = new File(XMLPath.substring(0,XMLPath.lastIndexOf(".")));
		dirOfDate.mkdirs();
		String line ;
		int dida = 0;
//		line = br.readLine();
		File fOut ;// = new File(dirOfDate.getAbsolutePath()+"/_1.xml");;
		int i = 0;
		fOut = new File(dirOfDate.getAbsolutePath()+"/"+XMLPath.substring(XMLPath.lastIndexOf('\\'), XMLPath.lastIndexOf('.')) + "_"+1+".csv");
		bw = new BufferedWriter ( new FileWriter(fOut ) );
		while ( ( line = br.readLine() ) != null){
			//如果包含头文件，结束上一个文件的写入，开始下一个文件
			if ( i++ == 900000 ){
				bw.flush();
				fOut = new File(dirOfDate.getAbsolutePath()+"/"+XMLPath.substring(XMLPath.lastIndexOf('\\'), XMLPath.lastIndexOf('.')) + "_"+2+".csv");
				bw = new BufferedWriter ( new FileWriter(fOut ) );
			}
			bw.write(line+"\n"); 
		}
		bw.flush();
	}
}
