groovy-simple-el-parser
================================================================================


A very basic parser of expression language that is intended to work with groovy's implementation of magery ()


Install
--------------------------------------------------------------------------------

Mavem dependecy

```
<dependecy>
  <group>org.code3<group>
  <artifactId>groovy-simple-el-parser</artifactId>
  <version>0.1</version>
</dependecy>
```
Usage
--------------------------------------------------------------------------------

```groovy
import org.code3.simpleELParser.Compiler

def compiler = new Compiler()
def expression = "a || 1 == 1"
def ast = compiler.compiler(expression)
```
