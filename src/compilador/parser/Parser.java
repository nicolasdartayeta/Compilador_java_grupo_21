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
public final static short CORCHETE_L=274;
public final static short CORCHETE_R=275;
public final static short PARENTESIS_L=276;
public final static short PARENTESIS_R=277;
public final static short COMA=278;
public final static short PUNTO=279;
public final static short PUNTO_Y_COMA=280;
public final static short INLINE_STRING=281;
public final static short ERROR=282;
public final static short STRUCT=283;
public final static short FOR=284;
public final static short UP=285;
public final static short DOWN=286;
public final static short SINGLE=287;
public final static short ULONGINT=288;
public final static short IF=289;
public final static short THEN=290;
public final static short ELSE=291;
public final static short BEGIN=292;
public final static short END=293;
public final static short END_IF=294;
public final static short OUTF=295;
public final static short TYPEDEF=296;
public final static short FUN=297;
public final static short RET=298;
public final static short TOS=299;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    3,    8,    5,
    5,    9,    9,    6,    6,   10,   10,   11,   11,   11,
   12,   12,    7,   13,   15,   14,   14,   14,   16,    4,
    4,    4,    4,   18,   19,   24,   24,   26,   23,   27,
   27,   27,   27,   27,   27,   25,   25,   28,   28,   20,
   20,   21,   29,   30,   30,   31,   31,   31,   31,   22,
   22,   17,   17,   32,   32,   32,   33,   33,   33,   34,
   34,   34,   34,   34,   35,   36,
};
final static short yylen[] = {                            2,
    4,    2,    1,    1,    1,    3,    1,    2,    9,    1,
    1,    3,    1,    3,    1,    1,    1,    1,    1,    1,
    3,    3,    4,    6,    2,    3,    2,    1,    5,    2,
    2,    2,    1,    3,    7,    2,    1,    2,    3,    1,
    1,    1,    1,    1,    1,    4,    1,    2,    1,    4,
    4,    2,    8,    3,    3,    2,    2,    2,    2,    3,
    1,    4,    1,    3,    3,    1,    3,    3,    1,    1,
    1,    1,    1,    1,    4,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   18,   19,   20,    0,   11,   10,    0,
    0,    0,    0,    3,    4,    5,    0,    0,    7,    0,
   15,    0,   17,    0,    0,    0,    0,   33,    0,    0,
    0,    0,    0,    1,    2,    0,    0,    0,    0,    8,
    0,    0,   30,   31,   32,    0,   47,   52,    0,    0,
    0,   71,   72,   73,    0,   70,    0,    0,   63,    0,
   69,   74,    0,    0,    0,    0,    6,    0,    0,   14,
    0,   21,    0,    0,    0,   28,   49,    0,    0,    0,
    0,    0,    0,    0,   42,   43,   40,   41,   44,   45,
    0,    0,    0,    0,   50,   51,   13,    0,    0,    0,
    0,    0,   23,    0,   48,   54,   55,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   67,   68,    0,    0,
    0,    0,    0,    0,    0,   46,    0,   75,   62,    0,
    0,    0,   12,   25,   24,    0,    0,    0,    0,   35,
    0,   36,    0,   29,   56,   57,   59,   58,   53,   38,
    0,    9,
};
final static short yydgoto[] = {                          2,
   13,   14,   15,   16,   17,   18,   19,   20,   98,   21,
   22,   23,   24,   75,  122,   76,   57,   25,   26,   27,
   28,   69,   58,  130,   48,  142,   91,   78,   29,   50,
  139,   59,   60,   61,   62,  110,
};
final static short yysindex[] = {                      -250,
 -273,    0, -129,    0,    0,    0, -228,    0,    0, -211,
 -200, -257, -162,    0,    0,    0, -222, -233,    0, -177,
    0, -174,    0, -241, -164, -161, -159,    0, -121, -168,
 -230, -237, -139,    0,    0, -145, -180, -230, -168,    0,
 -168, -178,    0,    0,    0, -111,    0,    0, -132, -131,
 -122,    0,    0,    0, -115,    0,   30, -112,    0, -223,
    0,    0, -105, -225, -216, -107,    0, -171, -116,    0,
 -174,    0, -100, -178, -110,    0,    0, -114, -153, -230,
 -230, -230, -175, -175,    0,    0,    0,    0,    0,    0,
 -230, -126, -175, -175,    0,    0,    0, -229, -216, -230,
 -230, -129,    0, -103,    0,    0,    0,  -98, -171,  -97,
 -171,  -81, -223, -223, -171, -121,    0,    0,  -76, -216,
 -168,  -63, -171, -217, -129,    0, -146,    0,    0,  -93,
  -75, -168,    0,    0,    0,  -61, -137, -119,  -52,    0,
 -121,    0, -262,    0,    0,    0,    0,    0,    0,    0,
  -25,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -78,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -42,    0,    0,    0,    0,    0,    0,    0,    0,  -24,
    0,    0,    0,    0,    0,    0,    0, -179,  -47,    0,
  -60,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -51,    0,    0,    0,    0,    0,    0,  -43,    0,
    0, -113,   -6,   12, -263,    0,    0,    0,    0,    0,
    0,    0, -176,    0,  -50,    0,    0,    0,    0,    0,
  -53,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  -32,  -11,    0,  -28,  -59,  -17,    0,    0,    0,  -27,
  209,  210,    0,    0,    0,  178,  -23,    0,    0,    0,
    0,    0,  175,    0, -108,    0,    0,    0,    0,    0,
    0,  177,   69,   63,    0,    0,
};
final static int YYTABLESIZE=303;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   47,   35,   49,   56,   56,   97,    1,  131,   64,   74,
   56,   70,  151,   39,   68,   39,   39,   77,    3,   51,
    5,    6,   52,   53,   54,   33,   51,    5,    6,   52,
   53,   54,  150,   38,    4,    5,    6,   83,   84,  121,
  119,   93,   94,   63,   39,   83,   84,   30,  120,  105,
   42,   96,   56,   56,   56,   56,   56,  109,  111,  136,
  133,   55,   35,   56,   31,   56,   56,  115,   55,  125,
    8,    9,   56,   56,   36,   32,  123,  124,    4,    5,
    6,   51,    5,    6,   52,   53,   54,   47,    4,    5,
    6,   83,   84,  134,    4,    5,    6,   39,   61,   67,
   61,   60,   40,   60,   41,    7,  106,  107,    8,    9,
   10,   66,   47,   35,  143,   43,   11,   12,   44,   73,
   45,    7,  145,  146,    8,    9,   10,    4,    5,    6,
   34,   65,   11,   12,   79,    4,    5,    6,  137,  138,
  147,  148,    4,    5,    6,    4,    5,    6,   80,   63,
   63,  113,  114,   81,    7,  117,  118,    8,    9,   10,
   82,  100,    7,  116,   92,   11,   12,   10,   99,    7,
   46,   95,    7,   11,   10,  101,  126,   10,  104,  128,
   11,  127,  103,   11,   16,   16,   16,   16,   16,   16,
   16,   16,   16,   16,   16,  129,   16,  132,   16,   16,
  140,   16,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   22,   22,  135,   22,  141,   22,   22,  144,   22,
   18,   18,   18,   18,  149,   18,   18,   18,   18,   18,
   18,  152,   34,   76,   18,   18,   18,   18,   66,   66,
   37,   27,   26,   66,   66,   66,   66,   66,   66,   71,
   72,  102,   66,   66,  108,   66,   64,   64,  112,    0,
    0,   64,   64,   64,   64,   64,   64,    0,    0,    0,
   64,   64,    0,   64,   65,   65,    0,    0,    0,   65,
   65,   65,   65,   65,   65,    0,    0,    0,   65,   65,
    0,   65,   83,   84,    0,    0,    0,   85,   86,   87,
   88,   89,   90,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         17,
   29,   13,   30,   31,   32,   65,  257,  116,   32,   42,
   38,   39,  275,  277,   38,  278,  280,   46,  292,  257,
  258,  259,  260,  261,  262,  283,  257,  258,  259,  260,
  261,  262,  141,  267,  257,  258,  259,  263,  264,   99,
  270,  265,  266,  281,  278,  263,  264,  276,  278,   78,
  292,  277,   80,   81,   82,   83,   84,   81,   82,  277,
  120,  299,   74,   91,  276,   93,   94,   91,  299,  102,
  287,  288,  100,  101,  297,  276,  100,  101,  257,  258,
  259,  257,  258,  259,  260,  261,  262,  116,  257,  258,
  259,  263,  264,  121,  257,  258,  259,  278,  278,  280,
  280,  278,  280,  280,  279,  284,  260,  261,  287,  288,
  289,  257,  141,  125,  132,  280,  295,  296,  280,  298,
  280,  284,  260,  261,  287,  288,  289,  257,  258,  259,
  293,  271,  295,  296,  267,  257,  258,  259,  285,  286,
  260,  261,  257,  258,  259,  257,  258,  259,  280,  263,
  264,   83,   84,  276,  284,   93,   94,  287,  288,  289,
  276,  278,  284,  290,  277,  295,  296,  289,  276,  284,
  292,  277,  284,  295,  289,  276,  280,  289,  293,  277,
  295,  280,  293,  295,  263,  264,  265,  266,  267,  268,
  269,  270,  271,  272,  273,  277,  275,  274,  277,  278,
  294,  280,  263,  264,  265,  266,  267,  268,  269,  270,
  271,  272,  273,  277,  275,  291,  277,  278,  280,  280,
  263,  264,  265,  266,  277,  268,  269,  270,  271,  272,
  273,  257,  280,  277,  277,  278,  279,  280,  263,  264,
  294,  293,  293,  268,  269,  270,  271,  272,  273,   41,
   41,   74,  277,  278,   80,  280,  263,  264,   82,   -1,
   -1,  268,  269,  270,  271,  272,  273,   -1,   -1,   -1,
  277,  278,   -1,  280,  263,  264,   -1,   -1,   -1,  268,
  269,  270,  271,  272,  273,   -1,   -1,   -1,  277,  278,
   -1,  280,  263,  264,   -1,   -1,   -1,  268,  269,  270,
  271,  272,  273,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=299;
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
null,null,null,"IDENTIFICADOR_GENERICO","IDENTIFICADOR_ULONGINT",
"IDENTIFICADOR_SINGLE","CONSTANTE_DECIMAL","CONSTANTE_OCTAL","CONSTANTE_SINGLE",
"SUMA","RESTA","MULTIPLICACION","DIVISION","ASIGNACION","MAYOR_O_IGUAL",
"MENOR_O_IGUAL","MAYOR","MENOR","IGUAL","DESIGUAL","CORCHETE_L","CORCHETE_R",
"PARENTESIS_L","PARENTESIS_R","COMA","PUNTO","PUNTO_Y_COMA","INLINE_STRING",
"ERROR","STRUCT","FOR","UP","DOWN","SINGLE","ULONGINT","IF","THEN","ELSE",
"BEGIN","END","END_IF","OUTF","TYPEDEF","FUN","RET","TOS",
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
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO",
"tipo : ULONGINT",
"tipo : SINGLE",
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
"cuerpo_funcion : sentencias sentencia_retorno sentencias",
"cuerpo_funcion : sentencias sentencia_retorno",
"cuerpo_funcion : sentencia_retorno",
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA",
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

//#line 164 "gramatica.y"
private static Lexer lex;

public static void main(String[] args) {
    Lexer lexer = new Lexer("src/programa_sin_funciones.txt");
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
//#line 418 "Parser.java"
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
