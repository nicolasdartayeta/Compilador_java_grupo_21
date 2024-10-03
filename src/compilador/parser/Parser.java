//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
    package compilador.parser;
    import compilador.lexer.Lexer;
    import compilador.lexer.token.*;
    import compilador.lexer.TablaSimbolos;
    import compilador.lexer.CampoTablaSimbolos;
    import compilador.lexer.TablaToken;

//#line 25 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IDENTIFICADOR_GENERICO=257;
public final static short IDENTIFICADOR_FUN=258;
public final static short IDENTIFICADOR_TIPO=259;
public final static short IDENTIFICADOR_ULONGINT=260;
public final static short IDENTIFICADOR_SINGLE=261;
public final static short CONSTANTE_DECIMAL=262;
public final static short CONSTANTE_OCTAL=263;
public final static short CONSTANTE_SINGLE=264;
public final static short SUMA=265;
public final static short RESTA=266;
public final static short MULTIPLICACION=267;
public final static short DIVISION=268;
public final static short ASIGNACION=269;
public final static short MAYOR_O_IGUAL=270;
public final static short MENOR_O_IGUAL=271;
public final static short MAYOR=272;
public final static short MENOR=273;
public final static short IGUAL=274;
public final static short DESIGUAL=275;
public final static short CORCHETE_L=276;
public final static short CORCHETE_R=277;
public final static short PARENTESIS_L=278;
public final static short PARENTESIS_R=279;
public final static short COMA=280;
public final static short PUNTO=281;
public final static short PUNTO_Y_COMA=282;
public final static short INLINE_STRING=283;
public final static short ERROR=284;
public final static short STRUCT=285;
public final static short FOR=286;
public final static short UP=287;
public final static short DOWN=288;
public final static short SINGLE=289;
public final static short ULONGINT=290;
public final static short IF=291;
public final static short THEN=292;
public final static short ELSE=293;
public final static short BEGIN=294;
public final static short END=295;
public final static short END_IF=296;
public final static short OUTF=297;
public final static short TYPEDEF=298;
public final static short FUN=299;
public final static short RET=300;
public final static short TOS=301;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    3,    8,    5,
    5,    5,    9,    9,    6,    6,   10,   10,   11,   11,
   11,   12,   12,    7,   13,   15,   14,   14,   14,   14,
   16,   16,   16,   16,   16,   20,   21,   18,   26,   26,
   27,   23,   23,   28,   28,    4,    4,    4,    4,   17,
   29,   32,   32,   34,   25,   35,   35,   35,   35,   35,
   35,   33,   33,   36,   36,   19,   19,   30,   22,   37,
   37,   38,   38,   38,   38,   31,   31,   24,   24,   39,
   39,   39,   40,   40,   40,   41,   41,   41,   41,   42,
   42,   42,   43,   44,
};
final static short yylen[] = {                            2,
    4,    2,    1,    1,    1,    3,    1,    1,   10,    1,
    1,    1,    3,    1,    3,    1,    1,    1,    1,    1,
    1,    3,    3,    4,    6,    2,    2,    2,    1,    1,
    2,    2,    2,    1,    1,    2,    5,    7,    2,    1,
    2,    4,    1,    2,    1,    2,    2,    2,    1,    3,
    7,    2,    1,    2,    3,    1,    1,    1,    1,    1,
    1,    4,    1,    2,    1,    4,    4,    2,    8,    3,
    3,    2,    2,    2,    2,    3,    1,    4,    1,    3,
    3,    1,    3,    3,    1,    1,    1,    2,    1,    1,
    1,    1,    4,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   19,   12,   20,   21,    0,   11,   10,
    0,    0,    0,    0,    3,    4,    5,    0,    0,    7,
    8,   16,    0,   18,    0,    0,    0,    0,    0,   49,
    0,    0,    0,    0,    1,    2,    0,    0,    0,    0,
    0,    0,   46,   48,    0,   63,   68,   47,    0,    0,
    0,   90,   91,   92,    0,    0,   86,    0,    0,   79,
    0,   85,   87,   89,    0,    0,    0,    0,    6,    0,
    0,   15,    0,   22,    0,    0,   30,    0,   29,    0,
    0,    0,   34,   35,    0,   65,    0,    0,    0,    0,
   88,    0,    0,    0,   58,   59,   56,   57,   60,   61,
    0,    0,    0,    0,   66,   67,   14,    0,    0,    0,
    0,    0,   24,   28,   27,   31,   32,   33,    0,   43,
   36,    0,   64,   70,   71,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   83,   84,    0,    0,    0,    0,
    0,    0,    0,   45,    0,   62,    0,   93,   78,    0,
    0,    0,   13,   26,   25,    0,    0,    0,   44,    0,
    0,    0,   51,    0,   52,    0,    0,   37,   42,   72,
   73,   75,   74,   69,   54,    0,    0,    0,    0,    0,
   39,   38,    9,   41,
};
final static short yydgoto[] = {                          2,
   14,   15,   16,   46,   18,   19,   20,   21,  108,   22,
   23,   24,   25,   78,  140,  120,   26,   81,   27,   83,
   84,   28,  121,   58,   59,  178,  181,  145,   29,   30,
   71,  150,   47,  165,  101,   87,   50,  162,   60,   61,
   62,   63,   64,  128,
};
final static short yysindex[] = {                      -249,
 -279,    0,  -24,    0,    0,    0,    0, -245,    0,    0,
 -216, -213, -211,  -66,    0,    0,    0, -163, -255,    0,
    0,    0, -196,    0, -206, -190, -183,  -46, -180,    0,
 -140, -205, -234, -167,    0,    0, -138, -232, -205, -140,
 -140, -129,    0,    0,   -7,    0,    0,    0, -159, -153,
 -127,    0,    0,    0, -115, -124,    0,  -86, -107,    0,
 -144,    0,    0,    0, -102, -244, -189, -122,    0, -123,
 -146,    0, -196,    0,  -95,  -88,    0, -145,    0,  -84,
  -77,  -73,    0,    0,  -94,    0,  -14, -104, -205, -205,
    0, -205,  124,  124,    0,    0,    0,    0,    0,    0,
 -205,  -96,  124,  124,    0,    0,    0, -185, -189, -205,
 -205, -205,    0,    0,    0,    0,    0,    0,  -79,    0,
    0,  -63,    0,    0,    0,  -60, -123,  -51, -123,  -45,
 -144, -144, -123,  -46,    0,    0,  -35, -189, -140,  -37,
 -123,  -30, -157,    0,  -87,    0, -112,    0,    0,  -52,
  -34, -140,    0,    0,    0,  -32,  -27,  -25,    0,  -98,
  -61,  -23,    0,  -46,    0, -246,  -94,    0,    0,    0,
    0,    0,    0,    0,    0,   -5,  -29,  -33,  -13,  -94,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   26,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   62,    0,    0,    0,    0,    0,    0,    0,    0, -169,
  -12,    0,   44,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -18,    0,    0,  -49,
   80,   98, -219,    0,    0,    0,    0,    0,    0,    0,
 -147,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -28,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -20,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  216,  -36,    6,  -62,  -15,    0,    0,    0,  -21,
  217,  230,    0,    0,    0,  -38,  -42,    0,  -41,    0,
    0,  -40, -126,  -26,  -72,    0,    0,    0,    0,    0,
    0,    0, -118,    0,    0,    0,    0,    0,  183,  133,
  135,  223,    0,    0,
};
final static int YYTABLESIZE=390;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         80,
   82,   85,   38,   79,  107,   77,   66,    1,   17,   49,
   57,   57,   70,   39,    3,  151,  126,   57,   72,   17,
   93,   94,    4,   51,   40,    6,    7,   52,   53,   54,
  176,   55,   31,   40,  106,   80,   82,   85,  142,  115,
  177,  114,   80,   82,   85,  175,  139,   40,   65,   69,
   86,    4,   51,  184,    6,    7,   52,   53,   54,   55,
   55,   32,   55,  127,   33,  129,   56,   57,   57,    5,
   57,   57,   57,   34,  133,  153,   80,   82,   85,   57,
  144,   57,   57,  141,   41,  143,  137,   42,   57,   57,
   57,   43,  123,    4,  138,   56,    6,    7,   44,    9,
   10,   48,   80,   82,   85,   67,  159,   93,   94,   88,
   77,    4,   77,    5,    6,    7,    4,  154,   68,    6,
    7,  157,  103,  104,   80,   82,   85,    4,   89,    5,
    6,    7,   76,  110,   76,   37,  166,   80,   82,   85,
    8,   93,   94,    9,   10,   75,   52,   53,   54,  113,
   90,   12,   13,   92,   76,  109,    8,  124,  125,    9,
   10,   75,    4,  170,  171,    6,    7,   12,   13,    4,
   76,  102,    6,    7,  160,  161,  105,    4,   93,   94,
    6,    7,  111,   95,   96,   97,   98,   99,  100,  112,
    4,    8,    5,    6,    7,  134,   75,  116,    8,  119,
  172,  173,   12,   75,  117,   76,    8,  158,  118,   12,
    4,   75,   76,    6,    7,   79,   79,   12,  146,    8,
   76,  147,    9,   10,   11,  131,  132,  148,   35,   36,
   12,   13,    4,  149,    5,    6,    7,  135,  136,    8,
  152,  155,    4,  163,   11,    6,    7,   45,  156,    4,
   12,  179,    6,    7,  168,  174,  169,   73,  164,  167,
   94,    8,  182,  180,    9,   10,   11,   53,  183,   50,
   74,    8,   12,   13,  130,   40,   11,   91,    8,    0,
  122,    0,   12,   11,    0,    0,    0,    0,    0,   12,
   17,   17,   17,   17,   17,   17,   17,   17,   17,   17,
   17,    0,   17,    0,   17,   17,    0,   17,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,    0,
   23,    0,   23,   23,    0,   23,   82,   82,    0,    0,
    0,   82,   82,   82,   82,   82,   82,    0,    0,    0,
   82,   82,    0,   82,   80,   80,    0,    0,    0,   80,
   80,   80,   80,   80,   80,    0,    0,    0,   80,   80,
    0,   80,   81,   81,    0,    0,    0,   81,   81,   81,
   81,   81,   81,    0,    0,    0,   81,   81,    0,   81,
    4,   51,    0,    6,    7,   52,   53,   54,    0,   55,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         42,
   42,   42,   18,   42,   67,   42,   33,  257,    3,   31,
   32,   33,   39,  269,  294,  134,   89,   39,   40,   14,
  265,  266,  257,  258,  280,  260,  261,  262,  263,  264,
  277,  266,  278,  280,  279,   78,   78,   78,  111,   78,
  167,   78,   85,   85,   85,  164,  109,  280,  283,  282,
   45,  257,  258,  180,  260,  261,  262,  263,  264,  279,
  266,  278,  282,   90,  278,   92,  301,   89,   90,  259,
   92,   93,   94,  285,  101,  138,  119,  119,  119,  101,
  119,  103,  104,  110,  281,  112,  272,  294,  110,  111,
  112,  282,   87,  257,  280,  301,  260,  261,  282,  289,
  290,  282,  145,  145,  145,  273,  145,  265,  266,  269,
  280,  257,  282,  259,  260,  261,  257,  139,  257,  260,
  261,  279,  267,  268,  167,  167,  167,  257,  282,  259,
  260,  261,  280,  280,  282,  299,  152,  180,  180,  180,
  286,  265,  266,  289,  290,  291,  262,  263,  264,  295,
  278,  297,  298,  278,  300,  278,  286,  262,  263,  289,
  290,  291,  257,  262,  263,  260,  261,  297,  298,  257,
  300,  279,  260,  261,  287,  288,  279,  257,  265,  266,
  260,  261,  278,  270,  271,  272,  273,  274,  275,  278,
  257,  286,  259,  260,  261,  292,  291,  282,  286,  294,
  262,  263,  297,  291,  282,  300,  286,  295,  282,  297,
  257,  291,  300,  260,  261,  265,  266,  297,  282,  286,
  300,  282,  289,  290,  291,   93,   94,  279,  295,   14,
  297,  298,  257,  279,  259,  260,  261,  103,  104,  286,
  276,  279,  257,  296,  291,  260,  261,  294,  279,  257,
  297,  257,  260,  261,  282,  279,  282,   41,  293,  292,
  279,  286,  296,  293,  289,  290,  291,  296,  282,  282,
   41,  286,  297,  298,   92,  296,  291,   55,  286,   -1,
  295,   -1,  297,  291,   -1,   -1,   -1,   -1,   -1,  297,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,   -1,  277,   -1,  279,  280,   -1,  282,  265,  266,
  267,  268,  269,  270,  271,  272,  273,  274,  275,   -1,
  277,   -1,  279,  280,   -1,  282,  265,  266,   -1,   -1,
   -1,  270,  271,  272,  273,  274,  275,   -1,   -1,   -1,
  279,  280,   -1,  282,  265,  266,   -1,   -1,   -1,  270,
  271,  272,  273,  274,  275,   -1,   -1,   -1,  279,  280,
   -1,  282,  265,  266,   -1,   -1,   -1,  270,  271,  272,
  273,  274,  275,   -1,   -1,   -1,  279,  280,   -1,  282,
  257,  258,   -1,  260,  261,  262,  263,  264,   -1,  266,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=301;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"IDENTIFICADOR_GENERICO","IDENTIFICADOR_FUN",
"IDENTIFICADOR_TIPO","IDENTIFICADOR_ULONGINT","IDENTIFICADOR_SINGLE",
"CONSTANTE_DECIMAL","CONSTANTE_OCTAL","CONSTANTE_SINGLE","SUMA","RESTA",
"MULTIPLICACION","DIVISION","ASIGNACION","MAYOR_O_IGUAL","MENOR_O_IGUAL",
"MAYOR","MENOR","IGUAL","DESIGUAL","CORCHETE_L","CORCHETE_R","PARENTESIS_L",
"PARENTESIS_R","COMA","PUNTO","PUNTO_Y_COMA","INLINE_STRING","ERROR","STRUCT",
"FOR","UP","DOWN","SINGLE","ULONGINT","IF","THEN","ELSE","BEGIN","END","END_IF",
"OUTF","TYPEDEF","FUN","RET","TOS",
};
final static String yyrule[] = {
"$accept : programa",
"programa : IDENTIFICADOR_GENERICO BEGIN sentencias END",
"sentencias : sentencias sentencia",
"sentencias : sentencia",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencia_declarativa : tipo lista_de_identificadores PUNTO_Y_COMA",
"sentencia_declarativa : funcion",
"sentencia_declarativa : struct",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"tipo : ULONGINT",
"tipo : SINGLE",
"tipo : IDENTIFICADOR_TIPO",
"lista_de_tipos : lista_de_tipos COMA tipo",
"lista_de_tipos : tipo",
"lista_de_identificadores : lista_de_identificadores COMA identificador",
"lista_de_identificadores : identificador",
"identificador : identificador_simple",
"identificador : identificador_compuesto",
"identificador_simple : IDENTIFICADOR_GENERICO",
"identificador_simple : IDENTIFICADOR_ULONGINT",
"identificador_simple : IDENTIFICADOR_SINGLE",
"identificador_compuesto : identificador_simple PUNTO identificador_compuesto",
"identificador_compuesto : identificador_simple PUNTO identificador_simple",
"funcion : encabezado_funcion BEGIN cuerpo_funcion END",
"encabezado_funcion : tipo FUN IDENTIFICADOR_GENERICO PARENTESIS_L parametro PARENTESIS_R",
"parametro : tipo identificador",
"cuerpo_funcion : cuerpo_funcion sentencia_ejecutable_en_funcion",
"cuerpo_funcion : cuerpo_funcion sentencia_declarativa",
"cuerpo_funcion : sentencia_ejecutable_en_funcion",
"cuerpo_funcion : sentencia_declarativa",
"sentencia_ejecutable_en_funcion : sentencia_asignacion PUNTO_Y_COMA",
"sentencia_ejecutable_en_funcion : sentencia_seleccion_en_funcion PUNTO_Y_COMA",
"sentencia_ejecutable_en_funcion : sentencia_salida PUNTO_Y_COMA",
"sentencia_ejecutable_en_funcion : sentencia_control_en_funcion",
"sentencia_ejecutable_en_funcion : sentencia_retorno",
"sentencia_control_en_funcion : encabezado_for bloque_de_sent_ejecutables_en_funcion",
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF",
"cuerpo_if_en_funcion : bloque_de_sent_ejecutables_en_funcion bloque_else_en_funcion",
"cuerpo_if_en_funcion : bloque_de_sent_ejecutables_en_funcion",
"bloque_else_en_funcion : ELSE bloque_de_sent_ejecutables_en_funcion",
"bloque_de_sent_ejecutables_en_funcion : BEGIN sentencias_ejecutable_en_funcion END PUNTO_Y_COMA",
"bloque_de_sent_ejecutables_en_funcion : sentencia_ejecutable_en_funcion",
"sentencias_ejecutable_en_funcion : sentencias_ejecutable_en_funcion sentencia_ejecutable_en_funcion",
"sentencias_ejecutable_en_funcion : sentencia_ejecutable_en_funcion",
"sentencia_ejecutable : sentencia_asignacion PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_seleccion PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_salida PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_control",
"sentencia_asignacion : lista_de_identificadores ASIGNACION lista_de_expresiones",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF",
"cuerpo_if : bloque_de_sent_ejecutables bloque_else",
"cuerpo_if : bloque_de_sent_ejecutables",
"bloque_else : ELSE bloque_de_sent_ejecutables",
"condicion : expresion comparador expresion",
"comparador : MAYOR",
"comparador : MENOR",
"comparador : MAYOR_O_IGUAL",
"comparador : MENOR_O_IGUAL",
"comparador : IGUAL",
"comparador : DESIGUAL",
"bloque_de_sent_ejecutables : BEGIN sentencias_ejecutables END PUNTO_Y_COMA",
"bloque_de_sent_ejecutables : sentencia_ejecutable",
"sentencias_ejecutables : sentencias_ejecutables sentencia_ejecutable",
"sentencias_ejecutables : sentencia_ejecutable",
"sentencia_salida : OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R",
"sentencia_salida : OUTF PARENTESIS_L expresion PARENTESIS_R",
"sentencia_control : encabezado_for bloque_de_sent_ejecutables",
"encabezado_for : FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion PARENTESIS_R",
"asignacion_enteros : identificador ASIGNACION CONSTANTE_DECIMAL",
"asignacion_enteros : identificador ASIGNACION CONSTANTE_OCTAL",
"accion : UP CONSTANTE_DECIMAL",
"accion : UP CONSTANTE_OCTAL",
"accion : DOWN CONSTANTE_OCTAL",
"accion : DOWN CONSTANTE_DECIMAL",
"lista_de_expresiones : lista_de_expresiones COMA expresion",
"lista_de_expresiones : expresion",
"expresion : TOS PARENTESIS_L expresion_aritmetica PARENTESIS_R",
"expresion : expresion_aritmetica",
"expresion_aritmetica : expresion SUMA termino",
"expresion_aritmetica : expresion RESTA termino",
"expresion_aritmetica : termino",
"termino : termino MULTIPLICACION factor",
"termino : termino DIVISION factor",
"termino : factor",
"factor : identificador",
"factor : constante",
"factor : RESTA constante",
"factor : invocacion_a_funcion",
"constante : CONSTANTE_DECIMAL",
"constante : CONSTANTE_OCTAL",
"constante : CONSTANTE_SINGLE",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L parametro_real PARENTESIS_R",
"parametro_real : expresion",
};

//#line 248 "gramatica.y"
private static Lexer lex;

public static void main(String[] args) {
    Lexer lexer = new Lexer("src/programa_sin_funciones.txt");
    Parser.lex = lexer;
    Parser parser = new Parser(true);
    parser.run();
    TablaSimbolos.imprimirTabla();
}

private int yylex() {
    Token tok = lex.getNextToken();
    yylval = new ParserVal(tok);
    return tok.getTokenID();
}

private void yyerror(String string) {
  throw new UnsupportedOperationException("ERROR");
}
//#line 478 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 6:
//#line 29 "gramatica.y"
{ System.out.println("Declaracion de VARIABLES detectada"); }
break;
case 7:
//#line 30 "gramatica.y"
{ System.out.println("Linea " + val_peek(0).ival + ": " + "Declaracion de FUNCION detectada"); }
break;
case 8:
//#line 31 "gramatica.y"
{ System.out.println("Linea " + val_peek(0).ival + ": " + "Declaracion de STRUCT detectada"); }
break;
case 9:
//#line 34 "gramatica.y"
{
                                                                                                                                                                yyval.ival = ((Token) val_peek(9).obj).getNumeroDeLinea();
                                                                                                                                                                String lexema = ((Token) val_peek(1).obj).getLexema().toString();
                                                                                                                                                                TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                                }
break;
case 10:
//#line 41 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 11:
//#line 42 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 12:
//#line 43 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 16:
//#line 51 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 17:
//#line 54 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 18:
//#line 55 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 19:
//#line 58 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 20:
//#line 59 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 21:
//#line 60 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 22:
//#line 63 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 23:
//#line 64 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 24:
//#line 67 "gramatica.y"
{ yyval.ival = val_peek(3).ival; }
break;
case 25:
//#line 70 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                        }
break;
case 31:
//#line 85 "gramatica.y"
{ System.out.println("Linea " + val_peek(1).ival + ": " + "ASIGNACION detectada"); }
break;
case 32:
//#line 86 "gramatica.y"
{ System.out.println("Linea " + val_peek(1).ival + ": " + "Sentencia IF detectada"); }
break;
case 33:
//#line 87 "gramatica.y"
{ System.out.println("Linea " + val_peek(1).ival + ": " + "Sentencia de SALIDA detectada"); }
break;
case 34:
//#line 88 "gramatica.y"
{ System.out.println("Linea " + val_peek(0).ival + ": " + "Sentencia FOR detectada"); }
break;
case 36:
//#line 92 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 38:
//#line 98 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 46:
//#line 116 "gramatica.y"
{ System.out.println("Linea " + val_peek(1).ival + ": " + "ASIGNACION detectada"); }
break;
case 47:
//#line 117 "gramatica.y"
{ System.out.println("Linea " + val_peek(1).ival + ": " + "Sentencia IF detectada"); }
break;
case 48:
//#line 118 "gramatica.y"
{ System.out.println("Linea " + val_peek(1).ival + ": " + "Sentencia de SALIDA detectada"); }
break;
case 49:
//#line 119 "gramatica.y"
{ System.out.println("Linea " + val_peek(0).ival + ": " + "Sentencia FOR detectada"); }
break;
case 50:
//#line 122 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 51:
//#line 125 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 66:
//#line 154 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 67:
//#line 155 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 68:
//#line 158 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 69:
//#line 161 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); }
break;
case 88:
//#line 194 "gramatica.y"
{
                                                    String lexema = ((Token) val_peek(0).obj).getLexema();
                                                    String tipoConstante = ((Token) val_peek(0).obj).getTokenName();

                                                    int cantidadDeUsos = TablaSimbolos.getCantidadDeUsos(lexema);
                                                    String tipoLexemaOriginal = TablaSimbolos.getTipo(lexema);

                                                    if (cantidadDeUsos > 1) {
                                                        /* Bajar la cantidad de usos en 1*/
                                                        TablaSimbolos.decrementarUso(lexema);
                                                    } else {
                                                        /* Eliminar la entrada*/
                                                        TablaSimbolos.eliminarLexema(lexema);
                                                    }

                                                    String lexemaNegativo = "-" + lexema;

                                                    /* Chequear rango*/
                                                    if (TablaToken.CONSTANTE_DECIMAL.equals(tipoConstante)) {
                                                        /* long numero = Long.parseUnsignedLong(lexemaNegativo);*/
                                                    } else if (TablaToken.CONSTANTE_OCTAL.equals(tipoConstante)) {
                                                        /* long numero = Long.parseUnsignedLong(lexemaNegativo,8);*/
                                                    } else if (TablaToken.CONSTANTE_SINGLE.equals(tipoConstante)) {
                                                        float numero = Float.parseFloat(lexemaNegativo.replace('s','e'));
                                                        if (numero == Float.POSITIVE_INFINITY || numero == Float.NEGATIVE_INFINITY || numero == -0.0f) {
                                                            System.out.println(numero);
                                                            throw new NumberFormatException("Linea " + ((Token) val_peek(0).obj).getNumeroDeLinea() + ": la constante se va de rango");
                                                        }
                                                    }

                                                    /* Hay que fijarse si ya esta la negativa en la tabla, sino agregarla como negativa.*/
                                                    if (TablaSimbolos.existeLexema(lexemaNegativo)) {
                                                        TablaSimbolos.aumentarUso(lexemaNegativo);
                                                    } else {
                                                        TablaSimbolos.agregarLexema(lexemaNegativo, new CampoTablaSimbolos(false, tipoLexemaOriginal));
                                                        TablaSimbolos.aumentarUso(lexemaNegativo);
                                                    }
                                                }
break;
case 90:
//#line 235 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 91:
//#line 236 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 92:
//#line 237 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
//#line 820 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
