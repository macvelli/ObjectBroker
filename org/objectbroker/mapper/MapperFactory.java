package org.objectbroker.mapper;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapperFactory {

	private static final Mapper OBJECT = new ObjectMapper();
	private static final Map<Class, Mapper>	classMap = new HashMap<Class, Mapper>();

	static {
		Mapper temp;
		classMap.put(BigDecimal.class, new BigDecimalMapper());
		temp = new CharMapper();
		classMap.put(char.class, temp);
		classMap.put(Character.class, temp);
		classMap.put(Date.class, new DateMapper());
		temp = new IntegerMapper();
		classMap.put(int.class, temp);
		classMap.put(Integer.class, temp);
		classMap.put(String.class, new StringMapper());
		classMap.put(Time.class, new TimeMapper());
	}

	public static boolean isDefined(final Class clazz) {
		return classMap.get(clazz) != null;
	}

	public static Mapper getInstance(final Class clazz) {
		Mapper mapper = classMap.get(clazz);

		return (mapper != null) ? mapper : OBJECT;
	}

}	// End MapperFactory
