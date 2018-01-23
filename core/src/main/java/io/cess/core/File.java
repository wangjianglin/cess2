//package lin.core;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//
//import lin.core.entity.FileMap;
//import lin.core.services.FileService;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
//public class File {
//	private FileService service;
//
//	private static File file;
//
//	static {
//		file = new File();
////		AutoWiring.autoWiring(file);
//	}
//
//	private File() {
//
//	}
//
//	public static InputStream getFile(String key) {
//		FileMap fileMap = file.service.download(key);
//		InputStream sbs = new ByteArrayInputStream(fileMap.getData());
//		return sbs;
//	}
//
//	public FileService getService() {
//		return service;
//	}
//
//	public void setService(FileService service) {
//		this.service = service;
//	}
//
//	public static File getFile() {
//		return file;
//	}
//
//	public static void setFile(File file) {
//		File.file = file;
//	}
//
//    /**
//     * Created by lin on 27/10/2016.
//     */
//    @ConfigurationProperties(prefix="jpa")
//    public static class JpaConfigProperties {
//
//        private String unitName;
//
//        private String url;
//
//        public String getUnitName() {
//            return unitName;
//        }
//
//        public void setUnitName(String unitName) {
//            this.unitName = unitName;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//    }
//}
