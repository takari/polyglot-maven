package net.openhft.compiler;

public class CompilerTest {
	
	public static void main(String[] args) throws Exception {
		String className = "mypackage.MyClass";
		 String javaCode = "package mypackage;\n" +
		                  "public class MyClass implements Runnable {\n" +
		                  "    public void run() {\n" +
		                  "        System.out.println(\"Hello World\");\n" +
		                  "    }\n" +
		                  "}\n";
		 Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
		 Runnable runner = (Runnable) aClass.newInstance();
		 runner.run();
	}

}
