package org.code3.simpleELParser

public class Parser {

  static List parse(String expression){
    def tokenizer = new Tokenizer()
    def tokens = tokenizer.tokenize(expression)
    return tokens
  }
}
