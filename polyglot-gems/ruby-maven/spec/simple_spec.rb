load File.join( File.expand_path( File.dirname( __FILE__ ) ), 'spec_setup.rb' )
require 'fileutils'

describe 'Maven::Tasks' do

  let( :my ){ My.new( 'simple' ) }
  let( :pkg ) { my.dir( 'pkg' ) }
  let( :simple_jar ) { my.dir( File.join( 'lib', 'simple.jar' ) ) }
  let( :simple_gem ) { my.dir( File.join( 'pkg', 'simple-0.0.1.gem' ) ) }

  before do
    FileUtils.rm_rf( simple_jar )    
    FileUtils.rm_rf( pkg )
  end

  describe :rake do

  it 'should fail on jar with gemspec' do
    skip if defined? JRUBY_VERSION 
    lambda { my.exec_in( 'simple_fail', 'jar' ) }.must_raise SystemExit
  end

  it 'should compile with gemspec' do
    my.exec( 'compile' )
    File.exists?( pkg ).must_equal true
  end

  # NOTE that spec conflicts with the failed spec from above
  #      something stays persistent
  # it 'should jar with gemspec' do
  #   my.exec( 'jar' )
  #   File.exists?( pkg ).must_equal true
  #   File.exists?( simple_jar ).must_equal true
  # end

  it 'should build with gemspec' do
    my.exec( 'build' )
    File.exists?( pkg ).must_equal true
    File.exists?( simple_jar ).must_equal true
    File.exists?( simple_gem ).must_equal true
  end

  it 'should clean with gemspec' do
    my.exec( 'clean' )
    skip if defined? JRUBY_VERSION 
    File.exists?( pkg ).must_equal false
    File.exists?( simple_jar ).must_equal false
  end
  end

  describe :rmvn do

    it 'should pack the gem' do
      my.rmvn( 'package' )
      File.exists?( pkg ).must_equal true
      File.exists?( simple_jar ).must_equal true
      File.exists?( simple_gem ).must_equal true
    end

    it 'should cleanup' do
      my.rmvn( 'package', 'clean' )
      File.exists?( pkg ).must_equal false
      File.exists?( simple_jar ).must_equal false
    end

    it 'should compile' do
      my.rmvn( 'compile' )
      File.exists?( pkg ).must_equal true
      File.exists?( simple_jar ).must_equal false
    end
  end
end
