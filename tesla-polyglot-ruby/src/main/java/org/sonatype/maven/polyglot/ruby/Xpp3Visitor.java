package org.sonatype.maven.polyglot.ruby;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.xml.Xpp3Dom;

class Xpp3Visitor
{
    Map<String, Object> children = new LinkedHashMap<String, Object>();

    static class Config
    {
        static enum Type {
            SINGLE, MULTI, MIXED, MAPS
        }

        Type type;
        String value;
        List<String> stringList;
        List<ListItem> list;
        List<Map<String, Object>> mapList;

        void add( String value )
        {
            if ( stringList != null )
            {
                this.stringList.add( value );
            }
            else if ( this.value == null )
            {
                this.type = Type.SINGLE;
                this.value = value;
            }
            else
            {
                this.type = Type.MULTI;
                this.stringList = new LinkedList<String>();
                this.stringList.add( this.value );
                this.stringList.add( value );
                this.value = null;
            }
        }

        void add( Xpp3Dom item )
        {
            if ( this.list == null )
            {
                this.type = Type.MIXED;
                this.list = new LinkedList<ListItem>();
            }
            if ( item.getValue() == null )
            {
                list.add( new ListItem( item ) );
            }
            else
            {
                list.add(  new ListItem( item.getValue() ) );
            }
        }

        void add( Map<String, Object> map )
        {
            if ( this.mapList == null )
            {
                this.type = Type.MAPS;
                this.mapList = new LinkedList<Map<String, Object>>();
            }
            this.mapList.add( map );
        }
    }
    static class Leaf
    {
        private String value;
        private String name;
        Leaf( String name, String value )
        {
            this.name = name;
            this.value = value;
        }

        void accept( Xpp3Visitor visitor )
        {
            final Config list ;
            if ( !visitor.children.containsKey( name ) )
            {
                list = new Config();
                visitor.children.put( name, list );
            }
            else
            {
                list = (Config) visitor.children.get( name );
            }
            list.add( value );
        }
    }

    static class Node
    {
        Xpp3Dom base;
        Node( Xpp3Dom base )
        {
            this.base = base;
        }
        void accept( Xpp3Visitor visitor )
        {
            Xpp3Visitor nextVisitor = new Xpp3Visitor();
            nextVisitor.visit( this );
            visitor.children.put( base.getName(), nextVisitor.children );
        }
    }

    static class ListItem
    {
        final String xml;
        final String value;

        ListItem( String value )
        {
            this.value = value;
            this.xml = null;
        }

        ListItem( Xpp3Dom xml )
        {
            this.value = null;
            this.xml = xml.toString().replaceFirst( "<?.*?>\\s*", "" );
        }

        boolean isXml()
        {
            return xml != null;
        }

        public String toString()
        {
            return xml == null ? value : xml;
        }
    }

    static class ListNode extends Node
    {
        ListNode( Xpp3Dom base )
        {
            super( base );
        }

        void accept( Xpp3Visitor visitor )
        {
            if ( base.getChild( 0 ).getChildCount() == 0 )
            {
                Config list = new Config();
                for( Xpp3Dom child : base.getChildren() )
                {
                    list.add( child );
                }
                visitor.children.put( base.getName(), list );
            }
            else
            {
                Config list = new Config();
                for( Xpp3Dom child : base.getChildren() )
                {
                    Xpp3Visitor nextVisitor = new Xpp3Visitor();
                    nextVisitor.visit( new Node( child ) );
                    list.add(  nextVisitor.children );
                }
                visitor.children.put( base.getName(), list );
            }
        }
    }

    void visit( Node node )
    {
        for( String name: node.base.getAttributeNames() )
        {
            new Leaf( "@" + name,
                      node.base.getAttribute( name )
                               .replaceAll( "'", "\\\\'" ) ).accept( this );
        }
        for ( Xpp3Dom child : node.base.getChildren() )
        {
            if ( child.getChildCount() == 0 && child.getAttributeNames().length == 0 )
            {
                new Leaf( child.getName(), child.getValue() ).accept( this );
            }
            else if ( ( child.getChildCount() > 1 && child.getChild(0).getName().equals( child.getChild( 1 ).getName() ) )
                    || ( child.getChildCount() == 1 &&  child.getName().equals( child.getChild( 0 ).getName() + "s" ) ) )
            {
                new ListNode( child ).accept( this );
            }
            else
            {
                new Node( child ).accept( this );
            }
        }
    }
}