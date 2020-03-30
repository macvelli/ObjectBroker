package org.objectbroker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import org.sqlbroker.Execution;
import org.sqlbroker.jdbc.DataAccessException;
import org.sqlbroker.param.FixedParams;
import org.sqlbroker.param.Parameters;

import root.log.Log;

public class ObjectBroker<T> {

	private static final Log log = new Log(ObjectBroker.class);

//	private final ClassInfo			info;
	private final DataSource		dataSource;
	private final ObjectFactory<T>	objFactory;

	public ObjectBroker(final DataSource dataSource, final Class<T> clazz) {
		this.dataSource = dataSource;
		objFactory = new ObjectFactory<T>(clazz);
//		info = new ClassInfo(clazz, null);
	}

//	public Configuration getConfig(final Class... hierarchy) {
//		ClassInfo ci = null;
//
//		for (Class c : hierarchy) {
//			if (ci == null) {
//				if (info.type != c)
//					throw new RuntimeException("Cannot find configuration for " + c);
//				ci = info;
//			} else {
//				for (RelationInfo r : ci.relations)
//					if (r.info.type == c)
//						ci = r.info;
//			}
//		}
//
//		return ci.config;
//	}

	public T load(final String sql, final Object... values) {
		return this.load(sql, new FixedParams(values), 0, 0);
	}

	public T load(final String sql, final Parameters params, final int maxRows, final int seconds) {
		log.debug("Executing SQL query [{P}] with parameters {P}", sql, params);

		final Execution exec = new Execution();
		ResultSet rs = null;

		try {
			final PreparedStatement stmt = exec.getStatement(dataSource, sql, params);
			if (maxRows > 0) stmt.setMaxRows(maxRows);
			if (seconds > 0) stmt.setQueryTimeout(seconds);
			rs = stmt.executeQuery();
			return objFactory.create(rs);
		} catch (Exception e) {
			throw new DataAccessException(sql, params, e);
		} finally {
			exec.complete(rs);
		}
	}

	public List<T> loadAll(final String sql, final Object... values) {
		return this.loadAll(sql, new FixedParams(values), 0, 0);
	}

	public List<T> loadAll(final String sql, final Parameters params, final int maxRows, final int seconds) {
		log.debug("Executing SQL query [{P}] with parameters {P}", sql, params);

		final Execution exec = new Execution();
		ResultSet rs = null;

		try {
			final PreparedStatement stmt = exec.getStatement(dataSource, sql, params);
			if (maxRows > 0) stmt.setMaxRows(maxRows);
			if (seconds > 0) stmt.setQueryTimeout(seconds);
			rs = stmt.executeQuery();
			return objFactory.createAll(rs);
		} catch (Exception e) {
			throw new DataAccessException(sql, params, e);
		} finally {
			exec.complete(rs);
		}
	}

//	@SuppressWarnings("unchecked")
//	public E load(final Parameters params) {
//		return (E) info.load(info.config.defaultLoadSql, params);
//	}

//	@SuppressWarnings("unchecked")
//	public E load(final String sql, final Parameters params) {
//		return (E) info.load(sql, params);
//		E obj = null;
//		final Row r = broker.query(sql, params).first();
//
//		if (r != null) {
//			obj = (E) info.getObject(r);
//			if (load != null)
//				load.elaborate(obj);
//		}
//
//		return obj;
//	}

//	public List<E> loadAll(final Parameters params) {
//		return null;
//		if (defaultSqlMap.get(Op.LoadAll) == null)
//			throw new IllegalStateException("Uninitialized Default SQL");
//
//		return this.loadAll(defaultSqlMap.get(Op.LoadAll), params, 0, 0);
//	}

//	@SuppressWarnings("unchecked")
//	public List<E> loadAll(final String sql, final Parameters params) {
//		return info.loadAll(sql, params, 0, 0);
//	}

//	@SuppressWarnings("unchecked")
//	public List<E> loadAll(final String sql, final Parameters params, final int maxRows, final int queryTimeout) {
//		return info.loadAll(sql, params, maxRows, queryTimeout);
//		final QueryResults res = broker.query(sql, params, maxRows, queryTimeout);
//		final List<E> results = new ArrayList<E>(res.size());
//
//		for (Row r : res) {
//			final E obj = (E) info.getObject(r);
//			if (forEach != null)
//				forEach.elaborate(obj);
//			results.add(obj);
//		}
//
//		return results;
//	}

//	@Override public String toString() {
//		return info.toString();
//	}

}	// End NewObjectBroker
