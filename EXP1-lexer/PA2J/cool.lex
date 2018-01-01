/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }

    int comment_depth=0;
    Boolean strTooLong=false;
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
    case BLOCK_COMMENT:
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "EOF encontered in block comment.");
    case STRING:
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "EOF encontered in string.");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

LineTerminator = \r|\n|\r\n
WhiteSpace     = [ \t\f\v\r\n]


ObjectIdentifier = [a-z][_a-zA-Z0-9]*
TypeIdentifier   = [A-Z][_a-zA-Z0-9]*
Digit = [0-9]*


%state BLOCK_COMMENT LINE_COMMENT STRING

%%

<YYINITIAL> "--"                    { yybegin(LINE_COMMENT); }
<YYINITIAL> "(*"                    { comment_depth++; yybegin(BLOCK_COMMENT); }
<YYINITIAL> "*)"                    { return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }

<LINE_COMMENT> [^\n]*               { /* ignore */ }
<LINE_COMMENT> \n                   { curr_lineno++; yybegin(YYINITIAL); }


<BLOCK_COMMENT> "(*"                { comment_depth++;   /*handle potential nesting by keeping count*/ }
<BLOCK_COMMENT> "*)"                { 
		                        comment_depth--;
		                        if(comment_depth < 0) {
		                            return new Symbol(TokenConstants.ERROR, "Unmatched *)");
		                        }else if (comment_depth == 0) {
		                            /*the nesting was balanced and we are outside block comment*/
		                            yybegin(YYINITIAL);
		                        }else {
		                            /* ignore */
                        	    }
                    }
<BLOCK_COMMENT> \n                  { curr_lineno++; }
<BLOCK_COMMENT> .                   { /* ignore. Not doing .* because maximal munch will mess things up so one at a time instead */ }


<YYINITIAL> \"                      { string_buf.setLength(0); yybegin(STRING); }   
<STRING>\"         {
                            yybegin(YYINITIAL);
                            if(string_buf.length() > MAX_STR_CONST) {
                              return new Symbol(TokenConstants.ERROR, "String constant too long");
                           }else {
                              String s = string_buf.toString();
                              string_buf.setLength(0);
                              return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(s));
                            }
                          }
<STRING>\0[^\"]*\"|\\\0[^\"]*\" { //eat up all characters after null and stop at ending quote (eat it too)
                            yybegin(YYINITIAL);
                            string_buf = new StringBuffer();
                            return new Symbol(TokenConstants.ERROR, "String contains escaped null character.");
                          }

<STRING>\\[ \t\b\f\r\x0b]*\n                        
                                    {   
                                        // escaped slash
                                        curr_lineno++;    
                                        string_buf = string_buf.append('\n');
                                    }
<STRING>\n                          {   
                                        // unescaped new line
                                        yybegin(YYINITIAL);
                                        curr_lineno++; 
                                        string_buf.setLength(0);
                                        return new Symbol(TokenConstants.ERROR, "Unterminated string constant.");
                                        
                                    }
<STRING>\015	{
                string_buf = string_buf.append('\015'); 
		}
                   
<STRING>\033	{
                 string_buf = string_buf.append('\033'); 
		}
                   
<STRING>\\.  { 
                    if (yytext().equals("\\n")) {
                        string_buf = string_buf.append('\n'); 
                    } else if (yytext().equals("\\b")){
                        string_buf = string_buf.append('\b'); 
                    } else if (yytext().equals("\\t")){
                        string_buf = string_buf.append('\t'); 
                    } else if (yytext().equals("\\f")){
                        string_buf = string_buf.append('\f'); 
                    }else {
                        string_buf = string_buf.append(yytext().charAt(1));
                    }
                }
<STRING>.       {   
                   
                     string_buf = string_buf.append(yytext().charAt(0));
                }



<YYINITIAL> {LineTerminator}        { curr_lineno++; }

<YYINITIAL> {WhiteSpace}            { /* ignore */ }


<YYINITIAL> [t][rR][uU][eE]                     { return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));  }
<YYINITIAL> [f][aA][lL][sS][eE]                 { return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }
<YYINITIAL> [cC][aA][sS][eE]                    { return new Symbol(TokenConstants.CASE);   }
<YYINITIAL> [cC][lL][aA][sS][sS]                { return new Symbol(TokenConstants.CLASS);  }
<YYINITIAL> [eE][lL][sS][eE]                    { return new Symbol(TokenConstants.ELSE);   }
<YYINITIAL> [fF][iI]                            { return new Symbol(TokenConstants.FI);     }
<YYINITIAL> [iI][fF]                            { return new Symbol(TokenConstants.IF);     }
<YYINITIAL> [iI][nN]                            { return new Symbol(TokenConstants.IN);     }
<YYINITIAL> [iI][nN][hH][eE][rR][iI][tT][sS]    { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL> [iI][sS][vV][oO][iI][dD]            { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL> [lL][eE][tT]                        { return new Symbol(TokenConstants.LET);    }
<YYINITIAL> [lL][oO][oO][pP]                    { return new Symbol(TokenConstants.LOOP);   }
<YYINITIAL> [pP][oO][oO][lL]                    { return new Symbol(TokenConstants.POOL);   }
<YYINITIAL> [tT][hH][eE][nN]                    { return new Symbol(TokenConstants.THEN);   }
<YYINITIAL> [wW][hH][iI][lL][eE]                { return new Symbol(TokenConstants.WHILE);  }
<YYINITIAL> [eE][sS][aA][cC]                    { return new Symbol(TokenConstants.ESAC);   }
<YYINITIAL> [nN][eE][wW]                        { return new Symbol(TokenConstants.NEW);    }
<YYINITIAL> [oO][fF]                            { return new Symbol(TokenConstants.OF);     }
<YYINITIAL> [nN][oO][tT]                        { return new Symbol(TokenConstants.NOT);    }


<YYINITIAL> "=>"	                            { return new Symbol(TokenConstants.DARROW); }
<YYINITIAL> "*"                                 { return new Symbol(TokenConstants.MULT);   }
<YYINITIAL> "("                                 { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL> ";"                                 { return new Symbol(TokenConstants.SEMI);   }
<YYINITIAL> "-"                                 { return new Symbol(TokenConstants.MINUS);  }
<YYINITIAL> ")"                                 { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL> "<"                                 { return new Symbol(TokenConstants.LT);     }
<YYINITIAL> ","                                 { return new Symbol(TokenConstants.COMMA);  }
<YYINITIAL> "/"                                 { return new Symbol(TokenConstants.DIV);    }
<YYINITIAL> "+"                                 { return new Symbol(TokenConstants.PLUS);   }
<YYINITIAL> "<-"                                { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL> "."                                 { return new Symbol(TokenConstants.DOT);    }
<YYINITIAL> "<="                                { return new Symbol(TokenConstants.LE);     }
<YYINITIAL> "="                                 { return new Symbol(TokenConstants.EQ);     }
<YYINITIAL> ":"                                 { return new Symbol(TokenConstants.COLON);  }
<YYINITIAL> "{"                                 { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL> "}"                                 { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL> "@"                                 { return new Symbol(TokenConstants.AT);     }
<YYINITIAL> "~"                                 { return new Symbol(TokenConstants.NEG);    }



<YYINITIAL> {ObjectIdentifier}                  { return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
<YYINITIAL> {TypeIdentifier}                    { return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
     

<YYINITIAL> {Digit}                             {
                                    return new Symbol(TokenConstants.INT_CONST,
                                                    AbstractTable.inttable.addString(yytext())); }








.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  //makSystem.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
                                  return new Symbol(TokenConstants.ERROR, yytext()); }
