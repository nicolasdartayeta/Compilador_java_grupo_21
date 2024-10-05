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
    9,    9,    6,    6,   10,   10,   11,   11,   11,   12,
   12,    7,   13,   13,   15,   15,   15,   15,   15,   14,
   14,   14,   14,   16,   16,   16,   16,   16,   16,   16,
   20,   20,   21,   21,   21,   18,   18,   18,   18,   18,
   18,   18,   18,   26,   26,   27,   27,   23,   23,   23,
   23,   28,   28,    4,    4,    4,    4,    4,    4,   17,
   29,   29,   29,   29,   29,   29,   29,   29,   32,   32,
   34,   34,   25,   25,   35,   35,   35,   35,   35,   35,
   33,   33,   33,   33,   36,   36,   19,   19,   19,   19,
   19,   19,   19,   19,   30,   30,   22,   22,   22,   22,
   22,   22,   22,   37,   37,   37,   37,   37,   38,   38,
   39,   39,   39,   39,   31,   31,   24,   24,   24,   40,
   40,   40,   40,   41,   41,   41,   42,   42,   42,   42,
   43,   43,   43,   44,   44,   44,   45,
};
final static short yylen[] = {                            2,
    4,    5,    3,    3,    3,    2,    5,    4,    4,    3,
    2,    1,    1,    1,    2,    3,    4,    4,    1,    1,
   10,    9,   11,   11,    9,    9,    8,    1,    1,    1,
    3,    1,    3,    1,    1,    1,    1,    1,    1,    3,
    3,    4,    6,    5,    2,    1,    1,    3,    1,    2,
    2,    1,    1,    2,    3,    3,    1,    1,    1,    1,
    2,    2,    5,    6,    6,    8,    9,    9,    7,    7,
    7,    6,    7,    2,    1,    2,    1,    4,    5,    5,
    1,    2,    1,    2,    3,    3,    1,    1,    1,    3,
    8,    9,    9,    7,    7,    7,    6,    7,    2,    1,
    2,    1,    3,    1,    1,    1,    1,    1,    1,    1,
    4,    5,    5,    1,    2,    1,    5,    5,    6,    6,
    6,    6,    4,    4,    2,    2,    6,    2,    1,    5,
    5,    5,    4,    7,    6,    6,    6,    6,    3,    3,
    2,    2,    2,    2,    3,    1,    4,    3,    1,    3,
    3,    1,    1,    3,    3,    1,    1,    1,    2,    1,
    1,    1,    1,    4,    3,    5,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   37,   30,   38,   39,    0,   29,
   28,    0,    0,    0,    0,    0,   12,   13,   14,    0,
    0,   19,   20,   34,    0,   36,    0,    0,   88,    0,
   87,   89,    0,    0,   15,    0,    0,    0,    0,    0,
  161,  162,  163,    0,    0,    0,  157,    0,    0,    0,
    0,  156,  158,  160,    0,    0,    0,    0,    0,    4,
   11,    0,    0,    0,    0,    0,    0,    0,   84,  126,
    0,  114,  125,    0,  128,    0,    3,    0,    0,    0,
    0,  159,    0,    0,  107,  108,  105,  106,  109,  110,
    0,    0,    0,    0,    0,    0,    0,    8,    0,    0,
    0,    0,    0,    0,    0,   32,    0,    0,    0,    0,
    0,    0,   16,  153,  146,    0,   33,    0,   40,    0,
    0,   53,    0,   52,    0,   57,   58,   59,   60,    0,
   85,   86,  116,    0,    0,    0,    0,    0,    0,  139,
  140,    0,  165,  167,    0,    0,    0,  148,    0,  103,
    0,    0,    0,    0,    0,  154,  155,    7,    2,  124,
  123,    0,    0,    0,    0,    0,    0,    0,    0,   49,
    0,   46,    0,   17,   18,    0,    0,    0,    0,   42,
   51,   50,    0,   54,   62,    0,   81,   61,    0,  115,
    0,    0,    0,    0,    0,    0,    0,  164,    0,    0,
  147,    0,    0,    0,   99,    0,  117,    0,  118,    0,
    0,   31,    0,    0,    0,    0,   44,  145,    0,    0,
    0,    0,   55,   56,   83,    0,    0,  111,  131,    0,
  132,    0,    0,    0,  138,  137,  135,  166,    0,    0,
    0,    0,   97,  101,  119,  121,  120,  122,    0,    0,
    0,    0,   43,   48,    0,    0,    0,    0,    0,    0,
    0,   82,  112,  113,  127,  134,  141,  142,  144,  143,
   98,   94,    0,   95,   96,    0,    0,    0,    0,    0,
    0,    0,    0,   74,    0,    0,   63,    0,   78,    0,
   91,    0,   27,    0,    0,    0,    0,    0,    0,    0,
   76,   72,   64,   65,   79,   80,   92,   93,   26,   22,
    0,   25,   73,   69,    0,   70,   71,    0,   21,    0,
   66,   23,   24,   67,   68,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   19,   20,   21,   22,   23,  107,   24,
   25,   26,   27,  123,  173,  187,   28,  126,   29,  128,
  129,   30,  258,   48,   49,  259,  284,  226,   31,   32,
  116,  152,  153,  205,   91,  134,   33,   38,  235,   50,
   51,   52,   53,   54,  145,
};
final static short yysindex[] = {                      -245,
  551,  657,    0, -253,    0,    0,    0,    0,  -18,    0,
    0, -147,  667, -145,  -56,  594,    0,    0,    0, -241,
 -109,    0,    0,    0, -139,    0, -142, -239,    0,  675,
    0,    0, -248,  604,    0,  130,  -45,  -93,    0,  -23,
    0,    0,    0,   64, -210,  -16,    0,  350,  -74,  -57,
  -35,    0,    0,    0, -259,  614, -178, -183, -155,    0,
    0,  -77,  -97,   95,  130,  130,  497,  -42,    0,    0,
  719,    0,    0, -210,    0, -113,    0,   12,  102, -210,
   35,    0,   26,  727,    0,    0,    0,    0,    0,    0,
   95,  -10,  684,  737,  737,  737,  737,    0,   29,  -28,
    2,    4,   33,   51, -103,    0, -247, -103,    5,   31,
 -120,   49,    0,    0,    0,   62,    0, -139,    0,   59,
   55,    0,  481,    0, -238,    0,    0,    0,    0,  407,
    0,    0,    0,  -50,   71, -210,   75, -210,   86,    0,
    0,   88,    0,    0, -167,   92,  684,    0,   -8,    0,
  684,   99,  108,  -35,  -35,    0,    0,    0,    0,    0,
    0, -179, -162, -184,  131, -103,    7,  134, -120,    0,
  130,    0,  147,    0,    0,   95, -210,   53,   95,    0,
    0,    0,   87,    0,    0,  448,    0,    0, -110,    0,
  154,  164,  173,  150,  186,  186,  178,    0,  677,  133,
    0,  153,  177,  684,    0,  107,    0,  117,    0,  130,
  130,    0,  192,  130,  196,  227,    0,    0,   56,  187,
  539,  212,    0,    0,    0,   43,  129,    0,    0,  215,
    0,  186,  223,  233,    0,    0,    0,    0,  219, -258,
  228,  231,    0,    0,    0,    0,    0,    0, -187,  -21,
  130,    3,    0,    0,  224,  539,  539,  222,  221,  -98,
  -82,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -80,    0,    0, -182,  264,   30,  265,  532,
  229,  237,  539,    0,  245,  149,    0,  159,    0,  171,
    0,  246,    0,  254,  280,  256,  259, -256,  260,  261,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -79,    0,    0,    0,  -61,    0,    0,  176,    0,  185,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  550,    0,    0,    0,    0,
    0,    0,    0,    0,  106,    0,    0,    0,    0,    0,
    0,    0,  420,    0,    0,    0,    0,    0,  316,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  358,
  190,    0,    0,    0,  552,  555,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  557,  558,
   23,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -52,    0,  148,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -115,  232,  274,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  272,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  427,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -70,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  284,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  469,
    0,  400,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -44,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -33,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   85,   39,  -54,  -26,  -37,  -20,    0,    0,  -48,  170,
  493,  498,    0,    0,  398,  -62,  -64,    0,  -60,    0,
    0,  -58, -128,  -49,  -39,  -43,    0,    0,    0,    0,
    0, -137,  -29,    0,    0,    0,    0,  533, -168,  486,
  405,  412,  531,    0,    0,
};
final static int YYTABLESIZE=1016;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         63,
   73,  188,  125,   72,  124,   83,  127,  104,  130,  200,
  109,    1,  122,  202,  115,    5,   68,  183,    7,    8,
  106,  106,   35,  272,  165,  314,  236,  237,   35,   74,
   75,  144,  166,   76,  135,   98,  137,  273,  139,  315,
  142,  150,   69,  184,  133,   39,    5,   40,    2,    7,
    8,   41,   42,   43,   61,   44,  164,   62,  125,  167,
  182,  240,  127,  266,  130,  125,   72,  106,  181,  127,
  106,  130,   61,  171,  292,    6,  206,  101,    5,   40,
  178,    7,    8,   41,   42,   43,   34,   44,  197,  276,
   46,  210,   65,  208,   61,  166,  192,   56,  194,  293,
  102,  105,  207,    6,  103,   10,   11,  190,   39,    5,
   40,  198,    7,    8,   41,   42,   43,  108,   44,  209,
   72,  125,   46,  225,   72,  127,  218,  130,  212,  222,
   45,  171,   57,   10,   11,  170,    5,  219,    6,    7,
    8,   66,   39,    5,   40,  227,    7,    8,   41,   42,
   43,   67,   44,   46,  301,    6,  125,  286,  112,   64,
  127,  125,  130,  262,  136,  127,  100,  130,   10,   11,
   65,  228,   72,  288,  244,  290,  318,   72,   37,  110,
  100,   47,   65,  287,  113,   10,   11,   46,   80,  249,
  250,  125,  125,  252,  320,  127,  127,  130,  130,  289,
  111,  291,  319,   90,   92,   37,    5,   94,   95,    7,
    8,  102,  281,  282,   47,  125,   58,   93,  125,  127,
  321,  130,  127,   79,  130,  102,   47,  159,   59,   90,
  278,   96,   97,   47,  117,    9,  298,   75,    5,  131,
   12,    7,    8,   47,  189,   47,   14,   47,   77,   47,
   47,   75,  132,   47,   81,  277,   94,   95,   65,   36,
   47,   84,   77,   47,   47,   47,   47,   39,    5,   40,
  201,    7,    8,   41,   42,   43,  168,   44,  213,  279,
  172,  151,   65,  160,  166,  161,  166,  153,  153,   47,
  114,    5,   40,  138,    7,    8,   41,   42,   43,    5,
   44,  153,    7,    8,  146,   47,  295,   47,  169,   65,
   35,  162,   46,  143,   39,    5,   40,  147,    7,    8,
   41,   42,   43,  158,   44,   41,   42,   43,    9,  163,
  174,  220,  179,  120,  255,   46,  177,  261,  172,   14,
  216,  176,  121,  175,  221,   47,   47,  256,   47,  191,
  114,    5,   40,  193,    7,    8,   41,   42,   43,   46,
   44,   35,   35,  140,  141,   35,   35,  195,  223,  196,
   35,   35,   35,   35,   35,   35,   35,   35,   35,   35,
   35,  224,   35,  199,   35,   35,    5,   35,  245,    7,
    8,   35,   35,   35,  203,   46,   35,   35,  247,   35,
  204,  246,   35,   41,   41,   35,  211,   41,   41,  214,
  263,  248,   41,   41,   41,   41,   41,   41,   41,   41,
   41,   41,   41,  264,   41,  217,   41,   41,  241,   41,
  303,  232,  229,   41,   41,   41,  233,  234,   41,   41,
  305,   41,  230,  304,   41,  152,  152,   41,  242,  152,
  152,  231,  307,  306,  152,  152,  238,  322,  243,  152,
  152,  152,  152,  152,  152,  308,  324,  251,  152,  152,
  323,  152,  233,  234,  253,  152,  152,  152,  257,  325,
  152,  152,  254,  152,  267,  268,  152,  150,  150,  152,
  260,  150,  150,  265,  269,  270,  150,  150,  154,  155,
  271,  150,  150,  150,  150,  150,  150,  156,  157,  274,
  150,  150,  275,  150,  283,  280,  285,  150,  150,  150,
  294,  296,  150,  150,  299,  150,  302,  309,  150,  151,
  151,  150,  300,  151,  151,  310,  311,  312,  151,  151,
  313,  316,  317,  151,  151,  151,  151,  151,  151,    6,
   47,   10,  151,  151,    5,  151,    9,    1,  118,  151,
  151,  151,   45,  119,  151,  151,  215,  151,   78,  149,
  151,  104,  104,  151,   82,  104,  104,    0,    0,    0,
  153,  153,    0,    0,    0,  153,  153,  153,  153,  153,
  153,    0,    0,    0,  104,    0,    0,  104,    0,    0,
    0,  104,  104,  104,    0,    0,  104,  104,    0,  104,
    0,    0,  104,  149,  149,  104,    0,  149,  149,   85,
   86,   87,   88,   89,   90,    0,    0,  149,  149,  149,
  149,  149,  149,    0,    0,    0,  149,  149,    0,  149,
    0,    0,    0,  149,  149,  149,    0,    0,  149,  149,
    0,  149,    0,    0,  149,  136,  136,  149,    0,  136,
  136,    0,  185,    5,    0,    0,    7,    8,    0,    0,
    0,    0,    0,    0,    0,  129,  129,  136,  136,  129,
  129,  136,  133,  133,    0,  136,  133,  133,    0,    0,
  136,    0,    9,  136,    0,    0,  136,  120,    0,  136,
  186,    0,    0,   14,    5,  129,  121,    7,    8,    0,
  129,    0,  133,  129,    0,    0,  129,  133,    0,  129,
  133,    0,    0,  133,  130,  130,  133,    0,  130,  130,
    0,    0,    0,    9,    0,    0,    0,    5,  120,    6,
    7,    8,    0,    0,   14,    0,    0,  121,    0,    0,
    0,    0,    0,    5,  130,    6,    7,    8,    0,  130,
    0,    0,  130,    0,    0,  130,    9,    0,  130,   10,
   11,  120,    0,    0,    0,  180,    0,   14,   15,    0,
  121,    0,    9,    0,    0,   10,   11,  120,    5,    0,
    0,    7,    8,   14,   15,    5,  121,    0,    7,    8,
    0,    0,    0,    0,    0,    0,    4,    5,    0,    6,
    7,    8,    0,    0,    0,    0,    0,    9,    0,    0,
    0,    0,  120,    0,    9,  186,    0,  297,   14,  120,
    0,  121,  186,    0,    0,   14,    9,    0,  121,   10,
   11,   12,    0,    0,   13,    0,    0,   14,   15,    4,
    5,    0,    6,    7,    8,    0,    0,    0,    0,    4,
    5,    0,    6,    7,    8,    0,    0,    0,    0,   99,
    5,    0,    6,    7,    8,    0,    0,    0,    0,    9,
    0,    0,   10,   11,   12,    0,    0,    0,   60,    9,
   14,   15,   10,   11,   12,    0,    0,    0,   77,    9,
   14,   15,   10,   11,   12,    0,    0,    0,  100,    0,
   14,   15,    4,    5,    0,    6,    7,    8,    0,    0,
    0,    0,   55,    5,    0,    6,    7,    8,    0,    0,
   70,    5,    0,    5,    7,    8,    7,    8,    0,    0,
    5,    0,    9,    7,    8,   10,   11,   12,    0,    0,
    0,    0,    9,   14,   15,   10,   11,   12,    0,    0,
    9,    0,    9,   14,   15,   12,    0,   12,   71,    9,
   71,   14,  239,   14,   12,    5,    0,   71,    7,    8,
   14,    0,  114,    5,   40,    0,    7,    8,   41,   42,
   43,    0,   44,    5,   40,    0,    7,    8,   41,   42,
   43,    0,   44,    0,    9,  148,    0,    0,    0,   12,
    0,    0,    0,    0,    0,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
   30,  130,   67,   30,   67,   45,   67,   57,   67,  147,
   59,  257,   67,  151,   64,  257,  256,  256,  260,  261,
   58,   59,  282,  282,  272,  282,  195,  196,  282,  278,
  279,   81,  280,  282,   74,  295,   76,  296,   78,  296,
   80,   91,  282,  282,   71,  256,  257,  258,  294,  260,
  261,  262,  263,  264,   16,  266,  105,  299,  123,  108,
  123,  199,  123,  232,  123,  130,   93,  105,  123,  130,
  108,  130,   34,  111,  257,  259,  256,  256,  257,  258,
  120,  260,  261,  262,  263,  264,    2,  266,  256,  277,
  301,  276,  280,  256,   56,  280,  136,   13,  138,  282,
  279,  285,  282,  259,  283,  289,  290,  134,  256,  257,
  258,  279,  260,  261,  262,  263,  264,  273,  266,  282,
  147,  186,  301,  186,  151,  186,  176,  186,  166,  179,
  278,  169,  278,  289,  290,  256,  257,  177,  259,  260,
  261,  281,  256,  257,  258,  256,  260,  261,  262,  263,
  264,  294,  266,  301,  283,  259,  221,  256,  256,  269,
  221,  226,  221,  226,  278,  226,  282,  226,  289,  290,
  280,  282,  199,  256,  204,  256,  256,  204,    9,  257,
  296,   12,  280,  282,  282,  289,  290,  301,  282,  210,
  211,  256,  257,  214,  256,  256,  257,  256,  257,  282,
  278,  282,  282,  256,  279,   36,  257,  265,  266,  260,
  261,  282,  256,  257,   45,  280,  273,  292,  283,  280,
  282,  280,  283,  269,  283,  296,   57,  256,  285,  282,
  251,  267,  268,   64,   65,  286,  280,  282,  257,  282,
  291,  260,  261,   74,  295,   76,  297,   78,  282,   80,
   81,  296,  295,   84,  278,  277,  265,  266,  280,  278,
   91,  278,  296,   94,   95,   96,   97,  256,  257,  258,
  279,  260,  261,  262,  263,  264,  272,  266,  272,  277,
  111,  292,  280,  282,  280,  282,  280,  265,  266,  120,
  256,  257,  258,  282,  260,  261,  262,  263,  264,  257,
  266,  279,  260,  261,  279,  136,  277,  138,  278,  280,
  282,  279,  301,  279,  256,  257,  258,  292,  260,  261,
  262,  263,  264,  295,  266,  262,  263,  264,  286,  279,
  282,  279,  278,  291,  279,  301,  278,  295,  169,  297,
  171,  280,  300,  295,  292,  176,  177,  292,  179,  279,
  256,  257,  258,  279,  260,  261,  262,  263,  264,  301,
  266,  256,  257,  262,  263,  260,  261,  282,  282,  282,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  295,  277,  292,  279,  280,  257,  282,  282,  260,
  261,  286,  287,  288,  296,  301,  291,  292,  282,  294,
  293,  295,  297,  256,  257,  300,  276,  260,  261,  276,
  282,  295,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,  295,  277,  279,  279,  280,  296,  282,
  282,  282,  279,  286,  287,  288,  287,  288,  291,  292,
  282,  294,  279,  295,  297,  256,  257,  300,  296,  260,
  261,  279,  282,  295,  265,  266,  279,  282,  282,  270,
  271,  272,  273,  274,  275,  295,  282,  276,  279,  280,
  295,  282,  287,  288,  279,  286,  287,  288,  292,  295,
  291,  292,  256,  294,  262,  263,  297,  256,  257,  300,
  279,  260,  261,  279,  262,  263,  265,  266,   94,   95,
  282,  270,  271,  272,  273,  274,  275,   96,   97,  282,
  279,  280,  282,  282,  293,  292,  296,  286,  287,  288,
  257,  257,  291,  292,  296,  294,  282,  282,  297,  256,
  257,  300,  296,  260,  261,  282,  257,  282,  265,  266,
  282,  282,  282,  270,  271,  272,  273,  274,  275,    0,
  279,    0,  279,  280,    0,  282,    0,    0,   66,  286,
  287,  288,  279,   66,  291,  292,  169,  294,   36,   84,
  297,  256,  257,  300,   44,  260,  261,   -1,   -1,   -1,
  265,  266,   -1,   -1,   -1,  270,  271,  272,  273,  274,
  275,   -1,   -1,   -1,  279,   -1,   -1,  282,   -1,   -1,
   -1,  286,  287,  288,   -1,   -1,  291,  292,   -1,  294,
   -1,   -1,  297,  256,  257,  300,   -1,  260,  261,  270,
  271,  272,  273,  274,  275,   -1,   -1,  270,  271,  272,
  273,  274,  275,   -1,   -1,   -1,  279,  280,   -1,  282,
   -1,   -1,   -1,  286,  287,  288,   -1,   -1,  291,  292,
   -1,  294,   -1,   -1,  297,  256,  257,  300,   -1,  260,
  261,   -1,  256,  257,   -1,   -1,  260,  261,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  278,  279,  260,
  261,  282,  256,  257,   -1,  286,  260,  261,   -1,   -1,
  291,   -1,  286,  294,   -1,   -1,  297,  291,   -1,  300,
  294,   -1,   -1,  297,  257,  286,  300,  260,  261,   -1,
  291,   -1,  286,  294,   -1,   -1,  297,  291,   -1,  300,
  294,   -1,   -1,  297,  256,  257,  300,   -1,  260,  261,
   -1,   -1,   -1,  286,   -1,   -1,   -1,  257,  291,  259,
  260,  261,   -1,   -1,  297,   -1,   -1,  300,   -1,   -1,
   -1,   -1,   -1,  257,  286,  259,  260,  261,   -1,  291,
   -1,   -1,  294,   -1,   -1,  297,  286,   -1,  300,  289,
  290,  291,   -1,   -1,   -1,  295,   -1,  297,  298,   -1,
  300,   -1,  286,   -1,   -1,  289,  290,  291,  257,   -1,
   -1,  260,  261,  297,  298,  257,  300,   -1,  260,  261,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,   -1,  286,   -1,   -1,
   -1,   -1,  291,   -1,  286,  294,   -1,  296,  297,  291,
   -1,  300,  294,   -1,   -1,  297,  286,   -1,  300,  289,
  290,  291,   -1,   -1,  294,   -1,   -1,  297,  298,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  286,
   -1,   -1,  289,  290,  291,   -1,   -1,   -1,  295,  286,
  297,  298,  289,  290,  291,   -1,   -1,   -1,  295,  286,
  297,  298,  289,  290,  291,   -1,   -1,   -1,  295,   -1,
  297,  298,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,
  256,  257,   -1,  257,  260,  261,  260,  261,   -1,   -1,
  257,   -1,  286,  260,  261,  289,  290,  291,   -1,   -1,
   -1,   -1,  286,  297,  298,  289,  290,  291,   -1,   -1,
  286,   -1,  286,  297,  298,  291,   -1,  291,  294,  286,
  294,  297,  296,  297,  291,  257,   -1,  294,  260,  261,
  297,   -1,  256,  257,  258,   -1,  260,  261,  262,  263,
  264,   -1,  266,  257,  258,   -1,  260,  261,  262,  263,
  264,   -1,  266,   -1,  286,  279,   -1,   -1,   -1,  291,
   -1,   -1,   -1,   -1,   -1,  297,
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
//#line 799 "Parser.java"
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
//#line 74 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 35:
//#line 77 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 36:
//#line 78 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 37:
//#line 81 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 38:
//#line 82 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 39:
//#line 83 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 40:
//#line 86 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 41:
//#line 87 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 42:
//#line 90 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 43:
//#line 99 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 44:
//#line 104 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                             }
break;
case 46:
//#line 111 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 47:
//#line 112 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 48:
//#line 113 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": La funcion no puede tener mas de un parametro");}
break;
case 49:
//#line 114 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ yyval.ival + ": Falta el parametro de la funcion");}
break;
case 54:
//#line 123 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 55:
//#line 124 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 56:
//#line 125 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 57:
//#line 126 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "IF"); }
break;
case 59:
//#line 128 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 60:
//#line 129 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 61:
//#line 132 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 62:
//#line 133 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 63:
//#line 136 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 64:
//#line 137 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 65:
//#line 138 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta ';' al final de la sentencia"); }
break;
case 66:
//#line 141 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 67:
//#line 142 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 68:
//#line 143 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 69:
//#line 144 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 70:
//#line 145 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 71:
//#line 146 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 72:
//#line 147 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 73:
//#line 148 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 77:
//#line 156 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 79:
//#line 160 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 80:
//#line 161 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 84:
//#line 169 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 85:
//#line 170 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 86:
//#line 171 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 89:
//#line 174 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 90:
//#line 177 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 91:
//#line 180 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 92:
//#line 181 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 93:
//#line 182 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 94:
//#line 183 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 95:
//#line 184 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 96:
//#line 185 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 97:
//#line 186 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 98:
//#line 187 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 102:
//#line 195 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 104:
//#line 199 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 105:
//#line 202 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 106:
//#line 203 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 107:
//#line 204 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 108:
//#line 205 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 109:
//#line 206 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 110:
//#line 207 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 112:
//#line 211 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 113:
//#line 212 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 117:
//#line 220 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 118:
//#line 221 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 119:
//#line 222 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 120:
//#line 223 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 121:
//#line 224 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 122:
//#line 225 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 123:
//#line 226 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 124:
//#line 227 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 125:
//#line 230 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 126:
//#line 231 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 127:
//#line 234 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 128:
//#line 235 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 129:
//#line 236 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 130:
//#line 237 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 131:
//#line 238 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 132:
//#line 239 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 133:
//#line 240 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 134:
//#line 243 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 135:
//#line 244 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 136:
//#line 245 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 137:
//#line 246 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 138:
//#line 247 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 147:
//#line 264 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 148:
//#line 265 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 153:
//#line 272 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta operador u operandos"); }
break;
case 159:
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
case 161:
//#line 323 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 162:
//#line 324 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 163:
//#line 325 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 165:
//#line 329 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 166:
//#line 330 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1467 "Parser.java"
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
