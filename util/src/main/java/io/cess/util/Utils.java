package io.cess.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 
 * @author lin
 *
 */
public class Utils {
	public static String printStackTrace(Throwable ex){
		Writer writer =new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null ) {
			printWriter.println();
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		return writer.toString();
	}
}
