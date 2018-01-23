package io.cess.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * 
 * @author 王江林
 * @date 2013-2-3 上午1:49:57
 *
 */
public class FileDownload {

	private File tmpDir=null;
	
	public FileDownload(String dir){
		this.tmpDir = new File(dir);
	}
	
	public FileDownload(File dir){
		this.tmpDir = dir;
	}
	
	public boolean hasFile(String key){
		File dir = cacheDir(key);
		File file = new File(dir.getAbsoluteFile()+System.getProperty("file.separator")+"cache.tmp");
		return file.exists();
	}
	
	
	private File cacheDir(String key){
		if(tmpDir == null){
			return null;
		}
		String fileSeparator = System.getProperty("file.separator");
		String tmpDir = this.tmpDir.getAbsolutePath();
		File file = null;
		if(tmpDir.endsWith(fileSeparator)){
			file = new File(tmpDir+key+fileSeparator);
		}
		file = new File(tmpDir+fileSeparator+key+fileSeparator);
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

	public Object getObj(String key){
		File dir = cacheDir(key);
		File objFile = new File(dir.getAbsoluteFile()+System.getProperty("file.separator")+"cache.obj");
		try {
			ObjectInputStream _in = new ObjectInputStream(new FileInputStream(objFile));
			Object obj = _in.readObject();
			_in.close();
			return obj;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void putFile(String key,InputStream _in, Serializable obj) {
		if(_in==null){
			return ;
		}
		try {
			_in.reset();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		File dir = cacheDir(key);
		dir.deleteOnExit();
		if(obj != null){
			File objFile = new File(dir.getAbsoluteFile()+System.getProperty("file.separator")+"cache.obj");
			objFile.deleteOnExit();
			try {
				ObjectOutputStream _objOut = new ObjectOutputStream(new FileOutputStream(objFile));
				_objOut.writeObject(obj);
				_objOut.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File file = new File(dir.getAbsoluteFile()+System.getProperty("file.separator")+"cache.tmp");
		file.deleteOnExit();
		try(OutputStream _out = new FileOutputStream(file)){
			byte[] bs = new byte[4096];
			int readCount = 0;
			while((readCount = _in.read(bs)) != -1){
				_out.write(bs,0,readCount);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public long size(String key) {
		File dir = cacheDir(key);
		File file = new File(dir.getAbsoluteFile()+System.getProperty("file.separator")+"cache.tmp");
		if(file.exists()){
			return file.length();
		}
		return 0;
	}

	public static final int ONCE_DOWNLOAD_SIZE = 4096 * 100;
	public ByteArrayInputStream download(String key, long p) {
		File dir = cacheDir(key);
		File file = new File(dir.getAbsoluteFile()+System.getProperty("file.separator")+"cache.tmp");
		if(file.exists()){
			byte[] bs = new byte[ONCE_DOWNLOAD_SIZE];
			FileInputStream _in;
			try {
				_in = new FileInputStream(file);
				_in.skip(p);
				int count = _in.read(bs);
				ByteArrayInputStream r = new ByteArrayInputStream(bs,0,count);
				
				_in.close();
				
				if(p + count == file.length()){
					try{
						file.delete();
						new File(dir.getAbsoluteFile()+System.getProperty("file.separator")+"cache.obj").delete();
						dir.delete();
					}catch(Throwable e){
						e.printStackTrace();
					}
				}
				
				return r;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
