package org.sonatype.maven.polyglot.java.dsl;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.sonatype.maven.polyglot.java.namedval.NamedValue;
import org.sonatype.maven.polyglot.java.namedval.NamedValueProcessor;

public interface DependencyTemplate {
	public default Dependency dependency(NamedValue<String>... keyValuePairs) {
		
		Dependency dependency = new Dependency();
		
		Map<String, String> map = new HashMap<>();
		asList(keyValuePairs).stream().filter(kvp -> kvp != null).filter(kvp -> !(kvp instanceof DependencyExclusions))
		.forEach(kvp -> map.put(kvp.name(), kvp.value()));
		NamedValueProcessor.mapToObject(dependency, map);
		
		asList(keyValuePairs).stream().filter(kvp -> kvp != null).filter(kvp -> (kvp instanceof DependencyExclusions))
		.forEach(excl -> ((DependencyExclusions)excl).getExclusions().forEach(exclusion -> dependency.addExclusion(exclusion)));

		return dependency;
	}
	
	public default NamedValue<String> exclusions(Exclusion... exclusions) {
		
		DependencyExclusions depExclusions = new DependencyExclusions();
		depExclusions.setExclusions(exclusions);
		
		return depExclusions;
	}
	
	public default Exclusion exclusion(NamedValue<String>... keyValuePairs) {
		Exclusion exclusion = new Exclusion();
		return NamedValueProcessor.namedToObject(exclusion, keyValuePairs);
	}


	public class DependencyExclusions implements NamedValue<String> {
		
		private Exclusion[] exclusions;

		
		public Stream<Exclusion> getExclusions(){
			return asList(exclusions).stream();
		}

		public void setExclusions(Exclusion[] exclusions) {
			this.exclusions = exclusions;
		}

		@Override
		public String apply(String t) {
			return t;
		}

	}
}
