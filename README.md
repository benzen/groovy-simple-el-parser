groovy-simple-el-parser
================================================================================


A very basic parser of expression language that is intended to work with groovy's implementation of magery ()

This parser don't produce an executable result, because the intended target is a template environement.
This parser only produce an standardized AST. The reason to normalize this AST is to
help easly build a common understanding of expression, particulary in regard with operator priority.
This is why we used js normal priority for this language.



Install
--------------------------------------------------------------------------------

Maven dependency

```
<dependency>
  <group>org.code3<group>
  <artifactId>groovy-simple-el-parser</artifactId>
  <version>0.1</version>
</dependency>
```
Usage
--------------------------------------------------------------------------------

```groovy
import org.code3.simpleELParser.Compiler

def compiler = new Compiler()
def expression = "a || 1 == 1"
def ast = compiler.compiler(expression)
```
