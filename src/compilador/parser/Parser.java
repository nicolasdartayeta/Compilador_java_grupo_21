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
    import java.util.Stack;

//#line 27 "Parser.java"




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
public final static short TOKERROR=284;
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
    4,    4,    9,    9,    9,    9,    9,    9,    9,    9,
    6,    6,    6,   10,   10,    7,    7,    7,   11,   11,
    1,    1,    1,   12,   12,    8,   13,   13,   15,   15,
   15,   15,   15,   14,   14,   14,   14,   16,   16,   16,
   16,   16,   16,   16,   20,   20,   21,   21,   21,   18,
   18,   18,   18,   18,   18,   18,   18,   26,   26,   26,
   26,   28,   29,   29,   29,   23,   23,   23,   23,   30,
   30,    5,    5,    5,    5,    5,    5,   17,   31,   31,
   31,   31,   31,   31,   31,   31,   25,   34,   34,   34,
   34,   35,   36,   36,   36,   27,   27,   38,   38,   38,
   38,   38,   38,   37,   37,   37,   37,   37,   39,   39,
   19,   19,   19,   19,   19,   19,   19,   19,   32,   32,
   22,   22,   22,   22,   22,   22,   22,   40,   40,   40,
   40,   40,   41,   42,   42,   42,   33,   33,   24,   24,
   24,   24,   44,   44,   44,   45,   45,   45,   45,   45,
   43,   43,   46,   46,   46,   46,   46,   46,   47,   47,
   47,
};
final static short yylen[] = {                            2,
    4,    5,    5,    3,    3,    3,    2,    5,    4,    4,
    3,    5,    2,    1,    1,    1,    2,    3,    4,    4,
    1,    1,   10,    9,   11,   11,    9,    8,    9,    9,
    1,    1,    1,    3,    1,    3,    2,    1,    1,    1,
    1,    1,    1,    3,    3,    4,    6,    5,    2,    1,
    1,    3,    1,    2,    2,    1,    1,    2,    3,    3,
    1,    1,    1,    1,    2,    2,    5,    6,    6,    4,
    5,    5,    3,    6,    6,    5,    3,    3,    2,    1,
    2,    1,    2,    1,    1,    4,    5,    5,    1,    2,
    1,    2,    3,    3,    1,    1,    1,    3,    4,    5,
    5,    3,    6,    6,    5,    3,    4,    3,    2,    1,
    2,    1,    2,    1,    1,    3,    2,    1,    1,    1,
    1,    1,    1,    4,    5,    3,    5,    1,    2,    1,
    5,    5,    6,    6,    6,    6,    4,    4,    2,    2,
    6,    2,    1,    5,    5,    5,    4,    7,    6,    6,
    6,    6,    3,    2,    2,    1,    3,    1,    3,    3,
    1,    1,    3,    3,    1,    1,    1,    4,    3,    1,
    1,    1,    1,    2,    1,    1,    2,    2,    4,    3,
    5,
};
final static short yydefred[] = {                         0,
   41,   42,   43,    0,    0,    0,    0,   33,    0,   32,
   31,    0,    0,    0,    0,    0,   14,   15,   16,    0,
    0,   21,   22,   38,   40,    0,    0,   96,    0,    0,
   95,   97,    0,    0,    0,    0,   17,    0,    0,    0,
  162,    0,  171,  172,  175,    0,    0,  176,    0,  166,
    0,    0,  173,    0,  165,  167,  170,    0,    0,    0,
    0,    4,   13,    0,    0,    0,    0,   37,    0,    0,
   92,  140,    0,  128,  139,    0,    0,    0,    0,  112,
    0,  142,    0,    0,    0,    0,    0,    5,    0,    0,
    0,    0,  177,  178,  174,    0,    0,  117,    0,    0,
  120,  121,  118,  119,  122,  123,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   35,    0,    0,    0,    0,
   44,    0,    0,    0,   18,    0,    0,   36,    0,    0,
   57,    0,   56,    0,   61,   62,   63,   64,    0,    0,
   93,   94,    0,  130,    0,    0,  106,  102,    0,    0,
  111,  114,    0,    0,    0,    0,    9,    0,    0,    0,
    0,    0,  153,    0,  180,    0,  107,    0,  169,    0,
    0,    0,    0,    0,    0,  163,  164,  138,  137,    0,
    0,    0,    0,    0,    0,    0,    0,   53,    0,   50,
    0,   19,   20,    0,    0,    0,    0,   46,   55,   54,
    0,   58,   66,    0,   89,   65,    0,    0,   82,    0,
    0,  126,    0,  129,  108,    0,   99,  113,    0,    0,
    0,    3,    8,    2,   12,    0,    0,    0,    0,  179,
    0,  168,    0,  105,    0,  131,    0,  132,    0,   34,
    0,    0,    0,    0,    0,    0,   48,    0,    0,    0,
    0,    0,   59,   60,   91,    0,    0,   77,   73,    0,
    0,   84,   81,    0,  124,  100,  101,  145,    0,  146,
    0,    0,    0,  152,  156,  151,  149,  181,  103,  104,
  133,  135,  134,  136,    0,    0,    0,    0,    0,   47,
   52,    0,    0,    0,    0,    0,   90,   78,    0,   70,
   83,  125,  127,  141,  148,  154,  155,    0,    0,    0,
    0,    0,    0,    0,   76,    0,   67,    0,   86,   71,
   72,    0,    0,    0,    0,   28,   74,   75,   68,   69,
   87,   88,   24,    0,   30,   29,   27,    0,   23,   25,
   26,
};
final static short yydgoto[] = {                          5,
   15,   16,   17,   18,   19,   20,   21,   22,   23,  117,
   24,   25,   26,  132,  191,  205,   27,  135,   28,  137,
  138,   29,  209,   51,   30,  210,   52,  211,  263,  256,
   31,   32,  127,   78,   79,  151,   80,  107,  145,   33,
   40,  274,   53,   54,   55,   56,   57,
};
final static short yysindex[] = {                       -97,
    0,    0,    0,  810,    0,  694, -257,    0,   61,    0,
    0,   74, -237, -225, -238,  704,    0,    0,    0, -234,
  515,    0,    0,    0,    0, -228, -144,    0,  104,  828,
    0,    0,   77, -136,  820,  684,    0,   93, -224, -169,
    0, -203,    0,    0,    0,   39,  176,    0, -160,    0,
  974,  837,    0,   60,    0,    0,    0,   32, -227, -163,
   93,    0,    0, -243,  482,  176,   93,    0,  508, -179,
    0,    0,  898,    0,    0,  930, -126, -260,  882,    0,
  176,    0,   85,  810,  -82,  747,  810,    0,  110,  126,
  176,  121,    0,    0,    0,  875,  146,    0,  188,  188,
    0,    0,    0,    0,    0,    0,  176,  891, -119,  188,
  188, -114,  -93,  -81, -131,    0, -161, -227,  226, -238,
    0,  -77,  207,  -41,    0,  131,  -74,    0,  157,  -68,
    0,  492,    0, -139,    0,    0,    0,    0,   13,  543,
    0,    0,  -52,    0,  937,  882,    0,    0, -128,  930,
    0,    0,  -31,  176,  -28,  757,    0,  -29,  -18,  767,
  176,  -25,    0,   -1,    0,  308,    0,  -12,    0,  -16,
   60,   60,  131,   -5,   27,    0,    0,    0,    0, -107,
 -101,   38, -227,  310,   41,   93,  207,    0,   93,    0,
   83,    0,    0,  176,  176,  559,  176,    0,    0,    0,
   62,    0,    0,  642,    0,    0,  625,  109,    0, -254,
  571,    0,  -57,    0,    0,   98,    0,    0,  120,  145,
  149,    0,    0,    0,    0,  -26,  -70,  -70,  152,    0,
  171,    0,  177,    0,  134,    0,  249,    0,   93,    0,
   44,   93,   93,  550,  182,  206,    0,  131,  587,  615,
  169,   40,    0,    0,    0,  632,  571,    0,    0,  -37,
  625,    0,    0,  256,    0,    0,    0,    0,  192,    0,
  -70,  126,  126,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  637,   93,  880,  942,  234,    0,
    0,  218,  240,  221,  -22,    3,    0,    0,  266,    0,
    0,    0,    0,    0,    0,    0,    0,  252,  949,  282,
  288,  258,  268,  274,    0,  283,    0,  298,    0,    0,
    0,  284, -251,  293,  299,    0,    0,    0,    0,    0,
    0,    0,    0,   15,    0,    0,    0,  321,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  213,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  433,    0,    0,  592,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  297,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -249,    0,
    0,    0,    0,    0,  598,  606,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  178,    0,    0,    0,    0,    0,    0,    0,  255,
    0,    0,    0,    0,    0, -150,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -212,    0,    0,    0, -175,
    0,    0,    0,    0,    0,    0,    0,  607,  608,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  339,  381,  423,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  336,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   30,    0,    0,    0,    0,    0,    0,    0,    0,  435,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  338,    0, -109,    0,    0,
    0,    0,    0,    0,    0,    0,  212,    0,    0,    0,
  237,    0,    0,    0,    0,    0,    0,    0,  480,    0,
  -14,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
   16,    5,   14,  -61,  179,  -50,  -19,    0,    0,  -42,
   -9,  561,    0,    0,  436,  -65,  -54,    0,  -45,    0,
    0,  -35, -132,  -53,  -17, -141,  -30,  413,  367,    0,
    0,    0,    0,  -15,  552,  483,  -10,    0,    0,    0,
  594, -207,  -44,  327,  345,    0,    0,
};
final static int YYTABLESIZE=1249;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         39,
   65,   95,   50,  133,  115,  334,  206,  131,  116,  116,
   36,   68,  126,  122,  134,    6,   96,  119,   75,  276,
  277,  148,    1,  136,   37,    2,    3,  259,   39,   63,
  335,    8,  110,  139,  123,  149,  109,   50,  166,   86,
   58,  260,   61,  170,   90,  163,  110,   59,   50,   63,
  153,  140,  155,  173,  251,   68,   50,  128,  162,   60,
  164,   10,   11,  305,   64,   69,  200,  116,  152,  109,
  199,   50,  189,   50,   92,  184,  120,  134,  262,   50,
  168,   50,   50,  109,  134,  134,  136,   50,  156,   50,
   50,  160,  174,  136,  136,    8,  139,   50,  196,   63,
   50,   50,  141,  139,  139,  158,  115,  292,  293,  118,
  182,   70,   91,  190,  140,  142,  201,   97,  183,   50,
  115,  140,  140,  220,  262,   10,   11,  216,  301,  158,
  226,  158,  240,   99,  100,  152,  189,   71,  255,  218,
  248,  134,  202,  252,   50,   37,  157,  181,  235,  134,
  136,   50,  134,  217,  237,  147,  134,   84,  136,    1,
  139,  136,    2,    3,  249,  136,  244,  178,  139,   63,
  157,  139,  157,   63,  236,  139,  175,  190,  140,  246,
  238,  275,  275,  275,   50,   50,  140,   50,  179,  140,
  297,   43,   44,  140,  134,  134,    4,  180,  264,   37,
  187,  134,  134,  136,  136,  194,  134,   74,   74,  197,
  136,  136,  157,  139,  139,  136,  272,  273,  299,  285,
  139,  139,  287,  288,  265,  139,  275,  306,  307,  212,
   74,  140,  140,  316,   68,   43,   44,  224,  140,  140,
  192,  150,  150,  140,  300,  150,  150,  219,   99,  100,
  221,  144,   37,  193,   74,  271,  227,   74,  318,  317,
  272,  273,  232,  150,  150,  223,  309,  150,  203,    1,
  338,  150,    2,    3,   74,   68,  150,   68,   68,  150,
  228,   98,  150,  231,  319,  150,   74,  112,    1,   42,
  233,    2,    3,   43,   44,   45,  339,   46,    9,   68,
   43,   44,   93,  129,   99,  100,  204,   98,  234,   13,
  113,   80,  130,  239,  114,   48,  243,    1,  295,  286,
    2,    3,   94,  214,   74,   80,  110,  111,   74,   41,
    1,   42,   49,    2,    3,   43,   44,   45,   38,   46,
   41,    1,   42,  253,    2,    3,   43,   44,   45,    1,
   46,   47,    2,    3,   81,   82,  254,   48,   83,   72,
    1,  247,  154,    2,    3,   41,    1,   42,   48,    2,
    3,   43,   44,   45,   49,   46,   41,    1,   42,  266,
    2,    3,   43,   44,   45,   49,   46,   43,   44,    9,
  258,  161,  267,   48,   12,   99,  100,   73,  268,  165,
   13,   41,    1,   42,   48,    2,    3,   43,   44,   45,
   49,   46,   41,    1,   42,  281,    2,    3,   43,   44,
   45,   49,   46,  269,  169,  171,  172,  270,  282,   48,
  278,   41,    1,   42,  195,    2,    3,   43,   44,   45,
   48,   46,  162,  162,    1,   42,   49,    2,    3,   43,
   44,   45,  279,   46,  176,  177,  162,   49,  280,   48,
  290,  291,  188,    1,  294,    8,    2,    3,   39,   39,
  304,   48,   39,   39,   39,   39,   49,   39,   39,   39,
   39,   39,   39,   39,   39,   39,   39,   39,   49,   39,
  312,   39,   39,   79,   39,   10,   11,  185,   39,   39,
   39,  186,  315,   39,   39,  183,   39,   79,  322,   39,
   45,   45,   39,  313,   45,   45,   45,   45,   85,   45,
   45,   45,   45,   45,   45,   45,   45,   45,   45,   45,
  283,   45,   85,   45,   45,  314,   45,  302,  324,  326,
   45,   45,   45,  284,  325,   45,   45,  320,   45,  327,
  303,   45,  161,  161,   45,  328,  161,  161,  161,  161,
  321,  161,  161,  229,  329,  333,  161,  161,  161,  161,
  161,  161,   99,  100,  336,  161,  161,  330,  161,  331,
  337,  241,  161,  161,  161,  242,  230,  161,  161,  183,
  161,    7,  332,  161,  159,  159,  161,   11,  159,  159,
  159,  159,  340,  159,  159,    6,   10,    1,  159,  159,
  159,  159,  159,  159,   51,  341,   49,  159,  159,  257,
  159,  121,  245,  298,  159,  159,  159,  146,  215,  159,
  159,   89,  159,    0,    0,  159,  160,  160,  159,    0,
  160,  160,  160,  160,    0,  160,  160,    0,    0,    0,
  160,  160,  160,  160,  160,  160,    0,    0,    0,  160,
  160,    0,  160,    0,    0,    0,  160,  160,  160,    0,
    0,  160,  160,    0,  160,    0,    0,  160,  116,  116,
  160,    0,  116,  116,  116,  116,    0,    0,  143,  143,
  147,  147,  143,  143,  147,  147,    0,    0,    0,    0,
    0,  116,    0,    0,  116,    0,    0,    0,  116,  116,
  116,    0,    0,  116,  116,    0,  116,    0,  143,  116,
  147,    0,  116,  143,    0,  147,  143,    0,  147,  143,
    0,  147,  143,    0,  147,  144,  144,  124,    1,  144,
  144,    2,    3,    0,    0,    0,    0,    0,    1,    0,
    8,    2,    3,    0,    0,    0,    0,    0,    0,    0,
    0,   67,    0,  125,    1,  144,    8,    2,    3,    0,
  144,    1,    0,  144,    2,    3,  144,    9,    0,  144,
   10,   11,  129,   66,    0,    0,  198,    0,   13,   14,
    0,  130,    0,    9,   67,    0,   10,   11,  129,    1,
    0,    0,    2,    3,   13,   14,    1,  130,    0,    2,
    3,    0,    0,    0,    0,    1,    0,    0,    2,    3,
    0,    0,    0,    0,    0,    0,  289,    1,    9,   67,
    2,    3,    0,  129,  207,    0,  204,  250,  208,   13,
    0,    0,  130,    1,    9,    0,    2,    3,    0,  129,
  207,    0,  204,    0,    0,   13,    9,    0,  130,    0,
    0,  129,    0,  261,  204,  167,    0,   13,    0,    0,
  130,    1,    9,    0,    2,    3,    0,  129,  207,    0,
  204,    1,    0,   13,    2,    3,  130,    0,    1,    0,
    0,    2,    3,    1,    0,    0,    2,    3,    1,    0,
    9,    2,    3,    0,    0,  129,  207,    0,  204,    0,
    9,   13,    0,  308,  130,  129,   67,    9,  204,    0,
    0,   13,  129,    0,  130,    0,  296,    9,   13,    0,
    0,  130,  129,    0,    0,    0,    0,    0,   13,    7,
    1,  130,    8,    2,    3,    0,    0,    0,    0,   34,
    1,    0,    8,    2,    3,    0,    0,    0,    0,    7,
    1,    0,    8,    2,    3,    0,    0,    0,    0,    9,
    0,    0,   10,   11,   12,    0,    0,   87,   88,    9,
   13,   14,   10,   11,   12,    0,    0,   35,    0,    9,
   13,   14,   10,   11,   12,    0,    0,    0,   62,    0,
   13,   14,  158,    1,    0,    8,    2,    3,    0,    0,
    0,    0,    7,    1,    0,    8,    2,    3,    0,    0,
    0,    0,    7,    1,    0,    8,    2,    3,    0,    0,
    0,    0,    9,    0,    0,   10,   11,   12,    0,    0,
    0,  159,    9,   13,   14,   10,   11,   12,    0,    0,
    0,  222,    9,   13,   14,   10,   11,   12,    0,    0,
    0,  225,    0,   13,   14,    7,    1,    0,    8,    2,
    3,    0,    0,    0,    0,   85,    1,    0,    8,    2,
    3,    0,    0,    0,    1,    0,    0,    2,    3,    0,
    0,    0,    0,    1,    0,    9,    2,    3,   10,   11,
   12,    0,    0,    0,    0,    9,   13,   14,   10,   11,
   12,    0,    0,    9,    0,  108,   13,   14,   12,   76,
    0,   73,    9,   77,   13,    0,    0,   12,   76,    0,
   73,    1,    0,   13,    2,    3,    1,    0,    1,    2,
    3,    2,    3,    0,    0,    0,    0,    1,    0,    0,
    2,    3,    0,  167,    1,    0,  310,    2,    3,   67,
    9,    0,    0,    0,    0,   12,   76,    9,   73,    0,
    0,   13,   12,    0,  150,   73,    9,    0,   13,    0,
    0,   12,   76,    9,   73,    0,    1,   13,   12,    2,
    3,    0,  143,    1,   13,    0,    2,    3,    1,    0,
    0,    2,    3,    0,    0,    1,    0,    0,    2,    3,
    0,    0,    0,    0,    0,    9,    0,    0,  311,    0,
   12,   67,    9,   73,    0,  323,   13,   12,   67,   98,
    0,  213,    0,   13,    0,    0,    0,    0,   99,  100,
    0,    0,    0,  101,  102,  103,  104,  105,  106,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   20,   46,   12,   69,   58,  257,  139,   69,   59,   60,
    6,   21,   66,  257,   69,    0,   47,   60,   29,  227,
  228,  282,  257,   69,  282,  260,  261,  282,   38,   16,
  282,  259,  282,   69,  278,  296,   52,   47,   92,   35,
  278,  296,  281,   97,  269,   90,  296,  273,   58,   36,
   81,   69,   83,  107,  196,   65,   66,   67,   89,  285,
   91,  289,  290,  271,  299,  294,  132,  118,   79,  282,
  132,   81,  123,   83,  278,  118,   61,  132,  211,   89,
   96,   91,   92,  296,  139,  140,  132,   97,   84,   99,
  100,   87,  108,  139,  140,  259,  132,  107,  129,   86,
  110,  111,  282,  139,  140,  256,  282,  249,  250,  273,
  272,  256,  282,  123,  132,  295,  256,  278,  280,  129,
  296,  139,  140,  154,  257,  289,  290,  256,  261,  280,
  161,  282,  183,  265,  266,  146,  187,  282,  204,  150,
  194,  196,  282,  197,  154,  282,  256,  279,  256,  204,
  196,  161,  207,  282,  256,  282,  211,  294,  204,  257,
  196,  207,  260,  261,  195,  211,  186,  282,  204,  156,
  280,  207,  282,  160,  282,  211,  296,  187,  196,  189,
  282,  226,  227,  228,  194,  195,  204,  197,  282,  207,
  256,  262,  263,  211,  249,  250,  294,  279,  256,  282,
  278,  256,  257,  249,  250,  280,  261,   29,   30,  278,
  256,  257,  295,  249,  250,  261,  287,  288,  256,  239,
  256,  257,  242,  243,  282,  261,  271,  272,  273,  282,
   52,  249,  250,  256,  244,  262,  263,  256,  256,  257,
  282,  256,  257,  261,  282,  260,  261,  279,  265,  266,
  279,   73,  282,  295,   76,  282,  282,   79,  256,  282,
  287,  288,  279,  278,  279,  295,  286,  282,  256,  257,
  256,  286,  260,  261,   96,  285,  291,  287,  288,  294,
  282,  256,  297,  296,  282,  300,  108,  256,  257,  258,
  296,  260,  261,  262,  263,  264,  282,  266,  286,  309,
  262,  263,  264,  291,  265,  266,  294,  282,  282,  297,
  279,  282,  300,  276,  283,  284,  276,  257,  279,  276,
  260,  261,  284,  145,  146,  296,  267,  268,  150,  256,
  257,  258,  301,  260,  261,  262,  263,  264,  278,  266,
  256,  257,  258,  282,  260,  261,  262,  263,  264,  257,
  266,  278,  260,  261,  278,  279,  295,  284,  282,  256,
  257,  279,  278,  260,  261,  256,  257,  258,  284,  260,
  261,  262,  263,  264,  301,  266,  256,  257,  258,  282,
  260,  261,  262,  263,  264,  301,  266,  262,  263,  286,
  282,  282,  295,  284,  291,  265,  266,  294,  279,  279,
  297,  256,  257,  258,  284,  260,  261,  262,  263,  264,
  301,  266,  256,  257,  258,  282,  260,  261,  262,  263,
  264,  301,  266,  279,  279,   99,  100,  279,  295,  284,
  279,  256,  257,  258,  278,  260,  261,  262,  263,  264,
  284,  266,  265,  266,  257,  258,  301,  260,  261,  262,
  263,  264,  282,  266,  110,  111,  279,  301,  282,  284,
  279,  256,  256,  257,  296,  259,  260,  261,  256,  257,
  279,  284,  260,  261,  262,  263,  301,  265,  266,  267,
  268,  269,  270,  271,  272,  273,  274,  275,  301,  277,
  257,  279,  280,  282,  282,  289,  290,  272,  286,  287,
  288,  276,  282,  291,  292,  280,  294,  296,  257,  297,
  256,  257,  300,  296,  260,  261,  262,  263,  282,  265,
  266,  267,  268,  269,  270,  271,  272,  273,  274,  275,
  282,  277,  296,  279,  280,  296,  282,  282,  257,  282,
  286,  287,  288,  295,  257,  291,  292,  282,  294,  282,
  295,  297,  256,  257,  300,  282,  260,  261,  262,  263,
  295,  265,  266,  256,  282,  282,  270,  271,  272,  273,
  274,  275,  265,  266,  282,  279,  280,  295,  282,  282,
  282,  272,  286,  287,  288,  276,  279,  291,  292,  280,
  294,    0,  295,  297,  256,  257,  300,    0,  260,  261,
  262,  263,  282,  265,  266,    0,    0,    0,  270,  271,
  272,  273,  274,  275,  279,  295,  279,  279,  280,  207,
  282,   61,  187,  257,  286,  287,  288,   76,  146,  291,
  292,   38,  294,   -1,   -1,  297,  256,  257,  300,   -1,
  260,  261,  262,  263,   -1,  265,  266,   -1,   -1,   -1,
  270,  271,  272,  273,  274,  275,   -1,   -1,   -1,  279,
  280,   -1,  282,   -1,   -1,   -1,  286,  287,  288,   -1,
   -1,  291,  292,   -1,  294,   -1,   -1,  297,  256,  257,
  300,   -1,  260,  261,  262,  263,   -1,   -1,  256,  257,
  256,  257,  260,  261,  260,  261,   -1,   -1,   -1,   -1,
   -1,  279,   -1,   -1,  282,   -1,   -1,   -1,  286,  287,
  288,   -1,   -1,  291,  292,   -1,  294,   -1,  286,  297,
  286,   -1,  300,  291,   -1,  291,  294,   -1,  294,  297,
   -1,  297,  300,   -1,  300,  256,  257,  256,  257,  260,
  261,  260,  261,   -1,   -1,   -1,   -1,   -1,  257,   -1,
  259,  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  280,   -1,  282,  257,  286,  259,  260,  261,   -1,
  291,  257,   -1,  294,  260,  261,  297,  286,   -1,  300,
  289,  290,  291,  269,   -1,   -1,  295,   -1,  297,  298,
   -1,  300,   -1,  286,  280,   -1,  289,  290,  291,  257,
   -1,   -1,  260,  261,  297,  298,  257,  300,   -1,  260,
  261,   -1,   -1,   -1,   -1,  257,   -1,   -1,  260,  261,
   -1,   -1,   -1,   -1,   -1,   -1,  277,  257,  286,  280,
  260,  261,   -1,  291,  292,   -1,  294,  279,  296,  297,
   -1,   -1,  300,  257,  286,   -1,  260,  261,   -1,  291,
  292,   -1,  294,   -1,   -1,  297,  286,   -1,  300,   -1,
   -1,  291,   -1,  293,  294,  279,   -1,  297,   -1,   -1,
  300,  257,  286,   -1,  260,  261,   -1,  291,  292,   -1,
  294,  257,   -1,  297,  260,  261,  300,   -1,  257,   -1,
   -1,  260,  261,  257,   -1,   -1,  260,  261,  257,   -1,
  286,  260,  261,   -1,   -1,  291,  292,   -1,  294,   -1,
  286,  297,   -1,  277,  300,  291,  280,  286,  294,   -1,
   -1,  297,  291,   -1,  300,   -1,  295,  286,  297,   -1,
   -1,  300,  291,   -1,   -1,   -1,   -1,   -1,  297,  256,
  257,  300,  259,  260,  261,   -1,   -1,   -1,   -1,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  286,
   -1,   -1,  289,  290,  291,   -1,   -1,  294,  295,  286,
  297,  298,  289,  290,  291,   -1,   -1,  294,   -1,  286,
  297,  298,  289,  290,  291,   -1,   -1,   -1,  295,   -1,
  297,  298,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,   -1,
   -1,  295,  286,  297,  298,  289,  290,  291,   -1,   -1,
   -1,  295,  286,  297,  298,  289,  290,  291,   -1,   -1,
   -1,  295,   -1,  297,  298,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,  257,   -1,   -1,  260,  261,   -1,
   -1,   -1,   -1,  257,   -1,  286,  260,  261,  289,  290,
  291,   -1,   -1,   -1,   -1,  286,  297,  298,  289,  290,
  291,   -1,   -1,  286,   -1,  279,  297,  298,  291,  292,
   -1,  294,  286,  296,  297,   -1,   -1,  291,  292,   -1,
  294,  257,   -1,  297,  260,  261,  257,   -1,  257,  260,
  261,  260,  261,   -1,   -1,   -1,   -1,  257,   -1,   -1,
  260,  261,   -1,  279,  257,   -1,  277,  260,  261,  280,
  286,   -1,   -1,   -1,   -1,  291,  292,  286,  294,   -1,
   -1,  297,  291,   -1,  293,  294,  286,   -1,  297,   -1,
   -1,  291,  292,  286,  294,   -1,  257,  297,  291,  260,
  261,   -1,  295,  257,  297,   -1,  260,  261,  257,   -1,
   -1,  260,  261,   -1,   -1,  257,   -1,   -1,  260,  261,
   -1,   -1,   -1,   -1,   -1,  286,   -1,   -1,  277,   -1,
  291,  280,  286,  294,   -1,  277,  297,  291,  280,  256,
   -1,  295,   -1,  297,   -1,   -1,   -1,   -1,  265,  266,
   -1,   -1,   -1,  270,  271,  272,  273,  274,  275,
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
"PARENTESIS_R","COMA","PUNTO","PUNTO_Y_COMA","INLINE_STRING","TOKERROR",
"STRUCT","FOR","UP","DOWN","SINGLE","ULONGINT","IF","THEN","ELSE","BEGIN","END",
"END_IF","OUTF","TYPEDEF","FUN","RET","TOS",
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
"struct : TYPEDEF STRUCT lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA",
"struct : TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R PUNTO_Y_COMA",
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
"sentencia_retorno : RET PARENTESIS_L expresion_aritmetica PARENTESIS_R PUNTO_Y_COMA",
"sentencia_retorno : RET PARENTESIS_L expresion_aritmetica PARENTESIS_R error PUNTO_Y_COMA",
"sentencia_retorno : RET PARENTESIS_L expresion_aritmetica PARENTESIS_R error END",
"sentencia_seleccion_en_funcion : inicio_seleccion cuerpo_if_en_funcion END_IF PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : inicio_seleccion cuerpo_if_en_funcion END_IF error PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : inicio_seleccion cuerpo_if_en_funcion END_IF error END",
"sentencia_seleccion_en_funcion : inicio_seleccion cuerpo_if_en_funcion PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion cuerpo_if_en_funcion END_IF PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF condicion PARENTESIS_R cuerpo_if_en_funcion END_IF PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF condicion cuerpo_if_en_funcion END_IF PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : inicio_seleccion END_IF PUNTO_Y_COMA",
"cuerpo_if_en_funcion : THEN cuerpo_then_en_funcion cuerpo_else_en_funcion",
"cuerpo_if_en_funcion : THEN cuerpo_then_en_funcion",
"cuerpo_if_en_funcion : cuerpo_then_en_funcion",
"cuerpo_if_en_funcion : cuerpo_then_en_funcion cuerpo_else_en_funcion",
"cuerpo_then_en_funcion : bloque_de_sent_ejecutables_en_funcion",
"cuerpo_else_en_funcion : ELSE bloque_de_sent_ejecutables_en_funcion",
"cuerpo_else_en_funcion : bloque_de_sent_ejecutables_en_funcion",
"cuerpo_else_en_funcion : ELSE",
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
"sentencia_seleccion : inicio_seleccion cuerpo_if END_IF PUNTO_Y_COMA",
"sentencia_seleccion : inicio_seleccion cuerpo_if END_IF error PUNTO_Y_COMA",
"sentencia_seleccion : inicio_seleccion cuerpo_if END_IF error END",
"sentencia_seleccion : inicio_seleccion cuerpo_if PUNTO_Y_COMA",
"sentencia_seleccion : IF PARENTESIS_L condicion cuerpo_if END_IF PUNTO_Y_COMA",
"sentencia_seleccion : IF condicion PARENTESIS_R cuerpo_if END_IF PUNTO_Y_COMA",
"sentencia_seleccion : IF condicion cuerpo_if END_IF PUNTO_Y_COMA",
"sentencia_seleccion : inicio_seleccion END_IF PUNTO_Y_COMA",
"inicio_seleccion : IF PARENTESIS_L condicion PARENTESIS_R",
"cuerpo_if : THEN cuerpo_then cuerpo_else",
"cuerpo_if : THEN cuerpo_then",
"cuerpo_if : cuerpo_then",
"cuerpo_if : cuerpo_then cuerpo_else",
"cuerpo_then : bloque_de_sent_ejecutables",
"cuerpo_else : ELSE bloque_de_sent_ejecutables",
"cuerpo_else : bloque_de_sent_ejecutables",
"cuerpo_else : ELSE",
"condicion : expresion_aritmetica comparador expresion_aritmetica",
"condicion : expresion_aritmetica error",
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
"sentencia_salida : OUTF PARENTESIS_L expresion_aritmetica PARENTESIS_R PUNTO_Y_COMA",
"sentencia_salida : OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R error PUNTO_Y_COMA",
"sentencia_salida : OUTF PARENTESIS_L expresion_aritmetica PARENTESIS_R error PUNTO_Y_COMA",
"sentencia_salida : OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R error END",
"sentencia_salida : OUTF PARENTESIS_L expresion_aritmetica PARENTESIS_R error END",
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
"asignacion_enteros : identificador ASIGNACION constante_entera",
"accion : UP constante_entera",
"accion : DOWN constante_entera",
"accion : constante_entera",
"lista_de_expresiones : lista_de_expresiones COMA expresion_aritmetica",
"lista_de_expresiones : expresion_aritmetica",
"expresion_aritmetica : expresion_aritmetica SUMA termino",
"expresion_aritmetica : expresion_aritmetica RESTA termino",
"expresion_aritmetica : termino",
"expresion_aritmetica : error",
"termino : termino MULTIPLICACION factor",
"termino : termino DIVISION factor",
"termino : factor",
"factor : identificador",
"factor : constante",
"factor : TOS PARENTESIS_L expresion_aritmetica PARENTESIS_R",
"factor : TOS PARENTESIS_L PARENTESIS_R",
"factor : invocacion_a_funcion",
"constante_entera : CONSTANTE_DECIMAL",
"constante_entera : CONSTANTE_OCTAL",
"constante : constante_entera",
"constante : RESTA constante_entera",
"constante : CONSTANTE_SINGLE",
"constante : TOKERROR",
"constante : RESTA CONSTANTE_SINGLE",
"constante : RESTA TOKERROR",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion_aritmetica PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion_aritmetica error PARENTESIS_R",
};

//#line 387 "gramatica.y"
private static int cantidadIdEnListaId = 0;
private static Lexer lex;
private static ArrayList<String> representacionPolaca;

// Pila de ambitos
public static final Stack<String> ambito = new Stack<>();

// Pila para bifurcaciones
public static final Stack<Integer> bfs = new Stack<>();

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

// Lista de tokens recibidos
public static final List<Token> tokensRecibidos = new ArrayList<>();

// Lista de identificadores
public static final List<String> listaIdentificadores = new ArrayList<>();

// Lista de tipos
public static final List<String> listaTipos = new ArrayList<>();

// Lista de estructuras detectadas
public static final List<String> estructurasDetectadas = new ArrayList<>();

// Listas de errores
public static final List<String> erroresLexicos = new ArrayList<>();
public static final List<String> erroresSintacticos = new ArrayList<>();

public static String getAmbitoActual() {
    StringBuilder ambitoActual = new StringBuilder();
    for (String elemento: ambito) {
        ambitoActual.append(elemento);
    }
    return ambitoActual.toString();
}

public static void agregarAmbitoAIdentificadores(List<String> identificadores) {
    String ambitoActual = getAmbitoActual();

    for (String lexema: identificadores) {
        TablaSimbolos.setAmbito(lexema, ambitoActual);
    }
}
public static void agregarAmbitoAIdentificador(String lexema) {
    String ambitoActual = getAmbitoActual();
    TablaSimbolos.setAmbito(lexema, ambitoActual);
}

// Agregar tipo a la tabla de simbolos para los identificadores que estan en la lista de identificadores
public static void agregarTipoAIdentificadores(String tipo) {
    for (String lexema: listaIdentificadores) {
        System.out.println(lexema + " es tipo " + tipo);
        TablaSimbolos.cambiarTipo(lexema, tipo);
    }
}

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

// Agregar los campos de una variable struct a la tabla de simbolos
public static void crearCampo(String tipo, String lexema){
    List<CampoTablaSimbolos.Campo> campos = TablaSimbolos.getCamposTablaSimbolos(tipo);
    for (CampoTablaSimbolos.Campo campo: campos){
        String nombreCampo = lexema + "." + campo.nombre();
        CampoTablaSimbolos nuevoCampo = new CampoTablaSimbolos(false, campo.tipo());
        TablaSimbolos.agregarLexema(nombreCampo, nuevoCampo);
    }
}

public static <T> void imprimirLista(List<T> lista) {
    for (T elemento : lista) {
        System.out.println(elemento);
    }
}

public static void imprimirPila(Stack<Integer> pila) {
    if (pila.isEmpty()) {
                System.out.println("La pila está vacía.");
    } else {
        System.out.println("Contenido de la pila:");
        // Recorrer la pila e imprimir cada elemento
        for (Integer elemento : pila) {
            System.out.println(elemento);
        }
    }
}

public static void imprimirPolaca(List<String> lista) {
    if (!lista.isEmpty()) {
    int i = 0;
    for (String elemento : lista) {
        System.out.println(i + " " + elemento);
        i++;
    }
    }
}

public static <T> void eliminarUltimosElementos(List<T> lista, int cantidadElementos){
    for (int i = 0; i < cantidadElementos; i++) {
        lista.remove(lista.size() - 1);
    }
}

public static <T> void appendListToList(List<T> lista1, List<T> lista2) {
}

public static void main(String[] args) {
    ambito.push(":main");
    representacionPolaca = new ArrayList<String>();
    Lexer lexer = new Lexer(args[0]);
    Parser.lex = lexer;
    Parser parser = new Parser(true);
    parser.run();

    System.out.println(Parser.VERDE + "TOKENS RECIBIDOS DEL ANALIZADOR LEXICO" + Parser.RESET);
    Parser.imprimirLista(tokensRecibidos);

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

    imprimirPolaca(representacionPolaca);
    imprimirPila(bfs);
}

private int yylex() {
    Token tok = lex.getNextToken();
    tokensRecibidos.add(tok);

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
//#line 966 "Parser.java"
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
//#line 20 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(((Token) val_peek(3).obj).getNumeroDeLinea(), "PROGRAMA"); }
break;
case 2:
//#line 21 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(1).obj).getNumeroDeLinea() + ": Todo lo que esta despues del END no forma parte del programa."); }
break;
case 3:
//#line 22 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(1).obj).getNumeroDeLinea() + ": Todo lo que esta despues del identificador del programa y antes del primer begin no forma parte del programa."); }
break;
case 4:
//#line 23 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea 1: Falta identificador de programa"); }
break;
case 5:
//#line 24 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + (((Token) val_peek(2).obj).getNumeroDeLinea() +1) + ": Falta un BEGIN despues del identificador del programa"); }
break;
case 6:
//#line 25 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Ultima linea: Falta un END al final del programa"); }
break;
case 7:
//#line 26 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta un BEGIN despues del identificador del programa y falta un END al final del programa"); }
break;
case 8:
//#line 27 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR."); }
break;
case 9:
//#line 28 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR."); }
break;
case 10:
//#line 29 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. FALTA EL END AL FINAL DEL PROGRAMA"); }
break;
case 11:
//#line 30 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. FALTA EL END AL FINAL DEL PROGRAMA"); }
break;
case 12:
//#line 31 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(3).obj).getNumeroDeLinea() + ": No se permite sentencias entre el identificador del programa y el BEGIN."); }
break;
case 17:
//#line 41 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)val_peek(0).obj).getNumeroDeLinea() + ": Syntax Error"); }
break;
case 18:
//#line 44 "gramatica.y"
{
                                                                                Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S");
                                                                                eliminarUltimosElementos(representacionPolaca, cantidadIdEnListaId);
                                                                                cantidadIdEnListaId = 0;
                                                                                agregarTipoAIdentificadores(val_peek(2).sval);
                                                                                agregarAmbitoAIdentificadores(listaIdentificadores);
                                                                                if (TablaSimbolos.esUnTipo(val_peek(2).sval) && TablaSimbolos.getTipo(val_peek(2).sval).equals("STRUCT")) {
                                                                                    for(String identificador: listaIdentificadores) {
                                                                                        crearCampo(val_peek(2).sval, identificador);
                                                                                    }
                                                                                };
                                                                                listaIdentificadores.clear();
                                                                            }
break;
case 19:
//#line 57 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 20:
//#line 58 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 21:
//#line 59 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FUNCION"); }
break;
case 23:
//#line 63 "gramatica.y"
{
                                                                                                                                                    yyval.ival = ((Token) val_peek(9).obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) val_peek(9).obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) val_peek(1).obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                    eliminarUltimosElementos(representacionPolaca, cantidadIdEnListaId);
                                                                                                                                                    cantidadIdEnListaId = 0;
                                                                                                                                                    listaIdentificadores.forEach((subcampo)->TablaSimbolos.deleteEntrada(subcampo));
                                                                                                                                                    agregarAmbitoAIdentificador(lexema);
                                                                                                                                                    TablaSimbolos.agregarCampos(lexema, listaTipos, listaIdentificadores);
                                                                                                                                                    listaIdentificadores.clear();
                                                                                                                                                    listaTipos.clear();
                                                                                                                                                }
break;
case 24:
//#line 76 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 25:
//#line 77 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(10).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 26:
//#line 78 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(10).ival + ": Falta ';' al final de la sentencia"); }
break;
case 27:
//#line 79 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 28:
//#line 80 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta simbolos '<' y '>' en la declaración del STRUCT");}
break;
case 29:
//#line 81 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 30:
//#line 82 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 31:
//#line 86 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema().toUpperCase(); }
break;
case 32:
//#line 87 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema().toUpperCase(); }
break;
case 33:
//#line 88 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema(); }
break;
case 34:
//#line 91 "gramatica.y"
{ listaTipos.add(val_peek(0).sval); }
break;
case 35:
//#line 92 "gramatica.y"
{ listaTipos.add(val_peek(0).sval); }
break;
case 36:
//#line 95 "gramatica.y"
{cantidadIdEnListaId++; listaIdentificadores.add(val_peek(0).sval);}
break;
case 37:
//#line 96 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ',' entre las identificadores"); }
break;
case 38:
//#line 97 "gramatica.y"
{ yyval.ival = val_peek(0).ival; cantidadIdEnListaId++; listaIdentificadores.add(val_peek(0).sval);}
break;
case 39:
//#line 100 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema(); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 40:
//#line 101 "gramatica.y"
{ yyval.ival = val_peek(0).ival; yyval.sval = val_peek(0).sval; System.out.println("ID COMPUESTO: " + val_peek(0).sval); representacionPolaca.add(val_peek(0).sval);}
break;
case 41:
//#line 104 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 42:
//#line 105 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 43:
//#line 106 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 44:
//#line 109 "gramatica.y"
{ yyval.ival = ((Token) val_peek(2).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(2).obj).getLexema() + ((Token) val_peek(1).obj).getLexema() + val_peek(0).sval;}
break;
case 45:
//#line 110 "gramatica.y"
{ yyval.ival = ((Token) val_peek(2).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(2).obj).getLexema() + ((Token) val_peek(1).obj).getLexema() +((Token) val_peek(0).obj).getLexema();}
break;
case 46:
//#line 113 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                                ambito.pop();
                                                                             }
break;
case 47:
//#line 123 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                        ambito.push(":" + lexema);
                                                                                                        TablaSimbolos.setAmbito(representacionPolaca.get(representacionPolaca.size()-1), this.getAmbitoActual());
                                                                                                        TablaSimbolos.setCantidadDeParametros(lexema, 1);
                                                                                                        TablaSimbolos.setTipoParametro(lexema, val_peek(1).sval);
                                                                                                        TablaSimbolos.setTipoRetorno(lexema, val_peek(5).sval);
                                                                                                        eliminarUltimosElementos(representacionPolaca, 1);
                                                                                                    }
break;
case 48:
//#line 134 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                             }
break;
case 49:
//#line 140 "gramatica.y"
{ yyval.sval = val_peek(1).sval; TablaSimbolos.cambiarTipo(val_peek(0).sval, val_peek(1).sval); }
break;
case 50:
//#line 141 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 51:
//#line 142 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 52:
//#line 143 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": La funcion no puede tener mas de un parametro");}
break;
case 53:
//#line 144 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ yyval.ival + ": Falta el parametro de la funcion");}
break;
case 58:
//#line 153 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 59:
//#line 154 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 60:
//#line 155 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 63:
//#line 158 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 64:
//#line 159 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 65:
//#line 162 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 66:
//#line 163 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 67:
//#line 166 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); representacionPolaca.add(((Token) val_peek(4).obj).getLexema()); }
break;
case 68:
//#line 167 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 69:
//#line 168 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 70:
//#line 171 "gramatica.y"
{ yyval.ival = val_peek(3).ival; Parser.agregarEstructuraDetectadas(val_peek(3).ival, "IF"); representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size())); }
break;
case 71:
//#line 172 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia de selección");}
break;
case 72:
//#line 173 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 73:
//#line 174 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el END_IF en la sentencia de selección"); }
break;
case 74:
//#line 175 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 75:
//#line 176 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 76:
//#line 177 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 77:
//#line 178 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 80:
//#line 183 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 81:
//#line 184 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 82:
//#line 187 "gramatica.y"
{yyval.ival = val_peek(0).ival;
                                                        representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size()+2));
                                                        representacionPolaca.add("");bfs.push(representacionPolaca.size()-1);
                                                        representacionPolaca.add("BI"); }
break;
case 84:
//#line 194 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ELSE en el cuerpo de la selección"); }
break;
case 85:
//#line 195 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 86:
//#line 198 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 87:
//#line 199 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 88:
//#line 200 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 92:
//#line 208 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 93:
//#line 209 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 94:
//#line 210 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 98:
//#line 216 "gramatica.y"
{ yyval.ival = val_peek(2).ival; representacionPolaca.add(((Token) val_peek(1).obj).getLexema());cantidadIdEnListaId = 0; listaIdentificadores.clear();}
break;
case 99:
//#line 219 "gramatica.y"
{ yyval.ival = val_peek(3).ival; Parser.agregarEstructuraDetectadas(val_peek(3).ival, "IF"); representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size())); }
break;
case 100:
//#line 220 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia de selección");}
break;
case 101:
//#line 221 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 102:
//#line 222 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el END_IF en la sentencia de selección"); }
break;
case 103:
//#line 223 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 104:
//#line 224 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 105:
//#line 225 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 106:
//#line 226 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 107:
//#line 229 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();representacionPolaca.add("");bfs.push(representacionPolaca.size()-1); representacionPolaca.add("BF");}
break;
case 110:
//#line 234 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 111:
//#line 235 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 112:
//#line 238 "gramatica.y"
{yyval.ival = val_peek(0).ival;
                                                            representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size()+2));
                                                            representacionPolaca.add("");bfs.push(representacionPolaca.size()-1);
                                                            representacionPolaca.add("BI"); }
break;
case 114:
//#line 245 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ELSE en el cuerpo de la selección"); }
break;
case 115:
//#line 246 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 116:
//#line 249 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 117:
//#line 250 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 118:
//#line 253 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 119:
//#line 254 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 120:
//#line 255 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 121:
//#line 256 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 122:
//#line 257 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 123:
//#line 258 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 124:
//#line 261 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 125:
//#line 262 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 126:
//#line 263 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
break;
case 127:
//#line 264 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 129:
//#line 268 "gramatica.y"
{ yyval.ival = ((Token) val_peek(1).obj).getNumeroDeLinea();}
break;
case 131:
//#line 272 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) val_peek(2).obj).getLexema()); representacionPolaca.add(((Token) val_peek(4).obj).getLexema());}
break;
case 132:
//#line 273 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) val_peek(4).obj).getLexema());}
break;
case 133:
//#line 274 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 134:
//#line 275 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 135:
//#line 276 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 136:
//#line 277 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 137:
//#line 278 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 138:
//#line 279 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 139:
//#line 282 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "FOR"); }
break;
case 140:
//#line 283 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 141:
//#line 286 "gramatica.y"
{ yyval.ival = val_peek(5).ival; }
break;
case 142:
//#line 287 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 143:
//#line 288 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 144:
//#line 289 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 145:
//#line 290 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 146:
//#line 291 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 147:
//#line 292 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 148:
//#line 295 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 149:
//#line 296 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 150:
//#line 297 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 151:
//#line 298 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 152:
//#line 299 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 153:
//#line 302 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 154:
//#line 305 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 155:
//#line 306 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 156:
//#line 307 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 159:
//#line 314 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 160:
//#line 315 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 161:
//#line 316 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 162:
//#line 317 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": Falta operador, operandos, o coma entre expresiones"); }
break;
case 163:
//#line 320 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 164:
//#line 321 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 165:
//#line 322 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 166:
//#line 325 "gramatica.y"
{ yyval.sval = TablaSimbolos.getTipo(val_peek(0).sval); System.out.println("AAAAAAAAAAAAAAAAA TIPO:" + TablaSimbolos.getTipo(val_peek(0).sval)); }
break;
case 167:
//#line 326 "gramatica.y"
{ yyval.sval = TablaSimbolos.getTipo(val_peek(0).sval); }
break;
case 168:
//#line 327 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();  yyval.sval = "single"; }
break;
case 169:
//#line 328 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 170:
//#line 329 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 171:
//#line 332 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 172:
//#line 333 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 173:
//#line 336 "gramatica.y"
{ yyval.sval = val_peek(0).sval;}
break;
case 174:
//#line 337 "gramatica.y"
{ yyval.sval = ((Token) val_peek(1).obj).getLexema() + val_peek(0).sval; agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": la constante se va de rango. No se permiten constantes enteras negativas."); }
break;
case 175:
//#line 338 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 176:
//#line 339 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 177:
//#line 340 "gramatica.y"
{
                                                            System.out.println(((Token) val_peek(1).obj).getLexema() + ((Token) val_peek(0).obj).getLexema());
                                                            yyval.sval = ((Token) val_peek(1).obj).getLexema() + ((Token) val_peek(0).obj).getLexema();
                                                            String lexema = ((Token) val_peek(0).obj).getLexema();
                                                            int cantidadDeUsos = TablaSimbolos.getCantidadDeUsos(lexema);

                                                            if (cantidadDeUsos > 1) {
                                                                /* Bajar la cantidad de usos en 1*/
                                                                TablaSimbolos.decrementarUso(lexema);
                                                            } else {
                                                                /* Eliminar la entrada*/
                                                                TablaSimbolos.eliminarLexema(lexema);
                                                            }

                                                            String lexemaNegativo = "-" + lexema;

                                                            float numero = Float.parseFloat(lexemaNegativo.replace('s','e'));
                                                            if (numero == Float.POSITIVE_INFINITY || numero == Float.NEGATIVE_INFINITY || numero == -0.0f) {
                                                                System.out.println(numero);
                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(0).obj).getNumeroDeLinea() + ": la constante se va de rango");
                                                            } else {
                                                                /* Hay que fijarse si ya esta la negativa en la tabla, sino agregarla como negativa.*/
                                                                if (TablaSimbolos.existeLexema(lexemaNegativo)) {
                                                                    TablaSimbolos.aumentarUso(lexemaNegativo);
                                                                } else {
                                                                    TablaSimbolos.agregarLexema(lexemaNegativo, new CampoTablaSimbolos(false, TablaSimbolos.SINGLE));
                                                                    TablaSimbolos.aumentarUso(lexemaNegativo);
                                                                }
                                                                representacionPolaca.add("-"+ ((Token) val_peek(0).obj).getLexema());
                                                            }
                                                        }
break;
case 178:
//#line 371 "gramatica.y"
{ yyval.obj = ((Token) val_peek(1).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 179:
//#line 374 "gramatica.y"
{
                                                                                                    yyval.sval = TablaSimbolos.getTipoRetorno(((Token) val_peek(3).obj).getLexema());
                                                                                                    if (!TablaSimbolos.getTipoRetorno(((Token) val_peek(3).obj).getLexema()).equals(val_peek(1).sval)) {
                                                                                                        agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea()  + ": Parametro real: " + val_peek(1).sval +". Parametro formal: " + TablaSimbolos.getTipoRetorno(((Token) val_peek(3).obj).getLexema()) + ".");
                                                                                                    }
                                                                                                    representacionPolaca.add(((Token) val_peek(3).obj).getLexema()); representacionPolaca.add("BI");

                                                                                                }
break;
case 180:
//#line 382 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 181:
//#line 383 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1818 "Parser.java"
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
