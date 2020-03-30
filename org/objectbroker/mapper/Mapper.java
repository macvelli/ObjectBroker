package org.objectbroker.mapper;

import org.sqlbroker.QueryResult.Row;

public interface Mapper {

	public Object getValue(Row r, String colName);

}	// End Mapper
