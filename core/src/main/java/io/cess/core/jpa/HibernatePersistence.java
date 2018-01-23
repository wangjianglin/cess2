//package lin.core.jpa;
//
//
//import java.util.Map;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.spi.LoadState;
//import javax.persistence.spi.PersistenceProvider;
//import javax.persistence.spi.PersistenceUnitInfo;
//import javax.persistence.spi.ProviderUtil;
//
//import org.hibernate.ejb.AvailableSettings;
//import org.hibernate.ejb.util.PersistenceUtilHelper;
//
///**
// * 
// * @author 王江林
// * @date 2012-7-2 下午2:37:57
// *
// *	用于改变JPA的jar-file默认实现，JPA规范是不支持正则表达式的，并且在web容器中运行时，相对目录测试没有通过（不清楚相对的具体情况）
// *
// *  修改之后，如果在web容器中运行，jar-file则是/WEB-INF/lib下的jar文件，且支持正则表达式
// */
//
//public class HibernatePersistence extends AvailableSettings implements PersistenceProvider {
//	private final PersistenceUtilHelper.MetadataCache cache = new PersistenceUtilHelper.MetadataCache();
//
//	/**
//	 * Get an entity manager factory by its entity manager name, using the specified
//	 * properties (they override any found in the peristence.xml file).
//	 * <p/>
//	 * This is the form used in JSE environments.
//	 *
//	 * @param persistenceUnitName entity manager name
//	 * @param properties The explicit property values
//	 *
//	 * @return initialized EntityManagerFactory
//	 */
//	@SuppressWarnings("rawtypes")
//	public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
//		CloudEjb3Configuration cfg = new CloudEjb3Configuration();
//		CloudEjb3Configuration configured = cfg.configure( persistenceUnitName, properties );
//		return configured != null ? configured.buildEntityManagerFactory() : null;
//	}
//
//
//	@SuppressWarnings("rawtypes")
//	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
//		CloudEjb3Configuration cfg = new CloudEjb3Configuration();
//		CloudEjb3Configuration configured = cfg.configure( info, properties );
//		return configured != null ? configured.buildEntityManagerFactory() : null;
//	}
//
//    @SuppressWarnings({ "rawtypes" })
//	public EntityManagerFactory createEntityManagerFactory(Map properties) {
//		// This is used directly by JBoss so don't remove until further notice.  bill@jboss.org
//		CloudEjb3Configuration cfg = new CloudEjb3Configuration();
//		return cfg.createEntityManagerFactory( properties );
//	}
//
//	private final ProviderUtil providerUtil = new ProviderUtil() {
//		public LoadState isLoadedWithoutReference(Object proxy, String property) {
//			return PersistenceUtilHelper.isLoadedWithoutReference( proxy, property, cache );
//		}
//
//		public LoadState isLoadedWithReference(Object proxy, String property) {
//			return PersistenceUtilHelper.isLoadedWithReference( proxy, property, cache );
//		}
//
//		public LoadState isLoaded(Object o) {
//			return PersistenceUtilHelper.isLoaded(o);
//		}
//	};
//
//
//	public ProviderUtil getProviderUtil() {
//		return providerUtil;
//	}
//
//}