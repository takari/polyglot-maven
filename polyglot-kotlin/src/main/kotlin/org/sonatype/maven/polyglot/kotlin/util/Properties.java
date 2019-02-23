package org.sonatype.maven.polyglot.kotlin.util;

import java.util.*;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.NotNull;

public class Properties extends java.util.Properties {

    @Override
    @NotNull
    public Set<Object> keySet() {
        return super.keySet().stream().sorted(Comparator.comparing(Object::toString))
            .collect(LinkedHashSet::new, Set::add, (objects, objects2) -> {
                objects.addAll(objects2);
            });
    }

    @Override
    @NotNull
    public Set<Map.Entry<Object, Object>> entrySet() {
        return super.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().toString()))
            .collect(LinkedHashSet::new, Set::add, (BiConsumer<Set<Map.Entry<Object, Object>>, Set<Map.Entry<Object, Object>>>) (entries, entries2) -> {
                entries.addAll(entries2);
            });
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        return new Enumeration<Object>() {

            private Iterator<Object> keys = keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                return keys.hasNext();
            }

            @Override
            public Object nextElement() {
                return keys.next();
            }
        };
    }
}
