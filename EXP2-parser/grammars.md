```
<program> ::= <class_list>
<class_list> ::= <class_list> <class>
| <class>
<class> ::= CLASS TYPEID <inheritance> LBRACE <feature_list_opt> RBRACE SEMI
<inheritance> ::= INHERITS TYPEID
| <empty>

<feature_list_opt> ::= <feature_list>
| <empty>
<feature_list> ::= <feature_list> <feature>
| <feature>

<feature> ::= <attribute>
| <method>

<attribute> ::= OBJECTID COLON TYPEID assign_opt SEMI
<assign_opt> ::= ASSIGN <expression>
| <empty>

<method> ::= OBJECTID LPAREN <formal_list_opt> RPAREN COLON TYPEID LBRACE <expression> RBRACE SEMI
<formal_list_opt> ::= <formal_list>
| <empty>
<formal_list> ::= <formal_list> , <formal>
| <formal>
<formal> ::= OBJECTID COLON TYPEID


<expression> ::= <constant>
| OBJECTID <assign_opt>
| <dispatch>
| IF <expression> THEN <expression> ELSE <expression> FI
| WHILE <expression> LOOP <expression> POOL
| LBRACE <expressions> RBRACE
| <let_expression>
| <case_expression>
| NEW TYPEID
| ISVOID <expression>
| <arith>
| <comp>
| LPAREN <expression> RPAREN


<constant> ::= STR_CONST
| INT_CONST
| BOOL_CONST

<dispatch> ::= <expression> DOT OBJECTID LPAREN <expression_list> RPAREN
| OBJECTID LPAREN <expression_list> RPAREN
| <expression> AT TYPEID DOT OBJECTID LPAREN <expression_list> RPAREN

<expression_list> ::= <expression> COMMA <expression_list>
| <expression>

<expressions> ::= <expressions> <expression> SEMI
| <expression> SEMI

<let_expression> ::= LET <nest_lets> IN <expression>
<nest_lets> ::= <formal> <assign_opt> COMMA <nest_lets> 
| <formal> <assign_opt>

<case_expression> ::= CASE <expression> OF <cases> ESAC
<cases> ::= <case_branch>
| <case_branch> cases
<case_branch> ::= <formal> DARROW <expression> SEMI

<arith> ::= <expression> PLUS <expression>
| <expression> MINUS <expression>
| <expression> MULT <expression>
| <expression> DIV <expression>
| NEG <expression>

<comp> ::= <expression> LT <expression>
| <expression> LE <expression>
| <expression> EQ <expression>
| NOT <expression>
```