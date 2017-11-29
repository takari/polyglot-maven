package org.sonatype.maven.polyglot.java.dsl;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Repository;
import org.apache.maven.model.RepositoryPolicy;
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
	
	public default Repository repository(String id, String name, String url) {		
		return repository(id, name, url, null, null, null);
	}
	
	/**
	 * Factory method to create repository. Used for both - "repositories" and "pluginRepositories"
	 * Each key-value pair is a construct 
	 * 	key -> "value"
	 * Where 'key' is the name of the property to set and "value" it's string value.
	 * @param keyValuePairs - array of key -> "value" pairs.
	 * @return	defined Repository
	 */
	public default Repository repository(NamedValue... keyValuePairs) {		
		Repository repository = new Repository();
		NamedValueProcessor.namedToObject(repository, keyValuePairs);
		return repository;
	}
	
	public default Repository repository(String id, String name, String url, String layout, RepositoryPolicy releases, RepositoryPolicy snapshots) {
		Repository repository = new Repository();
		if (id != null) {
			repository.setId(id);
		}
		if (name != null) {
			repository.setName(name);
		}
		if (url != null) {
			repository.setUrl(url);
		}
		if (layout != null) {
			repository.setLayout(layout);
		}
		if (releases != null) {
			repository.setReleases(releases);
		}
		if (snapshots != null) {
			repository.setSnapshots(snapshots);
		}
		return repository;
	}
	
	public default RepositoryPolicy repositoryPolicy(boolean enabled, String updatePolicy, String checksumPolicy) {
		RepositoryPolicy policy = new RepositoryPolicy();
		policy.setEnabled(enabled);
		if (updatePolicy != null) {
			policy.setUpdatePolicy(updatePolicy);
		}
		if (checksumPolicy != null) {
			policy.setChecksumPolicy(checksumPolicy);
		}
		return policy;
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
