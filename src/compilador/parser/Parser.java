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
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    1,    2,    2,    2,    3,    3,    3,    3,    8,    8,
    8,    8,    8,    8,    5,    5,    5,    9,    9,    6,
    6,   10,   10,   11,   11,   11,   12,   12,    7,   13,
   13,   15,   15,   15,   14,   14,   14,   14,   16,   16,
   16,   16,   16,   20,   20,   21,   18,   18,   18,   18,
   18,   18,   26,   26,   27,   27,   23,   23,   28,   28,
    4,    4,    4,    4,    4,    4,   17,   29,   29,   29,
   29,   29,   29,   32,   32,   34,   34,   25,   35,   35,
   35,   35,   35,   35,   33,   33,   36,   36,   19,   19,
   19,   30,   30,   22,   22,   22,   22,   22,   22,   22,
   37,   37,   37,   37,   37,   38,   38,   39,   39,   39,
   39,   31,   31,   24,   24,   24,   40,   40,   40,   41,
   41,   41,   42,   42,   42,   42,   43,   43,   43,   44,
   45,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    5,    4,    4,    3,    2,
    1,    1,    1,    2,    3,    2,    1,    1,   10,    9,
    9,    9,    9,    8,    1,    1,    1,    3,    1,    3,
    1,    1,    1,    1,    1,    1,    3,    3,    4,    6,
    5,    2,    1,    1,    2,    2,    1,    1,    2,    2,
    2,    1,    1,    2,    2,    5,    7,    7,    6,    6,
    5,    6,    2,    1,    2,    1,    4,    1,    2,    1,
    2,    1,    2,    1,    2,    1,    3,    7,    7,    6,
    6,    5,    6,    2,    1,    2,    1,    3,    1,    1,
    1,    1,    1,    1,    4,    1,    2,    1,    4,    4,
    3,    2,    2,    6,    2,    1,    5,    5,    5,    4,
    7,    6,    6,    6,    6,    3,    3,    2,    2,    2,
    2,    3,    1,    4,    3,    1,    3,    3,    1,    3,
    3,    1,    1,    1,    2,    1,    1,    1,    1,    4,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   34,   27,   35,   36,    0,   26,
   25,    0,    0,    0,    0,    0,   11,   12,   13,    0,
    0,   17,   18,   31,    0,   33,    0,    0,    0,    0,
    0,   76,    0,    0,   14,    0,    0,    0,    0,  137,
  138,  139,    0,    0,    0,  133,    0,    0,  126,    0,
  132,  134,  136,    0,    0,    0,    0,    0,    3,   10,
    0,    0,    0,    0,    0,    0,   71,   75,  103,    0,
   96,  102,   73,    0,  105,    0,    2,    0,    0,    0,
    0,  135,    0,    0,    0,    0,   91,   92,   89,   90,
   93,   94,    0,    0,    0,    0,    0,    7,    0,    1,
  101,    0,    0,    0,   29,    0,    0,    0,    0,    0,
   15,    0,    0,   30,    0,   37,    0,    0,   48,    0,
   47,    0,    0,    0,   52,   53,    0,   98,    0,    0,
    0,    0,    0,    0,  116,  117,    0,    0,    0,    0,
    0,  125,    0,    0,    0,    0,    0,    0,    0,    0,
  130,  131,    6,   99,  100,    0,    0,    0,    0,    0,
    0,    0,   43,    0,    0,    0,    0,    0,   39,   46,
   45,   49,   50,   51,   55,    0,   68,   54,    0,   97,
    0,    0,    0,    0,    0,    0,  140,    0,    0,  124,
    0,   82,    0,   84,    0,    0,   28,    0,    0,    0,
   42,   41,    0,    0,    0,    0,    0,   70,    0,   95,
  108,    0,  109,    0,    0,    0,  115,  114,  112,   83,
    0,   80,   81,   86,    0,    0,    0,    0,   40,    0,
    0,    0,    0,    0,    0,    0,   69,  104,  111,  118,
  119,  121,  120,   79,   78,    0,    0,    0,    0,    0,
    0,    0,    0,   63,   61,   56,   67,    0,   24,    0,
    0,    0,   62,    0,   59,   60,   65,   23,   20,    0,
   22,   58,   57,   19,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   19,   20,   21,   22,   23,  106,   24,
   25,   26,   27,  120,  164,  177,   28,  123,   29,  125,
  126,   30,  233,   47,   48,  234,  254,  209,   31,   32,
  113,  149,  150,  194,   93,  129,   33,   38,  217,   49,
   50,   51,   52,   53,  139,
};
final static short yysindex[] = {                      -243,
  648,  754,    0, -273,    0,    0,    0,    0, -234,    0,
    0, -168,  764, -262, -125,  691,    0,    0,    0, -219,
 -192,    0,    0,    0, -256,    0, -221, -185, -177,  335,
 -153,    0,   76,  701,    0,  160, -170, -102, -127,    0,
    0,    0,   67,  247,  -77,    0,  501, -239,    0,   -3,
    0,    0,    0, -154,  711, -229, -151,  -95,    0,    0,
 -138, -117,  247,  160,  160,  594,    0,    0,    0,  774,
    0,    0,    0,  247,    0, -105,    0,  -49,   97,  247,
  247,    0,  -94,  -16,  299,  299,    0,    0,    0,    0,
    0,    0,  247,  -86,  781,  299,  299,    0, -108,    0,
    0,  -43,  -47, -183,    0, -118, -183,  -56,  -40,  115,
    0,  102,  -29,    0, -256,    0,  289,  -19,    0,  578,
    0,  -61,   -5,    4,    0,    0,  497,    0, -182,   21,
  247,   25,  247,   28,    0,    0,   42,  102,   70,   61,
  781,    0,  102,   90,   -3,   -3,  102,  781,   98,  107,
    0,    0,    0,    0,    0,  -53,  138, -183,  -52,  143,
  115,  160,    0,  156,  247,  247,  -87,  247,    0,    0,
    0,    0,    0,    0,    0,  417,    0,    0,  157,    0,
  201,  205,  209,  -33,  111,  111,    0,  772,  170,    0,
  193,    0,  781,    0,  160,  160,    0,  214,  160,  212,
    0,    0,  102,  -82,  200,  636,   43,    0,  545,    0,
    0,  215,    0,  111,  150,  187,    0,    0,    0,    0,
 -244,    0,    0,    0,  -24,    2,  160,  168,    0,  210,
  636,  636,  204,  197,  219,  221,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -156,  249,  182,  257,  629,
  222,  223,  636,    0,    0,    0,    0,  238,    0,  240,
  270,  250,    0, -241,    0,    0,    0,    0,    0,  251,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  531,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,  390,    0,    0,
  403,    0,  504,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   91,
    0,    0,    0,  535,  538,    0,    0,    0,    0,    0,
    0,  226,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  542,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  284,  345,    0,   46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  275,    0,    0,
    0,    0,    0,  178,  136,  181,  -57,    0,    0, -232,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  279,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  524,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -226,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  327,    0,    0,    0,    0,    0,    0,    0,
    0,  566,    0,  455,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -175,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -169,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  239,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  130,   69,  -58,  -11,  -36,  -20,    0,    0,  -38,  646,
  503,  506,    0,    0,  408,  -59,  -64,    0,  -62,    0,
    0,  -60, -122,  -45,  -31,  -48,    0,    0,    0,    0,
    0, -131,  -27,    0,    0,    0,    0,  536, -103,  492,
  372,  368,  546,    0,    0,
};
final static int YYTABLESIZE=1078;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         62,
   32,  122,   72,  124,  178,  127,  121,  119,   35,  189,
  103,  244,   83,    1,  272,   56,  191,  112,   71,  108,
  105,  105,    5,   85,   65,    7,    8,    5,   39,   87,
    7,    8,   40,   41,   42,  138,   43,    5,  143,   94,
    7,    8,  130,   36,  132,   38,  134,  147,  137,  101,
    2,  245,   95,  102,  273,  122,  221,  124,  128,  127,
  171,  170,  122,   85,  124,  156,  127,  105,  159,   87,
  105,   45,   66,  162,    5,    6,   63,    7,    8,   61,
   64,  218,  219,   71,   60,  167,   66,   64,    5,   39,
  129,    7,    8,   40,   41,   42,   67,   43,   79,  182,
  258,  184,   60,    9,   68,   10,   11,    6,   12,   44,
  239,  122,  179,  124,   14,  127,  208,  180,  109,  203,
   64,  197,  207,   60,  162,  259,   66,   35,   73,   71,
  267,   34,   45,  104,  204,  127,   71,   10,   11,  110,
   98,  122,   55,  124,  122,  127,  124,   57,  127,  237,
   81,    5,   39,  157,    7,    8,   40,   41,   42,   58,
   43,  158,   64,    6,  111,  224,  122,  122,  124,  124,
  127,  127,  131,   35,  225,  226,   71,  107,  228,   80,
  128,   71,  251,  252,  140,  122,  153,  124,  122,  127,
  124,  205,  127,   10,   11,   45,  230,  141,   88,   88,
   84,  264,   88,   88,  206,  148,  248,    5,   39,  231,
    7,    8,   40,   41,   42,  160,   43,   85,   86,  198,
  172,   88,  195,  158,   88,   16,  158,  158,   88,   88,
   88,  155,  133,   88,   88,  154,   88,  161,   21,   88,
    5,   39,   88,    7,    8,   40,   41,   42,  214,   43,
  165,   45,  246,  215,  216,   64,   32,   32,  168,   32,
   32,   32,  142,   96,   97,   32,   32,   32,   32,   32,
   32,   32,   32,   32,   32,   32,  173,   32,  247,   32,
   32,   64,   32,  123,   45,  174,   32,   32,   32,   32,
   32,   32,   32,   32,   32,   32,   32,   32,   32,  181,
   32,   38,   38,  183,   38,   38,   38,   85,   86,  185,
   38,   38,   38,   38,   38,   38,   38,   38,   38,   38,
   38,  235,   38,  186,   38,   38,  122,   38,   40,   41,
   42,   38,   38,   38,   38,   38,   38,   38,   38,   38,
   38,   38,   38,   38,   77,   38,  129,  129,  187,  129,
  129,  129,  188,   74,   75,  129,  129,   76,  135,  136,
  129,  129,  129,  129,  129,  129,   85,   86,  190,  129,
  129,    5,  129,    6,    7,    8,  129,  129,  129,  129,
  129,  129,  129,  129,  129,  129,  129,  129,  129,   72,
  129,  127,  127,  192,  127,  127,  127,  215,  216,  193,
  127,  127,   74,   10,   11,  127,  127,  127,  127,  127,
  127,  240,  241,  196,  127,  127,    5,  127,  199,    7,
    8,  127,  127,  127,  127,  127,  127,  127,  127,  127,
  127,  127,  127,  127,  202,  127,  128,  128,  210,  128,
  128,  128,  126,  126,  249,  128,  128,   64,  242,  243,
  128,  128,  128,  128,  128,  128,  145,  146,  261,  128,
  128,   64,  128,  151,  152,  222,  128,  128,  128,  128,
  128,  128,  128,  128,  128,  128,  128,  128,  128,  211,
  128,   16,   16,  212,   16,   16,   16,  213,  223,  227,
  229,  232,  255,  238,   21,   21,  253,   21,   21,   21,
  256,  250,  257,    5,   39,  260,    7,    8,   40,   41,
   42,   16,   43,  262,   16,   16,   16,  265,  266,  268,
   16,  269,   16,   16,   21,   16,  270,   21,   21,   21,
    5,  271,  274,   21,    9,   21,   21,    4,   21,  123,
  123,    8,  123,  123,  123,    5,   39,   45,    7,    8,
   40,   41,   42,  141,   43,    5,   39,   44,    7,    8,
   40,   41,   42,  123,   43,  123,  166,  115,  200,  123,
  116,   78,  123,  123,  123,  144,  123,    0,  123,  123,
  123,  123,  122,  122,    0,  122,  122,  122,   82,   45,
   69,    5,    0,    0,    7,    8,    0,    0,    0,    0,
   77,   77,    0,   77,   77,   77,  122,    0,  122,    0,
    0,    0,  122,    0,    0,  122,  122,  122,    0,  122,
    9,  122,  122,  122,  122,   12,   77,    0,   70,    0,
   77,   14,    0,   77,   77,   77,    0,   77,    0,   77,
   77,   77,   77,    0,    0,   72,   72,    0,   72,   72,
   72,    0,    0,    0,   37,    0,    0,   46,   74,   74,
    0,   74,   74,   74,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    5,    0,   72,    7,    8,   72,   72,
   72,   37,   72,    0,   72,   72,   72,   72,   74,   46,
    0,   74,   74,   74,    0,   74,    0,   74,   74,   74,
   74,   46,    9,    0,    0,    0,    0,  117,   46,  114,
  113,  113,    0,   14,  113,  113,  118,    0,    0,   46,
    0,   46,    0,   46,    0,   46,   46,    0,    0,   46,
   46,   46,  113,  113,    0,    0,  113,    0,   46,    0,
  113,   46,   46,    0,    0,  113,    0,    0,  113,    0,
    0,  113,  175,    5,  113,  163,    7,    8,    0,  106,
  106,    0,   46,  106,  106,   85,   86,    0,    0,    0,
   87,   88,   89,   90,   91,   92,   46,    0,   46,  110,
  110,    0,    9,  110,  110,    0,    0,  117,    0,  106,
  176,    0,    0,   14,  106,    0,  118,  106,    0,    0,
  106,    5,    0,  106,    7,    8,  163,  201,    0,  110,
   46,   46,    0,   46,  110,    0,    0,  110,    0,    0,
  110,  107,  107,  110,    0,  107,  107,    0,    0,    0,
    9,    0,    0,    0,    5,  117,    6,    7,    8,  236,
    0,   14,    0,    0,  118,    0,    0,    0,    0,    0,
    5,  107,    6,    7,    8,    0,  107,    0,    0,  107,
    0,    0,  107,    9,    0,  107,   10,   11,  117,    0,
    0,    0,  169,    0,   14,   15,    0,  118,    0,    9,
    0,    0,   10,   11,  117,    5,    0,    0,    7,    8,
   14,   15,    5,  118,    0,    7,    8,    0,    0,    0,
    0,    0,    0,    4,    5,    0,    6,    7,    8,    0,
    0,    0,    0,    0,    9,    0,    0,    0,    0,  117,
    0,    9,  176,    0,  263,   14,  117,    0,  118,  176,
    0,    0,   14,    9,    0,  118,   10,   11,   12,    0,
    0,   13,    0,    0,   14,   15,    4,    5,    0,    6,
    7,    8,    0,    0,    0,    0,    4,    5,    0,    6,
    7,    8,    0,    0,    0,    0,   99,    5,    0,    6,
    7,    8,    0,    0,    0,    0,    9,    0,    0,   10,
   11,   12,    0,    0,    0,   59,    9,   14,   15,   10,
   11,   12,    0,    0,    0,   77,    9,   14,   15,   10,
   11,   12,    0,    0,    0,  100,    0,   14,   15,    4,
    5,    0,    6,    7,    8,    0,    0,    0,    0,   54,
    5,    0,    6,    7,    8,    0,    0,    0,    5,    0,
    5,    7,    8,    7,    8,    0,    0,    5,    0,    9,
    7,    8,   10,   11,   12,    0,    0,    0,    0,    9,
   14,   15,   10,   11,   12,    0,    0,    9,    0,    9,
   14,   15,   12,    0,   12,   70,    9,  220,   14,    0,
   14,   12,    0,    0,   70,    0,    0,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   66,   30,   66,  127,   66,   66,   66,  282,  141,
   56,  256,   44,  257,  256,  278,  148,   63,   30,   58,
   57,   58,  257,  256,  281,  260,  261,  257,  258,  256,
  260,  261,  262,  263,  264,   81,  266,  257,   84,  279,
  260,  261,   74,  278,   76,    0,   78,   93,   80,  279,
  294,  296,  292,  283,  296,  120,  188,  120,   70,  120,
  120,  120,  127,  296,  127,  104,  127,  104,  107,  296,
  107,  301,  294,  110,  257,  259,  269,  260,  261,  299,
  256,  185,  186,   95,   16,  117,  256,  280,  257,  258,
    0,  260,  261,  262,  263,  264,  282,  266,  269,  131,
  257,  133,   34,  286,  282,  289,  290,  259,  291,  278,
  214,  176,  295,  176,  297,  176,  176,  129,  257,  165,
  296,  158,  168,   55,  161,  282,  296,  282,  282,  141,
  253,    2,  301,  285,  166,    0,  148,  289,  290,  278,
  295,  206,   13,  206,  209,  206,  209,  273,  209,  209,
  278,  257,  258,  272,  260,  261,  262,  263,  264,  285,
  266,  280,  280,  259,  282,  193,  231,  232,  231,  232,
  231,  232,  278,  282,  195,  196,  188,  273,  199,  282,
    0,  193,  231,  232,  279,  250,  295,  250,  253,  250,
  253,  279,  253,  289,  290,  301,  279,  292,  256,  257,
  278,  250,  260,  261,  292,  292,  227,  257,  258,  292,
  260,  261,  262,  263,  264,  272,  266,  265,  266,  272,
  282,  279,  276,  280,  282,    0,  280,  280,  286,  287,
  288,  279,  282,  291,  292,  279,  294,  278,    0,  297,
  257,  258,  300,  260,  261,  262,  263,  264,  282,  266,
  280,  301,  277,  287,  288,  280,  256,  257,  278,  259,
  260,  261,  279,  267,  268,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  275,  282,  277,  277,  279,
  280,  280,  282,    0,  301,  282,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,  279,
  300,  256,  257,  279,  259,  260,  261,  265,  266,  282,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  279,  277,  282,  279,  280,    0,  282,  262,  263,
  264,  286,  287,  288,  289,  290,  291,  292,  293,  294,
  295,  296,  297,  298,    0,  300,  256,  257,  279,  259,
  260,  261,  292,  278,  279,  265,  266,  282,  262,  263,
  270,  271,  272,  273,  274,  275,  265,  266,  279,  279,
  280,  257,  282,  259,  260,  261,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,    0,
  300,  256,  257,  296,  259,  260,  261,  287,  288,  293,
  265,  266,    0,  289,  290,  270,  271,  272,  273,  274,
  275,  262,  263,  276,  279,  280,  257,  282,  276,  260,
  261,  286,  287,  288,  289,  290,  291,  292,  293,  294,
  295,  296,  297,  298,  279,  300,  256,  257,  282,  259,
  260,  261,  265,  266,  277,  265,  266,  280,  262,  263,
  270,  271,  272,  273,  274,  275,   85,   86,  277,  279,
  280,  280,  282,   96,   97,  296,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,  279,
  300,  256,  257,  279,  259,  260,  261,  279,  296,  276,
  279,  292,  296,  279,  256,  257,  293,  259,  260,  261,
  282,  292,  282,  257,  258,  257,  260,  261,  262,  263,
  264,  286,  266,  257,  289,  290,  291,  296,  296,  282,
  295,  282,  297,  298,  286,  300,  257,  289,  290,  291,
    0,  282,  282,  295,    0,  297,  298,    0,  300,  256,
  257,    0,  259,  260,  261,  257,  258,  301,  260,  261,
  262,  263,  264,  279,  266,  257,  258,  279,  260,  261,
  262,  263,  264,  280,  266,  282,  278,   65,  161,  286,
   65,   36,  289,  290,  291,   84,  293,   -1,  295,  296,
  297,  298,  256,  257,   -1,  259,  260,  261,   43,  301,
  256,  257,   -1,   -1,  260,  261,   -1,   -1,   -1,   -1,
  256,  257,   -1,  259,  260,  261,  280,   -1,  282,   -1,
   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,  293,
  286,  295,  296,  297,  298,  291,  282,   -1,  294,   -1,
  286,  297,   -1,  289,  290,  291,   -1,  293,   -1,  295,
  296,  297,  298,   -1,   -1,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,    9,   -1,   -1,   12,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,   -1,  286,  260,  261,  289,  290,
  291,   36,  293,   -1,  295,  296,  297,  298,  286,   44,
   -1,  289,  290,  291,   -1,  293,   -1,  295,  296,  297,
  298,   56,  286,   -1,   -1,   -1,   -1,  291,   63,   64,
  256,  257,   -1,  297,  260,  261,  300,   -1,   -1,   74,
   -1,   76,   -1,   78,   -1,   80,   81,   -1,   -1,   84,
   85,   86,  278,  279,   -1,   -1,  282,   -1,   93,   -1,
  286,   96,   97,   -1,   -1,  291,   -1,   -1,  294,   -1,
   -1,  297,  256,  257,  300,  110,  260,  261,   -1,  256,
  257,   -1,  117,  260,  261,  265,  266,   -1,   -1,   -1,
  270,  271,  272,  273,  274,  275,  131,   -1,  133,  256,
  257,   -1,  286,  260,  261,   -1,   -1,  291,   -1,  286,
  294,   -1,   -1,  297,  291,   -1,  300,  294,   -1,   -1,
  297,  257,   -1,  300,  260,  261,  161,  162,   -1,  286,
  165,  166,   -1,  168,  291,   -1,   -1,  294,   -1,   -1,
  297,  256,  257,  300,   -1,  260,  261,   -1,   -1,   -1,
  286,   -1,   -1,   -1,  257,  291,  259,  260,  261,  295,
   -1,  297,   -1,   -1,  300,   -1,   -1,   -1,   -1,   -1,
  257,  286,  259,  260,  261,   -1,  291,   -1,   -1,  294,
   -1,   -1,  297,  286,   -1,  300,  289,  290,  291,   -1,
   -1,   -1,  295,   -1,  297,  298,   -1,  300,   -1,  286,
   -1,   -1,  289,  290,  291,  257,   -1,   -1,  260,  261,
  297,  298,  257,  300,   -1,  260,  261,   -1,   -1,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,   -1,  286,   -1,   -1,   -1,   -1,  291,
   -1,  286,  294,   -1,  296,  297,  291,   -1,  300,  294,
   -1,   -1,  297,  286,   -1,  300,  289,  290,  291,   -1,
   -1,  294,   -1,   -1,  297,  298,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,   -1,  295,   -1,  297,  298,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,  257,   -1,
  257,  260,  261,  260,  261,   -1,   -1,  257,   -1,  286,
  260,  261,  289,  290,  291,   -1,   -1,   -1,   -1,  286,
  297,  298,  289,  290,  291,   -1,   -1,  286,   -1,  286,
  297,  298,  291,   -1,  291,  294,  286,  296,  297,   -1,
  297,  291,   -1,   -1,  294,   -1,   -1,  297,
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
"sentencia_ejecutable : sentencia_seleccion",
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
"expresion : TOS PARENTESIS_L PARENTESIS_R",
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

//#line 312 "gramatica.y"
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
//#line 766 "Parser.java"
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
case 6:
//#line 24 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO."); }
break;
case 7:
//#line 25 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO."); }
break;
case 8:
//#line 26 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. FALTA EL END AL FINAL DEL PROGRAMA"); }
break;
case 9:
//#line 27 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. FALTA EL END AL FINAL DEL PROGRAMA"); }
break;
case 14:
//#line 37 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)val_peek(0).obj).getNumeroDeLinea() + ": Syntax Error");}
break;
case 15:
//#line 40 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S"); }
break;
case 16:
//#line 41 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ';' al final de la sentencia"); }
break;
case 17:
//#line 42 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FUNCION"); }
break;
case 19:
//#line 46 "gramatica.y"
{
                                                                                                                                                    yyval.ival = ((Token) val_peek(9).obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) val_peek(9).obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) val_peek(1).obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                }
break;
case 20:
//#line 52 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 21:
//#line 53 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 22:
//#line 54 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 23:
//#line 55 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 24:
//#line 56 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 25:
//#line 60 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 26:
//#line 61 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 27:
//#line 62 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 31:
//#line 71 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 32:
//#line 74 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 33:
//#line 75 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 34:
//#line 78 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 35:
//#line 79 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 36:
//#line 80 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 37:
//#line 83 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 38:
//#line 84 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 39:
//#line 87 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 40:
//#line 96 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 41:
//#line 101 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                            }
break;
case 43:
//#line 108 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 44:
//#line 109 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 49:
//#line 118 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 50:
//#line 119 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 51:
//#line 120 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 52:
//#line 121 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 53:
//#line 122 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 54:
//#line 125 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 55:
//#line 126 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 56:
//#line 129 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 57:
//#line 132 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 58:
//#line 133 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 59:
//#line 134 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 60:
//#line 135 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 61:
//#line 136 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 62:
//#line 137 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 66:
//#line 145 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 71:
//#line 156 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 72:
//#line 157 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ';' al final de la sentencia");}
break;
case 73:
//#line 158 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 74:
//#line 159 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ';' al final de la sentencia");}
break;
case 75:
//#line 160 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "SALIDA"); }
break;
case 76:
//#line 161 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 77:
//#line 164 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 78:
//#line 167 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 79:
//#line 168 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 80:
//#line 169 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 81:
//#line 170 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 82:
//#line 171 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 83:
//#line 172 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 87:
//#line 180 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 99:
//#line 202 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 100:
//#line 203 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 101:
//#line 204 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 102:
//#line 207 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 103:
//#line 208 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 104:
//#line 211 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 105:
//#line 212 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 106:
//#line 213 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 107:
//#line 214 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 108:
//#line 215 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 109:
//#line 216 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 110:
//#line 217 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 111:
//#line 220 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 112:
//#line 221 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 113:
//#line 222 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 114:
//#line 223 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 115:
//#line 224 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 125:
//#line 242 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 135:
//#line 258 "gramatica.y"
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
case 137:
//#line 299 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 138:
//#line 300 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 139:
//#line 301 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
//#line 1314 "Parser.java"
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
