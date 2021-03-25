grammar language;

//sample code
/*
import <sample>;

  {
      class mycls :

      <
      int y = r = g ;

      Function myFunction(float afsd , int a)<
     int u = a+b*c; ;
      while(m == 5 )<
          float a = 2^3^4;
       >
          if( x == 5)<
          >
      >
       >
  }



  */


/*
parser rules
*/
compilationUnit: importDecleration*  mainBlock EOF;

importDecleration : IMPORT '<' IDENTIFIER '>' SEMI;

mainBlock : '{' classDecleration classDecleration* '}';

classDecleration: CLASS  IDENTIFIER ':' '<' classBody* '>';
classBody: memberDecleration | block;




//statement: IDENTIFIER;

statement : block
          | FOR '(' forControl ')' statement
          | WHILE '(' expression ')' statement
          | IF '(' expression ')' statement (ELSE statement)?
          | SWITCH '(' expression ')''{' switchBlockStatementGroup* switchLabel* '}'
          | ';'
          | expression
          ;


switchBlockStatementGroup
    : switchLabel+ blockStatement+
    ;

switchLabel
    : CASE (expression | IDENTIFIER) ':'
    | DEFAULT ':'
    ;

forControl : forInitialization? ';' expression ';' expressionList ;

forInitialization : fieldDecleration | expressionList;


expression: '(' expression ')'
          | literal
          | IDENTIFIER
          | functionCall
          | <assoc=right> expression '**' expression
          | prefix='~' expression
          | prefix=('+'|'-') expression
          | expression bop=('*'|'/'|'%'|'//') expression
          | expression bop=('+'|'-') expression
          | expression ('<' '<'| '>' '>') expression
          | expression bop=('&' | '^' | '|' ) expression
          | expression bop=('==' | '!=' | '<>') expression
          | expression bop=('<=' | '>=' | '>' | '<') expression
          | <assoc=right> expression
           bop=('=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '>>=' | '>>>=' | '<<=' | '%=') expression
          | expression LOGICALKEYWORDS expression
          | expression NUMBERSIGN expression
          ;


literal: number_literal
       | boolean_literal
       | NULL
       ;

expressionList
    : expression (',' expression)*
    ;

memberDecleration: functionDecleration
                 | fieldDecleration
                 | iotaDecleration
                 ;


formalParameters
    : '(' formalParameterList? ')'
    ;

fieldDecleration: typeType variableDeclarator ';';


typeType: PrimitiveType;

iotaDecleration: CONST '(' iotaBlock ')';
iotaBlock: iotaFirstVariable
           iotaVariable*
         ;
iotaFirstVariable: IDENTIFIER ASSIGN IOTA ((ADD|SUBTRACT) (INTEGER_LITERAL | IDENTIFIER))? ;
iotaVariable:DENTIFIER (ASSIGN IOTA ((ADD|SUBTRACT) (INTEGER_LITERAL | IDENTIFIER))?)?;

formalParameterList
    : formalParameter (',' formalParameter)*
    ;
formalParameter
    : typeType IDENTIFIER
    ;

block: '<' blockStatement* '>' ;

blockStatement: iotaDecleration |statement  | localVariableDeclaration;


functionDecleration: FUNCTION IDENTIFIER formalParameters (block | SEMI );
functionCall       : IDENTIFIER '(' expressionList? ')';

localVariableDeclaration : typeType variableDeclarator;

variableDeclarator
    : IDENTIFIER ('=' expression)?
    ;

 number_literal : INTEGER_LITERAL | FLOAT_LITERAL;
 boolean_literal : TRUE | FALSE;

 PrimitiveType: BOOLEAN
              | INT
              | FLOAT
              | CONST
              ;

WS:                 [ \t\r\n\u000C]+ -> channel(HIDDEN);
COMMENT:            '/*' .*? '*/'    -> channel(HIDDEN);
LINE_COMMENT:     ':-{)' ~[\r\n]*    -> channel(HIDDEN);
/*
lexer rules
*/


IMPORT:  'import';
CLASS:  'class';
FUNCTION: 'Function';
NULL: 'null';
CONST: 'const';
IOTA: 'iota';
SEMI: ';';
IF: 'if';

LOGICALKEYWORDS:  AND | OR | NOT ;

IDENTIFIER: LETTER(LETTER | [0-9])*;
LETTER: [a-z]|[A-Z];
WORD: LETTER+;

LPAREN:             '(';
RPAREN:             ')';
EXPONENTIAL:       '**';
LOGICALNOT:         '~';
ADD:                '+';
SUBTRACT:           '-';
MUL:                '*';
DIV:                '/';
MOD:                '%';
DIVNOREM:          '//';
LINECOMMENT:        '#';
LSHIFT:            '<<';
RSHIFT:            '>>';
BITOR:              '|';
BITAND:             '&';
BITXOR:             '^';
EQUAL:             '==';
NOTEQUAL:          '!=';
NUMBERSIGN:         '#';

GREATER:            '>';
LESS:               '<';
LESSE:             '<=';
GREATERE:          '>=';

ASSIGN:             '=';
ADD_ASSIGN:        '+=';
SUB_ASSIGN:        '-=';
MUL_ASSIGN:        '*=';
DIV_ASSIGN:        '/=';
MOD_ASSIGN:        '%=';
EXP_ASSIGN:       '**=';
DIV_NOREM_ASSIGN: '//=';

NOT:              'not';
AND:              'and';
OR:                'or';



FLOAT:          'float';
BOOLEAN:         'bool';
INT:              'int';



INTEGER_LITERAL : ('0' | [1-9] (DIGITS? | '_'+ DIGITS));
FLOAT_LITERAL : (DIGITS '.' DIGITS? | '.' DIGITS)  ExponentPart?;

DIGITS :  [0-9] ([0-9_]* [0-9])?;



 fragment ExponentPart
    : ('E' | 'e') ('+' | '-')? DIGITS
    ;

