package org.code3.simpleELParser;


// EXP: '(' TERM OP TERM ')' | TERM OP TERM | TERM
// OP: '&&' | '||' | '<' | '<=' | '>' | '>='
// TERM: '!' VAL | VAL
// VAL: VAR | CONST
// VAR: /^[a-zA-z]\w*$/
// CONST: INT | FLOAT | STRING
// INT: /\d+/
// FLOAT: /\d\.\d+/
// STRING: /\".*\"/

class Tokenizer {
  // ----------------------HELPERS----------------------------------------------
  def private notToken(){return [0, null]}
  def private tokenizeCharacter(type, value, input, current){
    value == input[current] ? [1, [type: type, value: value, start: current]] : notToken()
  }
  def private tokenizeExactString(type, value, input, current){
    def size = value.size()
    def canContainsToken = input.size() >= (current + size)
    if(!canContainsToken){
      return notToken()
    }
    value == input.substring(current, current+size) ? [size, [type: type, value: value, start: current]] : notToken()
  }

  def private tokenizePattern(type, pattern, input, current) {
    def consumedCh = input.size() - current
    def str = input.substring(current, current + consumedCh)

    while(consumedCh > 0 && !(str ==~ pattern)){
      consumedCh--
      str = input.substring(current, current+consumedCh)
    }

    if( str =~ pattern ){
      return [consumedCh, [type: type, value: str, start: current]]
    } else {
      return notToken()
    }

  }
  // ----------------------HELPERS----------------------------------------------

  def private tokenizeOpenPar(input, current) {
    tokenizeCharacter('lp', '(', input, current)
  }
  def private tokenizeClosePar(input, current) {
    tokenizeCharacter('rp', ')', input, current)
  }
  def private tokenizeNot(input, current) {
    tokenizeCharacter('not', '!', input, current)
  }
  def private tokenizeLt(input, current) {
    tokenizeCharacter('lt', '<', input, current)
  }
  def private tokenizeGt(input, current) {
    tokenizeCharacter('gt', '>', input, current)
  }
  def private tokenizeFloat(input, current) {
    tokenizePattern('float', /^-?\d+\.\d+/ ,input, current)
  }
  def private tokenizeInterger(input, current) {
    tokenizePattern('int', /^-?\d+/ ,input, current)
  }
  def private tokenizeString(input, current) {
    tokenizePattern('string', /^\".*\"/, input, current)
  }
  def private tokenizeVariable(input, current) {
    tokenizePattern('var', /^[a-zA-z]*$/, input, current)
  }
  def private tokenizeAnd(input, current){
    tokenizeExactString('and', "&&", input, current)
  }
  def private tokenizeOr(input, current){
    tokenizeExactString('or', "||", input, current)
  }
  def private tokenizeGte(input, current){
    tokenizeExactString('gte', ">=", input, current)
  }
  def private tokenizeLte(input, current){
    tokenizeExactString('lte', "<=", input, current)
  }
  def private tokenizeEq(input, current){
    tokenizeExactString('eq', "==", input, current)
  }
  def private tokenizeNEq(input, current){
    tokenizeExactString('neq', "!=", input, current)
  }
  def private skipWhiteSpaces(input, current){

    def matcher = input.substring(current) =~ /^\s+/
    if(matcher){
      return [matcher[0].size(), null]
    }
    return [0, null]
  }

  def tokenize(String input){
    def tokenizers = [
      this.&skipWhiteSpaces,
      this.&tokenizeOpenPar,
      this.&tokenizeClosePar,
      this.&tokenizeEq,
      this.&tokenizeNEq,
      this.&tokenizeOr,
      this.&tokenizeAnd,
      this.&tokenizeLte,
      this.&tokenizeLt,
      this.&tokenizeGte,
      this.&tokenizeGt,
      this.&tokenizeNot,
      this.&tokenizeVariable,
      this.&tokenizeFloat,
      this.&tokenizeInterger,
      this.&tokenizeString

    ]
    def current = 0
    def tokens = []

    while(current < input.size()){
      def tokenizer = tokenizers.find {tokenizer ->
        def (nbChConsumed, token) = tokenizer(input, current)
        nbChConsumed > 0
      }
      if(!tokenizer) {
        def msg = """
        Unrecognized character \"${input[current]}\"  on character $current
        ${input}
        ${"^".padLeft(current)}
        """.stripIndent().trim()
        throw new RuntimeException(msg)
      }
      else {
        def (nbChConsumed, token) = tokenizer(input, current)
        current = current + nbChConsumed
        if(token){
          tokens.push(token)
        }

      }
    }
    tokens
  }
}
