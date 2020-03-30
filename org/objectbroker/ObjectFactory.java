package org.objectbroker;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.objectbroker.value.Mapper;
import org.objectbroker.value.StringMapper;

public class ObjectFactory<T> {

	private final Class<T>					clazz;
	private final Map<String, Mapper<T>>	colMapperMap;

	public ObjectFactory(final Class<T> clazz) {
		this.clazz = clazz;
		colMapperMap = new HashMap<String, Mapper<T>>();
		this.map(clazz);
	}

	public T create(final ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException {
		return (rs.next()) ? this.getObject(rs) : null;
	}

	public List<T> createAll(final ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException {
		if (rs.next()) {
			final List<T> l = new LinkedList<T>();

			do {
				l.add(this.getObject(rs));
			} while (rs.next());

			return l;
		}

		return null;
	}

//	**************************** Private Methods *****************************

	private void map(final Class<?> clazz) {
		Class<?> c = clazz.getSuperclass();
		if (c != null && c != Object.class)
			this.map(c);

		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(Column.class)) {
				final String colName = this.getColName(f.getAnnotation(Column.class), f.getName());
				f.setAccessible(true);
				final String[] altNames = (f.isAnnotationPresent(Alternate.class)) ? f.getAnnotation(Alternate.class).value() : null;
				colMapperMap.put(colName, this.getMapper(f, colName, altNames));
			}
		}
	}

	private String getColName(final Column col, final String fieldName) {
		if (col.value().equals(""))
			return fieldName.toUpperCase();

		return col.value();
	}

	private Mapper<T> getMapper(final Field f, final String colName, final String[] altNames) {
		final Class<?> type = f.getType();

		if (type == String.class)
			return new StringMapper<T>(f, colName, altNames);

		return null;
	}

	private T getObject(final ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException {
		final T obj = clazz.newInstance();
		final ResultSetMetaData meta = rs.getMetaData();

		Mapper<T> m;
		for (int i=1; i <= meta.getColumnCount(); i++) {
			m = colMapperMap.get(meta.getColumnName(i));
			if (m != null)
				m.map(obj, rs);
		}

		return obj;
	}

}	// End ObjectFactory
