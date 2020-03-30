package org.objectbroker.mapper;

import org.sqlbroker.QueryResult.Row;

class IntegerMapper implements Mapper {

	public Object getValue(final Row r, final String colName) {
		return r.getInt(colName);
	}

}	// End IntegerMapper
