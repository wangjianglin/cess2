package io.cess.util;

/**
 * 
 * @author lin
 * @date 2011-2-27
 *
 */
public class UUID {

	public static String next(){
		return java.util.UUID.randomUUID().toString();
	}
}
