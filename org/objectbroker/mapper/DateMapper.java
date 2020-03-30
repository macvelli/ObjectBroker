package org.objectbroker.mapper;

import org.sqlbroker.QueryResult.Row;

import root.util.text.DateFormat;

public class DateMapper implements Mapper {

	private static final DateFormat YMD = new DateFormat("yyyyMMdd");

	public Object getValue(final Row r, final String colName) {
		return YMD.parse(r.getString(colName));
	}

}	// End DateMapper
