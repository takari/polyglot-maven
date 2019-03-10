project {
    ciManagement {
        system = "Jenkins"
        url = "http://jenkins.example.com"
        notifiers {
            notifier {
                type = "email"
                address = "build-manager@example.com"
                isSendOnError = false
                isSendOnFailure = false
                isSendOnSuccess = false
                isSendOnWarning = false
                configuration {
                    "projectId" to 1234
                }
            }
        }
    }
}