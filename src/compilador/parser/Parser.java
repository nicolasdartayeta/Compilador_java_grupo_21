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



package compilador.parser;



//#line 2 "gramatica.y"
    import compilador.lexer.Lexer;
    import compilador.lexer.token.*;
    import compilador.lexer.TablaSimbolos;
    import compilador.lexer.CampoTablaSimbolos;
    import compilador.lexer.TablaToken;
    import java.util.ArrayList;
    import java.util.List;

//#line 26 "Parser.java"




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
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    2,    2,    3,    3,    3,    4,    4,    4,    4,
    4,    9,    9,    9,    9,    9,    9,    9,    6,    6,
    6,   10,   10,    7,    7,    7,   11,   11,    1,    1,
    1,   12,   12,    8,   13,   13,   15,   15,   15,   15,
   15,   14,   14,   14,   14,   16,   16,   16,   16,   16,
   16,   16,   20,   20,   21,   21,   21,   18,   18,   18,
   18,   18,   18,   18,   18,   26,   26,   27,   27,   23,
   23,   23,   23,   28,   28,    5,    5,    5,    5,    5,
    5,   17,   29,   29,   29,   29,   29,   29,   29,   29,
   32,   32,   34,   34,   25,   25,   35,   35,   35,   35,
   35,   35,   33,   33,   33,   33,   33,   36,   36,   19,
   19,   19,   19,   19,   19,   19,   19,   30,   30,   22,
   22,   22,   22,   22,   22,   22,   37,   37,   37,   37,
   37,   38,   38,   39,   39,   39,   39,   39,   39,   31,
   31,   24,   24,   24,   40,   40,   40,   40,   41,   41,
   41,   42,   42,   42,   42,   43,   43,   43,   44,   44,
   44,
};
final static short yylen[] = {                            2,
    4,    5,    5,    3,    3,    3,    2,    5,    4,    4,
    3,    2,    1,    1,    1,    2,    3,    4,    4,    1,
    1,   10,    9,   11,   11,    9,    9,    8,    1,    1,
    1,    3,    1,    3,    2,    1,    1,    1,    1,    1,
    1,    3,    3,    4,    6,    5,    2,    1,    1,    3,
    1,    2,    2,    1,    1,    2,    3,    3,    1,    1,
    1,    1,    2,    2,    5,    6,    6,    8,    9,    9,
    7,    7,    7,    6,    7,    2,    1,    2,    1,    4,
    5,    5,    1,    2,    1,    2,    3,    3,    1,    1,
    1,    3,    8,    9,    9,    7,    7,    7,    6,    7,
    2,    1,    2,    1,    3,    1,    1,    1,    1,    1,
    1,    1,    4,    5,    3,    5,    1,    2,    1,    5,
    5,    6,    6,    6,    6,    4,    4,    2,    2,    6,
    2,    1,    5,    5,    5,    4,    7,    6,    6,    6,
    6,    3,    3,    2,    2,    2,    2,    1,    1,    3,
    1,    4,    3,    1,    3,    3,    1,    1,    3,    3,
    1,    1,    1,    2,    1,    1,    1,    1,    4,    3,
    5,
};
final static short yydefred[] = {                         0,
   39,   40,   41,    0,    0,    0,    0,   31,    0,   30,
   29,    0,    0,    0,    0,    0,   13,   14,   15,    0,
    0,   20,   21,   36,   38,    0,    0,   90,    0,   89,
   91,    0,    0,    0,    0,   16,    0,    0,    0,    0,
    0,  166,  167,  168,    0,    0,    0,  162,    0,    0,
    0,    0,  161,  163,  165,    0,    0,    0,    0,    4,
   12,    0,    0,    0,    0,   35,    0,    0,   86,  129,
    0,  117,  128,    0,  131,    0,    0,    0,    0,    5,
    0,    0,    0,    0,  164,    0,    0,  109,  110,  107,
  108,  111,  112,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   33,    0,    0,    0,    0,
   42,    0,    0,    0,   17,  158,  151,    0,   34,    0,
    0,   55,    0,   54,    0,   59,   60,   61,   62,    0,
   87,   88,    0,  119,    0,    0,    0,    0,    0,    9,
    0,    0,    0,    0,  142,  143,    0,  170,    0,    0,
    0,  153,    0,  105,    0,    0,    0,    0,    0,  159,
  160,  127,  126,    0,    0,    0,    0,    0,    0,    0,
    0,   51,    0,   48,    0,   18,   19,    0,    0,    0,
    0,   44,   53,   52,    0,   56,   64,    0,   83,   63,
  115,    0,  118,    0,    0,    0,    3,    8,    2,    0,
    0,    0,    0,  169,    0,    0,  152,    0,    0,    0,
  101,    0,  120,    0,  121,    0,    0,   32,    0,    0,
    0,    0,   46,  150,    0,    0,    0,    0,   57,   58,
   85,    0,    0,  113,  134,    0,  135,  149,  148,    0,
    0,    0,  141,  140,  138,  171,    0,    0,    0,    0,
   99,  103,  122,  124,  123,  125,    0,    0,    0,    0,
   45,   50,    0,    0,    0,    0,    0,    0,    0,   84,
  114,  116,  130,  137,  144,  145,  147,  146,  100,   96,
    0,   97,   98,    0,    0,    0,    0,    0,    0,    0,
    0,   76,    0,    0,   65,    0,   80,    0,   93,    0,
   28,    0,    0,    0,    0,    0,    0,    0,   78,   74,
   66,   67,   81,   82,   94,   95,   27,   23,    0,   26,
   75,   71,    0,   72,   73,    0,   22,    0,   68,   24,
   25,   69,   70,
};
final static short yydgoto[] = {                          5,
   15,   16,   17,   18,   19,   20,   21,   22,   23,  107,
   24,   25,   26,  123,  175,  189,   27,  126,   28,  128,
  129,   29,  266,   49,   50,  267,  292,  232,   30,   31,
  118,  156,  157,  211,   94,  135,   32,   39,  243,   51,
   52,   53,   54,   55,
};
final static short yysindex[] = {                      -138,
    0,    0,    0,  651,    0,  568, -257,    0, -100,    0,
    0, -148, -247, -180, -228,  578,    0,    0,    0, -174,
   56,    0,    0,    0,    0, -260, -232,    0,  702,    0,
    0,  250, -135,  694,  588,    0,  149, -172, -179,    0,
 -154,    0,    0,    0,  230,   31, -136,    0,  329, -263,
  -12,  184,    0,    0,    0,  -42,  -46,  -96,  149,    0,
    0, -230,  750,   46,  149,    0,  474, -156,    0,    0,
   81,    0,    0,   31,    0,  -76,  651,  -72,  631,    0,
    4,  242,   31, -112,    0,  -34,  760,    0,    0,    0,
    0,    0,    0,   46, -116,  711,  780,  780,  780,  780,
  -90,  -87,  -62,  -43, -229,    0,  -73, -229,   50, -228,
    0,  -36,  390,  -32,    0,    0,    0,  -28,    0,   18,
    5,    0,  458,    0, -224,    0,    0,    0,    0,  384,
    0,    0,    8,    0,  718,   19,   31,   47,  641,    0,
   84,   62,   31,   79,    0,    0,   82,    0, -233,   37,
  711,    0,  -10,    0,  711,   72,   40,  184,  184,    0,
    0,    0,    0, -183, -175,   51,  108, -229,  154,  139,
  390,    0,  149,    0,  141,    0,    0,   46,   31,  122,
   46,    0,    0,    0,  126,    0,    0,  526,    0,    0,
    0, -162,    0,  157,  166,  183,    0,    0,    0,  -54,
  -16,  -16,  189,    0,  704,  180,    0,  182,  205,  711,
    0,  136,    0,  153,    0,  149,  149,    0,  226,  149,
  231,  259,    0,    0,  164,  228,  516,  248,    0,    0,
    0,  425,  162,    0,    0,  257,    0,    0,    0,  -16,
  256,  272,    0,    0,    0,    0,  262, -244,  270,  275,
    0,    0,    0,    0,    0,    0,  359,  412,  149,  521,
    0,    0,  268,  516,  516,  269,  265, -150, -139,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -56,    0,    0, -239,  312,  531,  313,  509,  277,  278,
  516,    0,  289,  168,    0,  178,    0,  195,    0,  294,
    0,  295,  321,  300,  301,   38,  304,  306,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -25,    0,
    0,    0,   17,    0,    0,  203,    0,  204,    0,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   83,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  397,    0,    0,  589,    0,    0,    0,    0,  293,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  335,  167,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  594,  611,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   49,    0,    0,    0,    0,    0,    0,    0,    0,  125,
    0,    0,    0,    0,    0,    0,    0,   29,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  612,  613,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   39,  209,  251,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  339,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  404,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  345,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  446,    0,    0,    0,  377,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   77,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  107,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    9,    7,    1,  -60,  -14,  -13,  -19,    0,    0,   -7,
   -9,  566,    0,    0,  457,  -57,  -61,    0,  -59,    0,
    0,  -53, -126,  -45,  -41,   36,    0,    0,    0,    0,
    0, -129,  -27,    0,    0,    0,    0,  593, -181,  544,
  443,  447,  597,    0,
};
final static int YYTABLESIZE=1046;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         38,
   63,   73,   48,  190,   86,  125,  122,  127,    6,  124,
  104,   66,   35,  130,   72,   95,   61,  300,  117,  244,
  245,  206,  203,   68,   36,  208,  112,   38,   96,    8,
   56,  185,  136,   67,  138,   61,   48,  280,  149,  144,
   79,  147,  301,  106,  106,  204,   48,  113,  154,   69,
  109,  281,   59,   66,   48,  119,  134,  186,  274,   10,
   11,  125,  183,  127,   48,  184,   48,  110,  125,  130,
  127,   48,  212,   48,   48,  248,  130,   48,  180,   61,
  214,   72,    1,  139,   48,    2,    3,   48,   48,   48,
   48,  106,   57,  233,  106,  195,   82,  166,  213,  173,
  169,  200,   83,  174,   58,  294,  215,   40,    1,   41,
   48,    2,    3,   42,   43,   44,  296,   45,    1,  234,
  193,    2,    3,   84,   62,  131,  125,   48,  127,   46,
  231,  295,  224,   48,  130,  228,   72,  225,  132,   61,
   72,   87,  297,  116,    1,   41,   36,    2,    3,   42,
   43,   44,   47,   45,  218,    4,    1,  173,   77,    2,
    3,  174,    8,  222,  309,  125,  148,  127,   48,   48,
  125,   48,  127,  130,  270,  155,  108,   37,  130,   40,
    1,   41,  252,    2,    3,   42,   43,   44,   47,   45,
   72,  162,   10,   11,  163,   72,  257,  258,  167,  298,
  260,  137,  125,  125,  127,  127,  168,  238,  239,   36,
  130,  130,    8,  101,    1,   41,  164,    2,    3,   42,
   43,   44,  140,   45,   47,  299,  125,  240,  127,  125,
  326,  127,  241,  242,  130,  165,  102,  130,  105,  286,
  103,  171,   10,   11,  150,  238,  239,   66,   66,  176,
   66,  178,   97,   98,   97,   98,  327,  151,   47,   40,
    1,   41,  177,    2,    3,   42,   43,   44,  207,   45,
  241,  242,  328,   40,    1,   41,   66,    2,    3,   42,
   43,   44,  181,   45,   92,  143,   40,    1,   41,  191,
    2,    3,   42,   43,   44,  179,   45,  194,  329,  289,
  290,  116,    1,   41,   47,    2,    3,   42,   43,   44,
   92,   45,    1,  158,  158,    2,    3,  199,   47,  322,
  102,  170,  104,  306,   64,  196,  216,  158,  205,  168,
  168,   47,  210,  323,  102,   65,  104,    1,   37,   37,
    2,    3,   37,   37,   37,   37,   47,   37,   37,   37,
   37,   37,   37,   37,   37,   37,   37,   37,   77,   37,
  201,   37,   37,  202,   37,   36,    9,  209,   37,   37,
   37,   12,   77,   37,   37,  133,   37,   13,  198,   37,
   43,   43,   37,  217,   43,   43,   43,   43,   79,   43,
   43,   43,   43,   43,   43,   43,   43,   43,   43,   43,
  226,   43,   79,   43,   43,    1,   43,  229,    2,    3,
   43,   43,   43,  227,  220,   43,   43,  253,   43,  223,
  230,   43,  157,  157,   43,  219,  157,  157,  157,  157,
  254,  157,  157,  168,  255,  235,  157,  157,  157,  157,
  157,  157,  263,  271,  236,  157,  157,  256,  157,  311,
   99,  100,  157,  157,  157,  264,  272,  157,  157,  313,
  157,  237,  312,  157,  155,  155,  157,  246,  155,  155,
  155,  155,  314,  155,  155,  249,  315,  250,  155,  155,
  155,  155,  155,  155,  330,  332,  251,  155,  155,  316,
  155,   42,   43,   44,  155,  155,  155,  331,  333,  155,
  155,  259,  155,  145,  146,  155,  156,  156,  155,  261,
  156,  156,  156,  156,  262,  156,  156,  275,  276,  265,
  156,  156,  156,  156,  156,  156,  268,   74,   75,  156,
  156,   76,  156,  277,  278,  273,  156,  156,  156,  158,
  159,  156,  156,  279,  156,  160,  161,  156,  106,  106,
  156,  282,  106,  106,  106,  106,  283,  158,  158,  288,
  293,  291,  158,  158,  158,  158,  158,  158,  302,  304,
  310,  106,  307,  308,  106,  317,  318,  319,  106,  106,
  106,  320,  321,  106,  106,  324,  106,  325,    7,  106,
  154,  154,  106,   11,  154,  154,  154,  154,   88,   89,
   90,   91,   92,   93,  154,  154,  154,  154,  154,  154,
    6,   10,    1,  154,  154,    1,  154,   49,    2,    3,
  154,  154,  154,   47,  111,  154,  154,  221,  154,   81,
  153,  154,  139,  139,  154,  284,  139,  139,   65,  187,
    1,   85,    0,    2,    3,  172,    1,    0,    8,    2,
    3,    0,  132,  132,  139,  139,  132,  132,  139,  136,
  136,    0,  139,  136,  136,    0,    0,  139,    1,    9,
  139,    2,    3,  139,  120,    0,  139,  188,   10,   11,
   13,    1,  132,  121,    2,    3,    0,  132,  285,  136,
  132,   65,    0,  132,  136,    0,  132,  136,    0,    0,
  136,  133,  133,  136,    0,  133,  133,    0,    0,    0,
    9,    0,    0,    0,    1,  120,    8,    2,    3,  269,
    0,   13,    0,    0,  121,    0,    0,    0,    0,    0,
    1,  133,    8,    2,    3,    0,  133,    0,    0,  133,
    0,    0,  133,    9,    0,  133,   10,   11,  120,    0,
    0,    0,  182,    0,   13,   14,    0,  121,    0,    9,
    0,    0,   10,   11,  120,    1,    0,    0,    2,    3,
   13,   14,    1,  121,    0,    2,    3,    1,    0,    0,
    2,    3,    1,    0,    0,    2,    3,    1,    0,    0,
    2,    3,    0,    0,    9,    0,    0,  287,    0,  120,
   65,    9,  188,    0,  305,   13,  120,  303,  121,  188,
   65,    9,   13,    0,    0,  121,  120,    0,    0,    0,
    0,    0,   13,   33,    1,  121,    8,    2,    3,    0,
    0,    0,    0,    7,    1,    0,    8,    2,    3,    0,
    0,    0,    0,    7,    1,    0,    8,    2,    3,    0,
    0,    0,    0,    9,    0,    0,   10,   11,   12,    0,
    0,   34,    0,    9,   13,   14,   10,   11,   12,    0,
    0,    0,   60,    9,   13,   14,   10,   11,   12,    0,
    0,    0,   80,    0,   13,   14,  141,    1,    0,    8,
    2,    3,    0,    0,    0,    0,    7,    1,    0,    8,
    2,    3,    0,    0,    0,    0,    7,    1,    0,    8,
    2,    3,    0,    0,    0,    0,    9,    0,    0,   10,
   11,   12,    0,    0,    0,  142,    9,   13,   14,   10,
   11,   12,    0,    0,    0,  197,    9,   13,   14,   10,
   11,   12,    0,    0,    0,    0,    0,   13,   14,   78,
    1,    0,    8,    2,    3,    0,    0,   70,    1,    0,
    1,    2,    3,    2,    3,    0,    0,    1,    0,    0,
    2,    3,    0,    0,    1,    0,    0,    2,    3,    9,
    0,    0,   10,   11,   12,    0,    0,    9,    0,    9,
   13,   14,   12,    0,   12,   71,    9,   71,   13,  247,
   13,   12,    0,    9,   71,  114,    1,   13,   12,    2,
    3,    0,  192,    0,   13,  116,    1,   41,    0,    2,
    3,   42,   43,   44,    0,   45,    0,    0,    0,   65,
    0,  115,    0,    0,    0,    0,    1,   41,  152,    2,
    3,   42,   43,   44,    0,   45,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   20,   29,   12,  130,   46,   67,   67,   67,    0,   67,
   56,   21,    6,   67,   29,  279,   16,  257,   64,  201,
  202,  151,  256,  256,  282,  155,  257,   37,  292,  259,
  278,  256,   74,  294,   76,   35,   46,  282,   84,   81,
   34,   83,  282,   57,   58,  279,   56,  278,   94,  282,
   58,  296,  281,   63,   64,   65,   71,  282,  240,  289,
  290,  123,  123,  123,   74,  123,   76,   59,  130,  123,
  130,   81,  256,   83,   84,  205,  130,   87,  120,   79,
  256,   96,  257,   77,   94,  260,  261,   97,   98,   99,
  100,  105,  273,  256,  108,  137,  269,  105,  282,  113,
  108,  143,  282,  113,  285,  256,  282,  256,  257,  258,
  120,  260,  261,  262,  263,  264,  256,  266,  257,  282,
  135,  260,  261,  278,  299,  282,  188,  137,  188,  278,
  188,  282,  178,  143,  188,  181,  151,  179,  295,  139,
  155,  278,  282,  256,  257,  258,  282,  260,  261,  262,
  263,  264,  301,  266,  168,  294,  257,  171,  294,  260,
  261,  171,  259,  173,  291,  227,  279,  227,  178,  179,
  232,  181,  232,  227,  232,  292,  273,  278,  232,  256,
  257,  258,  210,  260,  261,  262,  263,  264,  301,  266,
  205,  282,  289,  290,  282,  210,  216,  217,  272,  256,
  220,  278,  264,  265,  264,  265,  280,  262,  263,  282,
  264,  265,  259,  256,  257,  258,  279,  260,  261,  262,
  263,  264,  295,  266,  301,  282,  288,  282,  288,  291,
  256,  291,  287,  288,  288,  279,  279,  291,  285,  259,
  283,  278,  289,  290,  279,  262,  263,  257,  258,  282,
  260,  280,  265,  266,  265,  266,  282,  292,  301,  256,
  257,  258,  295,  260,  261,  262,  263,  264,  279,  266,
  287,  288,  256,  256,  257,  258,  286,  260,  261,  262,
  263,  264,  278,  266,  256,  282,  256,  257,  258,  282,
  260,  261,  262,  263,  264,  278,  266,  279,  282,  264,
  265,  256,  257,  258,  301,  260,  261,  262,  263,  264,
  282,  266,  257,  265,  266,  260,  261,  256,  301,  282,
  282,  272,  282,  288,  269,  279,  276,  279,  292,  280,
  280,  301,  293,  296,  296,  280,  296,  257,  256,  257,
  260,  261,  260,  261,  262,  263,  301,  265,  266,  267,
  268,  269,  270,  271,  272,  273,  274,  275,  282,  277,
  282,  279,  280,  282,  282,  282,  286,  296,  286,  287,
  288,  291,  296,  291,  292,  295,  294,  297,  295,  297,
  256,  257,  300,  276,  260,  261,  262,  263,  282,  265,
  266,  267,  268,  269,  270,  271,  272,  273,  274,  275,
  279,  277,  296,  279,  280,  257,  282,  282,  260,  261,
  286,  287,  288,  292,  276,  291,  292,  282,  294,  279,
  295,  297,  256,  257,  300,  272,  260,  261,  262,  263,
  295,  265,  266,  280,  282,  279,  270,  271,  272,  273,
  274,  275,  279,  282,  279,  279,  280,  295,  282,  282,
  267,  268,  286,  287,  288,  292,  295,  291,  292,  282,
  294,  279,  295,  297,  256,  257,  300,  279,  260,  261,
  262,  263,  295,  265,  266,  296,  282,  296,  270,  271,
  272,  273,  274,  275,  282,  282,  282,  279,  280,  295,
  282,  262,  263,  264,  286,  287,  288,  295,  295,  291,
  292,  276,  294,  262,  263,  297,  256,  257,  300,  279,
  260,  261,  262,  263,  256,  265,  266,  262,  263,  292,
  270,  271,  272,  273,  274,  275,  279,  278,  279,  279,
  280,  282,  282,  262,  263,  279,  286,  287,  288,   97,
   98,  291,  292,  282,  294,   99,  100,  297,  256,  257,
  300,  282,  260,  261,  262,  263,  282,  265,  266,  292,
  296,  293,  270,  271,  272,  273,  274,  275,  257,  257,
  282,  279,  296,  296,  282,  282,  282,  257,  286,  287,
  288,  282,  282,  291,  292,  282,  294,  282,    0,  297,
  256,  257,  300,    0,  260,  261,  262,  263,  270,  271,
  272,  273,  274,  275,  270,  271,  272,  273,  274,  275,
    0,    0,    0,  279,  280,  257,  282,  279,  260,  261,
  286,  287,  288,  279,   59,  291,  292,  171,  294,   37,
   87,  297,  256,  257,  300,  277,  260,  261,  280,  256,
  257,   45,   -1,  260,  261,  256,  257,   -1,  259,  260,
  261,   -1,  256,  257,  278,  279,  260,  261,  282,  256,
  257,   -1,  286,  260,  261,   -1,   -1,  291,  257,  286,
  294,  260,  261,  297,  291,   -1,  300,  294,  289,  290,
  297,  257,  286,  300,  260,  261,   -1,  291,  277,  286,
  294,  280,   -1,  297,  291,   -1,  300,  294,   -1,   -1,
  297,  256,  257,  300,   -1,  260,  261,   -1,   -1,   -1,
  286,   -1,   -1,   -1,  257,  291,  259,  260,  261,  295,
   -1,  297,   -1,   -1,  300,   -1,   -1,   -1,   -1,   -1,
  257,  286,  259,  260,  261,   -1,  291,   -1,   -1,  294,
   -1,   -1,  297,  286,   -1,  300,  289,  290,  291,   -1,
   -1,   -1,  295,   -1,  297,  298,   -1,  300,   -1,  286,
   -1,   -1,  289,  290,  291,  257,   -1,   -1,  260,  261,
  297,  298,  257,  300,   -1,  260,  261,  257,   -1,   -1,
  260,  261,  257,   -1,   -1,  260,  261,  257,   -1,   -1,
  260,  261,   -1,   -1,  286,   -1,   -1,  277,   -1,  291,
  280,  286,  294,   -1,  296,  297,  291,  277,  300,  294,
  280,  286,  297,   -1,   -1,  300,  291,   -1,   -1,   -1,
   -1,   -1,  297,  256,  257,  300,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,
   -1,  294,   -1,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,   -1,  297,  298,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,   -1,   -1,   -1,  297,  298,  256,
  257,   -1,  259,  260,  261,   -1,   -1,  256,  257,   -1,
  257,  260,  261,  260,  261,   -1,   -1,  257,   -1,   -1,
  260,  261,   -1,   -1,  257,   -1,   -1,  260,  261,  286,
   -1,   -1,  289,  290,  291,   -1,   -1,  286,   -1,  286,
  297,  298,  291,   -1,  291,  294,  286,  294,  297,  296,
  297,  291,   -1,  286,  294,  256,  257,  297,  291,  260,
  261,   -1,  295,   -1,  297,  256,  257,  258,   -1,  260,
  261,  262,  263,  264,   -1,  266,   -1,   -1,   -1,  280,
   -1,  282,   -1,   -1,   -1,   -1,  257,  258,  279,  260,
  261,  262,  263,  264,   -1,  266,
};
}
final static short YYFINAL=5;
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
"programa : identificador_simple BEGIN sentencias END",
"programa : identificador_simple BEGIN sentencias END error",
"programa : identificador_simple error BEGIN sentencias END",
"programa : BEGIN sentencias END",
"programa : identificador_simple sentencias END",
"programa : identificador_simple BEGIN sentencias",
"programa : identificador_simple sentencias",
"programa : identificador_simple BEGIN sentencias error END",
"programa : identificador_simple BEGIN error END",
"programa : identificador_simple BEGIN sentencias error",
"programa : identificador_simple BEGIN error",
"sentencias : sentencias sentencia",
"sentencias : sentencia",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencia : error PUNTO_Y_COMA",
"sentencia_declarativa : tipo lista_de_identificadores PUNTO_Y_COMA",
"sentencia_declarativa : tipo lista_de_identificadores error PUNTO_Y_COMA",
"sentencia_declarativa : tipo lista_de_identificadores error END",
"sentencia_declarativa : funcion",
"sentencia_declarativa : struct",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO error PUNTO_Y_COMA",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO error END",
"struct : TYPEDEF STRUCT lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF MENOR STRUCT lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF MENOR STRUCT lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R PUNTO_Y_COMA",
"tipo : ULONGINT",
"tipo : SINGLE",
"tipo : IDENTIFICADOR_TIPO",
"lista_de_tipos : lista_de_tipos COMA tipo",
"lista_de_tipos : tipo",
"lista_de_identificadores : lista_de_identificadores COMA identificador",
"lista_de_identificadores : lista_de_identificadores identificador",
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
"encabezado_funcion : tipo FUN PARENTESIS_L parametro PARENTESIS_R",
"parametro : tipo identificador",
"parametro : identificador",
"parametro : tipo",
"parametro : tipo identificador error",
"parametro : error",
"cuerpo_funcion : cuerpo_funcion sentencia_ejecutable_en_funcion",
"cuerpo_funcion : cuerpo_funcion sentencia_declarativa",
"cuerpo_funcion : sentencia_ejecutable_en_funcion",
"cuerpo_funcion : sentencia_declarativa",
"sentencia_ejecutable_en_funcion : sentencia_asignacion PUNTO_Y_COMA",
"sentencia_ejecutable_en_funcion : sentencia_asignacion error PUNTO_Y_COMA",
"sentencia_ejecutable_en_funcion : sentencia_asignacion error END",
"sentencia_ejecutable_en_funcion : sentencia_seleccion_en_funcion",
"sentencia_ejecutable_en_funcion : sentencia_salida",
"sentencia_ejecutable_en_funcion : sentencia_control_en_funcion",
"sentencia_ejecutable_en_funcion : sentencia_retorno",
"sentencia_control_en_funcion : encabezado_for bloque_de_sent_ejecutables_en_funcion",
"sentencia_control_en_funcion : encabezado_for error",
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA",
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R error PUNTO_Y_COMA",
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R error END",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF error PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF error END",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion THEN cuerpo_if_en_funcion END_IF PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF condicion THEN cuerpo_if_en_funcion END_IF PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN END_IF PUNTO_Y_COMA",
"cuerpo_if_en_funcion : bloque_de_sent_ejecutables_en_funcion bloque_else_en_funcion",
"cuerpo_if_en_funcion : bloque_de_sent_ejecutables_en_funcion",
"bloque_else_en_funcion : ELSE bloque_de_sent_ejecutables_en_funcion",
"bloque_else_en_funcion : ELSE",
"bloque_de_sent_ejecutables_en_funcion : BEGIN sentencias_ejecutable_en_funcion END PUNTO_Y_COMA",
"bloque_de_sent_ejecutables_en_funcion : BEGIN sentencias_ejecutable_en_funcion END error PUNTO_Y_COMA",
"bloque_de_sent_ejecutables_en_funcion : BEGIN sentencias_ejecutable_en_funcion END error END",
"bloque_de_sent_ejecutables_en_funcion : sentencia_ejecutable_en_funcion",
"sentencias_ejecutable_en_funcion : sentencias_ejecutable_en_funcion sentencia_ejecutable_en_funcion",
"sentencias_ejecutable_en_funcion : sentencia_ejecutable_en_funcion",
"sentencia_ejecutable : sentencia_asignacion PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_asignacion error PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_asignacion error END",
"sentencia_ejecutable : sentencia_seleccion",
"sentencia_ejecutable : sentencia_salida",
"sentencia_ejecutable : sentencia_control",
"sentencia_asignacion : lista_de_identificadores ASIGNACION lista_de_expresiones",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF PUNTO_Y_COMA",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF error PUNTO_Y_COMA",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF error END",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if PUNTO_Y_COMA",
"sentencia_seleccion : IF PARENTESIS_L condicion THEN cuerpo_if END_IF PUNTO_Y_COMA",
"sentencia_seleccion : IF condicion PARENTESIS_R THEN cuerpo_if END_IF PUNTO_Y_COMA",
"sentencia_seleccion : IF condicion THEN cuerpo_if END_IF PUNTO_Y_COMA",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN END_IF PUNTO_Y_COMA",
"cuerpo_if : bloque_de_sent_ejecutables bloque_else",
"cuerpo_if : bloque_de_sent_ejecutables",
"bloque_else : ELSE bloque_de_sent_ejecutables",
"bloque_else : ELSE",
"condicion : expresion comparador expresion",
"condicion : error",
"comparador : MAYOR",
"comparador : MENOR",
"comparador : MAYOR_O_IGUAL",
"comparador : MENOR_O_IGUAL",
"comparador : IGUAL",
"comparador : DESIGUAL",
"bloque_de_sent_ejecutables : BEGIN sentencias_ejecutables END PUNTO_Y_COMA",
"bloque_de_sent_ejecutables : BEGIN sentencias_ejecutables END error PUNTO_Y_COMA",
"bloque_de_sent_ejecutables : BEGIN END PUNTO_Y_COMA",
"bloque_de_sent_ejecutables : BEGIN sentencias_ejecutables END error END",
"bloque_de_sent_ejecutables : sentencia_ejecutable",
"sentencias_ejecutables : sentencias_ejecutables sentencia_ejecutable",
"sentencias_ejecutables : sentencia_ejecutable",
"sentencia_salida : OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R PUNTO_Y_COMA",
"sentencia_salida : OUTF PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA",
"sentencia_salida : OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R error PUNTO_Y_COMA",
"sentencia_salida : OUTF PARENTESIS_L expresion PARENTESIS_R error PUNTO_Y_COMA",
"sentencia_salida : OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R error END",
"sentencia_salida : OUTF PARENTESIS_L expresion PARENTESIS_R error END",
"sentencia_salida : OUTF PARENTESIS_L PARENTESIS_R PUNTO_Y_COMA",
"sentencia_salida : OUTF PARENTESIS_L error PUNTO_Y_COMA",
"sentencia_control : encabezado_for bloque_de_sent_ejecutables",
"sentencia_control : encabezado_for error",
"encabezado_for : encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion PARENTESIS_R PARENTESIS_R",
"encabezado_for : encabezado_for_obligatorio PARENTESIS_R",
"encabezado_for : encabezado_for_obligatorio",
"encabezado_for : encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion PARENTESIS_R",
"encabezado_for : encabezado_for_obligatorio PARENTESIS_L condicion PARENTESIS_R PARENTESIS_R",
"encabezado_for : encabezado_for_obligatorio PUNTO_Y_COMA condicion PARENTESIS_R PARENTESIS_R",
"encabezado_for : encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion",
"encabezado_for_obligatorio : FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion",
"encabezado_for_obligatorio : FOR asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion",
"encabezado_for_obligatorio : FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA",
"encabezado_for_obligatorio : FOR PARENTESIS_L asignacion_enteros condicion PUNTO_Y_COMA accion",
"encabezado_for_obligatorio : FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion accion",
"asignacion_enteros : identificador ASIGNACION CONSTANTE_DECIMAL",
"asignacion_enteros : identificador ASIGNACION CONSTANTE_OCTAL",
"accion : UP CONSTANTE_DECIMAL",
"accion : UP CONSTANTE_OCTAL",
"accion : DOWN CONSTANTE_OCTAL",
"accion : DOWN CONSTANTE_DECIMAL",
"accion : CONSTANTE_OCTAL",
"accion : CONSTANTE_DECIMAL",
"lista_de_expresiones : lista_de_expresiones COMA expresion",
"lista_de_expresiones : expresion",
"expresion : TOS PARENTESIS_L expresion_aritmetica PARENTESIS_R",
"expresion : TOS PARENTESIS_L PARENTESIS_R",
"expresion : expresion_aritmetica",
"expresion_aritmetica : expresion_aritmetica SUMA termino",
"expresion_aritmetica : expresion_aritmetica RESTA termino",
"expresion_aritmetica : termino",
"expresion_aritmetica : error",
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
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion error PARENTESIS_R",
};

//#line 338 "gramatica.y"
private static Lexer lex;

// Tipo de mensajes
public static final String ERROR_LEXICO = "ERROR_LEXICO";
public static final String ERROR_SINTACTICO = "ERROR_SINTACTICO";

// Código ANSI para texto rojo
public static final String ROJO = "\033[31m";
public static final String VERDE = "\033[32m";
public static final String RESET = "\033[0m";

// Flags
public static boolean parsingConErrores = false;
public static boolean lexingConErrores = false;
private static boolean returnEncontrado = false;

// Lista de estructuras detectadas
public static final List<String> estructurasDetectadas = new ArrayList<>();

// Listas de errores
public static final List<String> erroresLexicos = new ArrayList<>();
public static final List<String> erroresSintacticos = new ArrayList<>();

// Agregar error a la lista de errores
public static void agregarEstructuraDetectadas(int numeroLinea, String estructura) {
    estructurasDetectadas.add("Linea " + numeroLinea + ": " + "Declaracion de " + estructura + " detectada");
}

// Agregar error a la lista de errores
public static void agregarError(List<String> listaErrores, String tipo, String error) {
    if (tipo.equals(Parser.ERROR_SINTACTICO)) {
        parsingConErrores = true;
    } else if (tipo.equals(Parser.ERROR_LEXICO)) {
        lexingConErrores = true;
    }

    listaErrores.add(String.format("%-20s", tipo) + "| "+ error);
}

public static void imprimirLista(List<String> lista) {
    for (String elemento : lista) {
        System.out.println(elemento);
    }
}

public static void main(String[] args) {
    Lexer lexer = new Lexer("src/programa_sin_funciones.txt");
    Parser.lex = lexer;
    Parser parser = new Parser(true);
    parser.run();

    if (Parser.lexingConErrores){
        System.out.println(Parser.ROJO + "SE ENCONTRARON ERRORES LEXICOS" + Parser.RESET);
        Parser.imprimirLista(erroresLexicos);
    } else {
        System.out.println(Parser.VERDE + "NO SE ENCONTRARON ERRORES LEXICOS" + Parser.RESET);
    }

    if (Parser.parsingConErrores){
        System.out.println(Parser.ROJO + "SE ENCONTRARON ERRORES SINTACTICOS" + Parser.RESET);
        Parser.imprimirLista(erroresSintacticos);
    } else {
        System.out.println(Parser.VERDE + "NO SE ENCONTRARON ERRORES SINTACTICOS" + Parser.RESET);
    }

    Parser.imprimirLista(estructurasDetectadas);

    TablaSimbolos.imprimirTabla();
}

private int yylex() {
    Token tok = lex.getNextToken();

    if (tok.isError()) {
        agregarError(erroresLexicos, ERROR_LEXICO, ((TokenError) tok).getDescripcionError());
    }

    yylval = new ParserVal(tok);
    return tok.getTokenID();
}

private void yyerror(String string) {
  parsingConErrores = true;
  System.out.println("Error: " + string );
}
//#line 815 "Parser.java"
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
case 1:
//#line 19 "gramatica.y"
{ Parser.agregarEstructuraDetectadas( yyval.ival = val_peek(3).ival, "PROGRAMA"); }
break;
case 2:
//#line 20 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(1).obj).getNumeroDeLinea() + ": Todo lo que esta despues del END no forma parte del programa."); }
break;
case 3:
//#line 21 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(1).obj).getNumeroDeLinea() + ": Todo lo que esta despues del identificador del programa y antes del primer begin no forma parte del programa."); }
break;
case 4:
//#line 22 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea 1: Falta identificador de programa"); }
break;
case 5:
//#line 23 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ( yyval.ival = val_peek(2).ival +1) + ": Falta un BEGIN despues del identificador del programa"); }
break;
case 6:
//#line 24 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Ultima linea: Falta un END al final del programa"); }
break;
case 7:
//#line 25 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta un BEGIN despues del identificador del programa y falta un END al final del programa"); }
break;
case 8:
//#line 26 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR."); }
break;
case 9:
//#line 27 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR."); }
break;
case 10:
//#line 28 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. FALTA EL END AL FINAL DEL PROGRAMA"); }
break;
case 11:
//#line 29 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. FALTA EL END AL FINAL DEL PROGRAMA"); }
break;
case 16:
//#line 39 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)val_peek(0).obj).getNumeroDeLinea() + ": Syntax Error"); }
break;
case 17:
//#line 42 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S"); }
break;
case 18:
//#line 43 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 19:
//#line 44 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 20:
//#line 45 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FUNCION"); }
break;
case 22:
//#line 49 "gramatica.y"
{
                                                                                                                                                    yyval.ival = ((Token) val_peek(9).obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) val_peek(9).obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) val_peek(1).obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                }
break;
case 23:
//#line 55 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 24:
//#line 56 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(10).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 25:
//#line 57 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(10).ival + ": Falta ';' al final de la sentencia"); }
break;
case 26:
//#line 58 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 27:
//#line 59 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 28:
//#line 60 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 29:
//#line 64 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 30:
//#line 65 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 31:
//#line 66 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 35:
//#line 74 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ',' entre las variables"); }
break;
case 36:
//#line 75 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 37:
//#line 78 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 38:
//#line 79 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 39:
//#line 82 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 40:
//#line 83 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 41:
//#line 84 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 42:
//#line 87 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 43:
//#line 88 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 44:
//#line 91 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 45:
//#line 100 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 46:
//#line 105 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                             }
break;
case 48:
//#line 112 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 49:
//#line 113 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 50:
//#line 114 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": La funcion no puede tener mas de un parametro");}
break;
case 51:
//#line 115 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ yyval.ival + ": Falta el parametro de la funcion");}
break;
case 56:
//#line 124 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 57:
//#line 125 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 58:
//#line 126 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 59:
//#line 127 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "IF"); }
break;
case 61:
//#line 129 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 62:
//#line 130 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 63:
//#line 133 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 64:
//#line 134 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 65:
//#line 137 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 66:
//#line 138 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 67:
//#line 139 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta ';' al final de la sentencia"); }
break;
case 68:
//#line 142 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 69:
//#line 143 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 70:
//#line 144 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 71:
//#line 145 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 72:
//#line 146 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 73:
//#line 147 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 74:
//#line 148 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 75:
//#line 149 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 79:
//#line 157 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 81:
//#line 161 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 82:
//#line 162 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 86:
//#line 170 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 87:
//#line 171 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 88:
//#line 172 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 91:
//#line 175 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 92:
//#line 178 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 93:
//#line 181 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 94:
//#line 182 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 95:
//#line 183 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 96:
//#line 184 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 97:
//#line 185 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 98:
//#line 186 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 99:
//#line 187 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 100:
//#line 188 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 104:
//#line 196 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 106:
//#line 200 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 107:
//#line 203 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 108:
//#line 204 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 109:
//#line 205 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 110:
//#line 206 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 111:
//#line 207 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 112:
//#line 208 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 114:
//#line 212 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 115:
//#line 213 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
break;
case 116:
//#line 214 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 120:
//#line 222 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 121:
//#line 223 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 122:
//#line 224 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 123:
//#line 225 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 124:
//#line 226 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 125:
//#line 227 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 126:
//#line 228 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 127:
//#line 229 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 128:
//#line 232 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 129:
//#line 233 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 130:
//#line 236 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 131:
//#line 237 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 132:
//#line 238 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 133:
//#line 239 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 134:
//#line 240 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 135:
//#line 241 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 136:
//#line 242 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 137:
//#line 245 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 138:
//#line 246 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 139:
//#line 247 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 140:
//#line 248 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 141:
//#line 249 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 148:
//#line 260 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 149:
//#line 261 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 152:
//#line 268 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 153:
//#line 269 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 158:
//#line 276 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + " :Falta operador u operandos"); }
break;
case 164:
//#line 286 "gramatica.y"
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
case 166:
//#line 327 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 167:
//#line 328 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 168:
//#line 329 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 170:
//#line 333 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 171:
//#line 334 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1503 "Parser.java"
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
