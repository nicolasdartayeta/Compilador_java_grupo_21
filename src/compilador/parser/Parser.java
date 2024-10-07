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
    0,    0,    2,    2,    3,    3,    3,    4,    4,    4,
    4,    4,    9,    9,    9,    9,    9,    9,    9,    6,
    6,    6,   10,   10,    7,    7,    7,   11,   11,    1,
    1,    1,   12,   12,    8,   13,   13,   15,   15,   15,
   15,   15,   14,   14,   14,   14,   16,   16,   16,   16,
   16,   16,   16,   20,   20,   21,   21,   21,   18,   18,
   18,   18,   18,   18,   18,   18,   18,   26,   26,   27,
   27,   23,   23,   23,   23,   28,   28,    5,    5,    5,
    5,    5,    5,   17,   29,   29,   29,   29,   29,   29,
   29,   29,   29,   32,   32,   34,   34,   25,   25,   35,
   35,   35,   35,   35,   35,   33,   33,   33,   33,   33,
   36,   36,   19,   19,   19,   19,   19,   19,   19,   19,
   30,   30,   22,   22,   22,   22,   22,   22,   22,   37,
   37,   37,   37,   37,   38,   38,   39,   39,   39,   39,
   39,   39,   31,   31,   24,   24,   24,   40,   40,   40,
   40,   41,   41,   41,   42,   42,   42,   42,   43,   43,
   43,   44,   44,   44,
};
final static short yylen[] = {                            2,
    4,    5,    5,    3,    3,    3,    2,    5,    4,    4,
    3,    5,    2,    1,    1,    1,    2,    3,    4,    4,
    1,    1,   10,    9,   11,   11,    9,    9,    8,    1,
    1,    1,    3,    1,    3,    2,    1,    1,    1,    1,
    1,    1,    3,    3,    4,    6,    5,    2,    1,    1,
    3,    1,    2,    2,    1,    1,    2,    3,    3,    1,
    1,    1,    1,    2,    2,    5,    6,    6,    8,    9,
    7,    9,    7,    7,    7,    6,    7,    2,    1,    2,
    1,    4,    5,    5,    1,    2,    1,    2,    3,    3,
    1,    1,    1,    3,    8,    9,    7,    9,    7,    7,
    7,    6,    7,    2,    1,    2,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    4,    5,    3,    5,    1,
    2,    1,    5,    5,    6,    6,    6,    6,    4,    4,
    2,    2,    6,    2,    1,    5,    5,    5,    4,    7,
    6,    6,    6,    6,    3,    3,    2,    2,    2,    2,
    1,    1,    3,    1,    4,    3,    1,    3,    3,    1,
    1,    3,    3,    1,    1,    1,    2,    1,    1,    1,
    1,    4,    3,    5,
};
final static short yydefred[] = {                         0,
   40,   41,   42,    0,    0,    0,    0,   32,    0,   31,
   30,    0,    0,    0,    0,    0,   14,   15,   16,    0,
    0,   21,   22,   37,   39,    0,    0,   92,    0,   91,
   93,    0,    0,    0,    0,   17,    0,    0,    0,    0,
    0,  169,  170,  171,    0,    0,    0,  165,    0,    0,
    0,    0,  164,  166,  168,    0,    0,    0,    0,    4,
   13,    0,    0,    0,    0,   36,    0,    0,   88,  132,
    0,  120,  131,    0,  134,    0,    0,    0,    0,    0,
    5,    0,    0,    0,    0,  167,    0,    0,  112,  113,
  110,  111,  114,  115,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   34,    0,    0,    0,
    0,   43,    0,    0,    0,   18,  161,  154,    0,   35,
    0,    0,   56,    0,   55,    0,   60,   61,   62,   63,
    0,   89,   90,    0,  122,    0,    0,    0,    0,    0,
    9,    0,    0,    0,    0,    0,  145,  146,    0,  173,
    0,    0,    0,  156,    0,  108,    0,    0,    0,    0,
    0,  162,  163,  130,  129,    0,    0,    0,    0,    0,
    0,    0,    0,   52,    0,   49,    0,   19,   20,    0,
    0,    0,    0,   45,   54,   53,    0,   57,   65,    0,
   85,   64,  118,    0,  121,    0,    0,    0,    3,    8,
    2,   12,    0,    0,    0,    0,  172,    0,    0,    0,
  155,    0,    0,    0,  104,    0,  123,    0,  124,    0,
    0,   33,    0,    0,    0,    0,   47,  153,    0,    0,
    0,    0,   58,   59,   87,    0,    0,  116,  137,    0,
  138,  152,  151,    0,    0,    0,  144,  143,  141,  174,
    0,    0,    0,    0,    0,  102,  106,  125,  127,  126,
  128,    0,    0,    0,    0,   46,   51,    0,    0,    0,
    0,    0,    0,    0,   86,  117,  119,  133,  140,  147,
  148,  150,  149,  103,   99,    0,   97,  100,  101,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   78,    0,
    0,   66,    0,   82,    0,   95,    0,   29,    0,    0,
    0,    0,    0,    0,    0,    0,   80,   76,   67,   68,
   83,   84,   96,   98,   28,   24,    0,   27,   77,   73,
    0,   71,   74,   75,    0,   23,    0,   69,   25,   26,
   70,   72,
};
final static short yydgoto[] = {                          5,
   15,   16,   17,   18,   19,   20,   21,   22,   23,  108,
   24,   25,   26,  124,  177,  191,   27,  127,   28,  129,
  130,   29,  271,   49,   50,  272,  299,  236,   30,   31,
  119,  158,  159,  215,   95,  136,   32,   39,  247,   51,
   52,   53,   54,   55,
};
final static short yysindex[] = {                      -216,
    0,    0,    0,  731,    0,  615, -273,    0,  -11,    0,
    0, -143, -212,  -58, -211,  625,    0,    0,    0, -189,
  -44,    0,    0,    0,    0, -201, -243,    0,  749,    0,
    0,  227,   -4,  741,  605,    0,  198, -172, -158,    0,
 -147,    0,    0,    0,  230,   82, -129,    0,  832,  -97,
   67,  232,    0,    0,    0,    2, -122,  -81,  198,    0,
    0,  -80,  570,   95,  198,    0,  508,  -56,    0,    0,
  130,    0,    0,   82,    0,   13,  731,    6,  668,  731,
    0,   36,  249,   82,   49,    0,    3,  801,    0,    0,
    0,    0,    0,    0,   95, -116,  758,  835,  835,  835,
  835, -102,  -30,    1,    8, -120,    0,   98, -120,  100,
 -211,    0,  -40,  439,   22,    0,    0,    0,    4,    0,
   63,   11,    0,  495,    0, -240,    0,    0,    0,    0,
   74,    0,    0,   26,    0,  793,   37,   82,   43,  678,
    0,   54,  125,  688,   82,   65,    0,    0,  102,    0,
  -49, -101,  758,    0, -133,    0,  758,  112,  117,  232,
  232,    0,    0,    0,    0, -220, -174, -265,  137, -120,
  105,  141,  439,    0,  198,    0,  143,    0,    0,   95,
   82,   75,   95,    0,    0,    0,   80,    0,    0,  560,
    0,    0,    0, -157,    0,  154,  159,  173,    0,    0,
    0,    0,  386, -228, -228,  190,    0,  751,  179,  187,
    0,  189,  182,  758,    0,   81,    0,   84,    0,  198,
  198,    0,  225,  198,  238,  266,    0,    0,   90,  235,
  543,  257,    0,    0,    0,  558,   91,    0,    0,  260,
    0,    0,    0, -228,  263,  272,    0,    0,    0,    0,
  261, -275,  269,  271,  277,    0,    0,    0,    0,    0,
    0,  461,  502,  198,  809,    0,    0,  -57,  543,  543,
  274,  268, -152, -140,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -134,    0,    0,    0, -231,
  297,  814,  311,  516,  273,  280,  281,  543,    0,  296,
  133,    0,  168,    0,  175,    0,  299,    0,  301,  327,
  303,  307, -117,  308,  313,  314,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -130,    0,    0,    0,
 -128,    0,    0,    0,  185,    0,  202,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  132,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  433,    0,    0,  593,    0,    0,    0,    0,  342,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  384,  216,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  601,  606,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -55,    0,    0,    0,    0,    0,    0,    0,    0,
  174,    0,    0,    0,    0,    0,    0,    0,  -68,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  609,  610,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -99,  258,
  300,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  332,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  446,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -54,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  339,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  453,
    0,    0,    0,  426,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -41,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -10,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    5,   23,   17,  -61,   -2,  -26,  -19,    0,    0,   -8,
   -9,  561,    0,    0,  449,  -63,  -50,    0,  -47,    0,
    0,  -37, -123,  -42,  -36, -245,    0,    0,    0,    0,
    0,  -46,  -27,    0,    0,    0,    0,  582, -186,  535,
  443,  447,  580,    0,
};
final static int YYTABLESIZE=1107;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         38,
   63,   73,   48,  125,    6,  123,  285,  192,   36,   87,
  220,   66,   68,  105,  170,  187,  126,  248,  249,  128,
  286,  118,  295,  296,  297,  307,   72,   38,   35,  131,
  107,  107,   61,  242,  243,  216,   48,  137,   69,  139,
    1,  188,  151,    2,    3,  146,   48,  149,  313,  110,
  308,   61,  156,   66,   48,  120,   79,  279,  245,  246,
  186,  217,  185,  111,   48,   56,   48,    1,  135,   59,
    2,    3,   48,  126,   48,   48,  128,    4,   48,  107,
  126,  218,  107,  128,  182,   48,  131,  175,   48,   48,
   48,   48,   67,  131,   72,   61,   83,  168,  237,  140,
  171,  197,  144,  301,  176,  209,  210,  219,  203,   62,
  212,   48,   40,    1,   41,  303,    2,    3,   42,   43,
   44,  305,   45,   84,  238,  335,  235,  337,   48,  302,
   85,   98,   99,  195,   46,   48,    8,  228,    8,  126,
  232,  304,  128,  222,  229,  211,  175,  306,   88,   72,
   72,  336,  131,  338,   72,    1,   61,   47,    2,    3,
   61,  252,  106,  176,  330,  226,   10,   11,   10,   11,
   48,   48,  275,   48,  317,  157,  113,    8,  331,  164,
  126,   96,  105,  128,    9,  126,  257,   94,  128,   12,
  208,  109,   71,  131,   97,   13,  105,  114,  131,    1,
  262,  263,    2,    3,  265,   72,  206,   10,   11,  161,
  161,   72,    1,   94,   57,    2,    3,  126,  126,  126,
  128,  128,  128,  161,   64,  132,   58,  107,    9,  207,
  131,  131,  131,  121,  294,   65,  190,  173,  133,   13,
   79,  107,  122,  126,  292,    1,  128,  126,    2,    3,
  128,  165,   66,   66,   79,   66,  131,  102,    1,   41,
  131,    2,    3,   42,   43,   44,   37,   45,   40,    1,
   41,   81,    2,    3,   42,   43,   44,   36,   45,  166,
  103,  152,   66,  180,  104,   81,  167,   36,  183,   77,
  138,   40,    1,   41,  153,    2,    3,   42,   43,   44,
  141,   45,   47,  178,  117,    1,   41,  193,    2,    3,
   42,   43,   44,   47,   45,  196,  179,  145,   40,    1,
   41,  198,    2,    3,   42,   43,   44,  150,   45,  189,
    1,   98,   99,    2,    3,   36,   47,   40,    1,   41,
  181,    2,    3,   42,   43,   44,  204,   45,  200,   47,
  117,    1,   41,  230,    2,    3,   42,   43,   44,    9,
   45,  233,  258,   47,  121,  260,  231,  190,  268,  169,
   13,  172,  276,  122,  234,  259,  223,  170,  261,  170,
  201,  269,   47,  205,  170,  277,    1,   38,   38,    2,
    3,   38,   38,   38,   38,   47,   38,   38,   38,   38,
   38,   38,   38,   38,   38,   38,   38,  213,   38,  214,
   38,   38,  221,   38,  319,    9,  224,   38,   38,   38,
   12,  227,   38,   38,  134,   38,   13,  320,   38,   44,
   44,   38,  239,   44,   44,   44,   44,  240,   44,   44,
   44,   44,   44,   44,   44,   44,   44,   44,   44,  321,
   44,  241,   44,   44,    1,   44,  323,    2,    3,   44,
   44,   44,  322,  256,   44,   44,  339,   44,  250,  324,
   44,  160,  160,   44,  253,  160,  160,  160,  160,  340,
  160,  160,  254,  341,  255,  160,  160,  160,  160,  160,
  160,   42,   43,   44,  160,  160,  342,  160,  100,  101,
  264,  160,  160,  160,   74,   75,  160,  160,   76,  160,
  147,  148,  160,  158,  158,  160,  266,  158,  158,  158,
  158,  267,  158,  158,  280,  281,  270,  158,  158,  158,
  158,  158,  158,  282,  283,  273,  158,  158,  278,  158,
  160,  161,  284,  158,  158,  158,  162,  163,  158,  158,
  287,  158,  288,  309,  158,  159,  159,  158,  289,  159,
  159,  159,  159,  300,  159,  159,  298,  311,  314,  159,
  159,  159,  159,  159,  159,  315,  316,  318,  159,  159,
  325,  159,  326,  327,  328,  159,  159,  159,  329,  332,
  159,  159,    7,  159,  333,  334,  159,  109,  109,  159,
   11,  109,  109,  109,  109,    6,  161,  161,   10,    1,
   50,  161,  161,  161,  161,  161,  161,   48,   82,  112,
  109,  225,  155,  109,   86,    0,    0,  109,  109,  109,
    0,    0,  109,  109,    0,  109,    0,    0,  109,  157,
  157,  109,    0,  157,  157,  157,  157,  242,  243,    0,
    0,    0,    0,  157,  157,  157,  157,  157,  157,    0,
    0,    0,  157,  157,    0,  157,    0,  244,    0,  157,
  157,  157,  245,  246,  157,  157,    0,  157,    0,    0,
  157,  142,  142,  157,    0,  142,  142,    0,  135,  135,
    0,    0,  135,  135,  174,    1,    0,    8,    2,    3,
    0,  139,  139,  142,  142,  139,  139,  142,  136,  136,
    0,  142,  136,  136,    0,    0,  142,    1,  135,  142,
    2,    3,  142,  135,    0,  142,  135,   10,   11,  135,
    0,  139,  135,    0,    0,    0,  139,  290,  136,  139,
   65,    0,  139,  136,    0,  139,  136,    0,    0,  136,
    0,    1,  136,    8,    2,    3,    0,    0,    1,    0,
    0,    2,    3,    0,    1,    0,    8,    2,    3,    0,
    0,    0,    1,    0,    0,    2,    3,    0,  291,    0,
    9,   65,    0,   10,   11,  121,    0,    0,    0,  184,
    0,   13,   14,    9,  122,    0,   10,   11,  121,    1,
    0,    9,    2,    3,   13,   14,  121,  122,    0,  190,
    0,  312,   13,    0,    1,  122,    1,    2,    3,    2,
    3,    0,    0,    0,    0,  115,    1,    0,    9,    2,
    3,    0,    0,  121,    0,    0,  190,    0,    0,   13,
    0,    0,  122,    9,    0,    9,    0,    0,  121,   65,
  121,  116,  274,    0,   13,    0,   13,  122,    0,  122,
    7,    1,    0,    8,    2,    3,    0,    0,    0,    0,
   33,    1,    0,    8,    2,    3,    0,    0,    0,    0,
    7,    1,    0,    8,    2,    3,    0,    0,    0,    0,
    9,    0,    0,   10,   11,   12,    0,    0,   80,   81,
    9,   13,   14,   10,   11,   12,    0,    0,   34,    0,
    9,   13,   14,   10,   11,   12,    0,    0,    0,   60,
    0,   13,   14,  142,    1,    0,    8,    2,    3,    0,
    0,    0,    0,    7,    1,    0,    8,    2,    3,    0,
    0,    0,    0,    7,    1,    0,    8,    2,    3,    0,
    0,    0,    0,    9,    0,    0,   10,   11,   12,    0,
    0,    0,  143,    9,   13,   14,   10,   11,   12,    0,
    0,    0,  199,    9,   13,   14,   10,   11,   12,    0,
    0,    0,  202,    0,   13,   14,    7,    1,    0,    8,
    2,    3,    0,    0,    0,    0,   78,    1,    0,    8,
    2,    3,    0,    0,   70,    1,    0,    1,    2,    3,
    2,    3,    0,    0,    1,    0,    9,    2,    3,   10,
   11,   12,    0,    0,    0,    0,    9,   13,   14,   10,
   11,   12,    0,    0,    9,    0,    9,   13,   14,   12,
    0,   12,   71,    9,   71,   13,  251,   13,   12,    1,
    0,   71,    2,    3,   13,    0,  117,    1,   41,    0,
    2,    3,   42,   43,   44,    1,   45,    0,    2,    3,
    1,    0,    0,    2,    3,    0,    0,    0,    9,  154,
    0,    0,    0,   12,    0,  293,    0,  194,   65,   13,
  310,    1,   41,   65,    2,    3,   42,   43,   44,    0,
   45,   89,   90,   91,   92,   93,   94,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   20,   29,   12,   67,    0,   67,  282,  131,  282,   46,
  276,   21,  256,   56,  280,  256,   67,  204,  205,   67,
  296,   64,  268,  269,  270,  257,   29,   37,    6,   67,
   57,   58,   16,  262,  263,  256,   46,   74,  282,   76,
  257,  282,   85,  260,  261,   82,   56,   84,  294,   58,
  282,   35,   95,   63,   64,   65,   34,  244,  287,  288,
  124,  282,  124,   59,   74,  278,   76,  257,   71,  281,
  260,  261,   82,  124,   84,   85,  124,  294,   88,  106,
  131,  256,  109,  131,  121,   95,  124,  114,   98,   99,
  100,  101,  294,  131,   97,   79,  269,  106,  256,   77,
  109,  138,   80,  256,  114,  152,  153,  282,  145,  299,
  157,  121,  256,  257,  258,  256,  260,  261,  262,  263,
  264,  256,  266,  282,  282,  256,  190,  256,  138,  282,
  278,  265,  266,  136,  278,  145,  259,  180,  259,  190,
  183,  282,  190,  170,  181,  279,  173,  282,  278,  152,
  153,  282,  190,  282,  157,  257,  140,  301,  260,  261,
  144,  208,  285,  173,  282,  175,  289,  290,  289,  290,
  180,  181,  236,  183,  298,  292,  257,  259,  296,  282,
  231,  279,  282,  231,  286,  236,  214,  256,  236,  291,
  292,  273,  294,  231,  292,  297,  296,  278,  236,  257,
  220,  221,  260,  261,  224,  208,  256,  289,  290,  265,
  266,  214,  257,  282,  273,  260,  261,  268,  269,  270,
  268,  269,  270,  279,  269,  282,  285,  282,  286,  279,
  268,  269,  270,  291,  292,  280,  294,  278,  295,  297,
  282,  296,  300,  294,  264,  257,  294,  298,  260,  261,
  298,  282,  262,  263,  296,  265,  294,  256,  257,  258,
  298,  260,  261,  262,  263,  264,  278,  266,  256,  257,
  258,  282,  260,  261,  262,  263,  264,  282,  266,  279,
  279,  279,  292,  280,  283,  296,  279,  282,  278,  294,
  278,  256,  257,  258,  292,  260,  261,  262,  263,  264,
  295,  266,  301,  282,  256,  257,  258,  282,  260,  261,
  262,  263,  264,  301,  266,  279,  295,  282,  256,  257,
  258,  279,  260,  261,  262,  263,  264,  279,  266,  256,
  257,  265,  266,  260,  261,  282,  301,  256,  257,  258,
  278,  260,  261,  262,  263,  264,  282,  266,  295,  301,
  256,  257,  258,  279,  260,  261,  262,  263,  264,  286,
  266,  282,  282,  301,  291,  282,  292,  294,  279,  272,
  297,  272,  282,  300,  295,  295,  272,  280,  295,  280,
  256,  292,  301,  282,  280,  295,  257,  256,  257,  260,
  261,  260,  261,  262,  263,  301,  265,  266,  267,  268,
  269,  270,  271,  272,  273,  274,  275,  296,  277,  293,
  279,  280,  276,  282,  282,  286,  276,  286,  287,  288,
  291,  279,  291,  292,  295,  294,  297,  295,  297,  256,
  257,  300,  279,  260,  261,  262,  263,  279,  265,  266,
  267,  268,  269,  270,  271,  272,  273,  274,  275,  282,
  277,  279,  279,  280,  257,  282,  282,  260,  261,  286,
  287,  288,  295,  282,  291,  292,  282,  294,  279,  295,
  297,  256,  257,  300,  296,  260,  261,  262,  263,  295,
  265,  266,  296,  282,  296,  270,  271,  272,  273,  274,
  275,  262,  263,  264,  279,  280,  295,  282,  267,  268,
  276,  286,  287,  288,  278,  279,  291,  292,  282,  294,
  262,  263,  297,  256,  257,  300,  279,  260,  261,  262,
  263,  256,  265,  266,  262,  263,  292,  270,  271,  272,
  273,  274,  275,  262,  263,  279,  279,  280,  279,  282,
   98,   99,  282,  286,  287,  288,  100,  101,  291,  292,
  282,  294,  282,  257,  297,  256,  257,  300,  282,  260,
  261,  262,  263,  296,  265,  266,  293,  257,  296,  270,
  271,  272,  273,  274,  275,  296,  296,  282,  279,  280,
  282,  282,  282,  257,  282,  286,  287,  288,  282,  282,
  291,  292,    0,  294,  282,  282,  297,  256,  257,  300,
    0,  260,  261,  262,  263,    0,  265,  266,    0,    0,
  279,  270,  271,  272,  273,  274,  275,  279,   37,   59,
  279,  173,   88,  282,   45,   -1,   -1,  286,  287,  288,
   -1,   -1,  291,  292,   -1,  294,   -1,   -1,  297,  256,
  257,  300,   -1,  260,  261,  262,  263,  262,  263,   -1,
   -1,   -1,   -1,  270,  271,  272,  273,  274,  275,   -1,
   -1,   -1,  279,  280,   -1,  282,   -1,  282,   -1,  286,
  287,  288,  287,  288,  291,  292,   -1,  294,   -1,   -1,
  297,  256,  257,  300,   -1,  260,  261,   -1,  256,  257,
   -1,   -1,  260,  261,  256,  257,   -1,  259,  260,  261,
   -1,  256,  257,  278,  279,  260,  261,  282,  256,  257,
   -1,  286,  260,  261,   -1,   -1,  291,  257,  286,  294,
  260,  261,  297,  291,   -1,  300,  294,  289,  290,  297,
   -1,  286,  300,   -1,   -1,   -1,  291,  277,  286,  294,
  280,   -1,  297,  291,   -1,  300,  294,   -1,   -1,  297,
   -1,  257,  300,  259,  260,  261,   -1,   -1,  257,   -1,
   -1,  260,  261,   -1,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,  257,   -1,   -1,  260,  261,   -1,  277,   -1,
  286,  280,   -1,  289,  290,  291,   -1,   -1,   -1,  295,
   -1,  297,  298,  286,  300,   -1,  289,  290,  291,  257,
   -1,  286,  260,  261,  297,  298,  291,  300,   -1,  294,
   -1,  296,  297,   -1,  257,  300,  257,  260,  261,  260,
  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  286,  260,
  261,   -1,   -1,  291,   -1,   -1,  294,   -1,   -1,  297,
   -1,   -1,  300,  286,   -1,  286,   -1,   -1,  291,  280,
  291,  282,  295,   -1,  297,   -1,  297,  300,   -1,  300,
  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,
  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,
  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,
  286,   -1,   -1,  289,  290,  291,   -1,   -1,  294,  295,
  286,  297,  298,  289,  290,  291,   -1,   -1,  294,   -1,
  286,  297,  298,  289,  290,  291,   -1,   -1,   -1,  295,
   -1,  297,  298,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,
   -1,   -1,  295,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,   -1,  297,  298,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,  256,  257,   -1,  257,  260,  261,
  260,  261,   -1,   -1,  257,   -1,  286,  260,  261,  289,
  290,  291,   -1,   -1,   -1,   -1,  286,  297,  298,  289,
  290,  291,   -1,   -1,  286,   -1,  286,  297,  298,  291,
   -1,  291,  294,  286,  294,  297,  296,  297,  291,  257,
   -1,  294,  260,  261,  297,   -1,  256,  257,  258,   -1,
  260,  261,  262,  263,  264,  257,  266,   -1,  260,  261,
  257,   -1,   -1,  260,  261,   -1,   -1,   -1,  286,  279,
   -1,   -1,   -1,  291,   -1,  277,   -1,  295,  280,  297,
  277,  257,  258,  280,  260,  261,  262,  263,  264,   -1,
  266,  270,  271,  272,  273,  274,  275,
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
"programa : identificador_simple sentencias BEGIN sentencias END",
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
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R cuerpo_if_en_funcion END_IF PUNTO_Y_COMA",
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
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R cuerpo_if END_IF PUNTO_Y_COMA",
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

//#line 341 "gramatica.y"
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
//#line 833 "Parser.java"
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
case 12:
//#line 30 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(3).obj).getNumeroDeLinea() + ": No se permite sentencias entre el identificador del programa y el BEGIN."); }
break;
case 17:
//#line 40 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)val_peek(0).obj).getNumeroDeLinea() + ": Syntax Error"); }
break;
case 18:
//#line 43 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S"); }
break;
case 19:
//#line 44 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 20:
//#line 45 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 21:
//#line 46 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FUNCION"); }
break;
case 23:
//#line 50 "gramatica.y"
{
                                                                                                                                                    yyval.ival = ((Token) val_peek(9).obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) val_peek(9).obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) val_peek(1).obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                }
break;
case 24:
//#line 56 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 25:
//#line 57 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(10).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 26:
//#line 58 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(10).ival + ": Falta ';' al final de la sentencia"); }
break;
case 27:
//#line 59 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 28:
//#line 60 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 29:
//#line 61 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 30:
//#line 65 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 31:
//#line 66 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 32:
//#line 67 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 36:
//#line 75 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ',' entre las variables"); }
break;
case 37:
//#line 76 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 38:
//#line 79 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 39:
//#line 80 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
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
//#line 85 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 43:
//#line 88 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 44:
//#line 89 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 45:
//#line 92 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 46:
//#line 101 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 47:
//#line 106 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                             }
break;
case 49:
//#line 113 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 50:
//#line 114 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 51:
//#line 115 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": La funcion no puede tener mas de un parametro");}
break;
case 52:
//#line 116 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ yyval.ival + ": Falta el parametro de la funcion");}
break;
case 57:
//#line 125 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 58:
//#line 126 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 59:
//#line 127 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 60:
//#line 128 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "IF"); }
break;
case 62:
//#line 130 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 63:
//#line 131 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 64:
//#line 134 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 65:
//#line 135 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 66:
//#line 138 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 67:
//#line 139 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 68:
//#line 140 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 69:
//#line 143 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 70:
//#line 144 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 71:
//#line 145 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
break;
case 72:
//#line 146 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 73:
//#line 147 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 74:
//#line 148 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 75:
//#line 149 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 76:
//#line 150 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 77:
//#line 151 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 81:
//#line 159 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 83:
//#line 163 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 84:
//#line 164 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 88:
//#line 172 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 89:
//#line 173 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 90:
//#line 174 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 93:
//#line 177 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 94:
//#line 180 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 95:
//#line 183 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 96:
//#line 184 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia de selección");}
break;
case 97:
//#line 185 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
break;
case 98:
//#line 186 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 99:
//#line 187 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 100:
//#line 188 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 101:
//#line 189 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 102:
//#line 190 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 103:
//#line 191 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 107:
//#line 199 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 109:
//#line 203 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
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
case 113:
//#line 209 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 114:
//#line 210 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 115:
//#line 211 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 117:
//#line 215 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 118:
//#line 216 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
break;
case 119:
//#line 217 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 123:
//#line 225 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 124:
//#line 226 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 125:
//#line 227 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 126:
//#line 228 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 127:
//#line 229 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 128:
//#line 230 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 129:
//#line 231 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 130:
//#line 232 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 131:
//#line 235 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 132:
//#line 236 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 133:
//#line 239 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 134:
//#line 240 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 135:
//#line 241 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 136:
//#line 242 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 137:
//#line 243 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 138:
//#line 244 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 139:
//#line 245 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 140:
//#line 248 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 141:
//#line 249 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 142:
//#line 250 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 143:
//#line 251 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 144:
//#line 252 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 151:
//#line 263 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 152:
//#line 264 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 155:
//#line 271 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 156:
//#line 272 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 161:
//#line 279 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + " :Falta operador u operandos"); }
break;
case 167:
//#line 289 "gramatica.y"
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
case 169:
//#line 330 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 170:
//#line 331 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 171:
//#line 332 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 173:
//#line 336 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 174:
//#line 337 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1533 "Parser.java"
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
