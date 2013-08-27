if defined? JRUBY_VERSION
  #load 'setup.rb'
  require 'java'
  describe Java::Test::C do
    
    it 'should' do
      Java::Test::C.new.name.must_equal 'me and the corner'
    end
  end
end
