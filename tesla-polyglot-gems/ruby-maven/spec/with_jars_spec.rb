load File.join( File.expand_path( File.dirname( __FILE__ ) ), 'spec_setup.rb' )
require 'fileutils'

describe 'Maven::Tasks' do

  let( :my ){ My.new( 'with_jars' ) }
  let( :pkg ) { my.dir( 'pkg' ) }
  let( :jar ) { my.dir( File.join( 'lib', 'with-jars.jar' ) ) }
  let( :gem ) { my.dir( File.join( 'pkg', 'with-jars-0.0.1.gem' ) ) }

  before do
    FileUtils.rm_rf( jar )    
    FileUtils.rm_rf( pkg )
  end

  describe :rake do
    it 'should build with gemspec' do
      skip( 'does not work (yet) with jruby' ) if defined? JRUBY_VERSION 
      my.exec( 'build2' )
      File.exists?( pkg ).must_equal true
      File.exists?( jar ).must_equal true
      File.exists?( gem ).must_equal true
    end

  # it 'should junit' do
  #   my.exec( 'junit' )
  # end

  # it 'should all tests' do
  #   my.exec( 'test' )
  # end

  # it 'should clean with gemspec' do
  #   my.exec( 'clean2' )
  #   File.exists?( pkg ).must_equal false
  #   File.exists?( jar ).must_equal false
  # end
  end

  describe :rmvn do
    it 'should pack' do
      skip( 'does not work (yet) with jruby' ) if defined? JRUBY_VERSION 
      my.rmvn( 'package' )
      File.exists?( pkg ).must_equal true
      File.exists?( jar ).must_equal true
      File.exists?( gem ).must_equal true
    end

    it 'should compile' do
      skip( 'does not work (yet) with jruby' ) if defined? JRUBY_VERSION 
      my.rmvn( 'compile' )
      File.exists?( pkg ).must_equal true
      File.exists?( jar ).must_equal false
      File.exists?( gem ).must_equal false
    end

    it 'should test' do
      skip( 'does not work (yet) with jruby' ) if defined? JRUBY_VERSION 
      my.rmvn( 'compile', 'compiler:testCompile', 'surefire:test' )
      File.exists?( pkg ).must_equal true
      File.exists?( jar ).must_equal false
      File.exists?( gem ).must_equal false
    end
  end

end
