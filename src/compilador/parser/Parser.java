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
   22,   37,   37,   38,   38,   38,   38,   31,   31,   24,
   24,   39,   39,   39,   40,   40,   40,   41,   41,   41,
   41,   42,   42,   42,   43,   44,
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
    4,    3,    2,    2,    8,    7,    7,    6,    7,    7,
    7,    3,    3,    2,    2,    2,    2,    3,    1,    4,
    1,    3,    3,    1,    3,    3,    1,    1,    1,    2,
    1,    1,    1,    1,    4,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   26,   19,   27,   28,    0,   18,
   17,    0,    0,    0,    0,    0,    7,    8,    9,    0,
    0,   13,    0,   23,    0,   25,    0,    0,    0,    0,
    0,   67,    0,   10,    0,    0,    0,    0,  122,  123,
  124,    0,    0,    0,  118,    0,    0,  111,    0,  117,
  119,  121,    0,    0,    0,    3,    6,    0,    0,    0,
    0,   14,    0,    0,   63,   66,   94,    0,   87,   93,
   65,    2,    0,    0,    0,    0,  120,    0,    0,    0,
    0,   82,   83,   80,   81,   84,   85,    0,    0,    0,
    0,    0,    1,   92,    0,    0,    0,    0,    0,   11,
    0,    0,   22,    0,   29,    0,    0,   40,    0,   39,
    0,    0,    0,   44,   45,    0,   89,    0,    0,    0,
  102,  103,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  115,  116,   90,   91,   21,
    0,    0,    0,   35,    0,    0,    0,    0,    0,   31,
   38,   37,   41,   42,   43,   47,    0,   60,   46,    0,
   88,    0,    0,    0,  125,    0,    0,  110,    0,   73,
    0,   75,    0,    0,    0,   34,   33,    0,    0,    0,
    0,    0,   62,    0,   86,    0,    0,    0,    0,    0,
    0,   74,    0,   71,   72,   77,    0,   20,   32,    0,
    0,    0,    0,    0,    0,    0,   61,   99,    0,  104,
  105,  107,  106,  101,  100,   96,   70,   69,    0,    0,
    0,    0,    0,   55,   53,   48,   59,   95,    0,   54,
    0,   51,   52,   57,   16,   50,   49,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   19,   20,   21,   22,   23,  141,   24,
   25,   26,   27,  109,  145,  158,   28,  112,   29,  114,
  115,   30,  203,   46,   47,  204,  224,  184,   31,   32,
  102,  134,  135,  172,   88,  118,   37,  189,   48,   49,
   50,   51,   52,  125,
};
final static short yysindex[] = {                      -239,
  541,  617,    0, -269,    0,    0,    0,    0,  -40,    0,
    0, -184,  617, -245, -247,  554,    0,    0,    0, -230,
 -240,    0, -220,    0, -229,    0, -251, -211, -196,  625,
 -186,    0,  564,    0,  -29, -212, -169, -151,    0,    0,
    0,   22,  -57, -131,    0,  101, -176,    0,  -72,    0,
    0,    0,  607, -238, -121,    0,    0, -164, -271,  -57,
  -29,    0,  -29,  406,    0,    0,    0,  -89,    0,    0,
    0,    0, -128,  -10,  -57,  -57,    0, -104,  -57,  427,
  427,    0,    0,    0,    0,    0,    0,  -57, -137,  634,
  427,  427,    0,    0, -120, -109, -225, -114,  259,    0,
   43, -106,    0, -229,    0,  -79, -102,    0,  364,    0,
  -96,  -84,  -70,    0,    0,  -67,    0,  669,  -57,  -64,
    0,    0,  -59,   43,  -88,  -53,  634,   43,  -54,  -72,
  -72,   43,  634,  -33,  -47,    0,    0,    0,    0,    0,
 -135,  259,  -29,    0,  -43,  -57,  -57,  -66,  -57,    0,
    0,    0,    0,    0,    0,    0,  508,    0,    0,   -5,
    0,  -23,  -32,  -32,    0,  627,  -14,    0,   -1,    0,
  634,    0,   24, -225,   25,    0,    0,   43,  -63,   18,
  466,  -99,    0,  493,    0,  -38,   67,   92,   45,   48,
   52,    0, -235,    0,    0,    0,  -29,    0,    0,   53,
  466,  466,   56,   57,   80,   88,    0,    0,   99,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -193,  451,
   83,   89,  466,    0,    0,    0,    0,    0,  127,    0,
 -208,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  387,    0,    0,    0,    0,
    0,    0,   91,    0,    1,    0,    0,  383,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  149,    0,
    0,    0,  392,    0,    0,    0,    0,    0,  104,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  279,  340,    0,   46,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  117,    0,    0,    0,    0,   93,  192,
  235,  -45,    0,    0, -190,    0,    0,    0,    0,    0,
    0,    0,  119,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -189,    0,    0,    0,    0,    0,    0,  322,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -46,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -188,    0,    0,    0,    0,    0,  294,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -171,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  118,   65,  -50,  -18,  -82,  -20,    0,    0,    0,  640,
  334,  337,    0,    0,  261,  -56,  -62,    0,  -60,    0,
    0,  -58, -113,  -44,  -36,  -51,    0,    0,    0,    0,
    0,  -91,  -25,    0,    0,    0,  372,  -74,  332,  276,
  277,  370,    0,    0,
};
final static int YYTABLESIZE=966;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         59,
   24,  111,  159,  113,   70,  116,   78,  110,   61,   96,
  100,   69,   34,  108,  140,  101,  143,    1,    5,   38,
  217,    7,    8,   39,   40,   41,    5,   42,   60,    7,
    8,  124,   54,    6,  128,  167,  120,   55,  123,   61,
   94,  169,   64,  132,   95,   30,  111,  236,  113,  117,
  116,   63,  152,  111,    2,  113,   74,  116,  151,  143,
  218,   62,   44,   10,   11,   76,   78,   56,   58,  148,
   65,   69,    5,   38,  193,    7,    8,   39,   40,   41,
   57,   42,  162,  229,   58,   66,   61,  237,  190,  191,
   15,  198,   98,   43,  111,   71,  113,   57,  116,  161,
  183,  178,   89,   12,  182,   76,   78,   56,   69,  234,
  179,  209,   75,   99,   69,   90,   44,   57,  111,   33,
  113,  111,  116,  113,   58,  116,   76,  207,    5,   38,
   53,    7,    8,   39,   40,   41,  173,   42,  111,  111,
  113,  113,  116,  116,  174,  196,   79,   69,  114,  221,
  222,   97,   69,  119,  133,   80,   81,  111,  138,  113,
  111,  116,  113,  142,  116,   80,   81,    5,  231,  139,
    7,    8,   44,  146,  126,  149,  219,    5,   38,  205,
    7,    8,   39,   40,   41,  153,   42,  127,  156,    5,
  165,  112,    7,    8,   91,   92,    9,  154,  147,    5,
   38,   12,    7,    8,   39,   40,   41,   14,   42,   98,
   98,  155,  180,   98,   98,  200,    5,  163,    9,    7,
    8,   44,  164,  106,  168,  181,  157,    5,  201,   14,
    7,    8,  107,   79,  113,  177,   79,   35,  166,   98,
  208,   79,   79,   44,   98,  171,   79,   98,  187,  188,
   98,  121,  122,   98,  187,  188,   24,   24,  186,   24,
   24,   24,  170,  187,  188,   24,   24,   24,   24,   24,
   24,   24,   24,   24,   24,   24,  185,   24,  109,   24,
   24,  194,   24,   39,   40,   41,   24,   24,   24,   24,
   24,   24,   24,   24,  195,   24,   24,   24,   24,  197,
   24,   30,   30,  199,   30,   30,   30,   80,   81,  202,
   30,   30,   30,   30,   30,   30,   30,   30,   30,   30,
   30,  108,   30,  214,   30,   30,  215,   30,  210,  211,
  216,   30,   30,   30,   30,   30,   30,   30,   30,   68,
   30,   30,   30,   30,  220,   30,   15,   15,  223,   15,
   15,   15,  225,  212,  213,  130,  131,  111,  111,   12,
   12,  226,   12,   12,   12,   80,   81,  136,  137,  227,
   82,   83,   84,   85,   86,   87,   15,  228,  232,   15,
   15,   15,   64,  235,  233,   15,    5,   15,   15,   12,
   15,    4,   12,   12,   12,  126,  104,   36,   12,  105,
   12,   12,  175,   12,  114,  114,   73,  114,  114,  114,
  129,   77,    0,  114,  114,    0,    0,    0,  114,  114,
  114,  114,  114,  114,    0,    0,    0,  114,  114,    0,
  114,    0,    0,    0,  114,  114,  114,  114,  114,  114,
  114,  114,    0,  114,  114,  114,  114,  112,  112,    0,
  112,  112,  112,    0,    0,    0,  112,  112,    0,    0,
    0,  112,  112,  112,  112,  112,  112,    0,    0,    0,
  112,  112,    0,  112,    0,    0,    0,  112,  112,  112,
  112,  112,  112,  112,  112,    0,  112,  112,  112,  112,
  113,  113,    0,  113,  113,  113,    0,    0,    0,  113,
  113,    0,    0,    0,  113,  113,  113,  113,  113,  113,
    0,    0,    0,  113,  113,    5,  113,    6,    7,    8,
  113,  113,  113,  113,  113,  113,  113,  113,    0,  113,
  113,  113,  113,    0,  109,  109,    0,  109,  109,  109,
    0,    0,    0,    0,    0,    0,    0,   10,   11,   97,
   97,    0,    0,   97,   97,    0,    0,    0,  109,    0,
  109,    0,    0,    0,  109,    0,    0,  109,  109,  109,
    0,  109,    0,  109,  109,  109,  109,  108,  108,   97,
  108,  108,  108,    0,   97,    0,    0,   97,    0,    0,
   97,    0,    0,   97,    0,   68,   68,    0,   68,   68,
   68,  108,    0,  108,    0,    0,    0,  108,    0,    0,
  108,  108,  108,    0,  108,    0,  108,  108,  108,  108,
    5,   68,    6,    7,    8,   68,    0,    0,   68,   68,
   68,    0,   68,    0,   68,   68,   68,   68,   64,   64,
    0,   64,   64,   64,    0,    0,    0,    0,   36,    9,
    0,   45,   10,   11,  106,    0,    0,    0,  150,    0,
   14,   15,    5,  107,    6,    7,    8,    0,   64,    0,
    0,   64,   64,   64,   36,   64,    0,   64,   64,   64,
   64,    0,   45,    5,   38,    0,    7,    8,   39,   40,
   41,    9,   42,   45,   10,   11,  106,    0,    0,   45,
  103,    0,   14,   15,    0,  107,    0,    5,    0,    0,
    7,    8,   45,    0,   45,   45,    0,    0,   45,   45,
   45,    0,    5,    0,    0,    7,    8,   45,    0,    0,
   45,   45,    0,    0,    0,    0,    9,    0,  144,    0,
    0,  106,    0,    0,  157,   45,  230,   14,    0,    5,
  107,    9,    7,    8,    0,    0,  106,    0,   45,  157,
    0,    0,   14,    0,    5,  107,    0,    7,    8,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    9,    0,
    0,  144,  176,  106,    0,   45,   45,  206,   45,   14,
    0,    0,  107,    9,    0,    0,    4,    5,  106,    6,
    7,    8,    0,    0,   14,    0,    0,  107,    0,    4,
    5,    0,    6,    7,    8,    0,    0,    0,    0,    4,
    5,    0,    6,    7,    8,    0,    9,    0,    0,   10,
   11,   12,    0,    0,   13,    0,    0,   14,   15,    9,
    0,    0,   10,   11,   12,    0,    0,    0,   56,    9,
   14,   15,   10,   11,   12,    0,    0,    0,   72,    0,
   14,   15,    4,    5,    0,    6,    7,    8,    0,    0,
    0,    0,    4,    5,    0,    6,    7,    8,    0,    0,
   67,    5,    0,    5,    7,    8,    7,    8,    0,    0,
    5,    0,    9,    7,    8,   10,   11,   12,    0,    0,
    0,   93,    9,   14,   15,   10,   11,   12,    0,    0,
    9,    0,    9,   14,   15,   12,    0,   12,   68,    9,
   68,   14,  192,   14,   12,    5,    0,   68,    7,    8,
   14,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    9,    0,    0,    0,    0,   12,
    0,    0,    0,  160,    0,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   64,  116,   64,   30,   64,   43,   64,  280,   54,
  282,   30,  282,   64,   97,   60,   99,  257,  257,  258,
  256,  260,  261,  262,  263,  264,  257,  266,  269,  260,
  261,   76,  278,  259,   79,  127,   73,  285,   75,  280,
  279,  133,  294,   88,  283,    0,  109,  256,  109,   68,
  109,  281,  109,  116,  294,  116,  269,  116,  109,  142,
  296,  282,  301,  289,  290,  256,  256,  256,  299,  106,
  282,   90,  257,  258,  166,  260,  261,  262,  263,  264,
   16,  266,  119,  277,  256,  282,  280,  296,  163,  164,
    0,  174,  257,  278,  157,  282,  157,   33,  157,  118,
  157,  146,  279,    0,  149,  296,  296,  296,  127,  223,
  147,  186,  282,  278,  133,  292,  301,   53,  181,    2,
  181,  184,  181,  184,  296,  184,  278,  184,  257,  258,
   13,  260,  261,  262,  263,  264,  272,  266,  201,  202,
  201,  202,  201,  202,  280,  171,  278,  166,    0,  201,
  202,  273,  171,  282,  292,  265,  266,  220,  279,  220,
  223,  220,  223,  278,  223,  265,  266,  257,  220,  279,
  260,  261,  301,  280,  279,  278,  197,  257,  258,  279,
  260,  261,  262,  263,  264,  282,  266,  292,  256,  257,
  279,    0,  260,  261,  267,  268,  286,  282,  278,  257,
  258,  291,  260,  261,  262,  263,  264,  297,  266,  256,
  257,  282,  279,  260,  261,  279,  257,  282,  286,  260,
  261,  301,  282,  291,  279,  292,  294,  257,  292,  297,
  260,  261,  300,  279,    0,  279,  282,  278,  292,  286,
  279,  287,  288,  301,  291,  293,  292,  294,  287,  288,
  297,  262,  263,  300,  287,  288,  256,  257,  282,  259,
  260,  261,  296,  287,  288,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  275,  282,  277,    0,  279,
  280,  296,  282,  262,  263,  264,  286,  287,  288,  289,
  290,  291,  292,  293,  296,  295,  296,  297,  298,  276,
  300,  256,  257,  279,  259,  260,  261,  265,  266,  292,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,    0,  277,  279,  279,  280,  279,  282,  262,  263,
  279,  286,  287,  288,  289,  290,  291,  292,  293,    0,
  295,  296,  297,  298,  292,  300,  256,  257,  293,  259,
  260,  261,  296,  262,  263,   80,   81,  265,  266,  256,
  257,  282,  259,  260,  261,  265,  266,   91,   92,  282,
  270,  271,  272,  273,  274,  275,  286,  279,  296,  289,
  290,  291,    0,  257,  296,  295,    0,  297,  298,  286,
  300,    0,  289,  290,  291,  279,   63,  279,  295,   63,
  297,  298,  142,  300,  256,  257,   35,  259,  260,  261,
   79,   42,   -1,  265,  266,   -1,   -1,   -1,  270,  271,
  272,  273,  274,  275,   -1,   -1,   -1,  279,  280,   -1,
  282,   -1,   -1,   -1,  286,  287,  288,  289,  290,  291,
  292,  293,   -1,  295,  296,  297,  298,  256,  257,   -1,
  259,  260,  261,   -1,   -1,   -1,  265,  266,   -1,   -1,
   -1,  270,  271,  272,  273,  274,  275,   -1,   -1,   -1,
  279,  280,   -1,  282,   -1,   -1,   -1,  286,  287,  288,
  289,  290,  291,  292,  293,   -1,  295,  296,  297,  298,
  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,  265,
  266,   -1,   -1,   -1,  270,  271,  272,  273,  274,  275,
   -1,   -1,   -1,  279,  280,  257,  282,  259,  260,  261,
  286,  287,  288,  289,  290,  291,  292,  293,   -1,  295,
  296,  297,  298,   -1,  256,  257,   -1,  259,  260,  261,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  289,  290,  256,
  257,   -1,   -1,  260,  261,   -1,   -1,   -1,  280,   -1,
  282,   -1,   -1,   -1,  286,   -1,   -1,  289,  290,  291,
   -1,  293,   -1,  295,  296,  297,  298,  256,  257,  286,
  259,  260,  261,   -1,  291,   -1,   -1,  294,   -1,   -1,
  297,   -1,   -1,  300,   -1,  256,  257,   -1,  259,  260,
  261,  280,   -1,  282,   -1,   -1,   -1,  286,   -1,   -1,
  289,  290,  291,   -1,  293,   -1,  295,  296,  297,  298,
  257,  282,  259,  260,  261,  286,   -1,   -1,  289,  290,
  291,   -1,  293,   -1,  295,  296,  297,  298,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,   -1,    9,  286,
   -1,   12,  289,  290,  291,   -1,   -1,   -1,  295,   -1,
  297,  298,  257,  300,  259,  260,  261,   -1,  286,   -1,
   -1,  289,  290,  291,   35,  293,   -1,  295,  296,  297,
  298,   -1,   43,  257,  258,   -1,  260,  261,  262,  263,
  264,  286,  266,   54,  289,  290,  291,   -1,   -1,   60,
   61,   -1,  297,  298,   -1,  300,   -1,  257,   -1,   -1,
  260,  261,   73,   -1,   75,   76,   -1,   -1,   79,   80,
   81,   -1,  257,   -1,   -1,  260,  261,   88,   -1,   -1,
   91,   92,   -1,   -1,   -1,   -1,  286,   -1,   99,   -1,
   -1,  291,   -1,   -1,  294,  106,  296,  297,   -1,  257,
  300,  286,  260,  261,   -1,   -1,  291,   -1,  119,  294,
   -1,   -1,  297,   -1,  257,  300,   -1,  260,  261,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  286,   -1,
   -1,  142,  143,  291,   -1,  146,  147,  295,  149,  297,
   -1,   -1,  300,  286,   -1,   -1,  256,  257,  291,  259,
  260,  261,   -1,   -1,  297,   -1,   -1,  300,   -1,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,
  257,   -1,  259,  260,  261,   -1,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,  294,   -1,   -1,  297,  298,  286,
   -1,   -1,  289,  290,  291,   -1,   -1,   -1,  295,  286,
  297,  298,  289,  290,  291,   -1,   -1,   -1,  295,   -1,
  297,  298,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,
  256,  257,   -1,  257,  260,  261,  260,  261,   -1,   -1,
  257,   -1,  286,  260,  261,  289,  290,  291,   -1,   -1,
   -1,  295,  286,  297,  298,  289,  290,  291,   -1,   -1,
  286,   -1,  286,  297,  298,  291,   -1,  291,  294,  286,
  294,  297,  296,  297,  291,  257,   -1,  294,  260,  261,
  297,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  286,   -1,   -1,   -1,   -1,  291,
   -1,   -1,   -1,  295,   -1,  297,
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
"encabezado_for : FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion PARENTESIS_R",
"encabezado_for : FOR asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion PARENTESIS_R",
"encabezado_for : FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion",
"encabezado_for : FOR asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion",
"encabezado_for : FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA PARENTESIS_R",
"encabezado_for : FOR PARENTESIS_L asignacion_enteros condicion PUNTO_Y_COMA accion PARENTESIS_R",
"encabezado_for : FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion accion PARENTESIS_R",
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

//#line 298 "gramatica.y"
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
//#line 713 "Parser.java"
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
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); }
break;
case 96:
//#line 206 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 97:
//#line 207 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en el encabezado del FOR"); }
break;
case 98:
//#line 208 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en el encabezado del FOR"); }
break;
case 99:
//#line 209 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 100:
//#line 210 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 101:
//#line 211 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 120:
//#line 244 "gramatica.y"
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
case 122:
//#line 285 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 123:
//#line 286 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 124:
//#line 287 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
//#line 1200 "Parser.java"
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
