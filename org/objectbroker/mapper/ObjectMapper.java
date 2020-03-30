package org.objectbroker.mapper;

import org.sqlbroker.QueryResult.Row;

class ObjectMapper implements Mapper {

	public Object getValue(final Row r, final String colName) {
		return r.getObject(colName);
	}

}	// End ObjectMapper
