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
//#line 21 "Parser.java"




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
   34,   34,   34,   35,   35,   35,   36,   37,
};
final static short yylen[] = {                            2,
    4,    2,    1,    1,    1,    3,    1,    2,    9,    1,
    1,    3,    1,    3,    1,    1,    1,    1,    1,    1,
    3,    3,    4,    6,    2,    3,    2,    1,    5,    2,
    2,    2,    1,    3,    7,    2,    1,    2,    3,    1,
    1,    1,    1,    1,    1,    4,    1,    2,    1,    4,
    4,    2,    8,    3,    3,    2,    2,    2,    2,    3,
    1,    4,    1,    3,    3,    1,    3,    3,    1,    1,
    1,    2,    1,    1,    1,    1,    4,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   18,   19,   20,    0,   11,   10,    0,
    0,    0,    0,    3,    4,    5,    0,    0,    7,    0,
   15,    0,   17,    0,    0,    0,    0,   33,    0,    0,
    0,    0,    0,    1,    2,    0,    0,    0,    0,    8,
    0,    0,   30,   31,   32,    0,   47,   52,    0,    0,
    0,   74,   75,   76,    0,    0,   70,    0,    0,   63,
    0,   69,   71,   73,    0,    0,    0,    0,    6,    0,
    0,   14,    0,   21,    0,    0,    0,   28,   49,    0,
    0,    0,    0,   72,    0,    0,    0,   42,   43,   40,
   41,   44,   45,    0,    0,    0,    0,   50,   51,   13,
    0,    0,    0,    0,    0,   23,    0,   48,   54,   55,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   67,
   68,    0,    0,    0,    0,    0,    0,    0,   46,    0,
   77,   62,    0,    0,    0,   12,   25,   24,    0,    0,
    0,    0,   35,    0,   36,    0,   29,   56,   57,   59,
   58,   53,   38,    0,    9,
};
final static short yydgoto[] = {                          2,
   13,   14,   15,   16,   17,   18,   19,   20,  101,   21,
   22,   23,   24,   77,  125,   78,   58,   25,   26,   27,
   28,   71,   59,  133,   48,  145,   94,   80,   29,   50,
  142,   60,   61,   62,   63,   64,  113,
};
final static short yysindex[] = {                      -210,
 -285,    0, -164,    0,    0,    0, -202,    0,    0, -188,
 -186, -191, -174,    0,    0,    0, -244, -258,    0, -184,
    0, -179,    0, -162, -134, -122, -120,    0, -156,  -46,
 -226, -235, -144,    0,    0,  -94, -259, -226,  -46,    0,
  -46, -209,    0,    0,    0, -114,    0,    0,  -96, -103,
  -83,    0,    0,    0,  -40,  -78,    0,   10,  -82,    0,
 -131,    0,    0,    0,  -73, -220, -248,  -42,    0, -126,
  -47,    0, -179,    0,  -35, -209,  -57,    0,    0, -117,
  -95, -226, -226,    0, -226, -153, -153,    0,    0,    0,
    0,    0,    0, -226,  -37, -153, -153,    0,    0,    0,
 -233, -248, -226, -226, -164,    0,  -30,    0,    0,    0,
  -23, -126,  -19, -126,  -18, -131, -131, -126, -156,    0,
    0,   -7, -248,  -46,   -9, -126, -165, -164,    0, -112,
    0,    0,  -10,  -25,  -46,    0,    0,    0,   -5,  -92,
  -31,   -6,    0, -156,    0, -224,    0,    0,    0,    0,
    0,    0,    0,   19,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -116,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -63,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -45,    0,    0,    0,    0,    0,    0,    0,    0, -250,
   -3,    0,  -81,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   -4,    0,    0,    0,    0,    0,
    0,    8,    0,    0,  -84,  -26,   -8, -151,    0,    0,
    0,    0,    0,    0,    0, -212,    0,   -2,    0,    0,
    0,    0,    0,   -1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -34,  -11,    0,  -28,  -61,  -17,    0,    0,    0,  -27,
  245,  246,    0,    0,    0,  212,  -22,    0,    0,    0,
    0,    0,  208,    0, -102,    0,    0,    0,    0,    0,
    0,  207,  153,  152,  239,    0,    0,
};
final static int YYTABLESIZE=294;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   47,   35,   49,   57,   57,  100,    3,   76,   38,   66,
   57,   72,    4,    5,    6,   70,  134,   79,   39,   39,
   69,   51,    5,    6,   52,   53,   54,   61,   55,   61,
   51,    5,    6,   52,   53,   54,  122,   55,    8,    9,
  124,  153,   86,   87,  123,   65,    1,    4,    5,    6,
  154,  108,   36,   39,   57,   57,   99,   57,   57,   57,
  112,  136,  114,   56,   35,   60,   57,   60,   57,   57,
  128,  118,   56,   30,    7,   57,   57,    8,    9,   10,
  126,  127,    4,    5,    6,   11,   12,   31,   75,   32,
   47,   33,    4,    5,    6,   40,  137,   86,   87,   41,
    4,    5,    6,   51,    5,    6,   52,   53,   54,    7,
   55,  139,    8,    9,   10,   47,   35,  146,   34,    7,
   11,   12,    8,    9,   10,   39,   67,    7,   39,   42,
   11,   12,   10,   96,   97,   46,   86,   87,   11,    4,
    5,    6,    4,    5,    6,   43,   16,   16,   16,   16,
   16,   16,   16,   16,   16,   16,   16,   44,   16,   45,
   16,   16,   68,   16,  109,  110,    7,  148,  149,    7,
   81,   10,  140,  141,   10,  107,   82,   11,   63,   63,
   11,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   22,   83,   22,   95,   22,   22,   85,   22,   18,
   18,   18,   18,   98,   18,   18,   18,   18,   18,   18,
    4,    5,    6,   18,   18,   18,   18,   66,   66,   52,
   53,   54,   66,   66,   66,   66,   66,   66,  150,  151,
  103,   66,   66,  102,   66,  106,   64,   64,  116,  117,
  104,   64,   64,   64,   64,   64,   64,  120,  121,  129,
   64,   64,  119,   64,   65,   65,  130,  131,  132,   65,
   65,   65,   65,   65,   65,  144,  135,  138,   65,   65,
  152,   65,   86,   87,  147,  155,   34,   88,   89,   90,
   91,   92,   93,  143,   78,   73,   74,  105,   27,  111,
   26,  115,   37,   84,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         17,
   29,   13,   30,   31,   32,   67,  292,   42,  267,   32,
   38,   39,  257,  258,  259,   38,  119,   46,  278,  278,
  280,  257,  258,  259,  260,  261,  262,  278,  264,  280,
  257,  258,  259,  260,  261,  262,  270,  264,  287,  288,
  102,  144,  263,  264,  278,  281,  257,  257,  258,  259,
  275,   80,  297,  278,   82,   83,  277,   85,   86,   87,
   83,  123,   85,  299,   76,  278,   94,  280,   96,   97,
  105,   94,  299,  276,  284,  103,  104,  287,  288,  289,
  103,  104,  257,  258,  259,  295,  296,  276,  298,  276,
  119,  283,  257,  258,  259,  280,  124,  263,  264,  279,
  257,  258,  259,  257,  258,  259,  260,  261,  262,  284,
  264,  277,  287,  288,  289,  144,  128,  135,  293,  284,
  295,  296,  287,  288,  289,  277,  271,  284,  280,  292,
  295,  296,  289,  265,  266,  292,  263,  264,  295,  257,
  258,  259,  257,  258,  259,  280,  263,  264,  265,  266,
  267,  268,  269,  270,  271,  272,  273,  280,  275,  280,
  277,  278,  257,  280,  260,  261,  284,  260,  261,  284,
  267,  289,  285,  286,  289,  293,  280,  295,  263,  264,
  295,  263,  264,  265,  266,  267,  268,  269,  270,  271,
  272,  273,  276,  275,  277,  277,  278,  276,  280,  263,
  264,  265,  266,  277,  268,  269,  270,  271,  272,  273,
  257,  258,  259,  277,  278,  279,  280,  263,  264,  260,
  261,  262,  268,  269,  270,  271,  272,  273,  260,  261,
  278,  277,  278,  276,  280,  293,  263,  264,   86,   87,
  276,  268,  269,  270,  271,  272,  273,   96,   97,  280,
  277,  278,  290,  280,  263,  264,  280,  277,  277,  268,
  269,  270,  271,  272,  273,  291,  274,  277,  277,  278,
  277,  280,  263,  264,  280,  257,  280,  268,  269,  270,
  271,  272,  273,  294,  277,   41,   41,   76,  293,   82,
  293,   85,  294,   55,
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
"factor : constante",
"factor : RESTA constante",
"factor : invocacion_a_funcion",
"constante : CONSTANTE_DECIMAL",
"constante : CONSTANTE_OCTAL",
"constante : CONSTANTE_SINGLE",
"invocacion_a_funcion : IDENTIFICADOR_GENERICO PARENTESIS_L parametro_real PARENTESIS_R",
"parametro_real : expresion",
};

//#line 169 "gramatica.y"
private static Lexer lex;

public static void main(String[] args) {
    Lexer lexer = new Lexer("src/programa_sin_funciones.txt");
    Parser.lex = lexer;
    Parser parser = new Parser(true);
    parser.run();
    }

private int yylex() {
    Token tok = lex.getNextToken();
    yylval = new ParserVal(tok);
    return tok.getTokenID();
}

private void yyerror(String string) {
  throw new UnsupportedOperationException("ERROR");
}
//#line 421 "Parser.java"
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
//#line 25 "gramatica.y"
{ System.out.println("Declaracion de VARIABLES detectada"); }
break;
case 7:
//#line 26 "gramatica.y"
{ System.out.println("Declaracion de FUNCION detectada"); }
break;
case 8:
//#line 27 "gramatica.y"
{ System.out.println("Declaracion de STRUCT detectada"); }
break;
case 15:
//#line 42 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 16:
//#line 45 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 17:
//#line 46 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 18:
//#line 49 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 19:
//#line 50 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 20:
//#line 51 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 21:
//#line 54 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 22:
//#line 55 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 30:
//#line 74 "gramatica.y"
{ System.out.println("Linea " + val_peek(1).ival + ": " + "ASIGNACION detectada"); }
break;
case 31:
//#line 75 "gramatica.y"
{ System.out.println("Linea " + val_peek(1).ival + ": " + "Sentencia IF detectada"); }
break;
case 32:
//#line 76 "gramatica.y"
{ System.out.println("Linea " + val_peek(1).ival + ": " + "Sentencia de SALIDA detectada"); }
break;
case 33:
//#line 77 "gramatica.y"
{ System.out.println("Linea " + val_peek(0).ival + ": " + "Sentencia FOR detectada"); }
break;
case 34:
//#line 80 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 35:
//#line 83 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 50:
//#line 112 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 51:
//#line 113 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 52:
//#line 116 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 53:
//#line 119 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); }
break;
case 71:
//#line 151 "gramatica.y"
{ System.out.println(val_peek(0).sval); }
break;
case 74:
//#line 156 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); }
break;
case 75:
//#line 157 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); }
break;
case 76:
//#line 158 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); }
break;
//#line 670 "Parser.java"
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
