import org.code3.simpleELParser.Compiler
import groovy.json.JsonSlurper
import groovy.util.GroovyTestCase

class CompilerTest  extends GroovyTestCase {
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
       "012-float-literral",
       "013-integer-literral",
       "021-useless-parenthesis",
       "022-use-parenthesis-to-change-groups",
       "101-unbalanced-parenthesis",
       "102-missing-left-parenthesis",
       "103-unknown-token",
       "104-missing-1-of-2-operand",
       "105-missing-1-of-1-operand",
       "106-missing-2-of-2-operand"
    ]
    .each {
      println "Testing $it"
      def unit = [
        test: it,
        exp: getFile("/simple-el-parser-test-suite/$it/exp.txt").text.trim(),
        result: new JsonSlurper().parseText(getFile("/simple-el-parser-test-suite/$it/result.json").text.trim()),
        error: getFile("/simple-el-parser-test-suite/$it/error.txt").text.trim(),
      ]

      try {

        def parsedExpression = new Compiler().compile(unit.exp)
        def expected = unit.result
        assert  expected == parsedExpression
      } catch (Exception e){
        // e.printStackTrace()
        def expectedError = unit.error
        def actualError = e.message
        assert expectedError ==  actualError
      }
    }
  }
}
