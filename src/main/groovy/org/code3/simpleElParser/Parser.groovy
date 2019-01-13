package org.code3.simpleELParser

public class Parser {
  def parse(List lexems, String expression){
    def lexemsInRPN = infixToRPN(lexems, expression)
    buildAST(lexemsInRPN, expression)
  }

  // https://en.wikipedia.org/wiki/Shunting-yard_algorithm
  def infixToRPN(lexems, exp){
    def precedence = [
      "not": 15,
      "lt": 11,
      "lte": 11,
      "gt": 11,
      "gte": 11,
      "and": 6,
      "or": 5,
      "rp": 19,
      "lp": 19,
      "eq": 10,
      "neq": 10,
    ]
    def associativity = [
      "not": "left",
      "lt": "right",
      "lte": "right",
      "gt": "right",
      "gte": "right",
      "and": "right",
      "or": "right",
      "eq": "right",
      "neq": "right",
    ]

    def ops = []
    def output = []
    for(def lexem : lexems){

      if(["string", "int", "float", "var"].contains(lexem.type)){
        output.push(lexem)
      } else if(["lt", "lte", "gt", "gte", "eq", "neq", "not", "and", "or" ].contains(lexem.type)) {

        def cond = { ->
          if(ops.size() > 0){
          }
          ops.size() > 0 &&
          ops.last().type != "lp" &&
          (( precedence[ops.last().type] > precedence[lexem.type]) ||
            (precedence[ops.last().type] == precedence[lexem.type] && associativity[ops.last().type] == "left" ))
        }
        while(cond()){
          output.push(ops.pop())
        }
        ops.push(lexem)
      } else if (lexem.type == "lp"){
        ops.push(lexem)
      } else if (lexem.type == "rp"){
        // TODO ensure that there a balanced paren
        while(ops.size() > 0  && ops.last().type != "lp"){
          output.push(ops.pop())
        }
        if(ops.size() == 0){
          def msg = """
          Unbalanced parenthesis ending on character $lexem.start
          ${exp}
          ${"^".padLeft(lexem.start+1)}
          """.stripIndent().trim()
          throw new RuntimeException(msg)
        }
        ops.pop()
      } else {
        throw new RuntimeException("Unkown lexem type: ${lexem.type}")
      }
    }
    if(ops.any({ op -> op.type=="lp"}) && ops.every({op -> op.type != "rp"})){
      def token = ops.find({op -> op.type == "lp"})
      def msg = """
      Unbalanced parenthesis starting on character $token.start
      ${exp}
      ${"^".padLeft(token.start)}
      """.stripIndent().trim()
      throw new RuntimeException(msg)
    }

    output = output + ops.reverse()
    output
  }

  def buildAST(List lexemsInRPN, String exp){

    def output = []
    for(def lexem : lexemsInRPN){
      if (["string", "int", "float", "var"].contains(lexem.type)){
        output.push(lexem)
      } else if(["lt", "lte", "gt", "gte", "eq", "neq", "and", "or" ].contains(lexem.type)) {
        if(output.size()< 2){

          def msg = """
          Missing operand around '$lexem.value' on character $lexem.start
          $exp
          ${" ".multiply(lexem.start)}${"^".multiply(lexem.value.size())}
          """.stripIndent().trim()
          throw new RuntimeException(msg)
        }
        def rhs = output.pop()
        def lhs = output.pop()
        lexem.children = [lhs, rhs]
        output.push(lexem)
      } else if (lexem.type== "not"){
        if(output.size() < 1) {
          def msg = """
          Missing operand next to '$lexem.value' on character $lexem.start
          $exp
          ${" ".multiply(lexem.start)}${"^".multiply(lexem.value.size())}
          """.stripIndent().trim()
          throw new RuntimeException(msg)
        }
        lexem.children = output.pop()
        output.push(lexem)
      }
    }
    output[0]
  }
}
