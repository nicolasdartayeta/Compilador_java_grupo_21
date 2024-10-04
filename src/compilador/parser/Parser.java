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
    0,    0,    0,    0,    0,    1,    1,    2,    2,    2,
    3,    3,    3,    3,    8,    8,    8,    8,    8,    8,
    5,    5,    5,    9,    9,    6,    6,   10,   10,   11,
   11,   11,   12,   12,    7,   13,   13,   15,   15,   15,
   14,   14,   14,   14,   16,   16,   16,   16,   16,   20,
   20,   21,   18,   18,   18,   18,   18,   18,   26,   26,
   27,   27,   23,   23,   28,   28,    4,    4,    4,    4,
    4,   17,   29,   29,   29,   29,   29,   29,   32,   32,
   34,   34,   25,   35,   35,   35,   35,   35,   35,   33,
   33,   36,   36,   19,   19,   19,   30,   30,   22,   22,
   22,   22,   22,   22,   22,   37,   37,   37,   37,   37,
   38,   38,   39,   39,   39,   39,   31,   31,   24,   24,
   40,   40,   40,   41,   41,   41,   42,   42,   42,   42,
   43,   43,   43,   44,   45,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    2,    1,    1,    1,    2,
    3,    2,    1,    1,   10,    9,    9,    9,    9,    8,
    1,    1,    1,    3,    1,    3,    1,    1,    1,    1,
    1,    1,    3,    3,    4,    6,    5,    2,    1,    1,
    2,    2,    1,    1,    2,    2,    2,    1,    1,    2,
    2,    5,    7,    7,    6,    6,    5,    6,    2,    1,
    2,    1,    4,    1,    2,    1,    2,    1,    2,    2,
    1,    3,    7,    7,    6,    6,    5,    6,    2,    1,
    2,    1,    3,    1,    1,    1,    1,    1,    1,    4,
    1,    2,    1,    4,    4,    3,    2,    2,    6,    2,
    1,    5,    5,    5,    4,    7,    6,    6,    6,    6,
    3,    3,    2,    2,    2,    2,    3,    1,    4,    1,
    3,    3,    1,    3,    3,    1,    1,    1,    2,    1,
    1,    1,    1,    4,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   30,   23,   31,   32,    0,   22,
   21,    0,    0,    0,    0,    0,    7,    8,    9,    0,
    0,   13,   14,   27,    0,   29,    0,    0,    0,    0,
    0,   71,    0,    0,   10,    0,    0,    0,    0,  131,
  132,  133,    0,    0,    0,  127,    0,    0,  120,    0,
  126,  128,  130,    0,    0,    0,    0,    3,    6,    0,
    0,    0,    0,    0,    0,   67,   70,   98,    0,   91,
   97,   69,    0,  100,    0,    2,    0,    0,    0,    0,
  129,    0,    0,    0,    0,   86,   87,   84,   85,   88,
   89,    0,    0,    0,    0,    0,    1,   96,    0,    0,
    0,   25,    0,    0,    0,    0,    0,   11,    0,    0,
   26,    0,   33,    0,    0,   44,    0,   43,    0,    0,
    0,   48,   49,    0,   93,    0,    0,    0,    0,    0,
    0,  111,  112,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  124,  125,   94,   95,
    0,    0,    0,    0,    0,    0,    0,   39,    0,    0,
    0,    0,    0,   35,   42,   41,   45,   46,   47,   51,
    0,   64,   50,    0,   92,    0,    0,    0,    0,    0,
    0,  134,    0,    0,  119,    0,   77,    0,   79,    0,
    0,   24,    0,    0,    0,   38,   37,    0,    0,    0,
    0,    0,   66,    0,   90,  103,    0,  104,    0,    0,
    0,  110,  109,  107,   78,    0,   75,   76,   81,    0,
    0,    0,    0,   36,    0,    0,    0,    0,    0,    0,
    0,   65,   99,  106,  113,  114,  116,  115,   74,   73,
    0,    0,    0,    0,    0,    0,    0,    0,   59,   57,
   52,   63,    0,   20,    0,    0,    0,   58,    0,   55,
   56,   61,   19,   16,    0,   18,   54,   53,   15,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   19,   20,   21,   22,   23,  103,   24,
   25,   26,   27,  117,  159,  172,   28,  120,   29,  122,
  123,   30,  228,   47,   48,  229,  249,  204,   31,   32,
  110,  145,  146,  189,   92,  126,   33,   38,  212,   49,
   50,   51,   52,   53,  136,
};
final static short yysindex[] = {                      -217,
  605,  711,    0, -273,    0,    0,    0,    0,  188,    0,
    0,  -15,  711, -208, -125,  648,    0,    0,    0, -209,
  -92,    0,    0,    0, -183,    0, -222, -175, -152, -160,
 -143,    0, -120,  658,    0,   98, -164, -127, -100,    0,
    0,    0,  150,  247,  -95,    0,  712, -135,    0,  101,
    0,    0,    0,  668, -177,  -86, -138,    0,    0, -162,
 -266,  247,   98,   98,  551,    0,    0,    0,  507,    0,
    0,    0,  247,    0,  289,    0, -232,  -10,  247,  247,
    0,  -71,  247,  299,  299,    0,    0,    0,    0,    0,
    0,  247, -178,  335,  299,  299,    0,    0,  -79,  -42,
 -140,    0,  -16, -140,    5,  -43,  115,    0,  133,  -30,
    0, -183,    0,  399,  -40,    0,  531,    0,  -65,  -23,
  -17,    0,    0,  -66,    0, -242,   25,  247,   31,  247,
   49,    0,    0,   67,  133,   74,   68,  335,  133,  138,
  101,  101,  133,  335,  125,  142,    0,    0,    0,    0,
 -201,  118, -140,   28,  163,  115,   98,    0,  171,  247,
  247,   30,  247,    0,    0,    0,    0,    0,    0,    0,
  436,    0,    0,  177,    0,  183,  201,  205,   42,  132,
  132,    0,  719,  196,    0,  197,    0,  335,    0,   98,
   98,    0,  218,   98,  222,    0,    0,  133,   75,  210,
  586,  -25,    0,  593,    0,    0,  224,    0,  132,  195,
  202,    0,    0,    0,    0, -234,    0,    0,    0,  -68,
  -48,   98,    2,    0,  214,  586,  586,  204,  223,  232,
  236,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -237,  263,  123,  265,  539,  231,  235,  586,    0,    0,
    0,    0,  250,    0,  251,  278,  256,    0, -223,    0,
    0,    0,    0,    0,  260,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  554,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,  390,    0,    0,
    0,    0,  413,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   91,
    0,    0,    0,  558,    0,    0,    0,    0,    0,    0,
  226,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  284,  345,
    0,   46,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  290,    0,    0,    0,    0,  178,
  136,  181,  -81,    0,    0, -220,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  292,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  487,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -174,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  327,    0,    0,
    0,    0,    0,    0,    0,    0,  489,    0,  -64,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -168,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -163,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  239,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  242,   93,  -53,  -27,  -33,  -20,    0,    0,  -44,  646,
  504,  508,    0,    0,  420,  -58,  -63,    0,  -61,    0,
    0,  -59, -119,  -45,  -36,  -73,    0,    0,    0,    0,
    0, -117,  -19,    0,    0,    0,    0,  542,  -77,  502,
  404,  395,  546,    0,    0,
};
final static int YYTABLESIZE=1016;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         61,
   28,  119,   70,  121,  173,  124,  118,   82,   35,  100,
   71,  116,  105,   63,    5,  108,  109,    7,    8,  253,
  184,  239,  102,  102,    5,   39,  186,    7,    8,   40,
   41,   42,  267,   43,  135,   80,  127,  139,  129,    1,
  131,  125,  134,    9,  254,   34,  143,    5,   12,  130,
    7,    8,  174,  119,   14,  121,  151,  124,  166,  154,
  119,  240,  121,  165,  124,  216,   70,  102,   45,   55,
  102,   65,  268,  157,  190,   80,    2,  162,  153,    5,
   39,   82,    7,    8,   40,   41,   42,   60,   43,   60,
  123,  177,   62,  179,  106,   68,    5,   64,  175,    7,
    8,   98,  213,  214,   78,   99,   66,  119,   59,  121,
   70,  124,  203,  144,  198,  107,   70,  202,    6,  192,
    6,   82,  157,   45,  199,    9,   59,   60,  262,   67,
   12,  234,   62,   69,  104,  121,   14,  119,   72,  121,
  119,  124,  121,   93,  124,  232,   59,   56,   10,   11,
   10,   11,  246,  247,   79,   70,   94,   73,   74,   57,
   70,   75,  119,  119,  121,  121,  124,  124,  219,  220,
  221,  259,    6,  223,   83,   83,   62,   80,   83,   83,
  122,  119,   83,  121,  119,  124,  121,   63,  124,  170,
    5,  108,  108,    7,    8,  108,  108,   83,  101,  149,
   83,  243,   10,   11,   83,   83,   83,  137,  241,   83,
   83,   63,   83,  108,  108,   83,  167,  108,   83,    9,
  138,  108,   84,   85,  114,   12,  108,  171,  242,  108,
   14,   63,  108,  115,  156,  108,  150,  163,   17,   84,
   85,    5,   39,   34,    7,    8,   40,   41,   42,  160,
   43,  132,  133,  230,   54,  152,   28,   28,  168,   28,
   28,   28,   44,  153,  169,   28,   28,   28,   28,   28,
   28,   28,   28,   28,   28,   28,  155,   28,  244,   28,
   28,   63,   28,  118,  153,   45,   28,   28,   28,   28,
   28,   28,   28,   28,   28,   28,   28,   28,   28,  193,
   28,   34,   34,  176,   34,   34,   34,  153,  200,  178,
   34,   34,   34,   34,   34,   34,   34,   34,   34,   34,
   34,  201,   34,  209,   34,   34,  117,   34,  210,  211,
  180,   34,   34,   34,   34,   34,   34,   34,   34,   34,
   34,   34,   34,   34,   72,   34,  123,  123,  181,  123,
  123,  123,  182,  225,    5,  123,  123,    7,    8,  183,
  123,  123,  123,  123,  123,  123,  226,   95,   96,  123,
  123,    5,  123,    6,    7,    8,  123,  123,  123,  123,
  123,  123,  123,  123,  123,  123,  123,  123,  123,   68,
  123,  121,  121,  191,  121,  121,  121,   84,   85,  256,
  121,  121,   63,   10,   11,  121,  121,  121,  121,  121,
  121,   40,   41,   42,  121,  121,  185,  121,  210,  211,
  187,  121,  121,  121,  121,  121,  121,  121,  121,  121,
  121,  121,  121,  121,  188,  121,  122,  122,  194,  122,
  122,  122,  120,  120,    5,  122,  122,    7,    8,  197,
  122,  122,  122,  122,  122,  122,  235,  236,  205,  122,
  122,  206,  122,  237,  238,   36,  122,  122,  122,  122,
  122,  122,  122,  122,  122,  122,  122,  122,  122,  207,
  122,   12,   12,  208,   12,   12,   12,  141,  142,  147,
  148,  217,  218,  222,   17,   17,  248,   17,   17,   17,
  224,  227,  233,    5,   39,  245,    7,    8,   40,   41,
   42,   12,   43,  251,   12,   12,   12,  252,  250,  255,
   12,  257,   12,   12,   17,   12,  260,   17,   17,   17,
  261,  263,  264,   17,  265,   17,   17,  266,   17,  118,
  118,  269,  118,  118,  118,    5,   39,   45,    7,    8,
   40,   41,   42,    5,   43,    5,   39,    4,    7,    8,
   40,   41,   42,  118,   43,  118,  128,  112,  135,  118,
   40,  113,  118,  118,  118,  195,  118,   77,  118,  118,
  118,  118,  117,  117,  140,  117,  117,  117,   81,   45,
    0,    5,    0,    0,    7,    8,    0,    0,    0,    0,
   72,   72,    0,   72,   72,   72,  117,    0,  117,    0,
    0,    0,  117,    0,    0,  117,  117,  117,    0,  117,
    9,  117,  117,  117,  117,   12,   72,    0,   69,    0,
   72,   14,    0,   72,   72,   72,    0,   72,    0,   72,
   72,   72,   72,    0,    0,   68,   68,    0,   68,   68,
   68,    0,    0,    0,   37,    5,   39,   46,    7,    8,
   40,   41,   42,    0,   43,    0,    0,    0,  101,  101,
    0,    0,  101,  101,    0,   68,  161,    0,   68,   68,
   68,   37,   68,    0,   68,   68,   68,   68,    0,   46,
    0,    0,    5,    0,    0,    7,    8,    0,  101,   45,
   46,    0,    0,  101,    0,    0,  101,   46,  111,  101,
    0,    0,  101,    0,    0,    0,    0,    0,   46,    0,
   46,    9,   46,    0,   46,   46,  114,    0,   46,   46,
   46,    0,   14,    0,    0,  115,    0,   46,    0,    0,
   46,   46,  105,  105,  102,  102,  105,  105,  102,  102,
    0,    0,  158,    0,    0,    0,    0,    0,    0,   46,
    0,    0,    0,    5,    0,    0,    7,    8,    0,    0,
    0,    0,  105,   46,  102,   46,    0,  105,    0,  102,
  105,    0,  102,  105,    0,  102,  105,    5,  102,    6,
    7,    8,    9,    0,    0,    5,    0,   12,    7,    8,
    0,  158,  196,   14,    0,   46,   46,    5,   46,    6,
    7,    8,    0,    0,    0,    0,    9,    0,    0,   10,
   11,  114,    0,    0,    9,  164,    0,   14,   15,  114,
  115,    0,  171,    0,  258,   14,    9,    0,  115,   10,
   11,  114,    5,    0,    0,    7,    8,   14,   15,    5,
  115,    0,    7,    8,    0,    0,    0,    0,    0,    0,
    4,    5,    0,    6,    7,    8,    0,    0,    0,    0,
    0,    9,    0,    0,    0,    0,  114,    0,    9,  171,
    0,    0,   14,  114,    0,  115,    0,  231,    0,   14,
    9,    0,  115,   10,   11,   12,    0,    0,   13,    0,
    0,   14,   15,    4,    5,    0,    6,    7,    8,    0,
    0,    0,    0,    4,    5,    0,    6,    7,    8,    0,
    0,    0,    0,    4,    5,    0,    6,    7,    8,    0,
    0,    0,    0,    9,    0,    0,   10,   11,   12,    0,
    0,    0,   58,    9,   14,   15,   10,   11,   12,    0,
    0,    0,   76,    9,   14,   15,   10,   11,   12,    0,
    0,    0,   97,    0,   14,   15,    4,    5,    0,    6,
    7,    8,    0,    0,    0,    5,   84,   85,    7,    8,
    0,   86,   87,   88,   89,   90,   91,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    9,    0,    0,   10,
   11,   12,    0,    0,    9,    0,    0,   14,   15,   12,
    0,    0,   69,    0,  215,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   65,   30,   65,  124,   65,   65,   44,  282,   55,
   30,   65,   57,  280,  257,  282,   62,  260,  261,  257,
  138,  256,   56,   57,  257,  258,  144,  260,  261,  262,
  263,  264,  256,  266,   80,  256,   73,   83,   75,  257,
   77,   69,   79,  286,  282,    0,   92,  257,  291,  282,
  260,  261,  295,  117,  297,  117,  101,  117,  117,  104,
  124,  296,  124,  117,  124,  183,   94,  101,  301,  278,
  104,  294,  296,  107,  276,  296,  294,  114,  280,  257,
  258,  256,  260,  261,  262,  263,  264,  256,  266,  299,
    0,  128,  256,  130,  257,  256,  257,  281,  126,  260,
  261,  279,  180,  181,  269,  283,  282,  171,   16,  171,
  138,  171,  171,  292,  160,  278,  144,  163,  259,  153,
  259,  296,  156,  301,  161,  286,   34,  296,  248,  282,
  291,  209,  296,  294,  273,    0,  297,  201,  282,  201,
  204,  201,  204,  279,  204,  204,   54,  273,  289,  290,
  289,  290,  226,  227,  282,  183,  292,  278,  279,  285,
  188,  282,  226,  227,  226,  227,  226,  227,  188,  190,
  191,  245,  259,  194,  256,  257,  269,  278,  260,  261,
    0,  245,  278,  245,  248,  245,  248,  280,  248,  256,
  257,  256,  257,  260,  261,  260,  261,  279,  285,  279,
  282,  222,  289,  290,  286,  287,  288,  279,  277,  291,
  292,  280,  294,  278,  279,  297,  282,  282,  300,  286,
  292,  286,  265,  266,  291,    0,  291,  294,  277,  294,
  297,  280,  297,  300,  278,  300,  279,  278,    0,  265,
  266,  257,  258,    2,  260,  261,  262,  263,  264,  280,
  266,  262,  263,  279,   13,  272,  256,  257,  282,  259,
  260,  261,  278,  280,  282,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  275,  272,  277,  277,  279,
  280,  280,  282,    0,  280,  301,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,  272,
  300,  256,  257,  279,  259,  260,  261,  280,  279,  279,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  292,  277,  282,  279,  280,    0,  282,  287,  288,
  282,  286,  287,  288,  289,  290,  291,  292,  293,  294,
  295,  296,  297,  298,    0,  300,  256,  257,  282,  259,
  260,  261,  279,  279,  257,  265,  266,  260,  261,  292,
  270,  271,  272,  273,  274,  275,  292,  267,  268,  279,
  280,  257,  282,  259,  260,  261,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,    0,
  300,  256,  257,  276,  259,  260,  261,  265,  266,  277,
  265,  266,  280,  289,  290,  270,  271,  272,  273,  274,
  275,  262,  263,  264,  279,  280,  279,  282,  287,  288,
  296,  286,  287,  288,  289,  290,  291,  292,  293,  294,
  295,  296,  297,  298,  293,  300,  256,  257,  276,  259,
  260,  261,  265,  266,  257,  265,  266,  260,  261,  279,
  270,  271,  272,  273,  274,  275,  262,  263,  282,  279,
  280,  279,  282,  262,  263,  278,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,  279,
  300,  256,  257,  279,  259,  260,  261,   84,   85,   95,
   96,  296,  296,  276,  256,  257,  293,  259,  260,  261,
  279,  292,  279,  257,  258,  292,  260,  261,  262,  263,
  264,  286,  266,  282,  289,  290,  291,  282,  296,  257,
  295,  257,  297,  298,  286,  300,  296,  289,  290,  291,
  296,  282,  282,  295,  257,  297,  298,  282,  300,  256,
  257,  282,  259,  260,  261,  257,  258,  301,  260,  261,
  262,  263,  264,    0,  266,  257,  258,    0,  260,  261,
  262,  263,  264,  280,  266,  282,  278,   64,  279,  286,
  279,   64,  289,  290,  291,  156,  293,   36,  295,  296,
  297,  298,  256,  257,   83,  259,  260,  261,   43,  301,
   -1,  257,   -1,   -1,  260,  261,   -1,   -1,   -1,   -1,
  256,  257,   -1,  259,  260,  261,  280,   -1,  282,   -1,
   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,  293,
  286,  295,  296,  297,  298,  291,  282,   -1,  294,   -1,
  286,  297,   -1,  289,  290,  291,   -1,  293,   -1,  295,
  296,  297,  298,   -1,   -1,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,    9,  257,  258,   12,  260,  261,
  262,  263,  264,   -1,  266,   -1,   -1,   -1,  256,  257,
   -1,   -1,  260,  261,   -1,  286,  278,   -1,  289,  290,
  291,   36,  293,   -1,  295,  296,  297,  298,   -1,   44,
   -1,   -1,  257,   -1,   -1,  260,  261,   -1,  286,  301,
   55,   -1,   -1,  291,   -1,   -1,  294,   62,   63,  297,
   -1,   -1,  300,   -1,   -1,   -1,   -1,   -1,   73,   -1,
   75,  286,   77,   -1,   79,   80,  291,   -1,   83,   84,
   85,   -1,  297,   -1,   -1,  300,   -1,   92,   -1,   -1,
   95,   96,  256,  257,  256,  257,  260,  261,  260,  261,
   -1,   -1,  107,   -1,   -1,   -1,   -1,   -1,   -1,  114,
   -1,   -1,   -1,  257,   -1,   -1,  260,  261,   -1,   -1,
   -1,   -1,  286,  128,  286,  130,   -1,  291,   -1,  291,
  294,   -1,  294,  297,   -1,  297,  300,  257,  300,  259,
  260,  261,  286,   -1,   -1,  257,   -1,  291,  260,  261,
   -1,  156,  157,  297,   -1,  160,  161,  257,  163,  259,
  260,  261,   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,  286,  295,   -1,  297,  298,  291,
  300,   -1,  294,   -1,  296,  297,  286,   -1,  300,  289,
  290,  291,  257,   -1,   -1,  260,  261,  297,  298,  257,
  300,   -1,  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,
   -1,  286,   -1,   -1,   -1,   -1,  291,   -1,  286,  294,
   -1,   -1,  297,  291,   -1,  300,   -1,  295,   -1,  297,
  286,   -1,  300,  289,  290,  291,   -1,   -1,  294,   -1,
   -1,  297,  298,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,
   -1,   -1,  295,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,   -1,  297,  298,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,  257,  265,  266,  260,  261,
   -1,  270,  271,  272,  273,  274,  275,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,  286,   -1,   -1,  297,  298,  291,
   -1,   -1,  294,   -1,  296,  297,
};
}
final static short YYFINAL=3;
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
"programa : BEGIN sentencias END",
"programa : IDENTIFICADOR_GENERICO sentencias END",
"programa : IDENTIFICADOR_GENERICO BEGIN sentencias",
"programa : IDENTIFICADOR_GENERICO sentencias",
"sentencias : sentencias sentencia",
"sentencias : sentencia",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencia : error PUNTO_Y_COMA",
"sentencia_declarativa : tipo lista_de_identificadores PUNTO_Y_COMA",
"sentencia_declarativa : tipo lista_de_identificadores",
"sentencia_declarativa : funcion",
"sentencia_declarativa : struct",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO",
"struct : TYPEDEF STRUCT lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF MENOR STRUCT lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF MENOR STRUCT lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R PUNTO_Y_COMA",
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
"encabezado_funcion : tipo FUN PARENTESIS_L parametro PARENTESIS_R",
"parametro : tipo identificador",
"parametro : identificador",
"parametro : tipo",
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
"sentencia_control_en_funcion : encabezado_for error",
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion error",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion THEN cuerpo_if_en_funcion END_IF",
"sentencia_seleccion_en_funcion : IF condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF",
"sentencia_seleccion_en_funcion : IF condicion THEN cuerpo_if_en_funcion END_IF",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN END_IF",
"cuerpo_if_en_funcion : bloque_de_sent_ejecutables_en_funcion bloque_else_en_funcion",
"cuerpo_if_en_funcion : bloque_de_sent_ejecutables_en_funcion",
"bloque_else_en_funcion : ELSE bloque_de_sent_ejecutables_en_funcion",
"bloque_else_en_funcion : ELSE",
"bloque_de_sent_ejecutables_en_funcion : BEGIN sentencias_ejecutable_en_funcion END PUNTO_Y_COMA",
"bloque_de_sent_ejecutables_en_funcion : sentencia_ejecutable_en_funcion",
"sentencias_ejecutable_en_funcion : sentencias_ejecutable_en_funcion sentencia_ejecutable_en_funcion",
"sentencias_ejecutable_en_funcion : sentencia_ejecutable_en_funcion",
"sentencia_ejecutable : sentencia_asignacion PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_asignacion",
"sentencia_ejecutable : sentencia_seleccion PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_salida PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_control",
"sentencia_asignacion : lista_de_identificadores ASIGNACION lista_de_expresiones",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if error",
"sentencia_seleccion : IF PARENTESIS_L condicion THEN cuerpo_if END_IF",
"sentencia_seleccion : IF condicion PARENTESIS_R THEN cuerpo_if END_IF",
"sentencia_seleccion : IF condicion THEN cuerpo_if END_IF",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN END_IF",
"cuerpo_if : bloque_de_sent_ejecutables bloque_else",
"cuerpo_if : bloque_de_sent_ejecutables",
"bloque_else : ELSE bloque_de_sent_ejecutables",
"bloque_else : ELSE",
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
"sentencia_salida : OUTF PARENTESIS_L PARENTESIS_R",
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

//#line 309 "gramatica.y"
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
  System.out.println("Error: " + string );
}
//#line 743 "Parser.java"
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
{ Parser.agregarEstructuraDetectadas(((Token) val_peek(3).obj).getNumeroDeLinea(), "PROGRAMA"); }
break;
case 2:
//#line 20 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea 1: Falta identificador de programa"); }
break;
case 3:
//#line 21 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + (((Token) val_peek(2).obj).getNumeroDeLinea()+1) + ": Falta un BEGIN despues del identificador del programa"); }
break;
case 4:
//#line 22 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Ultima linea: Falta un END al final del programa"); }
break;
case 5:
//#line 23 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta un BEGIN despues del identificador del programa y falta un END al final del programa"); }
break;
case 10:
//#line 33 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)val_peek(0).obj).getNumeroDeLinea());}
break;
case 11:
//#line 36 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S"); }
break;
case 12:
//#line 37 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ';' al final de la sentencia"); }
break;
case 13:
//#line 38 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FUNCION"); }
break;
case 15:
//#line 42 "gramatica.y"
{
                                                                                                                                                    yyval.ival = ((Token) val_peek(9).obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) val_peek(9).obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) val_peek(1).obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                }
break;
case 16:
//#line 48 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 17:
//#line 49 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 18:
//#line 50 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 19:
//#line 51 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 20:
//#line 52 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 21:
//#line 56 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 22:
//#line 57 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 23:
//#line 58 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 27:
//#line 67 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 28:
//#line 70 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 29:
//#line 71 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 30:
//#line 74 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 31:
//#line 75 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 32:
//#line 76 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 33:
//#line 79 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 34:
//#line 80 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 35:
//#line 83 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 36:
//#line 92 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 37:
//#line 97 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                            }
break;
case 39:
//#line 104 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 40:
//#line 105 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 45:
//#line 114 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 46:
//#line 115 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 47:
//#line 116 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 48:
//#line 117 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 49:
//#line 118 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 50:
//#line 121 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 51:
//#line 122 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 52:
//#line 125 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 53:
//#line 128 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 54:
//#line 129 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 55:
//#line 130 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 56:
//#line 131 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 57:
//#line 132 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 58:
//#line 133 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 62:
//#line 141 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 67:
//#line 152 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 68:
//#line 153 "gramatica.y"
{
                                                        Parser.agregarEstructuraDetectadas(val_peek(0).ival, "ASIGNACION");
                                                        agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ';' al final de la sentencia");
                                                    }
break;
case 69:
//#line 157 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 70:
//#line 158 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "SALIDA"); }
break;
case 71:
//#line 159 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 72:
//#line 162 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 73:
//#line 165 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 74:
//#line 166 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 75:
//#line 167 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 76:
//#line 168 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 77:
//#line 169 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 78:
//#line 170 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 82:
//#line 178 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 94:
//#line 200 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 95:
//#line 201 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 96:
//#line 202 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 97:
//#line 205 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 98:
//#line 206 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 99:
//#line 209 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 100:
//#line 210 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 101:
//#line 211 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 102:
//#line 212 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 103:
//#line 213 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 104:
//#line 214 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 105:
//#line 215 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 106:
//#line 218 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 107:
//#line 219 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 108:
//#line 220 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 109:
//#line 221 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 110:
//#line 222 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 129:
//#line 255 "gramatica.y"
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
case 131:
//#line 296 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 132:
//#line 297 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 133:
//#line 298 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
//#line 1270 "Parser.java"
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
