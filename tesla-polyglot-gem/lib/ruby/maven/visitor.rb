require 'maven/model'
module Maven
  class Visitor

    def initialize( io = STDOUT )
      @io = io
    end

    def indent
      @indent ||= ''
    end

    def inc
      @indent = @indent + '  '
    end

    def dec
      @indent = @indent[ 0..-3 ]
    end

    def start_tag( name )
      @io.puts "#{indent}<#{camel_case_lower( name )}>"
      inc
    end

    def end_tag( name )
      dec
      @io.puts "#{indent}</#{camel_case_lower( name )}>"
    end

    def tag( name, value )
      unless value.nil?
        name = camel_case_lower( name )
        @io.puts "#{indent}<#{name}>#{value}</#{name}>"
      end
    end

    def camel_case_lower( str )
      str = str.to_s
      str.split( '_' ).inject([]) do |buffer, e|
        buffer.push( buffer.empty? ? e : e.capitalize )
      end.join
    end
    
    def accept_project( project )
      accept( 'project', project )
      @io.close if @io.respond_to? :close
    end

    def accept( name, model )
      if model
        start_tag( name )
        visit( model )
        end_tag( name )
      end
    end

    def accept_array( name, array )
      unless array.empty?
        start_tag( name )
        n = name.to_s.sub( /ies$/, 'y' ).sub( /s$/, '' )
        case array.first
        when Virtus
          array.each do |i|
            start_tag( n )
            visit( i )
            end_tag( n )
          end
        else
          array.each do |i|
            tag( n, i )
          end
        end
        end_tag( name )
      end
    end

    def accept_hash( name, hash )
      unless hash.empty?
        start_tag( name )
        hash.each do |k, v|
          tag( k, v )
        end
        end_tag( name )
      end
    end

    def visit( model )
      model.attributes.each do |k, v|
        case v
        when Virtus
          accept( k, v )
        when Array
          accept_array( k, v )
        when Hash
          accept_hash( k, v )
        else
          tag( k, v )
        end
      end
    end
  end
end
