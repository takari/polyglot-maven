import org.codehaus.plexus.util.xml.Xpp3Dom

object ConfigurationConverter {

    fun configurationOf(config: Configuration): Xpp3Dom? {
        val (root) = config
        return domOf(root)
    }

    private fun domOf(root: Configuration.Tag?): Xpp3Dom? {
        if (root == null) return null

        return root.let {
            val (keyValue, values, childs) = it
            val configurationDom = Xpp3Dom(root.name)

            keyValue.forEach { tagName, value ->
                val tagDom = Xpp3Dom(tagName)
                tagDom.value = value
                configurationDom.addChild(tagDom)
            }

            values.forEach { parentKey, childValues ->
                val parentDom = Xpp3Dom(parentKey)
                val childKey = parentKey.removeSuffix("s")

                for (childValue in childValues) {
                    val childDom = Xpp3Dom(childKey)
                    childDom.value = childValue
                    parentDom.addChild(childDom)
                }
                configurationDom.addChild(parentDom)
            }

            for (child in childs) { configurationDom.addChild(domOf(child)) }

            configurationDom
        }
    }
}