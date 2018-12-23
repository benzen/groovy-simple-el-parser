package org.code3.simpleELParser

public class Parser {
  def parse(String exp){
    def lexems = new Tokenizer().tokenize(exp)
    def lexemsInRPN = infixToRPN(lexems)
    def ast = buildAST(lexemsInRPN)
  }

  // https://en.wikipedia.org/wiki/Shunting-yard_algorithm
  def infixToRPN(lexems){
    def precedence = [
      "not": 4,
      "lt": 8,
      "lte": 8,
      "gt": 8,
      "gte": 8,
      "and": 13,
      "or": 14,
      "rp": 10,
      "lp": 10,
      "eq": 9,
      "neq": 9,
    ]
    def associativity = [
      "not": "left",
      "lt": "right",
      "lte": "right",
      "gt": "right",
      "gte": "right",
      "and": "right",
      "or": "right",
      // "rp": "na",
      // "lp": "na",
      "eq": "right",
      "neq": "right",
    ]

    def ops = []
    def output = []
    for(def lexem : lexems){
      if(["string", "int", "float", "var"].contains(lexem.type)){
        output.push(lexem)
      } else if(["lt", "lte", "gt", "gte", "eq", "neq", "not", "and", "or" ].contains(lexem.type)) {
        // ((there is a function at the top of the operator stack)
        //        or (there is an operator at the top of the operator stack with greater precedence)
        //        or (the operator at the top of the operator stack has equal precedence and is left associative))
        //       and (the operator at the top of the operator stack is not a left bracket):
        def cond = { ->
          ops.size() > 0 &&
          ops[0].type != "lp" &&
          (( precedence(ops[0].type) > precedence(lexem.type)) ||
            (precedence(ops[0].type) == precedence(lexem.type) && associativity(ops[0].type) == "left" ))
        }
        while(cond()){
          output.push(ops.pop())
        }
        ops.push(lexem)
      } else if (lexem.type == "lp"){
        ops.push(lexem)
      } else if (lexem.type == "rp"){
        // TODO ensure that there a balanced paren
        while(ops[0].type != "lp"){
          output.push(ops.pop())
        }
        output.push(ops.pop())
      } else {
        throw new RuntimeException("Unkown lexem type: ${lexem.type}")
      }
    }
    while (ops.size () > 0){
      output.push(ops.pop())
    }
    output
  }

  def buildAST(List lexemsInRPN){

    def output = []
    for(def lexem : lexemsInRPN){
      if (["string", "int", "float", "var"].contains(lexem.type)){
        output.push(lexem)
      } else if(["lt", "lte", "gt", "gte", "eq", "neq", "and", "or" ].contains(lexem.type)) {
        if(output.size()< 2){ throw new RuntimeException("Malformed expression")}
        def rhs = output.pop()
        def lhs = output.pop()
        lexem.children = [lhs, rhs]
        output.push(lexem)
      } else if (lexem.type== "not"){
        if(output.size() < 1) {throw new RuntimeException("Malformed expression")}
        lexem.children = output.pop()
        output.push(lexem)
      }
    }
    output[0]
  }
}
