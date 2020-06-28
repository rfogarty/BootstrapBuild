package testBootstrapBuild


import junit.framework.Test
import junit.textui.TestRunner

class TestBootstrapAll {
   static Test suite() {
      def allTests = new GroovyTestSuite()
      allTests.addTestSuite(TestBooleanExpressionParser.class)
      return allTests
   }
}

TestRunner.run(TestBootstrapAll.suite())
