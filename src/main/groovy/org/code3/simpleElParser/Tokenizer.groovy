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
    value == input[current] ? [1, [type: type, value: value]] : notToken()
  }
  def private tokenizeExactString(type, value, input, current){
    def size = value.size()
    def canContainsToken = input.size() >= (current + size)
    if(!canContainsToken){
      return notToken()
    }
    value == input.substring(current, current+size) ? [size, [type: type, value: value]] : notToken()
  }
  def private tokenizePattern(type, pattern, input, current) {
    def consumedCh = input.size() - current
    def str =  input.substring(current, current + consumedCh)
    def value

    while(consumedCh > 0 && !(str =~ pattern)){
      consumedCh--
      str = input.substring(current, current+consumedCh)
    }
    str = input.substring(current, current+consumedCh)
    if( consumedCh > 0){
      return [consumedCh, [type: type, value: str]]
    } else {
      return notToken()
    }

  }
  // ----------------------HELPERS----------------------------------------------

  def tokenizeOpenPar(input, current) {
    tokenizeCharacter('lp', '(', input, current)
  }
  def tokenizeClosePar(input, current) {
    tokenizeCharacter('rp', ')', input, current)
  }
  def tokenizeNot(input, current) {
    tokenizeCharacter('not', '!', input, current)
  }
  def tokenizeLt(input, current) {
    tokenizeCharacter('lt', '<', input, current)
  }
  def tokenizeGt(input, current) {
    tokenizeCharacter('gt', '>', input, current)
  }
  def tokenizeFloat(input, current) {
    tokenizePattern('float', /^\d+\.\d+/ ,input, current)
  }
  def tokenizeInterger(input, current) {
    tokenizePattern('int', /^\d+/ ,input, current)
  }
  def tokenizeString(input, current) {
    tokenizePattern('string', /^\".*\"/, input, current)
  }
  def tokenizeVariable(input, current) {
    tokenizePattern('var', /^[a-zA-z]*$/, input, current)
  }
  def tokenizeAnd(input, current){
    tokenizeExactString('and', "&&", input, current)
  }
  def tokenizeOr(input, current){
    tokenizeExactString('or', "||", input, current)
  }
  def tokenizeGte(input, current){
    tokenizeExactString('gte', ">=", input, current)
  }
  def tokenizeLte(input, current){
    tokenizeExactString('lte', "<=", input, current)
  }
  def tokenizeEq(input, current){
    tokenizeExactString('eq', "==", input, current)
  }
  def tokenizeNEq(input, current){
    tokenizeExactString('neq', "!=", input, current)
  }
  def skipWhiteSpaces(input, current){

    def matcher = input.substring(current) =~ /^\s+/
    if(matcher){
      return [matcher[0].size(), null]
    }
    return [0, null]
  }
  def tokenize(String input){
    def tokenizers = [
      this.&skipWhiteSpaces,
      this.&tokenizeEq,
      this.&tokenizeNEq,
      this.&tokenizeOpenPar,
      this.&tokenizeClosePar,
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
        Unrecognize character \"${input[current]}\"  at position $current
        ${input}
        ${"".padLeft(current)}^
        """
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
