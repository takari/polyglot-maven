package org.sonatype.maven.polyglot.java.test;

import org.sonatype.maven.polyglot.java.compiler.CompilerUtils;
import org.sonatype.maven.polyglot.java.dsl.ModelTemplate;

public class RunTest {
	
public static void main(String[] args) {
		
		String className = "MyClass";
	
		 String javaCode = "" +
		                  "public class MyClass extends org.sonatype.maven.polyglot.java.dsl.ModelTemplate  {\n" +
		                  " public void project() {" + 
		                  "  groupId = \"my-grp\";" +
		                  "  artifactId = \"my-art\";" +
		                  " version = \"1.0\";" +
		               	
			                "properties(" +
			                "     property(key -> \"property_1\")" +
			                ");" +
		  
		                  "" +
		                  "" +
		                  "}" +
		                  "}\n";
		 try {
			Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
			ModelTemplate dsl = (ModelTemplate)aClass.newInstance();
			System.out.println(dsl.getModel().getProperties());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
