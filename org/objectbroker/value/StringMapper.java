package org.objectbroker.value;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import root.util.lang.StringUtil;

public class StringMapper<T> implements Mapper<T> {

	private final Field		f;
	private final String	colName;
	private final String[]	altNames;

	public StringMapper(final Field f, final String colName, final String[] altNames) {
		this.f = f;
		this.colName = colName;
		this.altNames = altNames;
	}

	public void map(final T obj, final ResultSet r) throws SQLException, IllegalArgumentException, IllegalAccessException {
		String value = r.getString(colName);
		if (altNames != null) {
			int i = 0;
			while (StringUtil.empty(value) && i < altNames.length)
				value = r.getString(altNames[i++]);
		}

		f.set(obj, value);
	}

}	// End StringMapper
