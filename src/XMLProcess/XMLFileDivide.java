/**
 * Copyright 1998-2013,2013 南方人才网
 * 文件名： DivideToSmall.java
 * JDK版本： 1.6.0_10
 * 作者： yf
 * 日期： 上午08:41:35
 * 描述：账号Service类
 * 
 */

package XMLProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;



/**
 * 功能：切割大XML文件
 * 
 * @author qiusd
 */
public class XMLFileDivide {

	BufferedReader br;
	BufferedWriter bw;

	/**
	 * 把一个包含很多个xml文件
	 * 完整内容的文件分为单独的xml文件
	 * 保存在文件名同名的文件夹下面
	 * 
	 * @param XMLPath
	 *            目标xml文件的完整路径名
	 * @return 分割完后的各个小的xml文件名
	 * @throws IOException
	 */
	public File[] divide(String XMLPath) throws IOException {
		File file = new File(XMLPath);
		ArrayList<File> fileList = new ArrayList<File>();
		br = new BufferedReader(new FileReader(file));

		File dirOfDate = new File(
				XMLPath.substring(0, XMLPath.lastIndexOf(".")));
		dirOfDate.mkdirs();

		String line;
		int dida = 0;
		String headLine = new String(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		File fOut;
		while ((line = br.readLine()) != null) {
			if (line.contains(headLine)) {
				if (dida > 0) {
					bw.flush();
				}
				++dida;
				fOut = new File(dirOfDate.getAbsolutePath()
						+ "/"
						+ XMLPath.substring(XMLPath.lastIndexOf('\\'),
								XMLPath.lastIndexOf('.')) + "_" + dida + ".xml");
				bw = new BufferedWriter(new FileWriter(fOut));
				fileList.add(fOut);
			}
			bw.write(line + "\n");
		}
		bw.flush();
		return fileList.toArray(new File[0]);
	}


	/**
	 * 返回某个文件夹下面的所有xml文件
	 * 
	 * @param dirPath
	 *            目标文件夹
	 * @return xml文件数组
	 * @throws Exception
	 */
	public File[] getXMLFilesByDir(String dirPath) throws Exception {
		File fatherDir = new File(dirPath);
		if (!fatherDir.exists() || !fatherDir.isDirectory())
			throw new Exception("文件夹路径错误");
		return fatherDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean accept = false;

				if (name.toLowerCase().endsWith("." + "xml")) {
					accept = true;
				}
				return accept;
			}
		});
	}

	/**
	 * 删除文件
	 * 
	 * @param file 文件
	 */
	public void deleteFile(File file) {
		if (file.exists() && file.isFile()) {
			try {
				System.gc();
				file.delete();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 删除某个文件夹及其下面的内容
	 * 其实也具有删除单个文件的功能
	 * TODO：考虑能不能把两个功能合并
	 * 
	 * @param dir
	 *            目标文件夹
	 */
	public void deleteDir(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			File[] subFiles = dir.listFiles();
			if (subFiles != null && subFiles.length > 0) {
				for (File fTemp : subFiles) {
					if (fTemp.isFile())
						deleteFile(fTemp);
					else
						deleteDir(fTemp);
				}
			}
		}
		System.out.println("删除全部文件完成");
		System.gc();
		dir.delete();
		System.out.println("删除文件夹完成");
	}

	
	/**
	 * 删除和某文件同名的文件夹
	 * @param file
	 */
	public void deleteDirOfSameName(File file) {
		File dir = null;
		if (file.getAbsolutePath().lastIndexOf('.') > 0)
			dir = new File(file.getAbsolutePath().substring(0,
					file.getAbsolutePath().lastIndexOf('.')));
		if (dir.exists() && dir.isDirectory())
			this.deleteDir(dir);
	}
	
	/**
	 * 关闭缓存读取和写入资源
	 * 
	 * @throws IOException
	 */
	public void closeAll() throws IOException {
		if (br != null)
			br.close();
		if (bw != null)
			bw.close();
	}
	
}
