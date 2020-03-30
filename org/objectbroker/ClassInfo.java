package org.objectbroker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectbroker.mapper.Mapper;
import org.objectbroker.mapper.MapperFactory;
import org.sqlbroker.QueryResult;
import org.sqlbroker.SQLBroker;
import org.sqlbroker.QueryResult.Row;
import org.sqlbroker.param.Parameters;

import root.adt.SList;
import root.lang.Array;

class ClassInfo {

	// Ids, Columns/Fields, Configuration, Mapper, Type, Classes/RelationInfo->parent/child ClassInfo

	Map<String, Field>	colFieldMap;
	SList<RelationInfo>	relations;

	final Class<?>		type;
	final SQLBroker		broker;
	final Configuration	config;

	private Mapper				mapper;
	private String[]			ids;

	ClassInfo(final Class<?> type, final SQLBroker broker) {
		this.type = type;
		this.broker = broker;
		config = new Configuration(type);

		if (MapperFactory.isDefined(type))
			mapper = MapperFactory.getInstance(type);
		else {
			colFieldMap = new HashMap<String, Field>();
			relations = new SList<RelationInfo>();
			this.mapFields(type);
		}
	}

	@SuppressWarnings("unchecked")
	Object load(final String sql, final Parameters params) {
		Object obj = null;
		final Row r = broker.query(sql, params).first();

		if (r != null) {
			obj = this.getObject(r);
			if (config.loadListener != null)
				config.loadListener.elaborate(obj);
		}

		return obj;
	}

	@SuppressWarnings("unchecked")
	List loadAll(final String sql, final Parameters params, final int maxRows, final int queryTimeout) {
		final QueryResult res = broker.query(sql, params, maxRows, queryTimeout);
		final List results = new ArrayList(res.size());

		for (Row r : res) {
			final Object obj = this.getObject(r);
			if (config.loadAllListener != null)
				config.loadAllListener.elaborate(obj);
			results.add(obj);
		}

		return results;
	}

	Object getObject(final Row r) {
		if (mapper != null)
			return null;
//			return mapper.getValue(r, r.getColumns()[0]);
		else {
			String col = null;
			Object obj = null;

			try {
				obj = type.newInstance();
				System.out.println("New object created of type " + type);
//				for (String s : r.getColumns()) {
//					col = s;
//					Field f = colFieldMap.get(s);
//					f.set(obj, MapperFactory.getInstance(f.getType()).getValue(r, s));
//				}
			} catch (Exception e) {
				throw new RuntimeException("Failed to load " + type.getName() + ((col != null) ? '.' + col : ':'), e);
			}

			try {
				for (RelationInfo rm : relations)
					rm.populate(obj);
			} catch (Exception e) {
				throw new RuntimeException("Failed to load " + type.getName(), e);
			}

			return obj;
		}
	}

	@Override public String toString() {
		final StringBuilder builder = new StringBuilder("ClassInfo: ").append(type);

		builder.append("\n\tIds: ").append(Array.join(ids));
		builder.append("\n\tColumn/Fields: ").append(colFieldMap);
		builder.append("\n\tMapper: ").append(mapper);
		builder.append(config);

		if (relations != null)
			for (RelationInfo r : relations)
				builder.append(r);

		return builder.toString();
	}

//	**************************** Private Methods *****************************

	private void mapFields(final Class<?> clazz) {
		Class<?> c = clazz.getSuperclass();
		if (c != null && c != Object.class)
			mapFields(c);

		boolean hasRelations = !relations.isEmpty();
		final List<String> ids = new ArrayList<String>();

		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(Column.class)) {
				Column col = f.getAnnotation(Column.class);
				f.setAccessible(true);
				colFieldMap.put(col.value(), f);
			} else if (f.isAnnotationPresent(Id.class)) {
				if (hasRelations)
					throw new RuntimeException("All Ids must be declared before Relations " + clazz.getName());

				Id id = f.getAnnotation(Id.class);
				f.setAccessible(true);
				colFieldMap.put(id.value(), f);
				ids.add(id.value());
			} else if (f.isAnnotationPresent(Relation.class)) {
				if (!hasRelations) {
					hasRelations = true;
					this.ids = ids.toArray(new String[ids.size()]);
				}

				Relation r = f.getAnnotation(Relation.class);
				f.setAccessible(true);
				RelationInfo rm = new RelationInfo(this, f, r.sql(), (r.keys().length > 0) ? r.keys() : this.ids);
				relations.add(rm);
			}
		}

		if (this.ids == null && !ids.isEmpty())
			this.ids = ids.toArray(new String[ids.size()]);
	}

}	// End ClassInfo
