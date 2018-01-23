package io.cess.core.spring;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * 
 * @author lin
 * @date Jul 5, 2015 7:25:14 PM
 *
 */
public class DateConverter implements Converter<String,Date> {

	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	@Override
	public Date convert(String source) {
		try {
			return format.parse(source);
		} catch (ParseException e) {
//			e.printStackTrace();
		}
		return null;
	}

}
