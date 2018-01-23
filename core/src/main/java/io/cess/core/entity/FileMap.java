//package lin.core.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Lob;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//
///**
// *
// * @author 王江林
// * @date 2012-7-2 下午4:27:39
// *
// */
//@Entity(name="cloud_file_map")
//@NamedQueries(value={
//@NamedQuery(name="cloud_fileSelectByKey",query="select a from lin.core.entity.FileMap a where a.key = ?1")
////,@NamedQuery(name="cloud_fileCopy", query="update set a.ref = a.ref + 1 from lin.core.entity.FileMap a")
////,@NamedQuery(name="cloud_fileDelete", query="delete from lin.core.entity.FileMap a.key = ?1")
//})
//public class FileMap implements java.io.Serializable {
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	public Long id;
//
//	@Column(name="file_key",unique=true)
//	private String key;
//
//	@Column(name="file_path")
//	private String path;
//
//	@Column(name="file_name",length=1024)
//	private String fileName;
//
//	@Column(name="content_type")
//	private String contentType;
//
//	@Column(name="file_ref")
//	private int ref;
//
//	@Lob
//	@Column(name="file_data")
//	private byte[] data;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
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
//	public String getPath() {
//		return path;
//	}
//
//	public void setPath(String path) {
//		this.path = path;
//	}
//
//	public byte[] getData() {
//		return data;
//	}
//
//	public void setData(byte[] data) {
//		this.data = data;
//	}
//
//	public int getRef() {
//		return ref;
//	}
//
//	public void setRef(int ref) {
//		this.ref = ref;
//	}
//
//	public String getFileName() {
//		return fileName;
//	}
//
//	public void setFileName(String fileName) {
//		this.fileName = fileName;
//	}
//
//	public String getContentType() {
//		return contentType;
//	}
//
//	public void setContentType(String contentType) {
//		this.contentType = contentType;
//	}
//}
