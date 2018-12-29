package org.code3.simpleELParser

class Compiler {
  def compile(String expression){
    def parser = new Parser()
    def tokenizer = new Tokenizer()

    def lexems = tokenizer.tokenize(expression)
    def ast = parser.parse(expression, lexems)
    ast
  }
}
