import org.code3.simpleELParser.Parser
import groovy.json.JsonSlurper
import groovy.util.GroovyTestCase

class ParserTest  extends GroovyTestCase {
  def getFile(pathInClassPath){
    new File(getClass().getResource(pathInClassPath).file)
  }
  void testAll(){
    def tests = [
       "001-and",
       "002-or",
       "003-not",
       "004-eq",
       "005-lt",
       "006-gt",
       "007-lte",
       "008-gte",
       "009-neq",
       "011-string-literral",
       "012-integer-literral",
       "013-float-literral",
    ]
    .each {
      println "Testing $it"
      def unit = [
        test: it,
        // error: getFile("/simple-el-parser-test-suite/$it/error.txt").text.trim(),
        exp: getFile("/simple-el-parser-test-suite/$it/exp.txt").text.trim(),
        result: new JsonSlurper().parseText(getFile("/simple-el-parser-test-suite/$it/result.json").text.trim())
      ]

      try {
        def parsedExpression = Parser.parse(unit.exp)
        def expected = unit.result
        assert  expected == parsedExpression
      } catch (Exception e){
        def expectedError = unit.error
        def actualError = e.message
        e.printStackTrace()
        assert expectedError ==  actualError
      }
    }
  }
}
