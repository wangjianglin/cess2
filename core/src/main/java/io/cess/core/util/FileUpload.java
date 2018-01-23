package io.cess.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

public class FileUpload {

	private File tmpDir=null;
			
	public FileUpload(String dir){
		this.tmpDir = new File(dir);
	}
	
	public FileUpload(File dir){
		this.tmpDir = dir;
	}
	
	public void add(File file,String md5,long start,long end,long total){
		File dir = this.cacheDir(md5);
		String fileSeparator = System.getProperty("file.separator");
		File partFile = new File(dir.getAbsoluteFile() + fileSeparator + start+"-"+end+"-"+total);
		FileCopy(file,partFile);
	}
	
	/**
	 * 文件复制
	 * 
	 * @param src
	 * @param dest
	 */
	private void FileCopy(File src, File dest) {
		try (InputStream _in = new FileInputStream(src);
				OutputStream _out = new FileOutputStream(dest)) {
			byte[] bs = new byte[4096];
			int count = 0;
			while ((count = _in.read(bs)) != -1) {
				_out.write(bs, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isComplete(String md5){
		
		String exileList = exile(md5);
		if(exileList != null && !"".equals(exileList)){
			return false;
		}
		return true;
	}
	
	private File[] getFiles(String md5){
		File dir = this.cacheDir(md5);
		File[] files = dir.listFiles(new FileFilter() {
			
			private Pattern pattern = Pattern.compile(".*-.*-.*");
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()){
					return false;
				}
				if(pattern.matcher(pathname.getName()).matches()){
					return true;
				}
				return false;
			}
		});
		
		if(files!=null && files.length > 0){
			
			Arrays.sort(files,new Comparator<File>(){
				
				@Override
				public int compare(File o1, File o2) {
					long[] r1 = FileUpload.this.parse(o1.getName());
					long[] r2 = FileUpload.this.parse(o2.getName());
					return (int) Math.signum(r1[0]-r2[0]);
				}
			});
		}
		
		return files;
	}
	
	public String exile(String md5){
		File[] files = getFiles(md5);
		
		if(files!=null && files.length > 0){
			
			long preEnd = -1;
			long[] r = null;
			StringBuffer exileList = new StringBuffer();
			exileList.append("exile list:");
			long total = -1;
			for(File file : files){
				r = parse(file.getName());
				if(preEnd < r[0]-1){
					exileList.append((preEnd + 1) + ":" + (r[0]-1) + ";");
				}
				preEnd = r[1];
				total = r[2];
			}
			if(preEnd < total-1){
				exileList.append((preEnd + 1) + ":" + (total-1) + ";");
			}
			if(total < 0){
				exileList.append("0:-1;");
			}
			return exileList.toString();
		}
		return null;
	}
	
	public File merge(String md5) {
		File dir = this.cacheDir(md5);
		String fileSeparator = System.getProperty("file.separator");
		File mergeFile = new File(dir.getAbsoluteFile() + fileSeparator
				+ "merge.tmp");
		try {
			mergeFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File[] files = getFiles(md5);
		try (OutputStream _out = new FileOutputStream(mergeFile)) {
			byte[] bs = new byte[4096];
			for (File file : files) {
				try (InputStream _in = new FileInputStream(file)) {
					int count = 0;
					while ((count = _in.read(bs)) != -1) {
						_out.write(bs, 0, count);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return mergeFile;
	}
	
	private long[] parse(String name){
		long[] r = new long[3];
		String[] fs = name.split("-");
		
		for(int n=0;n<3;n++){
			try{
				r[n] = Long.parseLong(fs[n]);
			}catch(Exception e){
			
			}
		}
		return r;
	}
	
	private File cacheDir(String md5){
		if(tmpDir == null){
			return null;
		}
		String fileSeparator = System.getProperty("file.separator");
		String tmpDir = this.tmpDir.getAbsolutePath();
		File file = null;
		if(tmpDir.endsWith(fileSeparator)){
			file = new File(tmpDir+md5+fileSeparator);
		}
		file = new File(tmpDir+fileSeparator+md5+fileSeparator);
		if(!file.exists()){
			file.mkdirs();
			//try {
				//file.createNewFile();
			//} catch (IOException e) {
			//	e.printStackTrace();
			//}
		}
		return file;
	}

	public void delete(String md5) {
		File dir = this.cacheDir(md5);
		try{
			deleteDir(dir);
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	private void deleteDir(File dir){
		if(dir == null){
			return;
		}
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			if(files == null || files.length == 0){
				return;
			}
			for(File file : files){
				deleteDir(file);
			}
		}
		dir.delete();
	}
}
