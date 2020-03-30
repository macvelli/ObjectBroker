package org.objectbroker.value;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {

	public void map(T obj, ResultSet r) throws SQLException, IllegalArgumentException, IllegalAccessException;

}	// End Mapper
