package org.sonatype.maven.polyglot.java.dsl;

import org.sonatype.maven.polyglot.java.namedval.NamedValue;

public interface PropertyTemplate {
	public default Property property(NamedValue<String> keyValuePair) {
		Property property = new Property();
		property.name = keyValuePair.name();
		property.value = keyValuePair.value();
		return property;
	}
	
	public class Property {
		private String name;
		private String value;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
}
