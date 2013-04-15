/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.maven.polyglot.scala

import java.io.{StringReader, Reader}
import org.junit.runner.RunWith
import org.scalatest._
import matchers.ShouldMatchers
import junit.JUnitRunner

import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class ScalaModelReaderSpec extends WordSpec with ShouldMatchers {

  "ScalaModelReader.read" when {
    val reader = new ScalaModelReader
    
    val inputEmpty = getClass.getResourceAsStream("/pomEmpty.scala")
    val inputMinimal = getClass.getResourceAsStream("/pomMinimal.scala")
    val inputWithContributors = getClass.getResourceAsStream("/pomWithContributors.scala")
    val inputWithDevelopers = getClass.getResourceAsStream("/pomWithDevelopers.scala")
    val inputWithScala = getClass.getResourceAsStream("/pomWithScala.scala")
    
    val emptyOptions = new java.util.HashMap[String, String]()

    "called with a null input and empty options" should {

      "throw an IllegalArgumentException" in {
        intercept[IllegalArgumentException] {
          reader.read(null, emptyOptions)
        }
      }
    }

    "called with a valid input and empty options" should {

      "return a valid model" in {
        val model = reader.read(inputEmpty, emptyOptions)
        model should not be(null)
      }
    }

    "called with minimal pom and empty options" should {
    
      "parse project coordinates" in {
        val model = reader.read(inputMinimal, emptyOptions)
        
        model.getModelVersion should equal ("4.0.0")
        model.getGroupId should equal ("org.blepharospasm")
        model.getArtifactId should equal ("squankdiliumtious")
        model.getVersion should equal ("0.0.0-SNAPSHOT")
      }
    }
    
    "called with pom that includes contributors" should {
    
      "parse contributors" in {
        val model = reader.read(inputWithContributors, emptyOptions)
        model.getContributors map {_.getName} should contain ("John Lennon")
        model.getContributors map {_.getName} should contain ("Elvis Presley")
        
        val m = model.getContributors filter {_.getName.equals("Elvis Presley")} get 0
        m.getProperties.apply("isAlive") should be("false")
      }
    }

    "called with pom that includes developers" should {
    
      "parse contributors" in {
        val model = reader.read(inputWithDevelopers, emptyOptions)
        model.getDevelopers map {_.getId} should contain ("bmaso")
        model.getDevelopers map {_.getId} should contain ("hseeberger")
      }
    }
    
    "called with a pom that includes Scala shorthand configuration" should {
    
      "parse scala options" in {
        val model = reader.read(inputWithScala, emptyOptions)
        
        //...should contain this following dependency...
        model.getDependencies.length should be(1)
        List(model.getDependencies()(0).getGroupId, model.getDependencies()(0).getArtifactId, model.getDependencies()(0).getVersion) should be(List("apache-httpclient", "commons-httpclient", "3.0.1"))
      }
    }
  }
}
