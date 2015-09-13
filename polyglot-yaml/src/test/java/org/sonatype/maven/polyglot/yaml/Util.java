package org.sonatype.maven.polyglot.yaml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {

    public static String getLocalResource(String theName) {
        try {
            InputStream input;
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream(theName);
            if (input == null) {
                throw new RuntimeException("Can not find " + theName);
            }
            BufferedInputStream is = new BufferedInputStream(input);
            StringBuilder buf = new StringBuilder(3000);
            int i;
            try {
                while ((i = is.read()) != -1) {
                    buf.append((char) i);
                }
            } finally {
                is.close();
            }
            String resource = buf.toString();
            // convert EOLs
            String[] lines = resource.split("\\r?\\n");
            StringBuilder buffer = new StringBuilder();
            for (int j = 0; j < lines.length; j++) {
                buffer.append(lines[j]);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
