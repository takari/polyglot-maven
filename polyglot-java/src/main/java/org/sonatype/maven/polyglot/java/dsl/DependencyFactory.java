package org.sonatype.maven.polyglot.java.dsl;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.sonatype.maven.polyglot.java.namedval.NamedValue;
import org.sonatype.maven.polyglot.java.namedval.NamedValueProcessor;

public interface DependencyFactory {
	public default Dependency dependency(NamedValue... keyValuePairs) {
		
		Dependency dependency = new Dependency();
		
		Map<String, String> map = new HashMap<>();
		asList(keyValuePairs).stream().filter(kvp -> kvp != null).filter(kvp -> !(kvp instanceof DependencyExclusions))
		.forEach(kvp -> map.put(kvp.name(), kvp.value()));
		NamedValueProcessor.mapToObject(dependency, map);
		
		asList(keyValuePairs).stream().filter(kvp -> kvp != null).filter(kvp -> (kvp instanceof DependencyExclusions))
		.forEach(excl -> ((DependencyExclusions)excl).getExclusions().forEach(exclusion -> dependency.addExclusion(exclusion)));

		return dependency;
	}		
	
	public default Dependency dependency(String definition) {
		return dependency(definition, null);
	}
	
	public default Dependency compile(String definition) {
		return dependency(definition, "compile");
	}
	
	public default Dependency provided(String definition) {
		return dependency(definition, "provided");
	}
	
	public default Dependency runtime(String definition) {
		return dependency(definition, "runtime");
	}
	
	public default Dependency test(String definition) {
		return dependency(definition, "test");
	}
	
	public default Dependency dependency(String definition, String scope) {
		
		Dependency dependency = new Dependency();
		
		String[] parts = definition.split(":");
		dependency.setGroupId(parts[0]);
		dependency.setArtifactId(parts[1]);
		
		if (parts.length > 2) {
			dependency.setVersion(parts[3]);
		}
		
		if (scope != null) {
			dependency.setScope(scope);
		}

		return dependency;
	}
	
	
	
	public default NamedValue exclusions(Exclusion... exclusions) {
		
		DependencyExclusions depExclusions = new DependencyExclusions();
		depExclusions.setExclusions(exclusions);
		
		return depExclusions;
	}
	
	public default Exclusion exclusion(NamedValue... keyValuePairs) {
		Exclusion exclusion = new Exclusion();
		return NamedValueProcessor.namedToObject(exclusion, keyValuePairs);
	}


	public class DependencyExclusions implements NamedValue {
		
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
