package org.objectbroker.mapper;

import org.sqlbroker.QueryResult.Row;

class CharMapper implements Mapper {

	public Object getValue(final Row r, final String colName) {
		return r.getChar(colName);
	}

}	// End CharMapper
