# simple-el-parser-test-suite
A simple Expresion Language parser

This test suite is created in the hope to allow other to implement the same behavior in what ever language they please.
If you've created an implementation of simple-el-parser, just let me know, we'll add a reference in this repository.

This test suite will serve as a reference point.

The construct allowed by this parser are intentionally reduced.
At the moment what is supported is the following:
- variable (value defined at runtime)
- integer/float litteral
- string litteral
- `&&` ( and )
- `||` (or)
- `==` (equality)
- `!=` (not equality)
- `>` (greater than)
- `<` (lesser than)
- `>=` (greater or equal)
- `<=` (lesser or equal)

Other operator will probably be added later.

Origin
----------

At first this project was created for magery (https://github.com/caolan/magery) a templating system which is language agnotic.
This module would work on expression parsing, and magery will do what ever is usefull to create a function/interpret it based on the results.
