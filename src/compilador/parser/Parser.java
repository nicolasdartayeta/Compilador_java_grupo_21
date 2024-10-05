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
    1,    1,    2,    2,    2,    3,    3,    3,    3,    3,
    8,    8,    8,    8,    8,    8,    8,    5,    5,    5,
    9,    9,    6,    6,    6,   10,   10,   11,   11,   11,
   12,   12,    7,   13,   13,   15,   15,   15,   15,   15,
   14,   14,   14,   14,   16,   16,   16,   16,   16,   16,
   16,   20,   20,   21,   21,   21,   18,   18,   18,   18,
   18,   18,   18,   18,   26,   26,   27,   27,   23,   23,
   23,   23,   28,   28,    4,    4,    4,    4,    4,    4,
   17,   29,   29,   29,   29,   29,   29,   29,   29,   32,
   32,   34,   34,   25,   25,   35,   35,   35,   35,   35,
   35,   33,   33,   33,   33,   36,   36,   19,   19,   19,
   19,   19,   19,   19,   19,   30,   30,   22,   22,   22,
   22,   22,   22,   22,   37,   37,   37,   37,   37,   38,
   38,   39,   39,   39,   39,   31,   31,   24,   24,   24,
   40,   40,   40,   40,   41,   41,   41,   42,   42,   42,
   42,   43,   43,   43,   44,   44,   44,   45,
};
final static short yylen[] = {                            2,
    4,    5,    3,    3,    3,    2,    5,    4,    4,    3,
    2,    1,    1,    1,    2,    3,    4,    4,    1,    1,
   10,    9,   11,   11,    9,    9,    8,    1,    1,    1,
    3,    1,    3,    2,    1,    1,    1,    1,    1,    1,
    3,    3,    4,    6,    5,    2,    1,    1,    3,    1,
    2,    2,    1,    1,    2,    3,    3,    1,    1,    1,
    1,    2,    2,    5,    6,    6,    8,    9,    9,    7,
    7,    7,    6,    7,    2,    1,    2,    1,    4,    5,
    5,    1,    2,    1,    2,    3,    3,    1,    1,    1,
    3,    8,    9,    9,    7,    7,    7,    6,    7,    2,
    1,    2,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    4,    5,    5,    1,    2,    1,    5,    5,    6,
    6,    6,    6,    4,    4,    2,    2,    6,    2,    1,
    5,    5,    5,    4,    7,    6,    6,    6,    6,    3,
    3,    2,    2,    2,    2,    3,    1,    4,    3,    1,
    3,    3,    1,    1,    3,    3,    1,    1,    1,    2,
    1,    1,    1,    1,    4,    3,    5,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   38,   30,   39,   40,    0,   29,
   28,    0,    0,    0,    0,    0,   12,   13,   14,    0,
    0,   19,   20,   35,    0,   37,    0,    0,   89,    0,
   88,   90,    0,    0,   15,    0,    0,    0,    0,    0,
  162,  163,  164,    0,    0,    0,  158,    0,    0,    0,
    0,  157,  159,  161,    0,    0,    0,    0,    0,    4,
   11,    0,    0,    0,    0,   34,    0,    0,    0,   85,
  127,    0,  115,  126,    0,  129,    0,    3,    0,    0,
    0,    0,  160,    0,    0,  108,  109,  106,  107,  110,
  111,    0,    0,    0,    0,    0,    0,    0,    8,    0,
    0,    0,    0,    0,    0,    0,   32,    0,    0,    0,
    0,    0,    0,   16,  154,  147,    0,   33,    0,   41,
    0,    0,   54,    0,   53,    0,   58,   59,   60,   61,
    0,   86,   87,  117,    0,    0,    0,    0,    0,    0,
  140,  141,    0,  166,  168,    0,    0,    0,  149,    0,
  104,    0,    0,    0,    0,    0,  155,  156,    7,    2,
  125,  124,    0,    0,    0,    0,    0,    0,    0,    0,
   50,    0,   47,    0,   17,   18,    0,    0,    0,    0,
   43,   52,   51,    0,   55,   63,    0,   82,   62,    0,
  116,    0,    0,    0,    0,    0,    0,    0,  165,    0,
    0,  148,    0,    0,    0,  100,    0,  118,    0,  119,
    0,    0,   31,    0,    0,    0,    0,   45,  146,    0,
    0,    0,    0,   56,   57,   84,    0,    0,  112,  132,
    0,  133,    0,    0,    0,  139,  138,  136,  167,    0,
    0,    0,    0,   98,  102,  120,  122,  121,  123,    0,
    0,    0,    0,   44,   49,    0,    0,    0,    0,    0,
    0,    0,   83,  113,  114,  128,  135,  142,  143,  145,
  144,   99,   95,    0,   96,   97,    0,    0,    0,    0,
    0,    0,    0,    0,   75,    0,    0,   64,    0,   79,
    0,   92,    0,   27,    0,    0,    0,    0,    0,    0,
    0,   77,   73,   65,   66,   80,   81,   93,   94,   26,
   22,    0,   25,   74,   70,    0,   71,   72,    0,   21,
    0,   67,   23,   24,   68,   69,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   19,   20,   21,   22,   23,  108,   24,
   25,   26,   27,  124,  174,  188,   28,  127,   29,  129,
  130,   30,  259,   48,   49,  260,  285,  227,   31,   32,
  117,  153,  154,  206,   92,  135,   33,   38,  236,   50,
   51,   52,   53,   54,  146,
};
final static short yysindex[] = {                      -248,
  525,  601,    0, -252,    0,    0,    0,    0,  -55,    0,
    0,    1,  644, -250, -120,  538,    0,    0,    0, -240,
  172,    0,    0,    0, -241,    0, -210, -245,    0,  652,
    0,    0,  259,  581,    0,  323, -153, -205,    0, -143,
    0,    0,    0,  237,   54, -119,    0,  351, -254,  -29,
  170,    0,    0,    0, -178,  591,  -11, -190, -140,    0,
    0, -228,   78,   90,  323,    0,  323,  475, -135,    0,
    0,  427,    0,    0,   54,    0,   20,    0,   31,  196,
   54,   43,    0,  -92,  703,    0,    0,    0,    0,    0,
    0,   90, -167,  661,  713,  713,  713,  713,    0,   75,
  -17,  -34,  -28,  -84,  -13, -192,    0,   69, -192,  177,
   -3, -146,   87,    0,    0,    0,    5,    0, -241,    0,
   67,   18,    0,  462,    0, -230,    0,    0,    0,    0,
 -155,    0,    0,    0,  104,   -8,   54,   29,   54,   37,
    0,    0,   77,    0,    0, -133,   92,  661,    0,  -49,
    0,  661,   40,   94,  170,  170,    0,    0,    0,    0,
    0,    0, -217, -164,   -7,  120, -192,  270,  131, -146,
    0,  323,    0,  133,    0,    0,   90,   54,  -89,   90,
    0,    0,    0,  107,    0,    0,  -87,    0,    0, -162,
    0,  147,  164,  175,   55,  240,  240,  189,    0,  654,
  114,    0,  183,  203,  661,    0,  129,    0,  149,    0,
  323,  323,    0,  234,  323,  236,  265,    0,    0,  -36,
  233,  510,  264,    0,    0,    0,  442,  171,    0,    0,
  278,    0,  240,  289,  301,    0,    0,    0,    0,  285,
 -174,  297,  303,    0,    0,    0,    0,    0,    0,  214,
  256,  323,  298,    0,    0,  302,  510,  510,  306,  300,
 -124, -116,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -104,    0,    0, -238,  348,  340,  349,
  483,  313,  315,  510,    0,  330,  185,    0,  191,    0,
  201,    0,  345,    0,  352,  378,  354,  357, -102,  359,
  360,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -100,    0,    0,    0,  -99,    0,    0,  213,    0,
  227,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  643,    0,    0,    0,    0,
    0,    0,    0,    0,  106,    0,    0,    0,    0,    0,
    0,    0,  -72,    0,    0,    0,    0,    0,  316,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  358,
  190,    0,    0,    0,  647,  648,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  651,
  653,  -39,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -81,    0,  148,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -67,  232,  274,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  375,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  407,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -64,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  380,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  420,    0,  400,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -22,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   44,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  207,   19,  -61,  -14,  -16,  -19,    0,    0,  -27,   -9,
  595,  598,    0,    0,  496,  -63,  -60,    0,  -50,    0,
    0,  -46, -129,  -51,  -30, -224,    0,    0,    0,    0,
    0, -138,  -26,    0,    0,    0,    0,  633, -173,  585,
  474,  495,  627,    0,    0,
};
final static int YYTABLESIZE=982;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   63,  189,   47,   74,  125,  105,  123,  126,    1,  201,
   69,   66,  116,  203,   84,   73,    5,  128,  293,    7,
    8,  131,  237,  238,   93,  184,   37,   57,  111,   35,
  145,  110,  282,  283,   61,   47,   70,   94,  207,   67,
  151,  107,  107,  294,  136,    2,  138,   47,  140,  112,
  143,  185,   61,   66,   47,  118,  299,  134,   62,  267,
  183,  241,  182,  126,  208,   47,    6,   47,    6,   47,
  126,   47,   47,  128,   61,   47,   81,  131,  165,   73,
  128,  168,   47,   68,  131,   47,   47,   47,   47,  107,
  179,  209,  107,  228,  106,  172,   10,   11,   10,   11,
  186,    5,  173,   35,    7,    8,  193,  273,  195,  171,
    5,   47,    6,    7,    8,   80,   99,  210,    6,  229,
  191,  274,  198,  226,  152,  219,  126,   47,  223,   47,
    9,  287,  109,   73,   82,  121,  128,   73,  187,  289,
  131,   14,   10,   11,  122,  199,  132,  220,   10,   11,
  213,  291,   58,  172,  302,  319,  321,  288,   85,  133,
  173,  126,  217,  263,   59,  290,  126,   47,   47,    5,
   47,  128,    7,    8,   91,  131,  128,  292,  245,  315,
  131,  320,  322,  130,  130,   73,  147,  130,  130,  221,
   73,  250,  251,  316,  163,  253,  126,  126,    9,  148,
   91,    5,  222,  121,    7,    8,  128,  128,   34,   14,
  131,  131,  122,  130,  101,   95,   96,  103,  130,   56,
  126,  130,   36,  126,  130,  154,  154,  130,  101,  202,
  128,  103,  279,  128,  131,   95,   96,  131,  160,  154,
   66,   66,  256,   66,  102,    5,   40,  161,    7,    8,
   41,   42,   43,  162,   44,  257,   39,    5,   40,   76,
    7,    8,   41,   42,   43,  164,   44,  103,  211,   66,
  192,  104,  167,   76,  170,   39,    5,   40,   45,    7,
    8,   41,   42,   43,  177,   44,   39,    5,   40,   46,
    7,    8,   41,   42,   43,  180,   44,  137,  115,    5,
   40,   46,    7,    8,   41,   42,   43,  194,   44,   39,
    5,   40,  139,    7,    8,   41,   42,   43,  196,   44,
   46,  144,   39,    5,   40,   78,    7,    8,   41,   42,
   43,   46,   44,  113,    5,  204,  233,    7,    8,   78,
  166,  234,  235,   46,  178,  115,    5,   40,  167,    7,
    8,   41,   42,   43,   46,   44,   35,   65,  197,  114,
    5,   36,   36,    7,    8,   36,   36,   46,  175,  159,
   36,   36,   36,   36,   36,   36,   36,   36,   36,   36,
   36,  176,   36,  200,   36,   36,  205,   36,  224,    9,
   46,   36,   36,   36,   12,  212,   36,   36,  190,   36,
   14,  225,   36,   42,   42,   36,  215,   42,   42,  242,
  246,  218,   42,   42,   42,   42,   42,   42,   42,   42,
   42,   42,   42,  247,   42,  230,   42,   42,    5,   42,
  248,    7,    8,   42,   42,   42,   97,   98,   42,   42,
   64,   42,  231,  249,   42,  153,  153,   42,  169,  153,
  153,   65,  264,  232,  153,  153,  167,  141,  142,  153,
  153,  153,  153,  153,  153,  265,  304,  239,  153,  153,
    5,  153,  306,    7,    8,  153,  153,  153,  243,  305,
  153,  153,  308,  153,  244,  307,  153,  151,  151,  153,
  277,  151,  151,   65,  323,  309,  151,  151,   41,   42,
   43,  151,  151,  151,  151,  151,  151,  324,  325,  252,
  151,  151,    5,  151,  254,    7,    8,  151,  151,  151,
  255,  326,  151,  151,  258,  151,  234,  235,  151,  152,
  152,  151,  278,  152,  152,   65,   75,   76,  152,  152,
   77,  214,  261,  152,  152,  152,  152,  152,  152,  167,
  268,  269,  152,  152,    5,  152,  266,    7,    8,  152,
  152,  152,  270,  271,  152,  152,  272,  152,  155,  156,
  152,  105,  105,  152,  280,  105,  105,   65,  275,    5,
  154,  154,    7,    8,  276,  154,  154,  154,  154,  154,
  154,  157,  158,  281,  105,  286,    5,  105,  284,    7,
    8,  105,  105,  105,  295,  297,  105,  105,  300,  105,
  301,  303,  105,  150,  150,  105,  296,  150,  150,   65,
   86,   87,   88,   89,   90,   91,  310,  150,  150,  150,
  150,  150,  150,  311,  312,  313,  150,  150,  314,  150,
  317,  318,    6,  150,  150,  150,   10,    5,  150,  150,
    9,  150,    1,   48,  150,  137,  137,  150,   46,  137,
  137,  119,  134,  134,  120,  216,  134,  134,   79,  150,
   83,    0,    0,    0,    0,  131,  131,  137,  137,  131,
  131,  137,    0,    5,    0,  137,    7,    8,    0,    0,
  137,    0,  134,  137,    0,    0,  137,  134,    5,  137,
  134,    7,    8,  134,    0,  131,  134,    0,    0,    0,
  131,    0,    9,  131,    0,    0,  131,   12,    5,  131,
    6,    7,    8,   14,    0,    0,    0,    9,    0,    0,
    0,    5,  121,    6,    7,    8,  262,    0,   14,    5,
    0,  122,    7,    8,    0,    0,    0,    9,    0,    0,
   10,   11,  121,    0,    0,    0,  181,    0,   14,   15,
    9,  122,    0,   10,   11,  121,    5,    0,    9,    7,
    8,   14,   15,  121,  122,    0,  187,    0,  298,   14,
    4,    5,  122,    6,    7,    8,    0,    0,    0,    0,
    0,    0,    0,    4,    5,    9,    6,    7,    8,    0,
  121,    0,    0,  187,    0,    0,   14,    0,    0,  122,
    9,    0,    0,   10,   11,   12,    0,    0,   13,    0,
    0,   14,   15,    9,    0,    0,   10,   11,   12,    0,
    0,    0,   60,    0,   14,   15,    4,    5,    0,    6,
    7,    8,    0,    0,    0,    0,  100,    5,    0,    6,
    7,    8,    0,    0,    0,    0,    4,    5,    0,    6,
    7,    8,    0,    0,    0,    0,    9,    0,    0,   10,
   11,   12,    0,    0,    0,   78,    9,   14,   15,   10,
   11,   12,    0,    0,    0,  101,    9,   14,   15,   10,
   11,   12,    0,    0,    0,    0,    0,   14,   15,   55,
    5,    0,    6,    7,    8,    0,    0,   71,    5,    0,
    5,    7,    8,    7,    8,    0,    0,    5,    0,    0,
    7,    8,    0,    0,    0,    0,    0,    0,    0,    9,
    0,    0,   10,   11,   12,    0,    0,    9,    0,    9,
   14,   15,   12,    0,   12,   72,    9,   72,   14,  240,
   14,   12,    0,    0,   72,    0,    0,   14,  115,    5,
   40,    0,    7,    8,   41,   42,   43,    0,   44,    5,
   40,    0,    7,    8,   41,   42,   43,    0,   44,    0,
    0,  149,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   20,  131,   12,   30,   68,   57,   68,   68,  257,  148,
  256,   21,   64,  152,   45,   30,  257,   68,  257,  260,
  261,   68,  196,  197,  279,  256,   36,  278,  257,  282,
   82,   59,  257,  258,   16,   45,  282,  292,  256,  281,
   92,   58,   59,  282,   75,  294,   77,   57,   79,  278,
   81,  282,   34,   63,   64,   65,  281,   72,  299,  233,
  124,  200,  124,  124,  282,   75,  259,   77,  259,   79,
  131,   81,   82,  124,   56,   85,  282,  124,  106,   94,
  131,  109,   92,  294,  131,   95,   96,   97,   98,  106,
  121,  256,  109,  256,  285,  112,  289,  290,  289,  290,
  256,  257,  112,  282,  260,  261,  137,  282,  139,  256,
  257,  121,  259,  260,  261,  269,  295,  282,  259,  282,
  135,  296,  256,  187,  292,  177,  187,  137,  180,  139,
  286,  256,  273,  148,  278,  291,  187,  152,  294,  256,
  187,  297,  289,  290,  300,  279,  282,  178,  289,  290,
  167,  256,  273,  170,  284,  256,  256,  282,  278,  295,
  170,  222,  172,  227,  285,  282,  227,  177,  178,  257,
  180,  222,  260,  261,  256,  222,  227,  282,  205,  282,
  227,  282,  282,  256,  257,  200,  279,  260,  261,  279,
  205,  211,  212,  296,  279,  215,  257,  258,  286,  292,
  282,  257,  292,  291,  260,  261,  257,  258,    2,  297,
  257,  258,  300,  286,  282,  265,  266,  282,  291,   13,
  281,  294,  278,  284,  297,  265,  266,  300,  296,  279,
  281,  296,  252,  284,  281,  265,  266,  284,  256,  279,
  250,  251,  279,  253,  256,  257,  258,  282,  260,  261,
  262,  263,  264,  282,  266,  292,  256,  257,  258,  282,
  260,  261,  262,  263,  264,  279,  266,  279,  276,  279,
  279,  283,  280,  296,  278,  256,  257,  258,  278,  260,
  261,  262,  263,  264,  280,  266,  256,  257,  258,  301,
  260,  261,  262,  263,  264,  278,  266,  278,  256,  257,
  258,  301,  260,  261,  262,  263,  264,  279,  266,  256,
  257,  258,  282,  260,  261,  262,  263,  264,  282,  266,
  301,  279,  256,  257,  258,  282,  260,  261,  262,  263,
  264,  301,  266,  256,  257,  296,  282,  260,  261,  296,
  272,  287,  288,  301,  278,  256,  257,  258,  280,  260,
  261,  262,  263,  264,  301,  266,  282,  280,  282,  282,
  257,  256,  257,  260,  261,  260,  261,  301,  282,  295,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  295,  277,  292,  279,  280,  293,  282,  282,  286,
  301,  286,  287,  288,  291,  276,  291,  292,  295,  294,
  297,  295,  297,  256,  257,  300,  276,  260,  261,  296,
  282,  279,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,  295,  277,  279,  279,  280,  257,  282,
  282,  260,  261,  286,  287,  288,  267,  268,  291,  292,
  269,  294,  279,  295,  297,  256,  257,  300,  272,  260,
  261,  280,  282,  279,  265,  266,  280,  262,  263,  270,
  271,  272,  273,  274,  275,  295,  282,  279,  279,  280,
  257,  282,  282,  260,  261,  286,  287,  288,  296,  295,
  291,  292,  282,  294,  282,  295,  297,  256,  257,  300,
  277,  260,  261,  280,  282,  295,  265,  266,  262,  263,
  264,  270,  271,  272,  273,  274,  275,  295,  282,  276,
  279,  280,  257,  282,  279,  260,  261,  286,  287,  288,
  256,  295,  291,  292,  292,  294,  287,  288,  297,  256,
  257,  300,  277,  260,  261,  280,  278,  279,  265,  266,
  282,  272,  279,  270,  271,  272,  273,  274,  275,  280,
  262,  263,  279,  280,  257,  282,  279,  260,  261,  286,
  287,  288,  262,  263,  291,  292,  282,  294,   95,   96,
  297,  256,  257,  300,  277,  260,  261,  280,  282,  257,
  265,  266,  260,  261,  282,  270,  271,  272,  273,  274,
  275,   97,   98,  292,  279,  296,  257,  282,  293,  260,
  261,  286,  287,  288,  257,  257,  291,  292,  296,  294,
  296,  282,  297,  256,  257,  300,  277,  260,  261,  280,
  270,  271,  272,  273,  274,  275,  282,  270,  271,  272,
  273,  274,  275,  282,  257,  282,  279,  280,  282,  282,
  282,  282,    0,  286,  287,  288,    0,    0,  291,  292,
    0,  294,    0,  279,  297,  256,  257,  300,  279,  260,
  261,   67,  256,  257,   67,  170,  260,  261,   36,   85,
   44,   -1,   -1,   -1,   -1,  256,  257,  278,  279,  260,
  261,  282,   -1,  257,   -1,  286,  260,  261,   -1,   -1,
  291,   -1,  286,  294,   -1,   -1,  297,  291,  257,  300,
  294,  260,  261,  297,   -1,  286,  300,   -1,   -1,   -1,
  291,   -1,  286,  294,   -1,   -1,  297,  291,  257,  300,
  259,  260,  261,  297,   -1,   -1,   -1,  286,   -1,   -1,
   -1,  257,  291,  259,  260,  261,  295,   -1,  297,  257,
   -1,  300,  260,  261,   -1,   -1,   -1,  286,   -1,   -1,
  289,  290,  291,   -1,   -1,   -1,  295,   -1,  297,  298,
  286,  300,   -1,  289,  290,  291,  257,   -1,  286,  260,
  261,  297,  298,  291,  300,   -1,  294,   -1,  296,  297,
  256,  257,  300,  259,  260,  261,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  256,  257,  286,  259,  260,  261,   -1,
  291,   -1,   -1,  294,   -1,   -1,  297,   -1,   -1,  300,
  286,   -1,   -1,  289,  290,  291,   -1,   -1,  294,   -1,
   -1,  297,  298,  286,   -1,   -1,  289,  290,  291,   -1,
   -1,   -1,  295,   -1,  297,  298,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,   -1,   -1,   -1,  297,  298,  256,
  257,   -1,  259,  260,  261,   -1,   -1,  256,  257,   -1,
  257,  260,  261,  260,  261,   -1,   -1,  257,   -1,   -1,
  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  286,
   -1,   -1,  289,  290,  291,   -1,   -1,  286,   -1,  286,
  297,  298,  291,   -1,  291,  294,  286,  294,  297,  296,
  297,  291,   -1,   -1,  294,   -1,   -1,  297,  256,  257,
  258,   -1,  260,  261,  262,  263,  264,   -1,  266,  257,
  258,   -1,  260,  261,  262,  263,  264,   -1,  266,   -1,
   -1,  279,
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
"programa : IDENTIFICADOR_GENERICO BEGIN sentencias END error",
"programa : BEGIN sentencias END",
"programa : IDENTIFICADOR_GENERICO sentencias END",
"programa : IDENTIFICADOR_GENERICO BEGIN sentencias",
"programa : IDENTIFICADOR_GENERICO sentencias",
"programa : IDENTIFICADOR_GENERICO BEGIN sentencias error END",
"programa : IDENTIFICADOR_GENERICO BEGIN error END",
"programa : IDENTIFICADOR_GENERICO BEGIN sentencias error",
"programa : IDENTIFICADOR_GENERICO BEGIN error",
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
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L parametro_real PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L parametro_real error PARENTESIS_R",
"parametro_real : expresion",
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
  System.out.println("Error: " + string );
}
//#line 794 "Parser.java"
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
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) val_peek(1).obj).getNumeroDeLinea() + ": Todo lo que esta despues del END no forma parte del programa."); }
break;
case 3:
//#line 21 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea 1: Falta identificador de programa"); }
break;
case 4:
//#line 22 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + (((Token) val_peek(2).obj).getNumeroDeLinea()+1) + ": Falta un BEGIN despues del identificador del programa"); }
break;
case 5:
//#line 23 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Ultima linea: Falta un END al final del programa"); }
break;
case 6:
//#line 24 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta un BEGIN despues del identificador del programa y falta un END al final del programa"); }
break;
case 7:
//#line 25 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. <3"); }
break;
case 8:
//#line 26 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. <3"); }
break;
case 9:
//#line 27 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. FALTA EL END AL FINAL DEL PROGRAMA <3"); }
break;
case 10:
//#line 28 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. FALTA EL END AL FINAL DEL PROGRAMA <3"); }
break;
case 15:
//#line 38 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)val_peek(0).obj).getNumeroDeLinea() + ": Syntax Error"); }
break;
case 16:
//#line 41 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S"); }
break;
case 17:
//#line 42 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 18:
//#line 43 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 19:
//#line 44 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FUNCION"); }
break;
case 21:
//#line 48 "gramatica.y"
{
                                                                                                                                                    yyval.ival = ((Token) val_peek(9).obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) val_peek(9).obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) val_peek(1).obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                }
break;
case 22:
//#line 54 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 23:
//#line 55 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(10).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 24:
//#line 56 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(10).ival + ": Falta ';' al final de la sentencia"); }
break;
case 25:
//#line 57 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 26:
//#line 58 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 27:
//#line 59 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 28:
//#line 63 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 29:
//#line 64 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 30:
//#line 65 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 34:
//#line 73 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ',' entre las variables"); }
break;
case 35:
//#line 74 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 36:
//#line 77 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 37:
//#line 78 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 38:
//#line 81 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
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
//#line 86 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 42:
//#line 87 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 43:
//#line 90 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 44:
//#line 99 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 45:
//#line 104 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                             }
break;
case 47:
//#line 111 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 48:
//#line 112 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 49:
//#line 113 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": La funcion no puede tener mas de un parametro");}
break;
case 50:
//#line 114 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ yyval.ival + ": Falta el parametro de la funcion");}
break;
case 55:
//#line 123 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 56:
//#line 124 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 57:
//#line 125 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 58:
//#line 126 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "IF"); }
break;
case 60:
//#line 128 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 61:
//#line 129 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 62:
//#line 132 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 63:
//#line 133 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 64:
//#line 136 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 65:
//#line 137 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 66:
//#line 138 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta ';' al final de la sentencia"); }
break;
case 67:
//#line 141 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 68:
//#line 142 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 69:
//#line 143 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 70:
//#line 144 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 71:
//#line 145 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 72:
//#line 146 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 73:
//#line 147 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 74:
//#line 148 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 78:
//#line 156 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 80:
//#line 160 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 81:
//#line 161 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 85:
//#line 169 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 86:
//#line 170 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 87:
//#line 171 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 90:
//#line 174 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 91:
//#line 177 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 92:
//#line 180 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 93:
//#line 181 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 94:
//#line 182 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 95:
//#line 183 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 96:
//#line 184 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 97:
//#line 185 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 98:
//#line 186 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 99:
//#line 187 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 103:
//#line 195 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 105:
//#line 199 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 106:
//#line 202 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
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
case 113:
//#line 211 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 114:
//#line 212 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 118:
//#line 220 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 119:
//#line 221 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 120:
//#line 222 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 121:
//#line 223 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
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
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 125:
//#line 227 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 126:
//#line 230 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 127:
//#line 231 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 128:
//#line 234 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 129:
//#line 235 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 130:
//#line 236 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 131:
//#line 237 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 132:
//#line 238 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 133:
//#line 239 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 134:
//#line 240 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 135:
//#line 243 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 136:
//#line 244 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 137:
//#line 245 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 138:
//#line 246 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 139:
//#line 247 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 148:
//#line 264 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 149:
//#line 265 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 154:
//#line 272 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta operador u operandos"); }
break;
case 160:
//#line 282 "gramatica.y"
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
case 162:
//#line 323 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 163:
//#line 324 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 164:
//#line 325 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 166:
//#line 329 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 167:
//#line 330 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1466 "Parser.java"
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
