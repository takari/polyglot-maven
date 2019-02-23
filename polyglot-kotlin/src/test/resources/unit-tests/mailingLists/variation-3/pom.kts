project {
    mailingLists {
        mailingList("polyglot-kotlin") {
            subscribe("mailto:polyglot-kotlin@example.com?subject=subscribe")
            unsubscribe("mailto:polyglot-kotlin@example.com?subject=unsubscribe")
            post("mailto:polyglot-kotlin@example.com")
            archive("http://www.example.com/mail/polyglot-kotlin/")
            otherArchives(
                "http://mail.example.com/polyglot-kotlin/",
                "http://mail.example.com/polyglot-maven/"
            )
        }
    }
}