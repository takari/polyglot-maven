class Configuration {
    protected var root = Tag("configuration")
    operator fun component1() = root

    @Scope operator fun String.get(vararg tagValues: String) {
        val (_, values, _) = root
        values[this] = tagValues
    }

    @Scope operator fun String.plusAssign(value: String) {
        val (keyValue, _, _) = root
        keyValue[this] = value
    }

    @Scope infix fun String.assign(value: String) {
        val (keyValue, _, _) = root
        keyValue[this] = value
    }

    @Scope operator fun String.invoke(block: Tag.() -> Unit) {
        val (_, _, childs) = root
        val child = Tag(this)
        childs.add(child)
        block(child)
    }

    @Scope operator fun String.unaryPlus() {
        val (_, _, childs) = root
        childs.add(Tag(this))
    }

    @Scope inner class Tag(val name: String) {
        protected val keyValue = mutableMapOf<String, String>()
        operator fun component1() = keyValue

        protected val values = mutableMapOf<String, Array<out String>>()
        operator fun component2() = values

        protected var childs: MutableList<Tag> = mutableListOf()
        operator fun component3() = childs

        @Scope operator fun String.plusAssign(value: String) {
            keyValue[this] = value
        }

        @Scope infix fun String.assign(value: String) {
            keyValue[this] = value
        }

        @Scope operator fun String.get(vararg tagValues: String) {
            values[this] = tagValues
        }

        @Scope operator fun String.invoke(block: Tag.() -> Unit) {
            val child = Tag(this)
            childs.add(child)
            block(child)
        }

        @Scope operator fun String.unaryPlus() {
            childs.add(Tag(this))
        }
   }
}