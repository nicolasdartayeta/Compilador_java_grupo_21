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
    import java.util.Arrays;
    import java.util.List;
    import java.util.Stack;
    import compilador.generadorCodigo.GeneradorAssembler;
//#line 28 "Parser.java"




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
    1,    1,    1,   12,   12,    8,   15,   13,   13,   16,
   16,   16,   16,   16,   14,   14,   14,   14,   17,   17,
   17,   17,   17,   17,   17,   21,   21,   22,   22,   22,
   19,   19,   19,   19,   19,   19,   19,   19,   27,   27,
   27,   27,   29,   30,   30,   30,   24,   24,   24,   24,
   31,   31,    5,    5,    5,    5,    5,    5,   18,   32,
   32,   32,   32,   32,   32,   32,   32,   26,   35,   35,
   35,   35,   36,   37,   37,   37,   28,   28,   39,   39,
   39,   39,   39,   39,   38,   38,   38,   38,   38,   40,
   40,   20,   20,   20,   20,   20,   20,   20,   20,   33,
   33,   23,   23,   23,   23,   23,   23,   23,   41,   41,
   41,   41,   41,   42,   43,   44,   44,   44,   34,   34,
   25,   25,   25,   25,   46,   46,   46,   47,   47,   47,
   47,   47,   45,   45,   48,   48,   48,   48,   48,   48,
   49,   49,   49,
};
final static short yylen[] = {                            2,
    4,    5,    5,    3,    3,    3,    2,    5,    4,    4,
    3,    5,    2,    1,    1,    1,    2,    3,    4,    4,
    1,    1,   10,    9,   11,   11,    9,    8,    9,    9,
    1,    1,    1,    3,    1,    3,    2,    1,    1,    1,
    1,    1,    1,    3,    3,    4,    3,    4,    5,    2,
    1,    1,    3,    1,    2,    2,    1,    1,    2,    3,
    3,    1,    1,    1,    1,    2,    2,    5,    6,    6,
    4,    5,    5,    3,    6,    6,    5,    3,    3,    2,
    1,    2,    1,    2,    1,    1,    4,    5,    5,    1,
    2,    1,    2,    3,    3,    1,    1,    1,    3,    4,
    5,    5,    3,    6,    6,    5,    3,    4,    3,    2,
    1,    2,    1,    2,    1,    1,    3,    2,    1,    1,
    1,    1,    1,    1,    4,    5,    3,    5,    1,    2,
    1,    5,    5,    6,    6,    6,    6,    4,    4,    2,
    2,    6,    2,    1,    5,    5,    5,    4,    7,    6,
    6,    6,    6,    1,    3,    2,    2,    1,    3,    1,
    3,    3,    1,    1,    3,    3,    1,    1,    1,    4,
    3,    1,    1,    1,    1,    2,    1,    1,    2,    2,
    4,    3,    5,
};
final static short yydefred[] = {                         0,
   41,   42,   43,    0,    0,    0,    0,   33,  154,   32,
   31,    0,    0,    0,    0,    0,   14,   15,   16,    0,
    0,   21,   22,   38,   40,    0,    0,    0,   97,    0,
    0,   96,   98,    0,    0,    0,    0,    0,   17,  164,
    0,  173,  174,  177,    0,    0,  178,    0,  168,    0,
    0,  175,    0,  167,  169,  172,    0,    0,    0,    0,
    4,   13,    0,    0,    0,    0,   37,    0,    0,    0,
   93,  141,    0,  129,  140,    0,    0,    0,    0,  113,
    0,  143,    0,    0,    0,    0,    0,    0,    0,    0,
    5,    0,  179,  180,  176,    0,    0,  118,    0,    0,
  121,  122,  119,  120,  123,  124,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   35,    0,    0,    0,    0,
   44,   47,    0,    0,   18,    0,    0,   36,    0,    0,
   58,    0,   57,    0,   62,   63,   64,   65,    0,    0,
   54,    0,   51,    0,   94,   95,    0,  131,    0,    0,
  107,  103,    0,    0,  112,  115,    0,    0,    0,    0,
    0,    0,    0,    9,    0,    0,    0,  182,    0,  108,
    0,  171,    0,    0,    0,    0,    0,    0,  165,  166,
  139,  138,    0,    0,    0,    0,    0,    0,    0,    0,
   19,   20,    0,    0,    0,    0,   46,   56,   55,    0,
   59,   67,    0,   90,   66,    0,    0,   83,    0,    0,
    0,    0,   48,  127,    0,  130,  109,    0,  100,  114,
    0,    0,    0,    0,    0,  155,    0,    3,    8,    2,
   12,    0,  181,    0,  170,    0,  106,    0,  132,    0,
  133,    0,   34,    0,    0,    0,    0,   49,    0,    0,
    0,    0,    0,   60,   61,   92,    0,    0,   78,   74,
    0,    0,   85,   82,   53,    0,  125,  101,  102,  146,
    0,  147,    0,    0,    0,  183,  104,  105,  134,  136,
  135,  137,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   91,   79,    0,   71,   84,  126,  128,  142,
    0,    0,    0,  153,  158,  152,  150,    0,    0,    0,
    0,    0,    0,    0,   77,    0,   68,    0,   87,   72,
   73,  149,  156,  157,    0,    0,    0,    0,   28,   75,
   76,   69,   70,   88,   89,   24,    0,   30,   29,   27,
    0,   23,   25,   26,
};
final static short yydgoto[] = {                          5,
   15,   16,   17,   18,   19,   20,   21,   22,   23,  117,
   24,   25,   26,  132,   27,  144,  204,   28,  135,   29,
  137,  138,   30,  208,   50,   31,  209,   51,  210,  264,
  257,   32,   33,  127,   78,   79,  155,   80,  107,  149,
   34,   35,   86,  304,   52,   53,   54,   55,   56,
};
final static short yysindex[] = {                      -231,
    0,    0,    0,  794,    0,  707, -249,    0,    0,    0,
    0,   53, -238,   27, -206,  721,    0,    0,    0, -250,
  518,    0,    0,    0,    0, -197, -164, -156,    0,  565,
  845,    0,    0,   83,    9,   36,  837,   -9,    0,    0,
 -158,    0,    0,    0,   60,  170,    0, -154,    0,  960,
  854,    0,  283,    0,    0,    0,   42, -247, -251,  256,
    0,    0, -104,  -81,  170,  256,    0,  575,  958, -254,
    0,    0,  188,    0,    0,  909, -116, -226,  892,    0,
  170,    0,   72,  256,  -71,  -94,  794,  -88,  731,  794,
    0,   85,    0,    0,    0,  864,  114,    0,  500,  500,
    0,    0,    0,    0,    0,    0,  170,  902,  -67,  500,
  500,  -57,  -36,  -30,   28,    0,   -8, -247,  163, -206,
    0,    0,  958,  -76,    0,  144,   -1,    0,  128,   23,
    0,  555,    0, -150,    0,    0,    0,    0,  491,  122,
    0,  323,    0,   13,    0,    0,   29,    0,  916,  892,
    0,    0, -143,  909,    0,    0,   17,  170,   41,  139,
  295,  170,  774,    0,  -58,   71,  784,    0,  -61,    0,
   59,    0,   74,  283,  283,  144,   61,   78,    0,    0,
    0,    0, -128, -123,   87, -247,  287,  111,  256,  138,
    0,    0,  170,  170,  597,  170,    0,    0,    0,  -38,
    0,    0,  679,    0,    0,  449,  160,    0, -192,  638,
    0,  148,    0,    0, -122,    0,    0,  125,    0,    0,
  165,  187,  189,  170,  193,    0,  198,    0,    0,    0,
    0,  212,    0,  214,    0,  228,    0,  142,    0,  143,
    0,  256,    0,  246,  256,  256,  610,    0,  144,  617,
  655,  231,   93,    0,    0,    0,  662,  638,    0,    0,
 -115,  449,    0,    0,    0,  146,    0,    0,    0,    0,
  254,    0,  -66, -227, -227,    0,    0,    0,    0,    0,
    0,    0,  624,  256,  852,  907,  286,  268,  279,  270,
 -113, -105,    0,    0,  191,    0,    0,    0,    0,    0,
 -227,  295,  295,    0,    0,    0,    0,  328,  944,  337,
  340,  317,  318,  319,    0,  226,    0,  233,    0,    0,
    0,    0,    0,    0,  324, -236,  327,  329,    0,    0,
    0,    0,    0,    0,    0,    0,  -99,    0,    0,    0,
  243,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  190,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  511,    0,    0,    0,  605,    0,    0,
  274,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  316,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -133,    0,
    0,    0,    0,    0,    0,    0,    0,  612,  622,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  102,    0,    0,    0,    0,    0,    0,    0,  232,
    0,    0,    0,    0,    0, -173,  -69,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  338,    0,    0,    0,    0,    0,    0,    0, -111,
    0,    0,    0,  -28,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  625,  626,    0,    0,    0,    0,
    0,    0,    0,  358,  400,  442,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -21,
   -3,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  513,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -170,    0,
    0,    0,    0,    0,    0,    0,    0,   70,    0,    0,
    0,  115,    0,    0,    0,    0,    0,    0,    0,    0,
  533,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  459,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    6,   18,  -11,  -64,  -14,  -44,  -19,    0,    0,  -41,
  -12,  567,    0,    0,    0,  512,  -65,  -48,    0,  -17,
    0,    0,    5,  -20,   24,   33, -148,  -33,  428,  378,
    0,    0,    0,    0,  -32,  563,  492,   14,    0,    0,
    0,    0,  557, -243,  -43,  470,  482,    0,    0,
};
final static int YYTABLESIZE=1248;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   64,   95,  133,  131,   62,    6,    1,    8,   67,    2,
    3,    8,   96,  116,  116,   74,   74,  119,  109,  134,
  337,  118,   85,   38,  142,    1,   62,  145,    2,    3,
  306,  307,   39,   49,   42,   43,   74,   10,   11,   57,
  146,   10,   11,   75,   49,  338,  252,  157,   63,  159,
  136,   67,   49,  128,   89,  152,  143,  322,  148,  302,
  303,   74,    4,  171,   74,  120,  199,  198,   49,  153,
   49,   85,  139,  116,   60,  177,  187,   62,  142,   49,
  115,   74,  160,  134,   49,  159,   49,   49,  126,  260,
  134,  134,  156,   74,   49,  195,   68,   49,   49,   70,
  140,  288,  289,  261,  163,  200,  160,  167,  160,  159,
  143,  159,  218,   69,  136,  169,   49,  226,  205,   92,
  173,  136,  136,   97,  222,   71,  225,  238,  227,  212,
  176,  201,  240,  266,  216,   74,  139,  256,  219,   74,
  295,  243,  316,  139,  139,   49,  134,   49,  111,   49,
  318,   62,  122,  239,  134,   62,  341,  134,  241,  267,
  250,  134,  111,  156,  140,  151,  296,  220,  317,  247,
  110,  140,  140,  123,  124,    1,  319,  136,    2,    3,
   49,   49,  342,   49,  110,  136,   99,  162,  136,  263,
  273,  293,  136,   39,  232,   42,   43,  161,   66,  139,
  125,  134,  134,   99,  100,  191,  164,  139,  134,  134,
  139,   49,   99,  134,  139,  301,  249,  233,  192,  253,
  302,  303,  283,   39,  181,  285,  286,  140,  178,  305,
  305,  305,  136,  136,   67,  140,  229,  263,  140,  136,
  136,  297,  140,  254,  136,  182,    7,    1,  183,    8,
    2,    3,   41,  116,  139,  139,  255,  305,  323,  324,
   81,  139,  139,  185,  309,    1,  139,  116,    2,    3,
   67,  186,   67,   67,   81,   50,    9,   41,  193,   10,
   11,   12,  140,  140,   90,   91,   84,   13,   14,  140,
  140,  213,   99,  100,  140,  221,   67,  112,   41,   58,
  196,    2,    3,   42,   43,   44,  184,   45,   40,   41,
  214,   59,    2,    3,   42,   43,   44,   39,   45,  223,
  113,   42,   43,   93,  114,   47,  230,   40,   41,   87,
   46,    2,    3,   42,   43,   44,   47,   45,   99,  100,
   40,   41,   48,   94,    2,    3,   42,   43,   44,  158,
   45,   80,  235,   48,  234,   47,  236,   99,  100,  237,
   81,   82,  242,  168,   83,   80,  164,  164,   47,   40,
   41,  291,   48,    2,    3,   42,   43,   44,    1,   45,
  164,    2,    3,   40,   41,   48,  246,    2,    3,   42,
   43,   44,  172,   45,   40,   41,   86,   47,    2,    3,
   42,   43,   44,  265,   45,  194,  268,    9,   99,  100,
   86,   47,  129,  206,   48,  203,  248,  207,   13,  269,
  224,  130,   47,  279,  281,   40,   41,  298,   48,    2,
    3,   42,   43,   44,  188,   45,  280,  282,  189,   48,
  299,  259,  186,  270,    1,   39,   39,    2,    3,   39,
   39,   39,   39,   47,   39,   39,   39,   39,   39,   39,
   39,   39,   39,   39,   39,  271,   39,  272,   39,   39,
   48,   39,  320,    9,  274,   39,   39,   39,   12,  275,
   39,   39,  147,   39,   13,  321,   39,   45,   45,   39,
  276,   45,   45,   45,   45,  277,   45,   45,   45,   45,
   45,   45,   45,   45,   45,   45,   45,  332,   45,  278,
   45,   45,    1,   45,  334,    2,    3,   45,   45,   45,
  333,  284,   45,   45,  343,   45,  290,  335,   45,   41,
   41,   45,  300,   41,   41,   41,   41,  344,   41,   41,
   41,   41,  312,   41,   41,   41,   41,   41,   41,  110,
  111,  315,   41,   41,   41,   41,   42,   43,  244,   41,
   41,   41,  245,  313,   41,   41,  186,   41,  174,  175,
   41,  163,  163,   41,  314,  163,  163,  163,  163,  211,
  163,  163,    2,    3,  325,  163,  163,  163,  163,  163,
  163,  179,  180,  327,  163,  163,  328,  163,  329,  330,
  331,  163,  163,  163,    7,  336,  163,  163,  339,  163,
  340,   11,  163,  161,  161,  163,   52,  161,  161,  161,
  161,    6,  161,  161,   10,    1,  121,  161,  161,  161,
  161,  161,  161,  258,  190,  294,  161,  161,  150,  161,
  160,  217,    0,  161,  161,  161,    0,    0,  161,  161,
    0,  161,    0,    0,  161,  162,  162,  161,    0,  162,
  162,  162,  162,    0,  162,  162,    0,    0,    0,  162,
  162,  162,  162,  162,  162,    0,    0,    0,  162,  162,
    0,  162,    0,    0,    0,  162,  162,  162,    0,    0,
  162,  162,    0,  162,    0,    0,  162,  117,  117,  162,
    0,  117,  117,  117,  117,    1,    0,    0,    2,    3,
    0,    0,    0,    0,  151,  151,    0,    0,  151,  151,
  117,    0,    0,  117,    0,    0,    0,  117,  117,  117,
    0,    0,  117,  117,    9,  117,  151,  151,  117,  129,
  151,  117,  203,    0,  151,   13,  202,    1,  130,  151,
    2,    3,  151,    0,    0,  151,   41,    0,  151,    2,
    3,   42,   43,   44,    0,   45,  144,  144,  148,  148,
  144,  144,  148,  148,    1,    0,    9,    2,    3,    0,
    0,  129,    0,   47,  203,    0,   65,   13,  145,  145,
  130,    0,  145,  145,    0,    0,  144,   66,  148,    0,
   48,  144,    0,  148,  144,    0,  148,  144,    0,  148,
  144,    1,  148,    8,    2,    3,    0,    0,  145,    0,
   72,    1,    0,  145,    2,    3,  145,    0,    0,  145,
    0,    1,  145,    8,    2,    3,    0,    0,    0,    0,
    9,    0,    0,   10,   11,  129,    0,    0,    0,  197,
    9,   13,   14,    1,  130,   12,    2,    3,   73,    0,
    9,   13,    0,   10,   11,  129,    1,    0,    0,    2,
    3,   13,   14,    1,  130,  251,    2,    3,    0,    0,
    1,    0,    9,    2,    3,    0,  287,  129,  206,   66,
  203,    0,    0,   13,    1,  170,  130,    2,    3,    0,
  308,    0,    9,   66,    0,    0,    0,  129,  206,    0,
  203,    1,    0,   13,    2,    3,  130,    0,    1,    0,
    0,    2,    3,    9,    0,    0,    0,    0,  129,    0,
  262,  203,    0,    0,   13,    1,    0,  130,    2,    3,
    9,    0,    0,    0,    0,  129,  206,    9,  203,    0,
    0,   13,  129,    0,  130,    0,  292,    0,   13,    0,
    0,  130,   36,    1,    9,    8,    2,    3,    0,  129,
    0,    0,    0,    0,    0,   13,    7,    1,  130,    8,
    2,    3,    0,    0,    0,    0,  165,    1,    0,    8,
    2,    3,    9,    0,    0,   10,   11,   12,    0,    0,
   37,    0,    0,   13,   14,    0,    9,    0,    0,   10,
   11,   12,    0,    0,    0,   61,    9,   13,   14,   10,
   11,   12,    0,    0,    0,  166,    0,   13,   14,    7,
    1,    0,    8,    2,    3,    0,    0,    0,    0,    7,
    1,    0,    8,    2,    3,    0,    0,    0,    0,    7,
    1,    0,    8,    2,    3,    0,    0,    0,    0,    9,
    0,    0,   10,   11,   12,    0,    0,    0,  228,    9,
   13,   14,   10,   11,   12,    0,    0,    0,  231,    9,
   13,   14,   10,   11,   12,    0,    0,    0,    0,    0,
   13,   14,   88,    1,    0,    8,    2,    3,    0,    0,
    0,    1,    0,    0,    2,    3,    0,    0,    1,    0,
    1,    2,    3,    2,    3,    0,    0,    0,    0,    0,
    1,    0,    9,    2,    3,   10,   11,   12,  310,    0,
    9,   66,  108,   13,   14,   12,   76,    0,   73,    9,
   77,   13,  170,    0,   12,   76,    0,   73,    1,    9,
   13,    2,    3,    0,   12,   76,    0,   73,    1,    0,
   13,    2,    3,    1,    0,    1,    2,    3,    2,    3,
    0,    0,    1,    0,    0,    2,    3,    9,    0,    0,
    0,    0,   12,  311,  154,   73,   66,    9,   13,    0,
    0,    0,   12,   76,    9,   73,    0,    0,   13,   12,
    1,    9,   73,    2,    3,   13,   12,    0,    0,    0,
  215,    0,   13,  141,    1,   98,    8,    2,    3,    0,
  326,    0,    0,   66,   99,  100,    0,    0,    0,  101,
  102,  103,  104,  105,  106,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   10,   11,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
   20,   45,   68,   68,   16,    0,  257,  259,   21,  260,
  261,  259,   46,   58,   59,   30,   31,   59,   51,   68,
  257,  273,   35,    6,   69,  257,   38,  282,  260,  261,
  274,  275,  282,   46,  262,  263,   51,  289,  290,  278,
  295,  289,  290,   30,   57,  282,  195,   81,  299,   83,
   68,   64,   65,   66,   37,  282,   69,  301,   73,  287,
  288,   76,  294,   96,   79,   60,  132,  132,   81,  296,
   83,   84,   68,  118,  281,  108,  118,   89,  123,   92,
   57,   96,  256,  132,   97,  256,   99,  100,   65,  282,
  139,  140,   79,  108,  107,  129,  294,  110,  111,  256,
   68,  250,  251,  296,   87,  256,  280,   90,  282,  280,
  123,  282,  256,  278,  132,   92,  129,  161,  139,  278,
   97,  139,  140,  278,  158,  282,  160,  256,  162,  142,
  107,  282,  256,  256,  149,  150,  132,  203,  282,  154,
  256,  186,  256,  139,  140,  158,  195,  160,  282,  162,
  256,  163,  257,  282,  203,  167,  256,  206,  282,  282,
  194,  210,  296,  150,  132,  282,  282,  154,  282,  189,
  282,  139,  140,  278,  256,  257,  282,  195,  260,  261,
  193,  194,  282,  196,  296,  203,  256,  282,  206,  210,
  224,  257,  210,  282,  256,  262,  263,  269,  280,  195,
  282,  250,  251,  265,  266,  282,  295,  203,  257,  258,
  206,  224,  282,  262,  210,  282,  193,  279,  295,  196,
  287,  288,  242,  282,  282,  245,  246,  195,  296,  273,
  274,  275,  250,  251,  247,  203,  295,  258,  206,  257,
  258,  262,  210,  282,  262,  282,  256,  257,  279,  259,
  260,  261,  256,  282,  250,  251,  295,  301,  302,  303,
  282,  257,  258,  272,  284,  257,  262,  296,  260,  261,
  283,  280,  285,  286,  296,  279,  286,  281,  280,  289,
  290,  291,  250,  251,  294,  295,  278,  297,  298,  257,
  258,  279,  265,  266,  262,  279,  309,  256,  257,  273,
  278,  260,  261,  262,  263,  264,  279,  266,  256,  257,
  282,  285,  260,  261,  262,  263,  264,  282,  266,  279,
  279,  262,  263,  264,  283,  284,  256,  256,  257,  294,
  278,  260,  261,  262,  263,  264,  284,  266,  265,  266,
  256,  257,  301,  284,  260,  261,  262,  263,  264,  278,
  266,  282,  279,  301,  296,  284,  296,  265,  266,  282,
  278,  279,  276,  279,  282,  296,  265,  266,  284,  256,
  257,  279,  301,  260,  261,  262,  263,  264,  257,  266,
  279,  260,  261,  256,  257,  301,  276,  260,  261,  262,
  263,  264,  279,  266,  256,  257,  282,  284,  260,  261,
  262,  263,  264,  256,  266,  278,  282,  286,  265,  266,
  296,  284,  291,  292,  301,  294,  279,  296,  297,  295,
  282,  300,  284,  282,  282,  256,  257,  282,  301,  260,
  261,  262,  263,  264,  272,  266,  295,  295,  276,  301,
  295,  282,  280,  279,  257,  256,  257,  260,  261,  260,
  261,  262,  263,  284,  265,  266,  267,  268,  269,  270,
  271,  272,  273,  274,  275,  279,  277,  279,  279,  280,
  301,  282,  282,  286,  282,  286,  287,  288,  291,  282,
  291,  292,  295,  294,  297,  295,  297,  256,  257,  300,
  279,  260,  261,  262,  263,  282,  265,  266,  267,  268,
  269,  270,  271,  272,  273,  274,  275,  282,  277,  282,
  279,  280,  257,  282,  282,  260,  261,  286,  287,  288,
  295,  276,  291,  292,  282,  294,  296,  295,  297,  256,
  257,  300,  279,  260,  261,  262,  263,  295,  265,  266,
  267,  268,  257,  270,  271,  272,  273,  274,  275,  267,
  268,  282,  279,  280,  281,  282,  262,  263,  272,  286,
  287,  288,  276,  296,  291,  292,  280,  294,   99,  100,
  297,  256,  257,  300,  296,  260,  261,  262,  263,  257,
  265,  266,  260,  261,  257,  270,  271,  272,  273,  274,
  275,  110,  111,  257,  279,  280,  257,  282,  282,  282,
  282,  286,  287,  288,    0,  282,  291,  292,  282,  294,
  282,    0,  297,  256,  257,  300,  279,  260,  261,  262,
  263,    0,  265,  266,    0,    0,   60,  270,  271,  272,
  273,  274,  275,  206,  123,  258,  279,  280,   76,  282,
   84,  150,   -1,  286,  287,  288,   -1,   -1,  291,  292,
   -1,  294,   -1,   -1,  297,  256,  257,  300,   -1,  260,
  261,  262,  263,   -1,  265,  266,   -1,   -1,   -1,  270,
  271,  272,  273,  274,  275,   -1,   -1,   -1,  279,  280,
   -1,  282,   -1,   -1,   -1,  286,  287,  288,   -1,   -1,
  291,  292,   -1,  294,   -1,   -1,  297,  256,  257,  300,
   -1,  260,  261,  262,  263,  257,   -1,   -1,  260,  261,
   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,  260,  261,
  279,   -1,   -1,  282,   -1,   -1,   -1,  286,  287,  288,
   -1,   -1,  291,  292,  286,  294,  278,  279,  297,  291,
  282,  300,  294,   -1,  286,  297,  256,  257,  300,  291,
  260,  261,  294,   -1,   -1,  297,  257,   -1,  300,  260,
  261,  262,  263,  264,   -1,  266,  256,  257,  256,  257,
  260,  261,  260,  261,  257,   -1,  286,  260,  261,   -1,
   -1,  291,   -1,  284,  294,   -1,  269,  297,  256,  257,
  300,   -1,  260,  261,   -1,   -1,  286,  280,  286,   -1,
  301,  291,   -1,  291,  294,   -1,  294,  297,   -1,  297,
  300,  257,  300,  259,  260,  261,   -1,   -1,  286,   -1,
  256,  257,   -1,  291,  260,  261,  294,   -1,   -1,  297,
   -1,  257,  300,  259,  260,  261,   -1,   -1,   -1,   -1,
  286,   -1,   -1,  289,  290,  291,   -1,   -1,   -1,  295,
  286,  297,  298,  257,  300,  291,  260,  261,  294,   -1,
  286,  297,   -1,  289,  290,  291,  257,   -1,   -1,  260,
  261,  297,  298,  257,  300,  279,  260,  261,   -1,   -1,
  257,   -1,  286,  260,  261,   -1,  277,  291,  292,  280,
  294,   -1,   -1,  297,  257,  279,  300,  260,  261,   -1,
  277,   -1,  286,  280,   -1,   -1,   -1,  291,  292,   -1,
  294,  257,   -1,  297,  260,  261,  300,   -1,  257,   -1,
   -1,  260,  261,  286,   -1,   -1,   -1,   -1,  291,   -1,
  293,  294,   -1,   -1,  297,  257,   -1,  300,  260,  261,
  286,   -1,   -1,   -1,   -1,  291,  292,  286,  294,   -1,
   -1,  297,  291,   -1,  300,   -1,  295,   -1,  297,   -1,
   -1,  300,  256,  257,  286,  259,  260,  261,   -1,  291,
   -1,   -1,   -1,   -1,   -1,  297,  256,  257,  300,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,  286,   -1,   -1,  289,  290,  291,   -1,   -1,
  294,   -1,   -1,  297,  298,   -1,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,   -1,  295,   -1,  297,  298,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  286,
   -1,   -1,  289,  290,  291,   -1,   -1,   -1,  295,  286,
  297,  298,  289,  290,  291,   -1,   -1,   -1,  295,  286,
  297,  298,  289,  290,  291,   -1,   -1,   -1,   -1,   -1,
  297,  298,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,  257,   -1,   -1,  260,  261,   -1,   -1,  257,   -1,
  257,  260,  261,  260,  261,   -1,   -1,   -1,   -1,   -1,
  257,   -1,  286,  260,  261,  289,  290,  291,  277,   -1,
  286,  280,  279,  297,  298,  291,  292,   -1,  294,  286,
  296,  297,  279,   -1,  291,  292,   -1,  294,  257,  286,
  297,  260,  261,   -1,  291,  292,   -1,  294,  257,   -1,
  297,  260,  261,  257,   -1,  257,  260,  261,  260,  261,
   -1,   -1,  257,   -1,   -1,  260,  261,  286,   -1,   -1,
   -1,   -1,  291,  277,  293,  294,  280,  286,  297,   -1,
   -1,   -1,  291,  292,  286,  294,   -1,   -1,  297,  291,
  257,  286,  294,  260,  261,  297,  291,   -1,   -1,   -1,
  295,   -1,  297,  256,  257,  256,  259,  260,  261,   -1,
  277,   -1,   -1,  280,  265,  266,   -1,   -1,   -1,  270,
  271,  272,  273,  274,  275,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  289,  290,
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
"nombre_funcion : tipo FUN IDENTIFICADOR_GENERICO",
"encabezado_funcion : nombre_funcion PARENTESIS_L parametro PARENTESIS_R",
"encabezado_funcion : tipo FUN PARENTESIS_L parametro PARENTESIS_R",
"parametro : tipo IDENTIFICADOR_GENERICO",
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
"encabezado_for_obligatorio : inicio_for PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion",
"encabezado_for_obligatorio : inicio_for asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion",
"encabezado_for_obligatorio : inicio_for PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA",
"encabezado_for_obligatorio : inicio_for PARENTESIS_L asignacion_enteros condicion PUNTO_Y_COMA accion",
"encabezado_for_obligatorio : inicio_for PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion accion",
"inicio_for : FOR",
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
"invocacion_a_funcion : IDENTIFICADOR_GENERICO PARENTESIS_L expresion_aritmetica PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_GENERICO PARENTESIS_L PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_GENERICO PARENTESIS_L expresion_aritmetica error PARENTESIS_R",
};

//#line 582 "gramatica.y"
private static int cantidadIdEnListaId = 0;
private static Lexer lex;
private static ArrayList<String> representacionPolaca;

// Pila de ambitos
public static final Stack<String> ambito = new Stack<>();

// Pila para bifurcaciones
public static final Stack<Integer> bfs = new Stack<>();

// Pila para encabezados con dos condiciones
public static final Stack<Boolean> dobleCondicion = new Stack<>();

// Pila para almacenar acción del for
public static final Stack<String> aux = new Stack<>();

// Tipo de mensajes
public static final String ERROR_LEXICO = "ERROR_LEXICO";
public static final String ERROR_SINTACTICO = "ERROR_SINTACTICO";
public static final String ERROR_SEMANTICO = "ERROR_SEMANTICO";

// Código ANSI para texto rojo
public static final String ROJO = "\033[31m";
public static final String VERDE = "\033[32m";
public static final String RESET = "\033[0m";

// Flags
public static boolean parsingConErrores = false;
public static boolean lexingConErrores = false;
public static boolean codIntermedioConErrores = false;
private static boolean returnEncontrado = false;

// Lista de tokens recibidos
public static final List<Token> tokensRecibidos = new ArrayList<>();

// Lista de identificadores
public static final List<String> listaIdentificadores = new ArrayList<>();

// Lista de expresiones
public static final List<String> listaExpresiones = new ArrayList<>();

// Lista del tipo al que se evaluan expresiones
public static final List<String> listaTipoExpresiones = new ArrayList<>();
// Lista de tipos
public static final List<String> listaTipos = new ArrayList<>();

// Lista de estructuras detectadas
public static final List<String> estructurasDetectadas = new ArrayList<>();

// Listas de errores
public static final List<String> erroresLexicos = new ArrayList<>();
public static final List<String> erroresSintacticos = new ArrayList<>();
public static final List<String> erroresSemanticos = new ArrayList<>();

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

public static void agregarUsoAIdentificador(String identificador, String uso) {
    TablaSimbolos.setUso(identificador, uso);
}

public static void agregarUsoAIdentificadores(List<String> identificadores, String uso) {
    for (String lexema: identificadores) {
        TablaSimbolos.setUso(lexema, uso);
    }
}

//Para hacer chequeos de ambitos
public static String estaAlAlcance(String lexema){
    String ambitoActual = getAmbitoActual();
    while (ambitoActual.length()> 1){
        String idAmb = lexema + ambitoActual;
        if (TablaSimbolos.existeLexema(idAmb)){
            return ambitoActual;
        }
        ambitoActual = ambitoActual.substring(0,ambitoActual.lastIndexOf(':'));
    }
    return null;
};

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
    } else if (tipo.equals(Parser.ERROR_SEMANTICO)){
        codIntermedioConErrores = true;
    }

    listaErrores.add(String.format("%-20s", tipo) + "| "+ error);
}

public static List<List<String>> formatearLista(List<String> lista) {
    String separador = ",";

    List<List<String>> sublists = new ArrayList<>();
    List<String> currentSublist = new ArrayList<>();

    for (String item : lista) {
        if (item.equals(separador)) {
            // Add the current sublist to the list of sublists if it's not empty
            if (!currentSublist.isEmpty()) {
                sublists.add(new ArrayList<>(currentSublist));
                currentSublist.clear();
            }
        } else {
            // Add item to the current sublist
            currentSublist.add(item);
        }
    }
    // Add the last sublist if it has elements
    if (!currentSublist.isEmpty()) {
        sublists.add(currentSublist);
    }
    return sublists;
}

// Agregar los campos de una variable struct a la tabla de simbolos
public static void crearCampo(String tipo, String lexema){
    List<CampoTablaSimbolos.Campo> campos = TablaSimbolos.getCamposTablaSimbolos(tipo);
    for (CampoTablaSimbolos.Campo campo: campos){
        if (TablaSimbolos.esUnTipo(campo.tipo())){
            crearCampo(campo.tipo(), lexema + "." + campo.nombre());
        } else {
            String nombreCampo = lexema + "." + campo.nombre() + getAmbitoActual();
            CampoTablaSimbolos nuevoCampo = new CampoTablaSimbolos(false, campo.tipo());
            TablaSimbolos.agregarLexema(nombreCampo, nuevoCampo);
            agregarAmbitoAIdentificador(nombreCampo);
            TablaSimbolos.setUso(nombreCampo, "nombre de variable");
        }
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

    //System.out.println(Parser.VERDE + "TOKENS RECIBIDOS DEL ANALIZADOR LEXICO" + Parser.RESET);
    //Parser.imprimirLista(tokensRecibidos);

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

    if (Parser.codIntermedioConErrores){
            System.out.println(Parser.ROJO + "SE ENCONTRARON ERRORES SEMANTICOS" + Parser.RESET);
            Parser.imprimirLista(erroresSemanticos);
        } else {
            System.out.println(Parser.VERDE + "NO SE ENCONTRARON ERRORES SEMANTICOS" + Parser.RESET);
        }

    //Parser.imprimirLista(estructurasDetectadas);

    TablaSimbolos.imprimirTabla();

    imprimirPolaca(representacionPolaca);

    if (!lexingConErrores && !parsingConErrores && !codIntermedioConErrores) {
        GeneradorAssembler gen = new GeneradorAssembler(representacionPolaca, "out.asm");
        gen.generarCodigoAssembler();
    }
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
//#line 1051 "Parser.java"
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
//#line 21 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(((Token) val_peek(3).obj).getNumeroDeLinea(), "PROGRAMA"); agregarUsoAIdentificador(((Token) val_peek(3).obj).getLexema(), "nombre de programa");}
break;
case 2:
//#line 22 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(1).obj).getNumeroDeLinea() + ": Todo lo que esta despues del END no forma parte del programa."); }
break;
case 3:
//#line 23 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(1).obj).getNumeroDeLinea() + ": Todo lo que esta despues del identificador del programa y antes del primer begin no forma parte del programa."); }
break;
case 4:
//#line 24 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea 1: Falta identificador de programa"); }
break;
case 5:
//#line 25 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + (((Token) val_peek(2).obj).getNumeroDeLinea() +1) + ": Falta un BEGIN despues del identificador del programa"); }
break;
case 6:
//#line 26 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Ultima linea: Falta un END al final del programa"); }
break;
case 7:
//#line 27 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta un BEGIN despues del identificador del programa y falta un END al final del programa"); }
break;
case 8:
//#line 28 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR."); }
break;
case 9:
//#line 29 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR."); }
break;
case 10:
//#line 30 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. FALTA EL END AL FINAL DEL PROGRAMA"); }
break;
case 11:
//#line 31 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. FALTA EL END AL FINAL DEL PROGRAMA"); }
break;
case 12:
//#line 32 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(3).obj).getNumeroDeLinea() + ": No se permite sentencias entre el identificador del programa y el BEGIN."); }
break;
case 17:
//#line 42 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)val_peek(0).obj).getNumeroDeLinea() + ": Syntax Error"); }
break;
case 18:
//#line 45 "gramatica.y"
{
                                                                                System.out.println(getAmbitoActual());
                                                                                Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S");
                                                                                eliminarUltimosElementos(representacionPolaca, listaIdentificadores.size());
                                                                                agregarTipoAIdentificadores(val_peek(2).sval);
                                                                                agregarAmbitoAIdentificadores(listaIdentificadores);
                                                                                agregarUsoAIdentificadores(listaIdentificadores, "nombre de variable");

                                                                                for (int i = 0; i < listaIdentificadores.size(); i++) {
                                                                                   System.out.println((TablaSimbolos.existeLexema(listaIdentificadores.get(i) + getAmbitoActual())));
                                                                                   if ((TablaSimbolos.existeLexema(listaIdentificadores.get(i) + getAmbitoActual())) || listaIdentificadores.get(i).charAt(0) == 'x' || listaIdentificadores.get(i).charAt(0) == 'y' || listaIdentificadores.get(i).charAt(0) == 'z' || listaIdentificadores.get(i).charAt(0) == 's'){
                                                                                        agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ val_peek(2).ival + ": Variable " + listaIdentificadores.get(i) +" ya declarada en el mismo ambito");
                                                                                   } else {
                                                                                        TablaSimbolos.cambiarLexema(listaIdentificadores.get(i), listaIdentificadores.get(i) + getAmbitoActual());
                                                                                        if (TablaSimbolos.esUnTipo(val_peek(2).sval) && TablaSimbolos.getTipo(val_peek(2).sval).equals("STRUCT")) {
                                                                                            for(String identificador: listaIdentificadores) {
                                                                                                crearCampo(val_peek(2).sval, identificador);
                                                                                            }
                                                                                        };
                                                                                     };
                                                                                };
                                                                                listaIdentificadores.clear();
                                                                            }
break;
case 19:
//#line 68 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 20:
//#line 69 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 21:
//#line 70 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FUNCION"); }
break;
case 23:
//#line 74 "gramatica.y"
{
                                                                                                                                                    yyval.ival = ((Token) val_peek(9).obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) val_peek(9).obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) val_peek(1).obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                    eliminarUltimosElementos(representacionPolaca, listaIdentificadores.size());
                                                                                                                                                    agregarUsoAIdentificador(lexema, "nombre de struct");
                                                                                                                                                    listaIdentificadores.forEach((subcampo)->TablaSimbolos.deleteEntrada(subcampo));
                                                                                                                                                    agregarAmbitoAIdentificador(lexema);
                                                                                                                                                    TablaSimbolos.agregarCampos(lexema, listaTipos, listaIdentificadores);
                                                                                                                                                    listaIdentificadores.clear();
                                                                                                                                                    listaTipos.clear();
                                                                                                                                                    /*TablaSimbolos.cambiarLexema(lexema, lexema + getAmbitoActual());*/
                                                                                                                                                }
break;
case 24:
//#line 88 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 25:
//#line 89 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(10).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 26:
//#line 90 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(10).ival + ": Falta ';' al final de la sentencia"); }
break;
case 27:
//#line 91 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 28:
//#line 92 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta simbolos '<' y '>' en la declaración del STRUCT");}
break;
case 29:
//#line 93 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 30:
//#line 94 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 31:
//#line 98 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema().toUpperCase(); }
break;
case 32:
//#line 99 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema().toUpperCase(); }
break;
case 33:
//#line 100 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema(); }
break;
case 34:
//#line 103 "gramatica.y"
{ listaTipos.add(val_peek(0).sval); }
break;
case 35:
//#line 104 "gramatica.y"
{ listaTipos.add(val_peek(0).sval); }
break;
case 36:
//#line 107 "gramatica.y"
{ listaIdentificadores.add(val_peek(0).sval);}
break;
case 37:
//#line 108 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ',' entre las identificadores"); }
break;
case 38:
//#line 109 "gramatica.y"
{ yyval.ival = val_peek(0).ival; listaIdentificadores.add(val_peek(0).sval);}
break;
case 39:
//#line 112 "gramatica.y"
{
                                                        yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea();
                                                        yyval.sval = ((Token) val_peek(0).obj).getLexema();
                                                        representacionPolaca.add(((Token) val_peek(0).obj).getLexema());
                                                        String idActual = ((Token) val_peek(0).obj).getLexema();
                                                        String idActualConAmbito = idActual + getAmbitoActual();
                                                        if (TablaSimbolos.existeLexema(idActualConAmbito)){
                                                           TablaSimbolos.aumentarUso(idActualConAmbito);
                                                           TablaSimbolos.deleteEntrada(idActual);
                                                        };
                                                     }
break;
case 40:
//#line 123 "gramatica.y"
{ yyval.ival = val_peek(0).ival; yyval.sval = val_peek(0).sval; System.out.println("ID COMPUESTO: " + val_peek(0).sval); representacionPolaca.add(val_peek(0).sval);
                                                         String idActual = val_peek(0).sval;
                                                         ArrayList<String> idSimples = new ArrayList<>(Arrays.asList(idActual.split("\\.")));
                                                         for (String idSimple : idSimples) {
                                                            if (TablaSimbolos.existeLexema(idSimple)){
                                                                TablaSimbolos.deleteEntrada(idSimple);
                                                            };}
                                                         String idActualConAmbito = idActual + getAmbitoActual();
                                                         if (TablaSimbolos.existeLexema(idActualConAmbito)){
                                                             TablaSimbolos.aumentarUso(idActualConAmbito);
                                                         };}
break;
case 41:
//#line 136 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 42:
//#line 137 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 43:
//#line 138 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 44:
//#line 141 "gramatica.y"
{ yyval.ival = ((Token) val_peek(2).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(2).obj).getLexema() + ((Token) val_peek(1).obj).getLexema() + val_peek(0).sval;}
break;
case 45:
//#line 142 "gramatica.y"
{ yyval.ival = ((Token) val_peek(2).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(2).obj).getLexema() + ((Token) val_peek(1).obj).getLexema() +((Token) val_peek(0).obj).getLexema();}
break;
case 46:
//#line 145 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                                ambito.pop();
                                                                                representacionPolaca.add(aux.pop());
                                                                                representacionPolaca.add("_fin");
                                                                                representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()));
                                                                                representacionPolaca.add("_L" + representacionPolaca.size());
                                                                             }
break;
case 47:
//#line 159 "gramatica.y"
{
                                                                    yyval.ival = val_peek(2).ival;
                                                                    String nombreFuncion = ((Token) val_peek(0).obj).getLexema();
                                                                    yyval.sval = nombreFuncion;
                                                                    TablaSimbolos.cambiarTipo(nombreFuncion, TablaSimbolos.FUN);
                                                                    agregarAmbitoAIdentificador(nombreFuncion);
                                                                    agregarUsoAIdentificador(nombreFuncion, "nombre de funcion");
                                                                    TablaSimbolos.setTipoRetorno(nombreFuncion, val_peek(2).sval);
                                                                    String ambitoDeLaFuncion = getAmbitoActual();
                                                                    TablaSimbolos.cambiarLexema(nombreFuncion, nombreFuncion + ambitoDeLaFuncion);
                                                                    ambito.push(":" + nombreFuncion);
                                                                }
break;
case 48:
//#line 173 "gramatica.y"
{
                                                                                        yyval.ival = val_peek(3).ival;
                                                                                        String nombreFuncion = val_peek(3).sval;
                                                                                        String lexemaParametro = representacionPolaca.get(representacionPolaca.size()-1);
                                                                                        eliminarUltimosElementos(representacionPolaca, 1);
                                                                                        representacionPolaca.add("");
                                                                                        bfs.push(representacionPolaca.size()-1);
                                                                                        representacionPolaca.add("BI");
                                                                                        representacionPolaca.add(nombreFuncion);
                                                                                        representacionPolaca.add("_inicio");
                                                                                        aux.push(nombreFuncion);
                                                                                        String ambitoActual = getAmbitoActual();
                                                                                        String nombreFuncionPadre = nombreFuncion + ambitoActual.substring(0, ambitoActual.lastIndexOf(':'));
                                                                                        TablaSimbolos.setNombreParametro(nombreFuncionPadre, val_peek(1).sval);
                                                                                        TablaSimbolos.setTipoParametro(nombreFuncionPadre, TablaSimbolos.getTipo(val_peek(1).sval));
                                                                                        TablaSimbolos.setCantidadDeParametros(nombreFuncionPadre, 1);
                                                                                        agregarAmbitoAIdentificador(lexemaParametro);
                                                                                        TablaSimbolos.cambiarLexema(lexemaParametro, lexemaParametro + getAmbitoActual());
                                                                                    }
break;
case 49:
//#line 192 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                             }
break;
case 50:
//#line 198 "gramatica.y"
{
                                                                String nombreParametro = ((Token) val_peek(0).obj).getLexema();
                                                                yyval.sval = nombreParametro;

                                                                representacionPolaca.add(nombreParametro);

                                                                TablaSimbolos.imprimirTabla();
                                                                TablaSimbolos.cambiarTipo(nombreParametro, val_peek(1).sval);
                                                                agregarUsoAIdentificador(nombreParametro, "nombre de parametro");
                                                            }
break;
case 51:
//#line 208 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 52:
//#line 209 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 53:
//#line 210 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": La funcion no puede tener mas de un parametro");}
break;
case 54:
//#line 211 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ yyval.ival + ": Falta el parametro de la funcion");}
break;
case 59:
//#line 220 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 60:
//#line 221 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 61:
//#line 222 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 64:
//#line 225 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 65:
//#line 226 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 66:
//#line 229 "gramatica.y"
{ yyval.ival = val_peek(1).ival;
                                                                                                  Parser.agregarEstructuraDetectadas(val_peek(1).ival, "FOR");
                                                                                                  representacionPolaca.add(aux.pop());
                                                                                                  representacionPolaca.add(aux.pop());
                                                                                                  representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()+2)); /* Se suma dos debido a los siguientes dos campos que se agregan en la polaca*/
                                                                                                  representacionPolaca.add(String.valueOf(bfs.pop()));
                                                                                                  representacionPolaca.add("BI");
                                                                                                  representacionPolaca.add("_L" + representacionPolaca.size());
                                                                                              }
break;
case 67:
//#line 238 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 68:
//#line 241 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); representacionPolaca.add(((Token) val_peek(4).obj).getLexema()); }
break;
case 69:
//#line 242 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 70:
//#line 243 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 71:
//#line 246 "gramatica.y"
{ yyval.ival = val_peek(3).ival; Parser.agregarEstructuraDetectadas(val_peek(3).ival, "IF"); representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size())); representacionPolaca.add("_L" + representacionPolaca.size()); }
break;
case 72:
//#line 247 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia de selección");}
break;
case 73:
//#line 248 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 74:
//#line 249 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el END_IF en la sentencia de selección"); }
break;
case 75:
//#line 250 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 76:
//#line 251 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 77:
//#line 252 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 78:
//#line 253 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 81:
//#line 258 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 82:
//#line 259 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 83:
//#line 262 "gramatica.y"
{yyval.ival = val_peek(0).ival;
                                                        representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size()+2));
                                                        representacionPolaca.add("");bfs.push(representacionPolaca.size()-1);
                                                        representacionPolaca.add("BI");
                                                        representacionPolaca.add("_L" + representacionPolaca.size());
                                                        }
break;
case 85:
//#line 271 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ELSE en el cuerpo de la selección"); }
break;
case 86:
//#line 272 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 87:
//#line 275 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 88:
//#line 276 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 89:
//#line 277 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 93:
//#line 285 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 94:
//#line 286 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 95:
//#line 287 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 99:
//#line 293 "gramatica.y"
{
                                                                                            yyval.ival = val_peek(2).ival;
                                                                                            System.out.println("POLACA: ");
                                                                                            imprimirPolaca(representacionPolaca);
                                                                                            eliminarUltimosElementos(representacionPolaca, listaIdentificadores.size());
                                                                                            System.out.println("POLACA: ");
                                                                                            imprimirPolaca(representacionPolaca);
                                                                                            List<List<String>> expresiones = formatearLista(listaExpresiones);
                                                                                            System.out.println("listaIdentificadores    " + listaIdentificadores);
                                                                                            if (listaIdentificadores.size() == expresiones.size()) {
                                                                                                for (int i = 0; i < listaIdentificadores.size(); i++){
                                                                                                    String identificador = listaIdentificadores.get(i);
                                                                                                    representacionPolaca.add(identificador);
                                                                                                    expresiones.get(i).forEach((elemento)->representacionPolaca.add(elemento));
                                                                                                    representacionPolaca.add(((Token) val_peek(1).obj).getLexema());

                                                                                                    /* Si la variable empieza con x, y, z, s puede ser que este siendo declarada al usarse, hay que chequear y agregarla a la tabla de simbolos de ser necesario.*/
                                                                                                    char primerCaracter = identificador.charAt(0);
                                                                                                    if (primerCaracter == 'x' || primerCaracter == 'y' || primerCaracter == 'z' || primerCaracter == 's') {
                                                                                                        if (!TablaSimbolos.existeLexema(identificador + getAmbitoActual())) {
                                                                                                            TablaSimbolos.cambiarLexema(identificador, identificador + getAmbitoActual());
                                                                                                            agregarUsoAIdentificador(identificador + getAmbitoActual(), "nombre de variable");
                                                                                                        } else {
                                                                                                            TablaSimbolos.aumentarUso(identificador + getAmbitoActual());
                                                                                                        }

                                                                                                        TablaSimbolos.eliminarLexema(identificador);
                                                                                                    }
                                                                                                }
                                                                                            } else {
                                                                                                agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea() + ": No coincide la cantidad de variables con la cantidad de valores a asignar.");
                                                                                            }

                                                                                            for (int i = 0; i < listaIdentificadores.size(); i++) {
                                                                                                String ambito = estaAlAlcance(listaIdentificadores.get(i));
                                                                                                String identificador = listaIdentificadores.get(i);
                                                                                                if (ambito == null) {
                                                                                                    agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ val_peek(2).ival + ": Variable " + identificador +" no declarada");
                                                                                                } else {
                                                                                                    TablaSimbolos.aumentarUso(identificador + ambito);
                                                                                                    TablaSimbolos.eliminarLexema(identificador);
                                                                                                }
                                                                                            }

                                                                                            listaExpresiones.clear();
                                                                                            listaIdentificadores.clear();
                                                                                            listaTipoExpresiones.clear();
                                                                                         }
break;
case 100:
//#line 343 "gramatica.y"
{ yyval.ival = val_peek(3).ival; Parser.agregarEstructuraDetectadas(val_peek(3).ival, "IF"); representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size())); representacionPolaca.add("_L" + representacionPolaca.size()); }
break;
case 101:
//#line 344 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia de selección");}
break;
case 102:
//#line 345 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 103:
//#line 346 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el END_IF en la sentencia de selección"); }
break;
case 104:
//#line 347 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 105:
//#line 348 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 106:
//#line 349 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 107:
//#line 350 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 108:
//#line 353 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();representacionPolaca.add("");bfs.push(representacionPolaca.size()-1); representacionPolaca.add("BF");}
break;
case 111:
//#line 358 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 112:
//#line 359 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 113:
//#line 362 "gramatica.y"
{yyval.ival = val_peek(0).ival;
                                                            representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size()+2));
                                                            representacionPolaca.add("");bfs.push(representacionPolaca.size()-1);
                                                            representacionPolaca.add("BI");
                                                            representacionPolaca.add("_L" + representacionPolaca.size());}
break;
case 115:
//#line 370 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ELSE en el cuerpo de la selección"); }
break;
case 116:
//#line 371 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 117:
//#line 374 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 118:
//#line 375 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 119:
//#line 378 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 120:
//#line 379 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 121:
//#line 380 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 122:
//#line 381 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 123:
//#line 382 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 124:
//#line 383 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 125:
//#line 386 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 126:
//#line 387 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 127:
//#line 388 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
break;
case 128:
//#line 389 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 130:
//#line 393 "gramatica.y"
{ yyval.ival = ((Token) val_peek(1).obj).getNumeroDeLinea();}
break;
case 132:
//#line 397 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) val_peek(2).obj).getLexema()); representacionPolaca.add(((Token) val_peek(4).obj).getLexema()); agregarUsoAIdentificador(((Token) val_peek(2).obj).getLexema(), "string");}
break;
case 133:
//#line 398 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) val_peek(4).obj).getLexema());}
break;
case 134:
//#line 399 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 135:
//#line 400 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 136:
//#line 401 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 137:
//#line 402 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 138:
//#line 403 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 139:
//#line 404 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 140:
//#line 407 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "FOR");
                                                                            representacionPolaca.add(aux.pop());
                                                                            representacionPolaca.add(aux.pop());
                                                                            representacionPolaca.add(aux.pop());
                                                                            if (dobleCondicion.pop()){
                                                                                representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()+2));
                                                                            }
                                                                            representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()+2)); /* Se suma dos debido a los siguientes dos campos que se agregan en la polaca*/

                                                                            representacionPolaca.add(String.valueOf(bfs.pop()));
                                                                            representacionPolaca.add("BI");
                                                                            representacionPolaca.add("_L" + representacionPolaca.size());
                                                                        }
break;
case 141:
//#line 420 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 142:
//#line 423 "gramatica.y"
{
                                                                                                                            dobleCondicion.push(true);
                                                                                                                            yyval.ival = val_peek(5).ival;
                                                                                                                            representacionPolaca.add("");
                                                                                                                            bfs.push(representacionPolaca.size()-1);
                                                                                                                            representacionPolaca.add("BF");
                                                                                                                        }
break;
case 143:
//#line 430 "gramatica.y"
{
                                                                            dobleCondicion.push(false);
                                                                            yyval.ival = val_peek(1).ival;
                                                                        }
break;
case 144:
//#line 434 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 145:
//#line 435 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 146:
//#line 436 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 147:
//#line 437 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 148:
//#line 438 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 149:
//#line 441 "gramatica.y"
{
                                                                                                                        yyval.ival = val_peek(6).ival;
                                                                                                                        aux.push(val_peek(4).sval);
                                                                                                                        representacionPolaca.remove(representacionPolaca.size()-1); /* Se borra el ultimo elemento ya que se registra en la polaca la constante de la accion aunque se vaya a insertar luego  */
                                                                                                                        representacionPolaca.add("");
                                                                                                                        bfs.push(representacionPolaca.size()-1);
                                                                                                                        representacionPolaca.add("BF");
                                                                                                                        }
break;
case 150:
//#line 449 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 151:
//#line 450 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta la acción en el encabezado del FOR"); }
break;
case 152:
//#line 451 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 153:
//#line 452 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 154:
//#line 455 "gramatica.y"
{yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea();}
break;
case 155:
//#line 458 "gramatica.y"
{
                                                                            representacionPolaca.add(listaExpresiones.get(0));
                                                                            listaExpresiones.clear();
                                                                            representacionPolaca.add(((Token) val_peek(1).obj).getLexema());
                                                                            /* Si la variable empieza con x, y, z, s puede ser que este siendo declarada al usarse, hay que chequear y agregarla a la tabla de simbolos de ser necesario.*/
                                                                            String identificador = val_peek(2).sval;
                                                                            char primerCaracter = identificador.charAt(0);
                                                                            if (primerCaracter == 'x' || primerCaracter == 'y' || primerCaracter == 'z' || primerCaracter == 's') {
                                                                                if (!TablaSimbolos.existeLexema(identificador + getAmbitoActual())) {
                                                                                    TablaSimbolos.cambiarLexema(identificador, identificador + getAmbitoActual());
                                                                                    agregarUsoAIdentificador(identificador + getAmbitoActual(), "nombre de variable");
                                                                                } else {
                                                                                    TablaSimbolos.aumentarUso(identificador + getAmbitoActual());
                                                                                }

                                                                                TablaSimbolos.eliminarLexema(identificador);
                                                                            }
                                                                            representacionPolaca.add("_L" + representacionPolaca.size());
                                                                            bfs.push(representacionPolaca.size()-1);
                                                                            yyval.sval = val_peek(2).sval;
                                                                            }
break;
case 156:
//#line 481 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); aux.push("UP"); aux.push(((Token) val_peek(0).obj).getLexema()); }
break;
case 157:
//#line 482 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); aux.push("DOWN"); aux.push(((Token) val_peek(0).obj).getLexema()); }
break;
case 158:
//#line 483 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 159:
//#line 486 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); listaTipoExpresiones.add(val_peek(0).sval); }
break;
case 160:
//#line 487 "gramatica.y"
{ listaExpresiones.add(","); listaTipoExpresiones.add(val_peek(0).sval); }
break;
case 161:
//#line 490 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;  if (!(val_peek(2).sval).equals(val_peek(0).sval)){agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": incompatibilidad de tipos. " + val_peek(2).sval + ((Token) val_peek(1).obj).getLexema() +val_peek(0).sval);};}
break;
case 162:
//#line 491 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;  if (!(val_peek(2).sval).equals(val_peek(0).sval)){agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": incompatibilidad de tipos. " + val_peek(2).sval + ((Token) val_peek(1).obj).getLexema() +val_peek(0).sval);};}
break;
case 163:
//#line 492 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 164:
//#line 493 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": Falta operador, operandos, o coma entre expresiones"); }
break;
case 165:
//#line 496 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;  if (!(val_peek(2).sval).equals(val_peek(0).sval)){agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": incompatibilidad de tipos. " + val_peek(2).sval + ((Token) val_peek(1).obj).getLexema() +val_peek(0).sval);};}
break;
case 166:
//#line 497 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;  if (!(val_peek(2).sval).equals(val_peek(0).sval)){agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": incompatibilidad de tipos. " + val_peek(2).sval + ((Token) val_peek(1).obj).getLexema() +val_peek(0).sval);};}
break;
case 167:
//#line 498 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 168:
//#line 501 "gramatica.y"
{
                                                representacionPolaca.remove(representacionPolaca.size() - 1);
                                                String ambitoEncontrado = estaAlAlcance(val_peek(0).sval);
                                                if (ambitoEncontrado != null) {
                                                    yyval.sval = TablaSimbolos.getTipo(val_peek(0).sval + ambitoEncontrado);
                                                } else {
                                                    agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea " + val_peek(0).ival + ": Variable " + val_peek(0).sval + " no declarada");
                                                    yyval.sval = null;  /* O cualquier valor predeterminado que necesites*/
                                                };
                                                listaExpresiones.add(val_peek(0).sval);
                                                }
break;
case 169:
//#line 512 "gramatica.y"
{ yyval.sval = TablaSimbolos.getTipo(val_peek(0).sval); agregarUsoAIdentificador(val_peek(0).sval, "constante");}
break;
case 170:
//#line 513 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();  yyval.sval = "SINGLE"; listaExpresiones.add(((Token) val_peek(3).obj).getLexema()); }
break;
case 171:
//#line 514 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 172:
//#line 515 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 173:
//#line 518 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); agregarUsoAIdentificador(((Token) val_peek(0).obj).getLexema(), "constante"); listaExpresiones.add(((Token) val_peek(0).obj).getLexema());}
break;
case 174:
//#line 519 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); agregarUsoAIdentificador(((Token) val_peek(0).obj).getLexema(), "constante"); listaExpresiones.add(((Token) val_peek(0).obj).getLexema());}
break;
case 175:
//#line 522 "gramatica.y"
{ yyval.sval = val_peek(0).sval;}
break;
case 176:
//#line 523 "gramatica.y"
{ yyval.sval = ((Token) val_peek(1).obj).getLexema() + val_peek(0).sval; agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": la constante se va de rango. No se permiten constantes enteras negativas."); }
break;
case 177:
//#line 524 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); listaExpresiones.add(((Token) val_peek(0).obj).getLexema());}
break;
case 178:
//#line 525 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 179:
//#line 526 "gramatica.y"
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
                                                                listaExpresiones.add("-"+ ((Token) val_peek(0).obj).getLexema());
                                                            }
                                                        }
break;
case 180:
//#line 557 "gramatica.y"
{ yyval.obj = ((Token) val_peek(1).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 181:
//#line 560 "gramatica.y"
{
                                                                                                        String lexemaFuncion = ((Token) val_peek(3).obj).getLexema();
                                                                                                        TablaSimbolos.eliminarLexema(lexemaFuncion);
                                                                                                        String ambitoFuncion = estaAlAlcance(lexemaFuncion);
                                                                                                        if (ambitoFuncion != null) {
                                                                                                            String lexemaFuncionConAmbito = lexemaFuncion + ambitoFuncion;
                                                                                                            String tipoParametroFormal = TablaSimbolos.getTipoRetorno(lexemaFuncionConAmbito);
                                                                                                            yyval.sval = tipoParametroFormal;
                                                                                                            String tipoParametroReal = val_peek(1).sval;
                                                                                                            if (!tipoParametroFormal.equals(tipoParametroReal)) {
                                                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea()  + ": Parametro real: " + tipoParametroReal +". Parametro formal: " + tipoParametroFormal + ".");}
                                                                                                            listaExpresiones.add(lexemaFuncion);
                                                                                                            listaExpresiones.add("CALL");
                                                                                                        } else {
                                                                                                            agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Funcion " + lexemaFuncion +" no esta el alcance");
                                                                                                        }
                                                                                                       }
break;
case 182:
//#line 577 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 183:
//#line 578 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 2107 "Parser.java"
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
