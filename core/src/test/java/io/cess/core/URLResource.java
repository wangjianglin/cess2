package io.cess.core;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.regex.Pattern;

import org.junit.Test;

public class URLResource {

	@Test
	public void testURL(){
		Pattern pattern = Pattern.compile("^/client/dotnet.*");
		
		System.out.println("r:"+pattern.matcher("/client/dotnet/HealthDevicesPlatform.zip").matches());
	}
	
	
	@Test
	public void testNumber(){
		Format ndf = new DecimalFormat("#0.0");

		System.out.println("d:"+ndf.format(new BigDecimal(4000000000.0)));
		System.out.println("r:"+new BigDecimal(4344345345000.0));
	}
}
