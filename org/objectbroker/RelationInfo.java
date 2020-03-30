package org.objectbroker;

import java.lang.reflect.Field;
import java.util.List;

import org.sqlbroker.param.FixedParams;
import org.sqlbroker.param.Parameters;

import root.lang.Array;
import root.util.GenericsUtil;

class RelationInfo {

	final ClassInfo	info;

	private final boolean	oneToMany;
	private final Field		field;
	private final String	sql;
	private final String[]	keys;
	private final ClassInfo	parent;

	RelationInfo(final ClassInfo parent, final Field field, final String sql, final String[] keys) {
		this.parent = parent;
		this.field = field;
		this.sql = sql;
		this.keys = keys;
		oneToMany = (field.getType() == List.class);
		if (oneToMany)
			try {
				info = new ClassInfo(GenericsUtil.getType(field.getGenericType()), parent.broker);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Cannot access instance of List<E> generic type", e);
			}
		else
			info = new ClassInfo(field.getType(), parent.broker);
	}

	void populate(final Object obj) throws IllegalArgumentException, IllegalAccessException {
		final Parameters params = new FixedParams(keys.length);

		for (String s : keys)
			params.add(parent.colFieldMap.get(s).get(obj));

		field.set(obj, (oneToMany) ? info.loadAll(sql, params, 0, 0) : info.load(sql, params));
	}

	@Override public String toString() {
		final StringBuilder builder = new StringBuilder("\n\tRelation: (");

		builder.append(field).append(", ");
		builder.append((oneToMany) ? "One-To-Many" : "One-To-One");
		if (keys.length > 0) {
			builder.append(", Keys: ");
			builder.append(Array.join(keys));
		}
		builder.append(")\n");
		builder.append(info);

		return builder.toString();
	}

}	// End RelationManager
