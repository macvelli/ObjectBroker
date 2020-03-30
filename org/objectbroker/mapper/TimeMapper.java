package org.objectbroker.mapper;

import java.sql.Time;
import java.util.Date;

import org.sqlbroker.QueryResult.Row;

import root.util.text.DateFormat;

public class TimeMapper implements Mapper {

	private static final DateFormat HMS = new DateFormat("HHmmss");

	public Object getValue(final Row r, final String colName) {
		final Date d = HMS.parse(r.getString(colName));
		return (d != null) ? new Time(d.getTime()) : null;
	}

}	// End TimeMapper
