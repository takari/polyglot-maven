/**
 * 
 */
package org.sonatype.maven.polyglot.ruby;

import java.io.PrintWriter;
import java.io.Writer;

class RubyPrintWriter extends PrintWriter {

    private final String indent = "  ";

    private String current = "";

    RubyPrintWriter(Writer writer) {
        super(writer);
    }

    void inc() {
        current += indent;
    }

    void dec() {
        current = current.substring(indent.length());
    }

    public void print(String value) {
        append(current).append(value);
    }

    void print(String name, String... values) {
        if (values.length == 1 && values[0] == null) {
            return;
        }
        append(current).append(name).append(" ");
        boolean first = true;
        for (String value : values) {
            if (value != null) {
                if (first) {
                    first = false;
                } else {
                    append(", ");
                }
                append("'").append(value).append("'");
            }
        }
    }

    public void println(String name) {
        print(name);
        println();
    }

    void println(String name, String... values) {
        if (values.length == 1 && values[0] == null) {
            return;
        }
        print(name, values);
        println();
    }

    void printStartBlock(String name) {
        print(name);
        printStartBlock();
    }

    void printStartBlock(String name, String... values) {
        print(name, values);
        printStartBlock();
    }

    void printStartBlock() {
        append(" do");
        println();
        inc();
    }

    void printEndBlock() {
        dec();
        append(current).append("end");
        println();
    }

}