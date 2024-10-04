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
    3,    3,    3,    3,    3,    8,    5,    5,    5,    9,
    9,    6,    6,   10,   10,   11,   11,   11,   12,   12,
    7,   13,   13,   15,   15,   15,   14,   14,   14,   14,
   16,   16,   16,   16,   16,   20,   20,   21,   18,   18,
   18,   18,   18,   18,   26,   26,   27,   27,   23,   23,
   28,   28,    4,    4,    4,    4,    4,   17,   29,   29,
   29,   29,   29,   29,   32,   32,   34,   34,   25,   35,
   35,   35,   35,   35,   35,   33,   33,   36,   36,   19,
   19,   19,   30,   30,   22,   22,   22,   22,   22,   22,
   22,   37,   37,   37,   37,   37,   38,   38,   39,   39,
   39,   39,   31,   31,   24,   24,   40,   40,   40,   41,
   41,   41,   42,   42,   42,   42,   43,   43,   43,   44,
   45,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    2,    1,    1,    1,    2,
    3,    2,    1,    2,    1,    9,    1,    1,    1,    3,
    1,    3,    1,    1,    1,    1,    1,    1,    3,    3,
    4,    6,    5,    2,    1,    1,    2,    2,    1,    1,
    2,    2,    2,    1,    1,    2,    2,    5,    7,    7,
    6,    6,    5,    6,    2,    1,    2,    1,    4,    1,
    2,    1,    2,    1,    2,    2,    1,    3,    7,    7,
    6,    6,    5,    6,    2,    1,    2,    1,    3,    1,
    1,    1,    1,    1,    1,    4,    1,    2,    1,    4,
    4,    3,    2,    2,    6,    2,    1,    5,    5,    5,
    4,    7,    6,    6,    6,    6,    3,    3,    2,    2,
    2,    2,    3,    1,    4,    1,    3,    3,    1,    3,
    3,    1,    1,    1,    2,    1,    1,    1,    1,    4,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   26,   19,   27,   28,    0,   18,
   17,    0,    0,    0,    0,    0,    7,    8,    9,    0,
    0,   13,    0,   23,    0,   25,    0,    0,    0,    0,
    0,   67,    0,    0,   10,    0,    0,    0,    0,  127,
  128,  129,    0,    0,    0,  123,    0,    0,  116,    0,
  122,  124,  126,    0,    0,    0,    3,    6,    0,    0,
    0,    0,   14,    0,    0,   63,   66,   94,    0,   87,
   93,   65,    0,   96,    0,    2,    0,    0,    0,    0,
  125,    0,    0,    0,    0,   82,   83,   80,   81,   84,
   85,    0,    0,    0,    0,    0,    1,   92,    0,    0,
    0,    0,    0,   11,    0,    0,   22,    0,   29,    0,
    0,   40,    0,   39,    0,    0,    0,   44,   45,    0,
   89,    0,    0,    0,    0,    0,    0,  107,  108,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  120,  121,   90,   91,   21,    0,    0,    0,
   35,    0,    0,    0,    0,    0,   31,   38,   37,   41,
   42,   43,   47,    0,   60,   46,    0,   88,    0,    0,
    0,    0,    0,    0,  130,    0,    0,  115,    0,   73,
    0,   75,    0,    0,    0,   34,   33,    0,    0,    0,
    0,    0,   62,    0,   86,   99,    0,  100,    0,    0,
    0,  106,  105,  103,   74,    0,   71,   72,   77,    0,
   20,   32,    0,    0,    0,    0,    0,    0,    0,   61,
   95,  102,  109,  110,  112,  111,   70,   69,    0,    0,
    0,    0,    0,   55,   53,   48,   59,    0,   54,    0,
   51,   52,   57,   16,   50,   49,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   19,   20,   21,   22,   23,  148,   24,
   25,   26,   27,  113,  152,  165,   28,  116,   29,  118,
  119,   30,  216,   47,   48,  217,  234,  194,   31,   32,
  106,  141,  142,  182,   92,  122,   33,   38,  202,   49,
   50,   51,   52,   53,  132,
};
final static short yysindex[] = {                      -246,
  618,  691,    0, -265,    0,    0,    0,    0,  -46,    0,
    0, -233,  691, -255, -206,  628,    0,    0,    0, -218,
 -213,    0, -211,    0, -232,    0, -184, -144, -134,  699,
 -122,    0, -171,  638,    0, -242, -129, -109, -133,    0,
    0,    0,   67,  290,  -93,    0,  462, -214,    0,  -73,
    0,    0,    0,  681, -164,  -75,    0,    0, -194,  -42,
  290, -242,    0, -242,  561,    0,    0,    0,  -32,    0,
    0,    0,  290,    0, -137,    0,  245,  -29,  290,  290,
    0, -197,  290,  411,  411,    0,    0,    0,    0,    0,
    0,  290,  -89,  335,  411,  411,    0,    0,  -67,  -56,
  -83,  -54,  115,    0,  -45,  -53,    0, -232,    0,  -15,
  -37,    0,  519,    0,  -38,   -5,   -3,    0,    0,  403,
    0,  708,    3,  290,    6,  290,   18,    0,    0,   22,
  -45,   29,  -28,  335,  -45,   43,  -73,  -73,  -45,  335,
   28,  -43,    0,    0,    0,    0,    0, -259,  115, -242,
    0,   70,  290,  290, -145,  290,    0,    0,    0,    0,
    0,    0,    0, -111,    0,    0,   73,    0,   81,   90,
  119,  -52,  -35,  -35,    0,  701,   98,    0,  103,    0,
  335,    0,  124,  -83,  133,    0,    0,  -45, -135,  111,
  569,  -48,    0,  576,    0,    0,  134,    0,  -35,   -7,
   47,    0,    0,    0,    0, -236,    0,    0,    0, -242,
    0,    0,  122,  569,  569,  126,  121,  138,  139,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -191,  491,
  143,  147,  569,    0,    0,    0,    0,  178,    0, -230,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  237,    0,    0,    0,    0,
    0,    0,  226,    0,    1,    0,    0,  390,    0,    0,
    0,    0,  405,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   91,
    0,    0,    0,  444,    0,    0,    0,    0,    0,  239,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  284,  345,    0,   46,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  166,    0,    0,    0,    0,   88,  136,  181,  -95,    0,
    0, -221,    0,    0,    0,    0,    0,    0,    0,  169,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  453,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -219,    0,    0,    0,    0,    0,    0,  327,    0,    0,
    0,    0,    0,    0,    0,    0,  507,    0,  -78,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -216,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -209,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  175,   58,  -51,  -18,  -94,  -20,    0,    0,    0,  646,
  385,  386,    0,    0,  308,  -55,  -63,    0,  -61,    0,
    0,  -59, -115,  -39,  -41, -142,    0,    0,    0,    0,
    0,  -70,  -22,    0,    0,    0,    0,  422,  -31,  376,
  274,  272,  419,    0,    0,
};
final static int YYTABLESIZE=1005;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         60,
   24,  115,   82,  117,  166,  120,  147,   71,  150,  114,
    1,   70,  183,  112,    5,  100,   35,    7,    8,  227,
  184,  105,   55,    5,   39,  245,    7,    8,   40,   41,
   42,  123,   43,  125,   76,  127,   78,  130,    5,   56,
  131,    7,    8,  135,   44,   30,   58,    2,   64,  115,
  121,  117,  139,  120,  150,   61,  115,  159,  117,  228,
  120,  158,  102,  177,   93,  246,   62,   45,  155,  179,
   63,  231,  232,   58,   76,   70,   78,   94,   56,   56,
   59,  133,  170,  103,  172,  238,   58,  240,   62,  211,
  119,   58,    5,   39,  134,    7,    8,   40,   41,   42,
  115,   43,  117,  168,  120,  206,   73,   74,  193,   65,
   75,   58,  189,  188,   98,   70,  192,  243,   99,    5,
   39,   70,    7,    8,   40,   41,   42,  115,   43,  117,
  115,  120,  117,  190,  120,  117,   45,   66,  220,   78,
  124,  203,  204,  213,   80,    5,  191,   67,    7,    8,
  115,  115,  117,  117,  120,  120,  214,   70,  209,   72,
   79,   79,   70,   45,   79,   79,  115,  222,  117,  115,
  120,  117,   79,  120,    9,    6,   34,  104,  104,  110,
  118,  104,  104,   79,   83,   14,   79,   54,  111,  229,
   79,   79,   79,   95,   96,   79,   79,  101,   79,  104,
  104,   79,  140,  104,   79,   10,   11,  104,   84,   85,
    5,  145,  104,    7,    8,  104,   84,   85,  104,   84,
   85,  104,  146,  149,    5,   15,  153,    7,    8,  199,
  218,   36,  128,  129,  200,  201,    5,   62,   12,  104,
  156,    5,   39,  160,    7,    8,   40,   41,   42,  181,
   43,  200,  201,    9,  223,  224,   24,   24,   12,   24,
   24,   24,  154,  176,   14,   24,   24,   24,   24,   24,
   24,   24,   24,   24,   24,   24,  161,   24,  162,   24,
   24,  169,   24,  114,  171,   45,   24,   24,   24,   24,
   24,   24,   24,   24,   24,   24,   24,   24,   24,  173,
   24,   30,   30,  174,   30,   30,   30,  175,  225,  226,
   30,   30,   30,   30,   30,   30,   30,   30,   30,   30,
   30,  178,   30,  180,   30,   30,  113,   30,   40,   41,
   42,   30,   30,   30,   30,   30,   30,   30,   30,   30,
   30,   30,   30,   30,   68,   30,  119,  119,  187,  119,
  119,  119,  116,  116,  195,  119,  119,  137,  138,  196,
  119,  119,  119,  119,  119,  119,  143,  144,  197,  119,
  119,    5,  119,    6,    7,    8,  119,  119,  119,  119,
  119,  119,  119,  119,  119,  119,  119,  119,  119,   64,
  119,  117,  117,  207,  117,  117,  117,  198,  208,  210,
  117,  117,  215,   10,   11,  117,  117,  117,  117,  117,
  117,  212,  221,  230,  117,  117,  235,  117,  233,  236,
  237,  117,  117,  117,  117,  117,  117,  117,  117,  117,
  117,  117,  117,  117,  244,  117,  118,  118,  241,  118,
  118,  118,  242,    4,  131,  118,  118,   36,  108,  109,
  118,  118,  118,  118,  118,  118,  185,   77,  136,  118,
  118,   81,  118,    0,    0,    0,  118,  118,  118,  118,
  118,  118,  118,  118,  118,  118,  118,  118,  118,    0,
  118,   15,   15,    0,   15,   15,   15,    0,    0,    0,
    0,    0,    0,    0,   12,   12,    0,   12,   12,   12,
    0,    5,   39,    0,    7,    8,   40,   41,   42,    0,
   43,   15,    0,    0,   15,   15,   15,    0,    0,    0,
   15,    0,   15,   15,   12,   15,  126,   12,   12,   12,
    0,    0,    0,   12,    0,   12,   12,    0,   12,  114,
  114,    0,  114,  114,  114,   45,    5,   39,    0,    7,
    8,   40,   41,   42,    0,   43,    0,    0,    0,    0,
    0,    0,    0,  114,    0,  114,    0,    0,    0,  114,
    0,    0,  114,  114,  114,    0,  114,    0,  114,  114,
  114,  114,  113,  113,    0,  113,  113,  113,    0,    0,
   45,    5,    0,    0,    7,    8,    0,    0,    0,    0,
   68,   68,    0,   68,   68,   68,  113,    0,  113,    0,
    0,    0,  113,    0,    0,  113,  113,  113,    0,  113,
    9,  113,  113,  113,  113,   12,   68,    0,   69,    0,
   68,   14,    0,   68,   68,   68,    0,   68,    0,   68,
   68,   68,   68,    0,    0,   64,   64,    0,   64,   64,
   64,    0,    0,    0,   37,    0,    0,   46,  163,    5,
   97,   97,    7,    8,   97,   97,    0,    5,   39,    0,
    7,    8,   40,   41,   42,   64,   43,    0,   64,   64,
   64,   37,   64,    0,   64,   64,   64,   64,    9,   46,
   97,    0,    0,  110,    0,   97,  164,    0,   97,   14,
   46,   97,  111,    0,   97,    0,   46,  107,  101,  101,
    0,    0,  101,  101,    0,    0,    0,    0,   46,    0,
   46,    0,   46,    0,   46,   46,   84,   85,   46,   46,
   46,   86,   87,   88,   89,   90,   91,   46,  101,    0,
   46,   46,    0,  101,    0,    0,  101,    5,  151,  101,
    7,    8,  101,    0,    0,   46,    0,    0,    0,    0,
    0,    0,   98,   98,    0,    0,   98,   98,    0,   46,
    0,   46,    0,    0,    0,    5,    9,    6,    7,    8,
    0,  110,    0,    0,  164,    0,  239,   14,    0,    0,
  111,    0,   98,    0,  151,  186,    0,   98,   46,   46,
   98,   46,    0,   98,    9,    0,   98,   10,   11,  110,
    0,    0,    0,  157,    0,   14,   15,    5,  111,    6,
    7,    8,    0,    0,    0,    5,    0,    0,    7,    8,
    0,    0,    5,    0,    0,    7,    8,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    9,    0,    0,   10,
   11,  110,    0,    0,    9,    0,    0,   14,   15,  110,
  111,    9,  164,    0,    0,   14,  110,    0,  111,    0,
  219,    0,   14,    4,    5,  111,    6,    7,    8,    0,
    0,    0,    0,    4,    5,    0,    6,    7,    8,    0,
    0,    0,    0,    4,    5,    0,    6,    7,    8,    0,
    0,    0,    0,    9,    0,    0,   10,   11,   12,    0,
    0,   13,    0,    9,   14,   15,   10,   11,   12,    0,
    0,    0,   57,    9,   14,   15,   10,   11,   12,    0,
    0,    0,   76,    0,   14,   15,    4,    5,    0,    6,
    7,    8,    0,    0,    0,    0,    4,    5,    0,    6,
    7,    8,    0,    0,   68,    5,    0,    5,    7,    8,
    7,    8,    0,    0,    5,    0,    9,    7,    8,   10,
   11,   12,    0,    0,    0,   97,    9,   14,   15,   10,
   11,   12,    0,    0,    9,    0,    9,   14,   15,   12,
    0,   12,   69,    9,   69,   14,  205,   14,   12,    0,
    0,    0,  167,    0,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   65,   44,   65,  120,   65,  101,   30,  103,   65,
  257,   30,  272,   65,  257,   55,  282,  260,  261,  256,
  280,   61,  278,  257,  258,  256,  260,  261,  262,  263,
  264,   73,  266,   75,  256,   77,  256,   79,  257,  256,
   80,  260,  261,   83,  278,    0,  256,  294,  281,  113,
   69,  113,   92,  113,  149,  269,  120,  113,  120,  296,
  120,  113,  257,  134,  279,  296,  280,  301,  110,  140,
  282,  214,  215,   16,  296,   94,  296,  292,  285,  296,
  299,  279,  124,  278,  126,  277,  296,  230,  280,  184,
    0,   34,  257,  258,  292,  260,  261,  262,  263,  264,
  164,  266,  164,  122,  164,  176,  278,  279,  164,  294,
  282,   54,  154,  153,  279,  134,  156,  233,  283,  257,
  258,  140,  260,  261,  262,  263,  264,  191,  266,  191,
  194,  191,  194,  279,  194,    0,  301,  282,  194,  269,
  278,  173,  174,  279,  278,  257,  292,  282,  260,  261,
  214,  215,  214,  215,  214,  215,  292,  176,  181,  282,
  256,  257,  181,  301,  260,  261,  230,  199,  230,  233,
  230,  233,  282,  233,  286,  259,    2,  256,  257,  291,
    0,  260,  261,  279,  278,  297,  282,   13,  300,  210,
  286,  287,  288,  267,  268,  291,  292,  273,  294,  278,
  279,  297,  292,  282,  300,  289,  290,  286,  265,  266,
  257,  279,  291,  260,  261,  294,  265,  266,  297,  265,
  266,  300,  279,  278,  257,    0,  280,  260,  261,  282,
  279,  278,  262,  263,  287,  288,    0,  280,    0,  282,
  278,  257,  258,  282,  260,  261,  262,  263,  264,  293,
  266,  287,  288,  286,  262,  263,  256,  257,  291,  259,
  260,  261,  278,  292,  297,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  275,  282,  277,  282,  279,
  280,  279,  282,    0,  279,  301,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,  282,
  300,  256,  257,  282,  259,  260,  261,  279,  262,  263,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  279,  277,  296,  279,  280,    0,  282,  262,  263,
  264,  286,  287,  288,  289,  290,  291,  292,  293,  294,
  295,  296,  297,  298,    0,  300,  256,  257,  279,  259,
  260,  261,  265,  266,  282,  265,  266,   84,   85,  279,
  270,  271,  272,  273,  274,  275,   95,   96,  279,  279,
  280,  257,  282,  259,  260,  261,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,    0,
  300,  256,  257,  296,  259,  260,  261,  279,  296,  276,
  265,  266,  292,  289,  290,  270,  271,  272,  273,  274,
  275,  279,  279,  292,  279,  280,  296,  282,  293,  282,
  282,  286,  287,  288,  289,  290,  291,  292,  293,  294,
  295,  296,  297,  298,  257,  300,  256,  257,  296,  259,
  260,  261,  296,    0,  279,  265,  266,  279,   64,   64,
  270,  271,  272,  273,  274,  275,  149,   36,   83,  279,
  280,   43,  282,   -1,   -1,   -1,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,   -1,
  300,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,
   -1,  257,  258,   -1,  260,  261,  262,  263,  264,   -1,
  266,  286,   -1,   -1,  289,  290,  291,   -1,   -1,   -1,
  295,   -1,  297,  298,  286,  300,  282,  289,  290,  291,
   -1,   -1,   -1,  295,   -1,  297,  298,   -1,  300,  256,
  257,   -1,  259,  260,  261,  301,  257,  258,   -1,  260,
  261,  262,  263,  264,   -1,  266,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  280,   -1,  282,   -1,   -1,   -1,  286,
   -1,   -1,  289,  290,  291,   -1,  293,   -1,  295,  296,
  297,  298,  256,  257,   -1,  259,  260,  261,   -1,   -1,
  301,  257,   -1,   -1,  260,  261,   -1,   -1,   -1,   -1,
  256,  257,   -1,  259,  260,  261,  280,   -1,  282,   -1,
   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,  293,
  286,  295,  296,  297,  298,  291,  282,   -1,  294,   -1,
  286,  297,   -1,  289,  290,  291,   -1,  293,   -1,  295,
  296,  297,  298,   -1,   -1,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,    9,   -1,   -1,   12,  256,  257,
  256,  257,  260,  261,  260,  261,   -1,  257,  258,   -1,
  260,  261,  262,  263,  264,  286,  266,   -1,  289,  290,
  291,   36,  293,   -1,  295,  296,  297,  298,  286,   44,
  286,   -1,   -1,  291,   -1,  291,  294,   -1,  294,  297,
   55,  297,  300,   -1,  300,   -1,   61,   62,  256,  257,
   -1,   -1,  260,  261,   -1,   -1,   -1,   -1,   73,   -1,
   75,   -1,   77,   -1,   79,   80,  265,  266,   83,   84,
   85,  270,  271,  272,  273,  274,  275,   92,  286,   -1,
   95,   96,   -1,  291,   -1,   -1,  294,  257,  103,  297,
  260,  261,  300,   -1,   -1,  110,   -1,   -1,   -1,   -1,
   -1,   -1,  256,  257,   -1,   -1,  260,  261,   -1,  124,
   -1,  126,   -1,   -1,   -1,  257,  286,  259,  260,  261,
   -1,  291,   -1,   -1,  294,   -1,  296,  297,   -1,   -1,
  300,   -1,  286,   -1,  149,  150,   -1,  291,  153,  154,
  294,  156,   -1,  297,  286,   -1,  300,  289,  290,  291,
   -1,   -1,   -1,  295,   -1,  297,  298,  257,  300,  259,
  260,  261,   -1,   -1,   -1,  257,   -1,   -1,  260,  261,
   -1,   -1,  257,   -1,   -1,  260,  261,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,  286,   -1,   -1,  297,  298,  291,
  300,  286,  294,   -1,   -1,  297,  291,   -1,  300,   -1,
  295,   -1,  297,  256,  257,  300,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,
   -1,  294,   -1,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,   -1,  297,  298,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,  256,  257,   -1,  257,  260,  261,
  260,  261,   -1,   -1,  257,   -1,  286,  260,  261,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,  286,   -1,  286,  297,  298,  291,
   -1,  291,  294,  286,  294,  297,  296,  297,  291,   -1,
   -1,   -1,  295,   -1,  297,
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
"sentencia_declarativa : struct PUNTO_Y_COMA",
"sentencia_declarativa : struct",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO",
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

//#line 305 "gramatica.y"
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
//#line 731 "Parser.java"
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
//#line 40 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ';' al final de la sentencia"); }
break;
case 16:
//#line 43 "gramatica.y"
{
                                                                                                                                                    yyval.ival = ((Token) val_peek(8).obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) val_peek(8).obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) val_peek(0).obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                }
break;
case 17:
//#line 52 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 18:
//#line 53 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 19:
//#line 54 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 23:
//#line 63 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 24:
//#line 66 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 25:
//#line 67 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 26:
//#line 70 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 27:
//#line 71 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 28:
//#line 72 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 29:
//#line 75 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 30:
//#line 76 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 31:
//#line 79 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 32:
//#line 88 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 33:
//#line 93 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                            }
break;
case 35:
//#line 100 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 36:
//#line 101 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 41:
//#line 110 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 42:
//#line 111 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 43:
//#line 112 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 44:
//#line 113 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 45:
//#line 114 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 46:
//#line 117 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 47:
//#line 118 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 48:
//#line 121 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 49:
//#line 124 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 50:
//#line 125 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 51:
//#line 126 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 52:
//#line 127 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 53:
//#line 128 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 54:
//#line 129 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 58:
//#line 137 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 63:
//#line 148 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 64:
//#line 149 "gramatica.y"
{
                                                        Parser.agregarEstructuraDetectadas(val_peek(0).ival, "ASIGNACION");
                                                        agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ';' al final de la sentencia");
                                                    }
break;
case 65:
//#line 153 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 66:
//#line 154 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "SALIDA"); }
break;
case 67:
//#line 155 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 68:
//#line 158 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 69:
//#line 161 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 70:
//#line 162 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 71:
//#line 163 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 72:
//#line 164 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 73:
//#line 165 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 74:
//#line 166 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 78:
//#line 174 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 90:
//#line 196 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 91:
//#line 197 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 92:
//#line 198 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 93:
//#line 201 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 94:
//#line 202 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 95:
//#line 205 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 96:
//#line 206 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 97:
//#line 207 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 98:
//#line 208 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 99:
//#line 209 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 100:
//#line 210 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 101:
//#line 211 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 102:
//#line 214 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 103:
//#line 215 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 104:
//#line 216 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 105:
//#line 217 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 106:
//#line 218 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 125:
//#line 251 "gramatica.y"
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
case 127:
//#line 292 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 128:
//#line 293 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 129:
//#line 294 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
//#line 1242 "Parser.java"
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
