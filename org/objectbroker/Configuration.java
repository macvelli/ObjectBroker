package org.objectbroker;

public class Configuration {

	String		defaultLoadSql;
	String		defaultLoadAllSql;
	Listener	loadListener;
	Listener	loadAllListener;

	@SuppressWarnings("unchecked")
	public Configuration(final Class clazz) {
		if (clazz.isAnnotationPresent(Load.class))
			defaultLoadSql = ((Load) clazz.getAnnotation(Load.class)).value();
	}

	public void setLoadAllListener(final Listener loadAllListener) {
		this.loadAllListener = loadAllListener;
	}

	public void setLoadListener(final Listener loadListener) {
		this.loadListener = loadListener;
	}

	@Override public String toString() {
		final StringBuilder builder = new StringBuilder("\n\tConfiguration:");

		builder.append("\n\t\tLoad SQL: ").append(defaultLoadSql);
		builder.append("\n\t\tLoad All SQL: ").append(defaultLoadAllSql);

		return builder.toString();
	}

}	// End Configuration
