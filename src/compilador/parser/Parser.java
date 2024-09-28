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
//#line 20 "Parser.java"




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
public final static short IDENTIFICADOR_ULONGINT=258;
public final static short IDENTIFICADOR_SINGLE=259;
public final static short CONSTANTE_DECIMAL=260;
public final static short CONSTANTE_OCTAL=261;
public final static short CONSTANTE_SINGLE=262;
public final static short SUMA=263;
public final static short RESTA=264;
public final static short MULTIPLICACION=265;
public final static short DIVISION=266;
public final static short ASIGNACION=267;
public final static short MAYOR_O_IGUAL=268;
public final static short MENOR_O_IGUAL=269;
public final static short MAYOR=270;
public final static short MENOR=271;
public final static short IGUAL=272;
public final static short DESIGUAL=273;
public final static short PARENTESIS_L=274;
public final static short PARENTESIS_R=275;
public final static short COMA=276;
public final static short PUNTO=277;
public final static short PUNTO_Y_COMA=278;
public final static short INLINE_STRING=279;
public final static short ERROR=280;
public final static short STRUCT=281;
public final static short FOR=282;
public final static short UP=283;
public final static short DOWN=284;
public final static short SINGLE=285;
public final static short ULONGINT=286;
public final static short IF=287;
public final static short THEN=288;
public final static short ELSE=289;
public final static short BEGIN=290;
public final static short END=291;
public final static short END_IF=292;
public final static short OUTF=293;
public final static short TYPEDEF=294;
public final static short FUN=295;
public final static short RET=296;
public final static short TOS=297;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    3,    8,    5,
    5,    9,    9,    6,    6,   10,   10,   10,    7,   11,
   12,   12,   12,   12,   13,    4,    4,    4,    4,   15,
   16,   21,   21,   23,   20,   24,   24,   24,   24,   24,
   24,   22,   22,   25,   25,   17,   17,   18,   26,   27,
   27,   28,   28,   28,   28,   19,   19,   14,   14,   29,
   29,   29,   30,   30,   30,   31,   31,   31,   31,   31,
   32,   33,
};
final static short yylen[] = {                            2,
    4,    2,    1,    1,    1,    3,    1,    2,    9,    1,
    1,    3,    1,    3,    1,    1,    1,    1,    9,    2,
    2,    2,    1,    1,    5,    2,    2,    2,    1,    3,
    7,    2,    1,    2,    3,    1,    1,    1,    1,    1,
    1,    4,    1,    2,    1,    4,    4,    2,    8,    3,
    3,    2,    2,    2,    2,    3,    1,    4,    1,    3,
    3,    1,    3,    3,    1,    1,    1,    1,    1,    1,
    4,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,   17,   18,    0,   11,   10,    0,
    0,    0,    0,    3,    4,    5,    0,    0,    7,    0,
   15,    0,    0,    0,   29,    0,    0,    0,    0,    0,
    1,    2,    0,    0,    0,    0,    8,   26,   27,   28,
    0,   43,   48,    0,    0,    0,   67,   68,   69,    0,
   66,    0,    0,   59,    0,   65,   70,    0,    0,    0,
    0,    6,    0,    0,   14,   45,    0,    0,    0,    0,
    0,    0,    0,   38,   39,   36,   37,   40,   41,    0,
    0,    0,    0,   46,   47,   13,    0,    0,    0,    0,
   44,   50,   51,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   63,   64,    0,    0,    0,    0,    0,   42,
    0,   71,   58,    0,    0,    0,   12,   20,    0,    0,
    0,    0,   31,    0,   32,    0,    0,   52,   53,   55,
   54,   49,   34,    0,    0,   23,    0,   24,    9,    0,
   19,   21,   22,    0,    0,   25,
};
final static short yydgoto[] = {                          2,
   13,   14,   15,   16,   17,   18,   19,   20,   87,   51,
  108,  137,  138,   52,   22,   23,   24,   25,   64,   53,
  114,   43,  125,   80,   67,   26,   45,  122,   54,   55,
   56,   57,   96,
};
final static short yysindex[] = {                      -237,
 -268,    0, -117,    0,    0,    0, -225,    0,    0, -197,
 -194, -183, -130,    0,    0,    0, -245, -258,    0, -157,
    0, -146, -144, -139,    0, -170, -184, -201, -231, -147,
    0,    0, -114, -179, -201, -184,    0,    0,    0,    0,
  -99,    0,    0, -121, -116, -103,    0,    0,    0, -102,
    0, -178, -101,    0, -229,    0,    0, -100, -256, -232,
  -95,    0, -195,  -96,    0,    0, -109, -198, -201, -201,
 -201,   -1,   -1,    0,    0,    0,    0,    0,    0, -201,
 -107,   -1,   -1,    0,    0,    0, -253, -232, -201,  -93,
    0,    0,    0,  -92, -195,  -88, -195,  -74, -229, -229,
 -195, -170,    0,    0,   66, -232, -184,  -71, -195,    0,
 -200,    0,    0,  -84,  -80, -184,    0,    0,  -70, -154,
 -142,  -65,    0, -170,    0, -125, -149,    0,    0,    0,
    0,    0,    0,  -40,  -50,    0, -215,    0,    0, -201,
    0,    0,    0, -223,  -53,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -73,    0,    0,    0,    0,
    0,    0,    0,    0,  -57,    0,    0,    0,    0,    0,
    0,    0, -175,  -52,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -42,    0,    0, -133,  -41,  -24,
 -243,    0,    0,    0,    0,    0,    0,    0, -165,    0,
    0,    0,    0,    0,  -56,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  -11,    0,  -20,  -55,  -14,    0,    0,    0,   -2,
    0,    0,  101,  -25,    0,    0,    0,    0,    0,  172,
    0,  -86,    0,    0,    0,    0,    0,    0,  171,   81,
   84,    0,    0,
};
final static int YYTABLESIZE=261;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        134,
   21,   32,   34,   59,   86,   42,   72,   73,   35,   63,
   21,    4,    5,    6,   21,  115,  105,   36,   85,    1,
   66,    3,  106,   21,   44,   46,    5,    6,   47,   48,
   49,   35,  107,   65,   35,   82,   83,  133,   21,   72,
   73,    4,    5,    6,   95,   97,   91,   58,   27,   33,
  117,  145,    8,    9,  101,   46,    5,    6,   47,   48,
   49,   92,   93,  109,   21,   50,    7,   72,   73,    8,
    9,   10,    4,    5,    6,  141,   28,   11,   12,   29,
  135,   42,  120,  121,   72,   73,    4,    5,    6,   74,
   75,   76,   77,   78,   79,   50,   36,   30,   62,   21,
   57,  126,   57,   42,  118,  128,  129,    4,    5,    6,
   56,    7,   56,   21,  144,  136,   10,  130,  131,   41,
   37,   21,   11,   60,   21,  142,    4,    5,    6,   59,
   59,   38,    7,   39,   21,    8,    9,   10,   40,    4,
    5,    6,   61,   11,   12,   68,  135,    4,    5,    6,
   36,    7,   99,  100,    8,    9,   10,    4,    5,    6,
   31,   69,   11,   12,    7,  103,  104,    8,    9,   10,
   70,   71,    7,   81,   84,   11,   12,   10,   88,   89,
  102,   90,    7,   11,  110,  111,  112,   10,  116,   16,
   16,   16,   16,   11,   16,   16,   16,   16,   16,   16,
  113,   16,   16,  119,   16,   62,   62,  123,  124,  132,
   62,   62,   62,   62,   62,   62,  139,   62,   62,  127,
   62,   60,   60,  140,  146,   30,   60,   60,   60,   60,
   60,   60,   72,   60,   60,   33,   60,  143,   61,   61,
   94,   98,    0,   61,   61,   61,   61,   61,   61,    0,
   61,   61,    0,   61,    0,   46,    5,    6,   47,   48,
   49,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        125,
    3,   13,   17,   29,   60,   26,  263,  264,  267,   35,
   13,  257,  258,  259,   17,  102,  270,  276,  275,  257,
   41,  290,  276,   26,   27,  257,  258,  259,  260,  261,
  262,  275,   88,   36,  278,  265,  266,  124,   41,  263,
  264,  257,  258,  259,   70,   71,   67,  279,  274,  295,
  106,  275,  285,  286,   80,  257,  258,  259,  260,  261,
  262,  260,  261,   89,   67,  297,  282,  263,  264,  285,
  286,  287,  257,  258,  259,  291,  274,  293,  294,  274,
  296,  102,  283,  284,  263,  264,  257,  258,  259,  268,
  269,  270,  271,  272,  273,  297,  276,  281,  278,  102,
  276,  116,  278,  124,  107,  260,  261,  257,  258,  259,
  276,  282,  278,  116,  140,  127,  287,  260,  261,  290,
  278,  124,  293,  271,  127,  137,  257,  258,  259,  263,
  264,  278,  282,  278,  137,  285,  286,  287,  278,  257,
  258,  259,  257,  293,  294,  267,  296,  257,  258,  259,
  276,  282,   72,   73,  285,  286,  287,  257,  258,  259,
  291,  278,  293,  294,  282,   82,   83,  285,  286,  287,
  274,  274,  282,  275,  275,  293,  294,  287,  274,  276,
  288,  291,  282,  293,  278,  278,  275,  287,  123,  263,
  264,  265,  266,  293,  268,  269,  270,  271,  272,  273,
  275,  275,  276,  275,  278,  263,  264,  292,  289,  275,
  268,  269,  270,  271,  272,  273,  257,  275,  276,  290,
  278,  263,  264,  274,  278,  278,  268,  269,  270,  271,
  272,  273,  275,  275,  276,  292,  278,  137,  263,  264,
   69,   71,   -1,  268,  269,  270,  271,  272,  273,   -1,
  275,  276,   -1,  278,   -1,  257,  258,  259,  260,  261,
  262,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=297;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,"'{'",null,
"'}'",null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,"IDENTIFICADOR_GENERICO","IDENTIFICADOR_ULONGINT",
"IDENTIFICADOR_SINGLE","CONSTANTE_DECIMAL","CONSTANTE_OCTAL","CONSTANTE_SINGLE",
"SUMA","RESTA","MULTIPLICACION","DIVISION","ASIGNACION","MAYOR_O_IGUAL",
"MENOR_O_IGUAL","MAYOR","MENOR","IGUAL","DESIGUAL","PARENTESIS_L",
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
"sentencia_declarativa : struct PUNTO_Y_COMA",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR '{' lista_de_identificadores '}' IDENTIFICADOR_GENERICO",
"tipo : ULONGINT",
"tipo : SINGLE",
"lista_de_tipos : lista_de_tipos COMA tipo",
"lista_de_tipos : tipo",
"lista_de_identificadores : lista_de_identificadores COMA identificador",
"lista_de_identificadores : identificador",
"identificador : IDENTIFICADOR_GENERICO",
"identificador : IDENTIFICADOR_ULONGINT",
"identificador : IDENTIFICADOR_SINGLE",
"funcion : tipo FUN IDENTIFICADOR_GENERICO PARENTESIS_L parametro PARENTESIS_R BEGIN cuerpo_funcion END",
"parametro : tipo identificador",
"cuerpo_funcion : cuerpo_funcion sentencia",
"cuerpo_funcion : cuerpo_funcion return",
"cuerpo_funcion : sentencia",
"cuerpo_funcion : return",
"return : RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA",
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
"factor : CONSTANTE_DECIMAL",
"factor : CONSTANTE_OCTAL",
"factor : CONSTANTE_SINGLE",
"factor : invocacion_a_funcion",
"invocacion_a_funcion : IDENTIFICADOR_GENERICO PARENTESIS_L parametro_real PARENTESIS_R",
"parametro_real : expresion",
};

//#line 155 "gramatica.y"
private static Lexer lex;

public static void main(String[] args) {
    Lexer lexer = new Lexer("C:/Users/santi/Downloads/programa1.txt");
    Parser.lex = lexer;
    Parser parser = new Parser(true);
    parser.run();
    }

private int yylex() {
    return lex.yylex();
}

private void yyerror(String string) {
  throw new UnsupportedOperationException("ERROR");
}
//#line 401 "Parser.java"
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
