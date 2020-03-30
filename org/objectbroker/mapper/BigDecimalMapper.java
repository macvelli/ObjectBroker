package org.objectbroker.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.sqlbroker.QueryResult.Row;

public class BigDecimalMapper implements Mapper {

	public Object getValue(final Row r, final String colName) {
		final String value = r.getString(colName);

		if (value != null)
			try {
				return new BigDecimal(r.getString(colName)).setScale(2, RoundingMode.HALF_DOWN);			
			} catch (Exception e) {}

		return new BigDecimal("0.0");
	}

}	// End BigDecimalMapper
