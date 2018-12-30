import org.code3.simpleELParser.Tokenizer
import groovy.util.GroovyTestCase


class TokenizerTest extends GroovyTestCase {
  void testInteger(){
    def tokens = new Tokenizer().tokenize("123")
    def expectedTokens = [[type:"int", value:"123", start:0]]
    assert tokens == expectedTokens
  }
  void testNegativeInteger(){
    def tokens = new Tokenizer().tokenize("-123")
    def expectedTokens = [[type:"int", value:"-123", start:0]]
    assert tokens == expectedTokens
  }

  void testFloat(){
    def tokens = new Tokenizer().tokenize("123.21")
    def expectedTokens = [[type:"float", value:"123.21", start:0]]
    assert tokens == expectedTokens
  }
  void testNegativeFloat(){
    def tokens = new Tokenizer().tokenize("-123.21")
    def expectedTokens = [[type:"float", value:"-123.21", start:0]]
    assert tokens == expectedTokens
  }
  void testString(){
    def tokens = new Tokenizer().tokenize("\"abc\"")
    def expectedTokens = [[type:"string", value:"\"abc\"", start:0]]
    assert tokens == expectedTokens
  }
  void testAnd(){
    def tokens = new Tokenizer().tokenize("&&")
    def expectedTokens = [[type:"and", value:"&&", start:0]]
    assert tokens == expectedTokens
  }
  void testOr(){
    def tokens = new Tokenizer().tokenize("||")
    def expectedTokens = [[type:"or", value:"||", start:0]]
    assert tokens == expectedTokens
  }

  void testGt(){
    def tokens = new Tokenizer().tokenize(">")
    def expectedTokens = [[type:"gt", value:">", start:0]]
    assert tokens == expectedTokens
  }
  void testLt(){
    def tokens = new Tokenizer().tokenize("<")
    def expectedTokens = [[type:"lt", value:"<", start:0]]
    assert tokens == expectedTokens
  }
  void testVariable(){
    def tokens = new Tokenizer().tokenize(" a ")
    def expectedTokens = [[type:"var", value:"a", start:1]]
    assert tokens == expectedTokens
  }

  void testMoreComplexeExpression(){
    def tokens = new Tokenizer().tokenize("a == \"def\"")
    def expectedTokens = [[type:"var", value:"a", start:0], [type:"eq", value:"==", start:2], [type:"string", value:"\"def\"", start:5]]
    assert tokens == expectedTokens
  }
}
