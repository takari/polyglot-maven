project {
    ciManagement {
        system("Jenkins")
        url("http://jenkins.example.com")
        notifiers {
            notifier {
                type("email")
                address("build-manager@example.com")
                sendOnError(false)
                sendOnFailure(false)
                sendOnSuccess(false)
                sendOnWarning(false)
                configuration {
                    "projectId" to 1234
                }
            }
        }
    }
}