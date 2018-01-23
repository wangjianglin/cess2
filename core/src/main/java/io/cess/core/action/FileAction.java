//package lin.core.action;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.InputStream;
//
//import javax.servlet.http1.HttpServletRequest;
//import javax.servlet.http1.HttpServletResponse;
//
//import lin.core.entity.FileMap;
//
//import org.apache.struts2.StrutsStatics;
//import org.apache.struts2.convention.annotation.Action;
//import org.apache.struts2.convention.annotation.InterceptorRef;
//import org.apache.struts2.convention.annotation.Result;
//import org.apache.struts2.convention.annotation.Results;
//
//import lin.core.services.FileService;
//import lin.core.util.FileDownload;
//import lin.core.util.FileUpload;
//
//import com.opensymphony.xwork2.ActionContext;
//
//
///**
// * 
// * @author 王江林
// * @date 2012-7-2 下午4:23:13
// *
// */
//@Action(interceptorRefs={
//		@InterceptorRef(value="fileUpload"),
//		@InterceptorRef(value="defaultStack")
//		})
//@Results(value={
//		@Result(type="cloud",name="upload",params={"root","key"})
//		,@Result(type="cloud",name="copy",params={"root","key"})
//		,@Result(type="cloud",name="delete",params={"root","null"})
//		,@Result(type="cloud",name="download",params={"__result__","stream","inputName",
//				"inputStream","contentType","${file.contentType}",
//				"contentDisposition","attachment;filename=\"${file.fileName}\"",
//				"status","${status}"
////				,
////				"Status","",
////				"Content-Range"
//		})
//})
//
////contentType">text/plain</param>
////<!-- 下载文件处理方法 -->
////<paramname="contentDisposition">
////          attachment;filename="${downloadChineseFileName}"
////</param>
////<!-- 下载文件输出流定义 -->
////<paramname="inputName">downloadFile</param>
//public class FileAction {
//
////	private List<File> uploads;
////	private List<String> fileNames;
////	private List<String> uploadContentTypes;
//	
//	private File upload;
//	private String uploadFileName;
//	private String uploadContentType;
//	private String key;
//	
////	private File[] upload;
////	private String[] uploadFileName;
////	private String[] uploadContentType;
//	
////	private String[] keys;
//	private FileMap file;
//	private InputStream inputStream;
//	
//	private FileService fileService;
//	
//	private String md5 = null;
//	private long start = 0;
//	private long end = 0;
//	private long total = 0;
//	
//	private static FileUpload staticFileUpload;// = new FileUpload();
//	private static FileDownload fileDownload;
//
//	private int status = -1;
//	
//	public String exileList(){
//		key = staticFileUpload.exile(md5);
//		
//		return "upload";
//	}
//	
//	private FileUpload getFileUpload(){
//		if(staticFileUpload == null){
//			File tmpPath = (File) ((HttpServletRequest)ActionContext.getContext().getActionInvocation().getInvocationContext().get(StrutsStatics.HTTP_REQUEST)).getServletContext().getAttribute("javax.servlet.context.tempdir");
//			//System.out.println("path:"+ new File(".").getAbsolutePath());
//			String tmpPathS = tmpPath.getAbsoluteFile() + System.getProperty("file.separator") + "fileupload"+ System.getProperty("file.separator");
//			staticFileUpload = new FileUpload(new File(tmpPathS));
//		}
//		return staticFileUpload;
//	}
//	
//	public String upload(){
//		if(end == total && total == 0){
//			key = fileService.upload(upload, uploadFileName, uploadContentType).getKey();
//			return "upload";
//		}
//		
//		FileUpload fileUpload = getFileUpload();
//		fileUpload.add(upload, md5, start, end, total);
//		
//		//if(fileUpload.isComplete(md5)){
//		String exileList = fileUpload.exile(md5);
//		if(exileList != null && exileList.length() == 11){
//			key = fileService.upload(fileUpload.merge(md5), uploadFileName, uploadContentType).getKey();
//			fileUpload.delete(md5);
//		}else{
//			key = exileList;
//		}
//		
//		
////		if(upload != null && upload.length > 0){
////			ArrayList<String> list = new ArrayList<String>();
////			for(int n=0;n<upload.length;n++){
////				list.add(fileService.upload(upload[n], uploadFileName[0], uploadContentType[0]).getKey());
////			}
////			keys = list.toArray(new String[]{});
////		}
//		return "upload";
//	}
//	
//	public String download(){
//		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().getActionInvocation().getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
//		if (request.getHeader("Range")!= null){// 客户端请求的下载的文件块的开始字节
//			downloadPart(request);
//		}else{
//			downloadImpl(request);
//		}
//
//		return "download";
//	}
//	
//	private void downloadPart(HttpServletRequest request){
//		if(fileDownload == null){
//			File tmpPath = (File) (request).getServletContext().getAttribute("javax.servlet.context.tempdir");
//			//System.out.println("path:"+ new File(".").getAbsolutePath());
//			String tmpPathS = tmpPath.getAbsoluteFile() + System.getProperty("file.separator") + "filedownload"+ System.getProperty("file.separator");
//			fileDownload = new FileDownload(new File(tmpPathS));
//		}
//		HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().getActionInvocation().getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
//		
//		//response.setStatus(javax.servlet.http1.HttpServletResponse.SC_PARTIAL_CONTENT);// 206
//		status = javax.servlet.http1.HttpServletResponse.SC_PARTIAL_CONTENT;
//		
//		if(!fileDownload.hasFile(key)){
//			file = fileService.download(key);
//			InputStream _in = new ByteArrayInputStream(file.getData());
//			FileMap obj = new FileMap();
//			obj.setContentType(file.getContentType());
//			obj.setFileName(file.getFileName());
//			obj.setKey(file.getKey());
//			obj.setId(file.getId());
//			obj.setPath(file.getPath());
//			obj.setRef(file.getRef());
//			fileDownload.putFile(key,_in,obj);
//		}else{
//			file = (FileMap)fileDownload.getObj(key);
//		}
//		
//// 从请求中得到开始的字节
//// 请求的格式是:
//// Range: bytes=[文件块的开始字节]-
//		long p = 0;
//        long l = 0;
//        // l = raf.length();
//        //l = file.length();
//        p = Long.parseLong(request.getHeader("Range")
//        .replaceAll("bytes=", "").replaceAll("-", ""));
//		l = fileDownload.size(key);
//		inputStream = fileDownload.download(key,p);
//		
//		long onceDownloadSize = FileDownload.ONCE_DOWNLOAD_SIZE;
//		if(l - p < FileDownload.ONCE_DOWNLOAD_SIZE){
//			onceDownloadSize = l - p;
//		}
//		//response.setContentLength(88);
//		response.addHeader("Content-Length", onceDownloadSize+"");
//		//if (p != 0) {
//// 不是从最开始下载,
//// 响应的格式是:
//// Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
//		response.addHeader("Content-Range", "bytes "
//		+ new Long(p).toString() + "-"
//		+ new Long(p + onceDownloadSize - 1).toString() + "/"
//		+ new Long(l).toString());
//		    //}
//		    // response.setHeader("Connection", "Close"); //如果有此句话不能用 IE
//		    // 直接下载
//		    // 使客户端直接下载
//		    // 响应的格式是:
//		    // Content-Type: application/octet-stream
//		  //response.setContentType("application/octet-stream");
//// 为客户端下载指定默认的下载文件名称
//		    // 响应的格式是:
//		    // Content-Disposition: attachment;filename="[文件名]"
//		    // response.setHeader("Content-Disposition",
//		    // "attachment;filename=\"" + s.substring(s.lastIndexOf("\\") +
//		    // 1) +
//		    // "\""); //经测试 RandomAccessFile 也可以实现,有兴趣可将注释去掉,并注释掉
//		    // FileInputStream 版本的语句
//		if(this.file!=null){
//		  //response.setHeader("Content-Disposition",
//		//"attachment;filename=" + file.getFileName() + "");
//		}
//
//		//file = fileService.download(key);
//		//inputStream = new ByteArrayInputStream(file.getData());
////		try {
////			//inputName = new FileInputStream("c:\\tmpcal2.txt");
////			inputStream =  ServletActionContext.getServletContext().getResourceAsStream("/images/bg1.gif");
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//	}
//	
//	private void downloadImpl(HttpServletRequest request){
//		file = fileService.download(key);
//		inputStream = new ByteArrayInputStream(file.getData());
//	}
//
//	public String copy(){
//		key = fileService.copy(key);
//		return "copy";
//	}
//	
//	public String delete(){
//		fileService.delete(key);
//		return "delete";
//	}
//
//	public File getUpload() {
//		return upload;
//	}
//
//	public void setUpload(File upload) {
//		this.upload = upload;
//	}
//
//	public String getUploadFileName() {
//		return uploadFileName;
//	}
//
//	public void setUploadFileName(String uploadFileName) {
//		this.uploadFileName = uploadFileName;
//	}
//
//	public String getUploadContentType() {
//		return uploadContentType;
//	}
//
//	public void setUploadContentType(String uploadContentType) {
//		this.uploadContentType = uploadContentType;
//	}
//
//	public String getKey() {
//		return key;
//	}
//
//	public void setKey(String key) {
//		this.key = key;
//	}
//
//	public void setFileService(FileService fileService) {
//		this.fileService = fileService;
//	}
//
//	public FileMap getFile() {
//		return file;
//	}
//
//	public void setFile(FileMap file) {
//		this.file = file;
//	}
//
//	public InputStream getInputStream() {
//		return inputStream;
//	}
//
//	public void setInputStream(InputStream inputStream) {
//		this.inputStream = inputStream;
//	}
//
//	public String getMd5() {
//		return md5;
//	}
//
//	public void setMd5(String md5) {
//		this.md5 = md5;
//	}
//
//	public long getStart() {
//		return start;
//	}
//
//	public void setStart(long start) {
//		this.start = start;
//	}
//
//	public long getEnd() {
//		return end;
//	}
//
//	public void setEnd(long end) {
//		this.end = end;
//	}
//
//	public long getTotal() {
//		return total;
//	}
//
//	public void setTotal(long total) {
//		this.total = total;
//	}
//
//	public int getStatus() {
//		return status;
//	}
//
//	public void setStatus(int status) {
//		this.status = status;
//	}
//}
