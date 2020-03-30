package org.objectbroker.mapper;

import org.sqlbroker.QueryResult.Row;

class StringMapper implements Mapper {

	public Object getValue(final Row r, final String colName) {
		return r.getString(colName);
	}

}	// End StringMapper
