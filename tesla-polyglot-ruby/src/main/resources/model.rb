require 'virtus'

module GAV
  def self.included( base )
    base.attribute :group_id, String
    base.attribute :artifact_id, String
    base.attribute :version, String
  end
end
module SU
  def self.included( base )
    base.attribute :system, String
    base.attribute :url, String
  end
end
module NU
  def self.included( base )
    base.attribute :name, String
    base.attribute :url, String
  end
end
module INU
  def self.included( base )
    base.attribute :id, String
    base.attribute :name, String
    base.attribute :url, String
  end
end

class Parent
  include Virtus

  include GAV

  attribute :relative_path, String
end
class Organization
  include Virtus

  include NU
end
class License
  include Virtus

  include NU
  attribute :distribution, String
  attribute :comments, String
end
class Developer
  include Virtus

  include INU
  attribute :email, String
  attribute :organization, String
  attribute :organization_url, String
  attribute :roles, String
  attribute :timezone, String
  attribute :properties, Hash
end
class Contributor
  include Virtus

  include NU
  attribute :email, String
  attribute :organization, String
  attribute :organization_url, String
  attribute :roles, String
  attribute :timezone, String
  attribute :properties, Hash
end
class Prerequisites
  include Virtus

  attribute :maven, String
end
class Scm
  include Virtus

  attribute :connection, String
  attribute :developer_connection, String
  attribute :tag, String
  attribute :url, String
end
class IssueManagement
  include Virtus

  include SU
end
class Notifier
  include Virtus
  
  attribute :type, String
  attribute :send_on_error, Boolean
  attribute :send_on_failure, Boolean
  attribute :send_on_success, Boolean
  attribute :send_on_warning, Boolean
  attribute :address, String
  attribute :configuration, Hash
end
class CiManagement
  include Virtus

  include SU
  
  attribute :notifiers, Array[ Notfifier ]
end
class Project
  include Virtus

  attribute :model_version, String
  attribute :parent, Parent

  include GAV

  attribute :packaging, String

  include NU

  attribute :description, String
  attribute :inception_year, String
  attribute :organization, Organization
  attribute :licenses, Array[ License ]
  attribute :developers, Array[ Developer ]
  attribute :contributors, Array[ Contributor ]
  attribute :mailing_lists, Array[ MailingList ]
  attribute :prerequisites, Prerequisites
  attribute :modules, Array[ String ]
  attribute :scm, Scm
  attribute :issue_management, IssueManagement
  attribute :ci_management, CiManagement
  attribute :distribution_management, DistributionManagement
  attribute :properties, Hash

end
