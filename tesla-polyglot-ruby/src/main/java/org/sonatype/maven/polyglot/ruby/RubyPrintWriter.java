/**
 * 
 */
package org.sonatype.maven.polyglot.ruby;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.Xpp3DomUtils;

class RubyPrintWriter extends PrintWriter {

    private static final String INDENT = "  ";

    private String current = "";

    RubyPrintWriter(Writer writer) {
        super(writer);
    }

    void inc() {
        current += INDENT;
    }

    void dec() {
        current = current.substring(INDENT.length());
    }

    public void print(String value) {
        append(current).append(value);
    }

    void print(String name, String... values) {
        if (values.length == 1 && values[0] == null) {
            return;
        }
        append( current ).append( name ).append( " " );
        boolean first = true;
        for (String value : values) {
            if (value != null) {
                if (first) {
                    first = false;
                } else {
                    append(", ");
                }
                if(value.startsWith(":")){
                    append(value);
                }
                else {
                    append("'").append(value).append("'");
                }
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

    void printWithOptions( String prefix, Map<String, Object> options, String... args){
        printWithOptions( prefix, options, null, args);
    }
    
    void printWithOptions( String prefix, Map<String, Object> options, Object config, String... args){
        if ( !options.isEmpty() || config != null )
        {
            prefix += "(";
            print( prefix, args );
            String indent = prefix.replaceAll( ".", " " ) + ' ';
            boolean first = args.length == 0;
            for( Map.Entry<String, Object> item: options.entrySet() )
            {
                if ( first )
                {
                    first = false;
                }
                else
                {
                    append(",").println();
                    print( indent );
                }
                appendName( item.getKey() ).append( " => " );
                if ( item.getValue() instanceof String )
                {
                    append( "'" ).append( item.getValue().toString() ).append( "'" );
                }
                else {
                    append( item.getValue().toString() );
                }
            }
            if ( config != null )
            {
                printConfiguration( indent, config );
                println();
            }
            else
            {
                append( " )" ).println();
            }
        }
        else
        {
            println( prefix, args );
        }
    }

    void printConfiguration( String indent,  Object config ) {
        if (config != null ){
            Xpp3Dom configuration = (Xpp3Dom) config;
            if (configuration.getChildCount() != 0) {
                append(",");
                println();
                printHashConfig( indent, configuration );
            }
            append( " )");
        }
    }
    
    void printHashConfig(String indent, Xpp3Dom base) {
        printHashConfig( indent, base, false );
    }
    
    RubyPrintWriter appendName( String name )
    {
        if ( name.matches( "^[a-zA-Z_]*$" ) )
        {
            append( ":" ).append( name );
        }
        else
        {
            append( "'" ).append( name ).append( "'" );
        }
        return this;
    }
    
    void printHashConfig(String indent, Xpp3Dom base, boolean skipFirst ) {
        int count = base.getChildCount();
        boolean first = true;
        for( String name: base.getAttributeNames() )
        {
            if ( first )
            {
                first = false;
                if ( !skipFirst )
                {
                    print( indent );
                }
            }
            else
            {
                append( "," ).println();
                print( indent );
            }
            append( "'@" ).append( name ).append( "' => '" ).append( base.getAttribute( name ).replaceAll( "'", "\\\\'" ) ).append(  "'" );
        }
        if ( base.getAttributeNames().length > 0 && count > 0 )
        {
            append( "," ).println();
            print( indent );
            skipFirst = true;
        }
        for (int j = 0; j < count; j++)
        {
            Xpp3Dom c = base.getChild(j);
            if ( j != 0 || !skipFirst )
            {
                print(indent);
            }
            if ( c.getChildCount() > 0 || c.getAttributeNames().length > 0 )
            {
                if ( ( c.getChildCount() > 1 && c.getChild(0).getName().equals( c.getChild(1).getName() ) ) 
                    || ( c.getChildCount() == 1 &&  c.getName().equals( c.getChild(0).getName() + "s" ) ) )
                {
                    appendName( c.getName() ).append( " => [" );
                    printListConfig(indent + "    " + c.getName().replaceAll( ".", " " ), c);
                    append( " ]");
                }
                else
                {
                  appendName( c.getName() ).append( " => {").println();
                  printHashConfig( indent + INDENT, c );
                  println();
                  print( indent );
                  append( "}");
                }
            } 
            else if ( c.getValue() == null )
            {
                // assume empty is a boolean like <ignore />
                appendName( c.getName() ).append( " => true");
            } 
            else
            {
                appendName( c.getName() ).append( " => '" ).append( c.getValue() ).append(  "'" );
            }
            if (j + 1 != count) {
                append(",").println();
            }
        }
    }
    
    void printListConfig( String indent, Xpp3Dom base ) {
        int count = base.getChildCount();
        for (int j = 0; j < count; ) {
            Xpp3Dom c = base.getChild(j);
            if ( c.getValue() != null )
            {
                append( " '" ).append( c.getValue() ).append( "'" );
            }
            else if ( c.getChildCount() == 0 )
            {
                append( " xml( '" ).append( c.toString().replaceFirst( "<?.*?>\\s*", "" ) ).append( "' )" );
            }
            else 
            {
                append( " { ");
                printHashConfig( "   " + indent + INDENT, c, true );
                append( " }");
            }
            if( ++j < count )
            {
                append(",");
                println();
                print( indent );
                append( "  ");
            }
        }
    }

    void printStartBlock(String name) {
        print(name);
        printStartBlock();
    }
    void printStartBlock(String name, String... values) {
        printStartBlock( name, null, values );
    }
    
    private void printStartBlock(String name, Map<String, Object> options, String... values) {
        if ( options != null )
        {
            printWithOptions( name, options, values );
        }
        else
        {
            print( name, values );
        }
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