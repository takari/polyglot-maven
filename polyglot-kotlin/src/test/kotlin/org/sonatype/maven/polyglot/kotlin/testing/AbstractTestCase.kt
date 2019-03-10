package org.sonatype.maven.polyglot.kotlin.testing

abstract class AbstractTestCase {

    protected val testName: String
        get() {
            var offset = 0
            val stackTrace = Thread.currentThread().stackTrace
            while (stackTrace[offset].className != testClass) offset++
            while (stackTrace[offset].className == testClass) offset++
            return stackTrace[offset - 1].methodName
        }

    private val testClass: String = this.javaClass.name
}