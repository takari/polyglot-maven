/**
 * Copyright (c) 2015 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.groovy.builder.factory;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;

import groovy.util.FactoryBuilderSupport;

/**
 * Builds dependency scope nodes. If a {@link Dependency} scope is not set,
 * the scope of its parent Node will be assigned.
 * 
 * @author Fred Bricon
 */
public class ScopeFactory extends ListFactory {

	private Map<Object, List<Dependency>> scopeHierarchy = new IdentityHashMap<>();
	
	private String scope;
	
	public ScopeFactory(String scope) {
		//scope nodes are prefixed with $ because import (scope) is a keyword.
		super("$"+scope);
		this.scope = scope;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setParent(FactoryBuilderSupport builder, Object dependencies, Object scopeBlock) {
		if (dependencies instanceof List && scopeBlock instanceof List) {
			scopeHierarchy.put(scopeBlock, (List<Dependency>)dependencies);
		}
	}

	@Override
	public void setChild(FactoryBuilderSupport builder, Object scopeBlock, Object child) {
		if (child instanceof Dependency) {
			Dependency dep = (Dependency) child;
			if (!Artifact.SCOPE_COMPILE.equals(scope) && dep.getScope() == null) {
				dep.setScope(scope);
			}//XXX Should we throw an exception if dependency scope != current block scope?
			scopeHierarchy.get(scopeBlock).add(dep);
		}
	}
}
