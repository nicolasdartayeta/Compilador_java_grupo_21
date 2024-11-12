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
   18,   18,   18,   18,   18,   18,   18,   18,   26,   26,
   27,   27,   23,   23,   23,   23,   28,   28,    5,    5,
    5,    5,    5,    5,   17,   29,   29,   29,   29,   29,
   29,   29,   29,   29,   32,   32,   34,   34,   25,   25,
   35,   35,   35,   35,   35,   35,   33,   33,   33,   33,
   33,   36,   36,   19,   19,   19,   19,   19,   19,   19,
   19,   30,   30,   22,   22,   22,   22,   22,   22,   22,
   37,   37,   37,   37,   37,   38,   39,   39,   39,   31,
   31,   24,   24,   24,   24,   41,   41,   41,   42,   42,
   42,   42,   42,   40,   40,   43,   43,   43,   43,   43,
   43,   44,   44,   44,
};
final static short yylen[] = {                            2,
    4,    5,    5,    3,    3,    3,    2,    5,    4,    4,
    3,    5,    2,    1,    1,    1,    2,    3,    4,    4,
    1,    1,   10,    9,   11,   11,    9,    8,    9,    9,
    1,    1,    1,    3,    1,    3,    2,    1,    1,    1,
    1,    1,    1,    3,    3,    4,    6,    5,    2,    1,
    1,    3,    1,    2,    2,    1,    1,    2,    3,    3,
    1,    1,    1,    1,    2,    2,    5,    6,    6,    8,
    9,    7,    9,    7,    7,    7,    6,    7,    2,    1,
    2,    1,    4,    5,    5,    1,    2,    1,    2,    3,
    3,    1,    1,    1,    3,    8,    9,    7,    9,    7,
    7,    7,    6,    7,    2,    1,    2,    1,    3,    2,
    1,    1,    1,    1,    1,    1,    4,    5,    3,    5,
    1,    2,    1,    5,    5,    6,    6,    6,    6,    4,
    4,    2,    2,    6,    2,    1,    5,    5,    5,    4,
    7,    6,    6,    6,    6,    3,    2,    2,    1,    3,
    1,    3,    3,    1,    1,    3,    3,    1,    1,    1,
    4,    3,    1,    1,    1,    1,    2,    1,    1,    2,
    2,    4,    3,    5,
};
final static short yydefred[] = {                         0,
   41,   42,   43,    0,    0,    0,    0,   33,    0,   32,
   31,    0,    0,    0,    0,    0,   14,   15,   16,    0,
    0,   21,   22,   38,   40,    0,    0,   93,    0,   92,
   94,    0,    0,    0,    0,   17,    0,    0,    0,  155,
    0,  164,  165,  168,    0,    0,  169,    0,  159,    0,
    0,  166,    0,  158,  160,  163,    0,    0,    0,    0,
    4,   13,    0,    0,    0,    0,   37,    0,    0,   89,
  133,    0,  121,  132,    0,  135,    0,    0,    0,    0,
    0,    5,    0,    0,    0,    0,  170,  171,  167,    0,
    0,  110,    0,    0,  113,  114,  111,  112,  115,  116,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   35,
    0,    0,    0,    0,   44,    0,    0,    0,   18,    0,
    0,   36,    0,    0,   57,    0,   56,    0,   61,   62,
   63,   64,    0,   90,   91,    0,  123,    0,    0,    0,
    0,    0,    9,    0,    0,    0,    0,    0,  146,    0,
  173,    0,    0,    0,  162,    0,    0,    0,    0,    0,
    0,    0,  156,  157,  131,  130,    0,    0,    0,    0,
    0,    0,    0,    0,   53,    0,   50,    0,   19,   20,
    0,    0,    0,    0,   46,   55,   54,    0,   58,   66,
    0,   86,   65,  119,    0,  122,    0,    0,    0,    3,
    8,    2,   12,    0,    0,    0,    0,  172,    0,    0,
    0,  161,    0,    0,    0,  105,    0,  124,    0,  125,
    0,   34,    0,    0,    0,    0,    0,    0,   48,    0,
    0,    0,    0,    0,   59,   60,   88,    0,    0,  117,
  138,    0,  139,    0,    0,    0,  145,  149,  144,  142,
  174,    0,    0,    0,    0,    0,  103,  107,  126,  128,
  127,  129,    0,    0,    0,    0,    0,   47,   52,    0,
    0,    0,    0,    0,    0,    0,   87,  118,  120,  134,
  141,  147,  148,  104,  100,    0,   98,  101,  102,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   79,
    0,    0,   67,    0,   83,    0,   96,    0,    0,    0,
    0,   28,    0,    0,    0,    0,    0,   81,   77,   68,
   69,   84,   85,   97,   99,   24,    0,   30,   29,   27,
   78,   74,    0,   72,   75,   76,    0,   23,    0,   70,
   25,   26,   71,   73,
};
final static short yydgoto[] = {                          5,
   15,   16,   17,   18,   19,   20,   21,   22,   23,  111,
   24,   25,   26,  126,  178,  192,   27,  129,   28,  131,
  132,   29,  273,   50,   51,  274,  300,  238,   30,   31,
  121,  161,  162,  216,  101,  138,   32,   39,  247,   52,
   53,   54,   55,   56,
};
final static short yysindex[] = {                      -144,
    0,    0,    0,  693,    0,  610, -267,    0,  306,    0,
    0,   39, -252, -265, -251,  620,    0,    0,    0, -226,
  102,    0,    0,    0,    0, -262, -229,    0,  744,    0,
    0,  298,  -39,  736,  567,    0,  151, -206, -179,    0,
 -171,    0,    0,    0,  262,  136,    0, -166,    0,  834,
 -212,    0,   54,    0,    0,    0,   10, -189, -168,  151,
    0,    0, -233,  801,  136,  151,    0,  500, -207,    0,
    0,  477,    0,    0,  136,    0,   52,  693, -137,  630,
  693,    0,  -22,   -4,  136,   71,    0,    0,    0, -128,
   85,    0,  392,  392,    0,    0,    0,    0,    0,    0,
  136, -126,  746,  392,  392, -154, -121,  -85,  -51,    0,
  -68, -189,  133, -251,    0,  -57,  795,  -82,    0,   95,
  -48,    0,  117,  -25,    0,  487,    0, -162,    0,    0,
    0,    0,  403,    0,    0,  -31,    0,  753,  -10,  136,
    8,  673,    0,   62,   63,  683,  136,   44,    0,   74,
    0,  -96,  -11,  746,    0,   19,   54,   54,   95,  746,
   70,   65,    0,    0,    0,    0, -147, -146,   92, -189,
  346,   94,  151,  795,    0,  151,    0,   97,    0,    0,
  136,  136,   73,  136,    0,    0,    0,   72,    0,    0,
  427,    0,    0,    0, -141,    0,  124,  144,  175,    0,
    0,    0,    0,  169, -164, -164,  179,    0,  130,  119,
  178,    0,  197,  181,  746,    0,  122,    0,  167,    0,
  151,    0,  220,  151,  151,  560,  221,  243,    0,   95,
  187,  213,  548,   25,    0,    0,    0,  555,  209,    0,
    0,  231,    0, -164,   -4,   -4,    0,    0,    0,    0,
    0,  234, -157,  251,  253,  256,    0,    0,    0,    0,
    0,    0,  751,  151,  809,  811,  284,    0,    0,  510,
  548,  548,  249,  254, -119, -114,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -75,    0,    0,    0,  295,
  816,  301,  311,  300, -104,  287,  296,  304,  548,    0,
  307,  216,    0,  226,    0,  252,    0,  312, -244,  313,
  323,    0,  326, -134,  327,  328,  335,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -71,    0,    0,    0,
    0,    0,  -55,    0,    0,    0,  258,    0,  293,    0,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  173,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  425,    0,    0,  619,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  257,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  624,  625,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   41,    0,    0,    0,    0,
    0,    0,    0,  215,    0,    0,    0,    0,    0, -240,
  -53,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  631,  634,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  299,  341,  383,    0,
    0,  -66,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  351,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  435,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -35,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  357,    0, -150,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  445,    0,  128,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   42,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   43,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    5,   12,    1,  -54,  -20,  -15,  -19,    0,    0,  -52,
   -9,  577,    0,    0,  468,  -64,  -62,    0,  -47,    0,
    0,   -7, -122,  -32,  -36,  -73,    0,    0,    0,    0,
    0, -131,  -27,    0,    0,    0,    0,  611, -155,  -26,
  184,  364,    0,    0,
};
final static int YYTABLESIZE=1109;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         38,
   64,   74,   49,  127,    6,  128,  113,   58,   73,   90,
  193,   67,  327,  125,   36,  151,   62,   35,   89,   59,
  130,  210,  211,  116,  109,   57,   69,   38,  213,   60,
    1,   68,  120,    2,    3,   62,   49,  328,  139,  151,
  141,  151,  110,  110,  117,   80,  148,   49,  150,  249,
  250,  137,   70,  152,   67,   49,  122,  149,  156,  171,
  133,  187,   84,  128,  114,   49,  102,   49,  159,    8,
  128,  186,   63,   49,  134,   49,   49,  253,  130,  103,
   62,   49,   73,   49,   49,  130,  183,  135,  281,  142,
    8,   49,  146,  188,   49,   49,  110,   42,   43,   10,
   11,  176,   85,  198,  112,  150,   86,  177,  217,  219,
  204,   91,    1,   49,  239,    2,    3,  196,  133,  189,
   10,   11,  245,  246,  285,  133,  237,  165,  128,  150,
   49,  150,   73,   73,  218,  220,  302,   49,  286,   73,
  240,  304,   62,  130,   36,  231,   62,  332,  230,    4,
  153,  234,    1,  226,  222,    2,    3,  143,  176,  207,
  166,  333,  303,  154,  177,  160,  228,  305,   93,   94,
  128,   49,   49,  277,   49,  128,  318,  248,  248,  248,
  306,    9,  208,  133,  337,  130,  123,  258,   73,  191,
  130,  313,   13,  167,   73,  124,  296,  297,  298,  179,
  339,  263,   95,  169,  265,  266,  307,  128,  128,  128,
  338,  170,  180,   93,   94,  106,   67,  248,  282,  283,
  174,  314,  130,  130,  130,  133,  340,  168,   95,  106,
  133,  181,  128,   40,    1,   41,  128,    2,    3,   42,
   43,   44,   36,   45,  291,    1,  108,  130,    2,    3,
  194,  130,  184,   67,   78,   67,   67,   42,   43,  147,
  108,   47,  133,  133,  133,  106,    1,   41,  197,    2,
    3,   42,   43,   44,    9,   45,  157,  158,   48,   12,
  209,   67,   72,   93,   94,   13,  199,  133,  107,   93,
   94,  133,  108,   47,   40,    1,   41,  212,    2,    3,
   42,   43,   44,  275,   45,  155,  155,   40,    1,   41,
   48,    2,    3,   42,   43,   44,   46,   45,  202,  155,
  104,  105,   47,   80,   82,  205,   40,    1,   41,  140,
    2,    3,   42,   43,   44,   47,   45,   80,   82,   48,
   40,    1,   41,   36,    2,    3,   42,   43,   44,  151,
   45,  232,   48,  235,   47,  206,  201,  215,    1,   93,
   94,    2,    3,  155,  233,  214,  236,  221,   47,  225,
   65,   48,   40,    1,   41,  229,    2,    3,   42,   43,
   44,   66,   45,  143,  143,   48,    1,  143,  143,    2,
    3,   40,    1,   41,  182,    2,    3,   42,   43,   44,
   47,   45,  241,  259,  172,  143,  143,    1,  173,  143,
    2,    3,  170,  143,  254,    9,  260,   48,  143,   47,
   12,  143,  242,   72,  143,  252,   13,  143,   39,   39,
   42,   43,   39,   39,   39,   39,   48,   39,   39,   39,
   39,   39,   39,   39,   39,   39,   39,   39,  261,   39,
  244,   39,   39,  243,   39,  245,  246,  251,   39,   39,
   39,  262,  257,   39,   39,  270,   39,  163,  164,   39,
   45,   45,   39,  255,   45,   45,   45,   45,  271,   45,
   45,   45,   45,   45,   45,   45,   45,   45,   45,   45,
  278,   45,  256,   45,   45,  264,   45,  320,  269,  268,
   45,   45,   45,  279,  272,   45,   45,  322,   45,  280,
  321,   45,  154,  154,   45,  284,  154,  154,  154,  154,
  323,  154,  154,   42,   43,   87,  154,  154,  154,  154,
  154,  154,  287,  324,  288,  154,  154,  289,  154,  341,
  294,  299,  154,  154,  154,   88,  325,  154,  154,  301,
  154,  308,  342,  154,  152,  152,  154,  310,  152,  152,
  152,  152,    1,  152,  152,    2,    3,  311,  152,  152,
  152,  152,  152,  152,  343,   75,   76,  152,  152,   77,
  152,  312,  315,   37,  152,  152,  152,  344,  319,  152,
  152,  316,  152,  326,  329,  152,  153,  153,  152,  317,
  153,  153,  153,  153,  330,  153,  153,  331,  334,  335,
  153,  153,  153,  153,  153,  153,  336,  223,    7,  153,
  153,  224,  153,   11,    6,  170,  153,  153,  153,   51,
   10,  153,  153,    1,  153,   49,  115,  153,  109,  109,
  153,  227,  109,  109,  109,  109,    0,   83,    1,   41,
    0,    2,    3,   42,   43,   44,    0,   45,  190,    1,
    0,  109,    2,    3,  109,    0,    0,    0,  109,  109,
  109,    0,    0,  109,  109,   47,  109,    0,    0,  109,
  136,  136,  109,    1,  136,  136,    2,    3,    9,    0,
  140,  140,   48,  123,  140,  140,  191,    0,    0,   13,
  137,  137,  124,    0,  137,  137,    0,    0,    0,    0,
  136,    0,    9,    0,    0,  136,    0,  123,  136,    0,
  140,  136,    0,   13,  136,  140,  124,    0,  140,    0,
  137,  140,    0,    1,  140,  137,    2,    3,  137,    0,
    0,  137,    0,    1,  137,    8,    2,    3,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    0,    8,    2,
    3,    0,    9,    0,    0,    0,    1,   12,    0,    2,
    3,  136,    9,   13,    0,   10,   11,  123,    0,    0,
    0,  185,    0,   13,   14,    9,  124,    0,   10,   11,
  123,    0,    0,    0,    0,    9,   13,   14,    0,  124,
  123,  295,    0,  191,    1,    0,   13,    2,    3,  124,
    0,    1,    0,    0,    2,    3,    1,    0,    0,    2,
    3,    0,    7,    1,    0,    8,    2,    3,    0,    0,
    0,    0,    0,    9,    0,    0,  267,    0,  123,   66,
    9,  191,    0,    0,   13,  123,    0,  124,    0,  276,
    0,   13,    9,    0,  124,   10,   11,   12,    0,    0,
   81,   82,    0,   13,   14,   33,    1,    0,    8,    2,
    3,    0,    0,    0,    0,    7,    1,    0,    8,    2,
    3,    0,    0,    0,    0,  144,    1,    0,    8,    2,
    3,    0,    0,    0,    0,    9,    0,    0,   10,   11,
   12,    0,    0,   34,    0,    9,   13,   14,   10,   11,
   12,    0,    0,    0,   61,    9,   13,   14,   10,   11,
   12,    0,    0,    0,  145,    0,   13,   14,    7,    1,
    0,    8,    2,    3,    0,    0,    0,    0,    7,    1,
    0,    8,    2,    3,    0,    0,    0,    0,    7,    1,
    0,    8,    2,    3,    0,    0,    0,    0,    9,    0,
    0,   10,   11,   12,    0,    0,    0,  200,    9,   13,
   14,   10,   11,   12,    0,    0,    0,  203,    9,   13,
   14,   10,   11,   12,    0,    0,    0,    0,    0,   13,
   14,   79,    1,    0,    8,    2,    3,    0,    0,   71,
    1,    0,    1,    2,    3,    2,    3,    1,    0,    1,
    2,    3,    2,    3,    0,    0,    0,    0,    0,    0,
    0,    9,    0,    0,   10,   11,   12,  290,    0,    9,
   66,    9,   13,   14,   12,    0,   12,   72,    9,   72,
   13,    0,   13,   12,    0,    0,    0,  195,    0,   13,
  175,    1,    0,    8,    2,    3,  118,    1,    0,    0,
    2,    3,    0,    0,    0,    1,    0,    1,    2,    3,
    2,    3,    1,    0,    0,    2,    3,    0,    0,    0,
   66,    0,  119,   10,   11,  292,    0,  293,   66,   92,
   66,    0,  309,    0,    0,   66,    0,    0,   93,   94,
    0,    0,    0,   95,   96,   97,   98,   99,  100,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   20,   29,   12,   68,    0,   68,   59,  273,   29,   46,
  133,   21,  257,   68,  282,  256,   16,    6,   45,  285,
   68,  153,  154,  257,   57,  278,  256,   37,  160,  281,
  257,  294,   65,  260,  261,   35,   46,  282,   75,  280,
   77,  282,   58,   59,  278,   34,   83,   57,   85,  205,
  206,   72,  282,   86,   64,   65,   66,   84,   91,  112,
   68,  126,  269,  126,   60,   75,  279,   77,  101,  259,
  133,  126,  299,   83,  282,   85,   86,  209,  126,  292,
   80,   91,  103,   93,   94,  133,  123,  295,  244,   78,
  259,  101,   81,  256,  104,  105,  112,  262,  263,  289,
  290,  117,  282,  140,  273,  256,  278,  117,  256,  256,
  147,  278,  257,  123,  256,  260,  261,  138,  126,  282,
  289,  290,  287,  288,  282,  133,  191,  282,  191,  280,
  140,  282,  153,  154,  282,  282,  256,  147,  296,  160,
  282,  256,  142,  191,  282,  182,  146,  282,  181,  294,
  279,  184,  257,  173,  170,  260,  261,  295,  174,  256,
  282,  296,  282,  292,  174,  292,  176,  282,  265,  266,
  233,  181,  182,  238,  184,  238,  299,  204,  205,  206,
  256,  286,  279,  191,  256,  233,  291,  215,  209,  294,
  238,  296,  297,  279,  215,  300,  270,  271,  272,  282,
  256,  221,  256,  272,  224,  225,  282,  270,  271,  272,
  282,  280,  295,  265,  266,  282,  226,  244,  245,  246,
  278,  295,  270,  271,  272,  233,  282,  279,  282,  296,
  238,  280,  295,  256,  257,  258,  299,  260,  261,  262,
  263,  264,  282,  266,  264,  257,  282,  295,  260,  261,
  282,  299,  278,  263,  294,  265,  266,  262,  263,  282,
  296,  284,  270,  271,  272,  256,  257,  258,  279,  260,
  261,  262,  263,  264,  286,  266,   93,   94,  301,  291,
  292,  291,  294,  265,  266,  297,  279,  295,  279,  265,
  266,  299,  283,  284,  256,  257,  258,  279,  260,  261,
  262,  263,  264,  279,  266,  265,  266,  256,  257,  258,
  301,  260,  261,  262,  263,  264,  278,  266,  256,  279,
  267,  268,  284,  282,  282,  282,  256,  257,  258,  278,
  260,  261,  262,  263,  264,  284,  266,  296,  296,  301,
  256,  257,  258,  282,  260,  261,  262,  263,  264,  279,
  266,  279,  301,  282,  284,  282,  295,  293,  257,  265,
  266,  260,  261,  279,  292,  296,  295,  276,  284,  276,
  269,  301,  256,  257,  258,  279,  260,  261,  262,  263,
  264,  280,  266,  256,  257,  301,  257,  260,  261,  260,
  261,  256,  257,  258,  278,  260,  261,  262,  263,  264,
  284,  266,  279,  282,  272,  278,  279,  257,  276,  282,
  260,  261,  280,  286,  296,  286,  295,  301,  291,  284,
  291,  294,  279,  294,  297,  296,  297,  300,  256,  257,
  262,  263,  260,  261,  262,  263,  301,  265,  266,  267,
  268,  269,  270,  271,  272,  273,  274,  275,  282,  277,
  282,  279,  280,  279,  282,  287,  288,  279,  286,  287,
  288,  295,  282,  291,  292,  279,  294,  104,  105,  297,
  256,  257,  300,  296,  260,  261,  262,  263,  292,  265,
  266,  267,  268,  269,  270,  271,  272,  273,  274,  275,
  282,  277,  296,  279,  280,  276,  282,  282,  256,  279,
  286,  287,  288,  295,  292,  291,  292,  282,  294,  279,
  295,  297,  256,  257,  300,  282,  260,  261,  262,  263,
  295,  265,  266,  262,  263,  264,  270,  271,  272,  273,
  274,  275,  282,  282,  282,  279,  280,  282,  282,  282,
  257,  293,  286,  287,  288,  284,  295,  291,  292,  296,
  294,  257,  295,  297,  256,  257,  300,  257,  260,  261,
  262,  263,  257,  265,  266,  260,  261,  257,  270,  271,
  272,  273,  274,  275,  282,  278,  279,  279,  280,  282,
  282,  282,  296,  278,  286,  287,  288,  295,  282,  291,
  292,  296,  294,  282,  282,  297,  256,  257,  300,  296,
  260,  261,  262,  263,  282,  265,  266,  282,  282,  282,
  270,  271,  272,  273,  274,  275,  282,  272,    0,  279,
  280,  276,  282,    0,    0,  280,  286,  287,  288,  279,
    0,  291,  292,    0,  294,  279,   60,  297,  256,  257,
  300,  174,  260,  261,  262,  263,   -1,   37,  257,  258,
   -1,  260,  261,  262,  263,  264,   -1,  266,  256,  257,
   -1,  279,  260,  261,  282,   -1,   -1,   -1,  286,  287,
  288,   -1,   -1,  291,  292,  284,  294,   -1,   -1,  297,
  256,  257,  300,  257,  260,  261,  260,  261,  286,   -1,
  256,  257,  301,  291,  260,  261,  294,   -1,   -1,  297,
  256,  257,  300,   -1,  260,  261,   -1,   -1,   -1,   -1,
  286,   -1,  286,   -1,   -1,  291,   -1,  291,  294,   -1,
  286,  297,   -1,  297,  300,  291,  300,   -1,  294,   -1,
  286,  297,   -1,  257,  300,  291,  260,  261,  294,   -1,
   -1,  297,   -1,  257,  300,  259,  260,  261,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,  259,  260,
  261,   -1,  286,   -1,   -1,   -1,  257,  291,   -1,  260,
  261,  295,  286,  297,   -1,  289,  290,  291,   -1,   -1,
   -1,  295,   -1,  297,  298,  286,  300,   -1,  289,  290,
  291,   -1,   -1,   -1,   -1,  286,  297,  298,   -1,  300,
  291,  292,   -1,  294,  257,   -1,  297,  260,  261,  300,
   -1,  257,   -1,   -1,  260,  261,  257,   -1,   -1,  260,
  261,   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,   -1,   -1,  286,   -1,   -1,  277,   -1,  291,  280,
  286,  294,   -1,   -1,  297,  291,   -1,  300,   -1,  295,
   -1,  297,  286,   -1,  300,  289,  290,  291,   -1,   -1,
  294,  295,   -1,  297,  298,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,  290,
  291,   -1,   -1,  294,   -1,  286,  297,  298,  289,  290,
  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,  290,
  291,   -1,   -1,   -1,  295,   -1,  297,  298,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  286,   -1,
   -1,  289,  290,  291,   -1,   -1,   -1,  295,  286,  297,
  298,  289,  290,  291,   -1,   -1,   -1,  295,  286,  297,
  298,  289,  290,  291,   -1,   -1,   -1,   -1,   -1,  297,
  298,  256,  257,   -1,  259,  260,  261,   -1,   -1,  256,
  257,   -1,  257,  260,  261,  260,  261,  257,   -1,  257,
  260,  261,  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  286,   -1,   -1,  289,  290,  291,  277,   -1,  286,
  280,  286,  297,  298,  291,   -1,  291,  294,  286,  294,
  297,   -1,  297,  291,   -1,   -1,   -1,  295,   -1,  297,
  256,  257,   -1,  259,  260,  261,  256,  257,   -1,   -1,
  260,  261,   -1,   -1,   -1,  257,   -1,  257,  260,  261,
  260,  261,  257,   -1,   -1,  260,  261,   -1,   -1,   -1,
  280,   -1,  282,  289,  290,  277,   -1,  277,  280,  256,
  280,   -1,  277,   -1,   -1,  280,   -1,   -1,  265,  266,
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

//#line 368 "gramatica.y"
private static int cantidadIdEnListaId = 0;
private static Lexer lex;
private static ArrayList<String> representacionPolaca;

// Pila de ambitos
public static final Stack<String> ambito = new Stack<>();

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
//#line 913 "Parser.java"
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
                                                                                                        TablaSimbolos.setAmbito(representacionPolaca.getLast(), this.getAmbitoActual());
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
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 71:
//#line 172 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 72:
//#line 173 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
break;
case 73:
//#line 174 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 74:
//#line 175 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 75:
//#line 176 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 76:
//#line 177 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 77:
//#line 178 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 78:
//#line 179 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 82:
//#line 187 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 84:
//#line 191 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 85:
//#line 192 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 89:
//#line 200 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 90:
//#line 201 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 91:
//#line 202 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 95:
//#line 208 "gramatica.y"
{ yyval.ival = val_peek(2).ival; representacionPolaca.add(((Token) val_peek(1).obj).getLexema());cantidadIdEnListaId = 0; listaIdentificadores.clear();}
break;
case 96:
//#line 211 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 97:
//#line 212 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia de selección");}
break;
case 98:
//#line 213 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
break;
case 99:
//#line 214 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 100:
//#line 215 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 101:
//#line 216 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 102:
//#line 217 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 103:
//#line 218 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 104:
//#line 219 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 108:
//#line 227 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 109:
//#line 230 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 110:
//#line 231 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 111:
//#line 234 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 112:
//#line 235 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 113:
//#line 236 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 114:
//#line 237 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 115:
//#line 238 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 116:
//#line 239 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 118:
//#line 243 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 119:
//#line 244 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
break;
case 120:
//#line 245 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 124:
//#line 253 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) val_peek(2).obj).getLexema()); representacionPolaca.add(((Token) val_peek(4).obj).getLexema());}
break;
case 125:
//#line 254 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) val_peek(4).obj).getLexema());}
break;
case 126:
//#line 255 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 127:
//#line 256 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 128:
//#line 257 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 129:
//#line 258 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 130:
//#line 259 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 131:
//#line 260 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 132:
//#line 263 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "FOR"); }
break;
case 133:
//#line 264 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 134:
//#line 267 "gramatica.y"
{ yyval.ival = val_peek(5).ival; }
break;
case 135:
//#line 268 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 136:
//#line 269 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 137:
//#line 270 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 138:
//#line 271 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 139:
//#line 272 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 140:
//#line 273 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 141:
//#line 276 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 142:
//#line 277 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 143:
//#line 278 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 144:
//#line 279 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 145:
//#line 280 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 146:
//#line 283 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 147:
//#line 286 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 148:
//#line 287 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 149:
//#line 288 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 152:
//#line 295 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 153:
//#line 296 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 154:
//#line 297 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 155:
//#line 298 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": Falta operador, operandos, o coma entre expresiones"); }
break;
case 156:
//#line 301 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 157:
//#line 302 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 158:
//#line 303 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 159:
//#line 306 "gramatica.y"
{ yyval.sval = TablaSimbolos.getTipo(val_peek(0).sval); System.out.println("AAAAAAAAAAAAAAAAA TIPO:" + TablaSimbolos.getTipo(val_peek(0).sval)); }
break;
case 160:
//#line 307 "gramatica.y"
{ yyval.sval = TablaSimbolos.getTipo(val_peek(0).sval); }
break;
case 161:
//#line 308 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();  yyval.sval = "single"; }
break;
case 162:
//#line 309 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 163:
//#line 310 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 164:
//#line 313 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 165:
//#line 314 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 166:
//#line 317 "gramatica.y"
{ yyval.sval = val_peek(0).sval;}
break;
case 167:
//#line 318 "gramatica.y"
{ yyval.sval = ((Token) val_peek(1).obj).getLexema() + val_peek(0).sval; agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": la constante se va de rango. No se permiten constantes enteras negativas."); }
break;
case 168:
//#line 319 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 169:
//#line 320 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 170:
//#line 321 "gramatica.y"
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
case 171:
//#line 352 "gramatica.y"
{ yyval.obj = ((Token) val_peek(1).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 172:
//#line 355 "gramatica.y"
{
                                                                                                    yyval.sval = TablaSimbolos.getTipoRetorno(((Token) val_peek(3).obj).getLexema());
                                                                                                    if (!TablaSimbolos.getTipoRetorno(((Token) val_peek(3).obj).getLexema()).equals(val_peek(1).sval)) {
                                                                                                        agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea()  + ": Parametro real: " + val_peek(1).sval +". Parametro formal: " + TablaSimbolos.getTipoRetorno(((Token) val_peek(3).obj).getLexema()) + ".");
                                                                                                    }
                                                                                                    representacionPolaca.add(((Token) val_peek(3).obj).getLexema()); representacionPolaca.add("BI");

                                                                                                }
break;
case 173:
//#line 363 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 174:
//#line 364 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1719 "Parser.java"
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
