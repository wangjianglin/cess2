import java.util.regex.Pattern;

import org.junit.Test;



public class TestJars {

	@Test
	public void testJars(){
		
		System.out.println("result:"+Pattern.matches(".*comm.jar.*", "jar:file:/D:/work/cloud/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/web/WEB-INF/lib/comm.jar!/"));
	}
	
	public static void main(String[] args){
		
	}
}
