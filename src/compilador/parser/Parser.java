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
   12,    7,   13,   13,   15,   15,   15,   14,   14,   14,
   14,   16,   16,   16,   16,   16,   20,   20,   21,   21,
   21,   18,   18,   18,   18,   18,   18,   18,   18,   26,
   26,   27,   27,   23,   23,   23,   23,   28,   28,    4,
    4,    4,    4,    4,    4,   17,   29,   29,   29,   29,
   29,   29,   29,   29,   32,   32,   34,   34,   25,   25,
   35,   35,   35,   35,   35,   35,   33,   33,   33,   33,
   36,   36,   19,   19,   19,   19,   19,   19,   19,   19,
   30,   30,   22,   22,   22,   22,   22,   22,   22,   37,
   37,   37,   37,   37,   38,   38,   39,   39,   39,   39,
   31,   31,   24,   24,   24,   40,   40,   40,   40,   41,
   41,   41,   42,   42,   42,   42,   43,   43,   43,   44,
   45,
};
final static short yylen[] = {                            2,
    4,    5,    3,    3,    3,    2,    5,    4,    4,    3,
    2,    1,    1,    1,    2,    3,    4,    4,    1,    1,
   10,    9,   11,   11,    9,    9,    8,    1,    1,    1,
    3,    1,    3,    1,    1,    1,    1,    1,    1,    3,
    3,    4,    6,    5,    2,    1,    1,    2,    2,    1,
    1,    1,    1,    1,    1,    1,    2,    2,    5,    6,
    6,    8,    9,    9,    7,    7,    7,    6,    7,    2,
    1,    2,    1,    4,    5,    5,    1,    2,    1,    2,
    3,    3,    1,    1,    1,    3,    8,    9,    9,    7,
    7,    7,    6,    7,    2,    1,    2,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    4,    5,    5,    1,
    2,    1,    5,    5,    6,    6,    6,    6,    4,    4,
    2,    2,    6,    2,    1,    5,    5,    5,    4,    7,
    6,    6,    6,    6,    3,    3,    2,    2,    2,    2,
    3,    1,    4,    3,    1,    3,    3,    1,    1,    3,
    3,    1,    1,    1,    2,    1,    1,    1,    1,    4,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   37,   30,   38,   39,    0,   29,
   28,    0,    0,    0,    0,    0,   12,   13,   14,    0,
    0,   19,   20,   34,    0,   36,    0,    0,   84,    0,
   83,   85,    0,    0,   15,    0,    0,    0,    0,    0,
  157,  158,  159,    0,    0,    0,  153,    0,    0,    0,
    0,  152,  154,  156,    0,    0,    0,    0,    0,    4,
   11,    0,    0,    0,    0,    0,    0,    0,   80,  122,
    0,  110,  121,    0,  124,    0,    3,    0,    0,    0,
    0,  155,    0,    0,  103,  104,  101,  102,  105,  106,
    0,    0,    0,    0,    0,    0,    0,    8,    0,    0,
    0,    0,    0,    0,    0,   32,    0,    0,    0,    0,
    0,    0,   16,  149,  142,    0,   33,    0,   40,    0,
    0,   51,    0,   50,   52,   53,   54,   55,   56,    0,
   81,   82,  112,    0,    0,    0,    0,    0,    0,  135,
  136,    0,  161,    0,    0,    0,  144,    0,   99,    0,
    0,    0,    0,    0,  150,  151,    7,    2,  120,  119,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   46,
    0,   17,   18,    0,    0,    0,    0,   42,   49,   48,
   58,    0,   77,   57,    0,  111,    0,    0,    0,    0,
    0,    0,  160,    0,    0,  143,    0,    0,    0,   95,
    0,  113,    0,  114,    0,    0,   31,    0,    0,    0,
   45,   44,  141,    0,    0,    0,    0,   79,    0,    0,
  107,  127,    0,  128,    0,    0,    0,  134,  133,  131,
    0,    0,    0,    0,   93,   97,  115,  117,  116,  118,
    0,    0,    0,    0,   43,    0,    0,    0,    0,    0,
    0,    0,   78,  108,  109,  123,  130,  137,  138,  140,
  139,   94,   90,    0,   91,   92,    0,    0,    0,    0,
    0,    0,    0,    0,   70,    0,    0,   59,    0,   74,
    0,   87,    0,   27,    0,    0,    0,    0,    0,    0,
    0,   72,   68,   60,   61,   75,   76,   88,   89,   26,
   22,    0,   25,   69,   65,    0,   66,   67,    0,   21,
    0,   62,   23,   24,   63,   64,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   19,   20,   21,   22,   23,  107,   24,
   25,   26,   27,  123,  171,  183,   28,  126,   29,  128,
  129,   30,  249,   48,   49,  250,  275,  219,   31,   32,
  116,  151,  152,  200,   91,  134,   33,   38,  228,   50,
   51,   52,   53,   54,  144,
};
final static short yysindex[] = {                      -233,
  573,  646,    0, -247,    0,    0,    0,    0, -229,    0,
    0, -121,  656, -173, -119,  583,    0,    0,    0, -246,
  -79,    0,    0,    0, -167,    0, -147, -231,    0,  695,
    0,    0,  258,  593,    0,   37, -106,  -12,    0, -109,
    0,    0,    0,  159,   47,  -56,    0,  310, -256, -156,
  199,    0,    0,    0, -255,  636, -172, -113,    6,    0,
    0, -228, -234,   77,   37,   37,  523,  -89,    0,    0,
   60,    0,    0,   47,    0,   -2,    0, -184,   64,   47,
   77,    0,   22,  751,    0,    0,    0,    0,    0,    0,
   77,  -93,  709,  714,  714,  714,  714,    0,   33,   16,
   30,   34,   43,   57, -128,    0,  -55, -128,  -32,   96,
 -157,   36,    0,    0,    0,   65,    0, -167,    0,   24,
  114,    0,  507,    0,    0,    0,    0,    0,    0,  434,
    0,    0,    0,  -65,  117,   47,  121,   47,  120,    0,
    0,  134,    0,  140,  145,  709,    0,  -63,    0,  709,
  149,  148,  199,  199,    0,    0,    0,    0,    0,    0,
 -230, -226, -238,  174, -128,   20,  176, -157,   37,    0,
  182,    0,    0,   77,   47,   27,   77,    0,    0,    0,
    0,  472,    0,    0, -169,    0,  189,  207,  211,   89,
  224,  224,    0,  702,  213,    0,  217,  245,  709,    0,
   74,    0,  119,    0,   37,   37,    0,  230,   37,  252,
    0,    0,    0,   63,  243,  531,  272,    0,  -82,  164,
    0,    0,  275,    0,  224,  234,  279,    0,    0,    0,
  290, -249,  294,  304,    0,    0,    0,    0,    0,    0,
  -83,  -35,   37,  -33,    0,  266,  531,  531,  301,  291,
 -159, -122,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -112,    0,    0, -176,  338,   67,  339,
  -23,  303,  305,  531,    0,  320,  165,    0,  169,    0,
  200,    0,  321,    0,  335,  364,  340,  343, -170,  344,
  345,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -108,    0,    0,    0,  -58,    0,    0,  209,    0,
  210,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  630,    0,    0,    0,    0,
    0,    0,    0,    0,   93,    0,    0,    0,    0,    0,
    0,    0,  452,    0,    0,    0,    0,    0,  363,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  318,
  183,    0,    0,    0,  631,  632,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  639,  640,
  -38,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  387,    0,  138,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -131,  228,  273,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  362,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  462,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -39,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  479,    0,  410,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -29,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -13,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  276,   59,  -54,  -26,  -40,  -20,    0,    0,  -50,  155,
  586,  587,    0,    0,  488,  -61,  -66,    0,  -64,    0,
    0,  -60, -125,  -47,  -37, -152,    0,    0,    0,    0,
    0, -134,  -28,    0,    0,    0,    0,  622, -171,  575,
  455,  460,  617,    0,    0,
};
final static int YYTABLESIZE=1030;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         63,
  125,   73,  127,   72,  184,  124,  130,   83,  109,  104,
    5,  195,  122,    7,    8,  197,  115,  106,  106,  229,
  230,  112,   92,    1,   68,  201,   35,    5,  110,  203,
    7,    8,  263,  143,   35,   93,  135,  205,  137,   98,
  139,  165,  142,  149,  133,   65,  264,  113,   36,  111,
   69,  202,   62,  257,  163,  204,  125,  166,  127,  232,
    2,  180,  130,  125,  106,  127,   72,  106,  179,  130,
  169,   39,    5,   40,   61,    7,    8,   41,   42,   43,
  283,   44,  176,  101,    5,   40,  220,    7,    8,   41,
   42,   43,   61,   44,  272,  273,  277,  138,  188,    5,
  190,    6,    7,    8,   57,  284,  102,  186,   94,   95,
  103,  305,  221,   66,   61,  125,   46,  127,  289,   72,
  218,  130,  278,   72,  207,  306,  213,  169,   46,  217,
    6,   10,   11,  279,   39,    5,   40,  214,    7,    8,
   41,   42,   43,  281,   44,    6,   67,  309,  292,  125,
   96,  127,  125,   58,  127,  130,   45,  253,  130,  280,
   10,   11,   79,   37,   96,   59,   47,   72,   81,  282,
  236,  105,   72,  310,    5,   10,   11,    7,    8,   46,
  125,  125,  127,  127,  241,  242,  130,  130,  244,   64,
   37,    5,  131,  267,    7,    8,   65,  311,  150,   47,
   65,   94,   95,    9,  125,  132,  127,  125,  120,  127,
  130,   47,  252,  130,   14,  196,  164,  121,   47,  117,
    9,   84,  269,  312,  165,   12,  149,  149,   47,  185,
   47,   14,   47,    5,   47,   47,    7,    8,   47,  167,
  149,  268,   98,  270,   65,   47,   65,  165,   47,   47,
   47,   47,   71,   39,    5,   40,   98,    7,    8,   41,
   42,   43,    9,   44,    6,  170,   71,  120,   73,   80,
  182,  158,  288,   14,   47,  136,  121,   34,  108,   39,
    5,   40,   73,    7,    8,   41,   42,   43,   56,   44,
   47,  208,   47,    5,   10,   11,    7,    8,   46,  165,
  145,  175,   39,    5,   40,  215,    7,    8,   41,   42,
   43,  159,   44,  146,   35,  160,    5,  172,  216,    7,
    8,  161,  170,  211,   46,  140,  141,  157,   47,   47,
  173,   47,  114,    5,   40,  162,    7,    8,   41,   42,
   43,  246,   44,  286,  174,    9,   65,   46,   35,   35,
   12,   35,   35,   35,  247,  237,   14,   35,   35,   35,
   35,   35,   35,   35,   35,   35,   35,   35,  238,   35,
  225,   35,   35,  168,   35,  226,  227,   46,   35,   35,
   35,   35,   35,   35,   35,   35,   35,   35,   35,   35,
   35,  177,   35,   41,   41,  187,   41,   41,   41,  189,
  239,  191,   41,   41,   41,   41,   41,   41,   41,   41,
   41,   41,   41,  240,   41,  192,   41,   41,  193,   41,
   41,   42,   43,   41,   41,   41,   41,   41,   41,   41,
   41,   41,   41,   41,   41,   41,  194,   41,  148,  148,
  199,  148,  148,  148,  198,  254,  294,  148,  148,  206,
  296,  209,  148,  148,  148,  148,  148,  148,  255,  295,
  212,  148,  148,  297,  148,   96,   97,  222,  148,  148,
  148,  148,  148,  148,  148,  148,  148,  148,  148,  148,
  148,  298,  148,  146,  146,  223,  146,  146,  146,  224,
  313,  315,  146,  146,  299,  258,  259,  146,  146,  146,
  146,  146,  146,  314,  316,  243,  146,  146,  233,  146,
  226,  227,  234,  146,  146,  146,  146,  146,  146,  146,
  146,  146,  146,  146,  146,  146,  235,  146,  147,  147,
  245,  147,  147,  147,  248,   74,   75,  147,  147,   76,
  260,  261,  147,  147,  147,  147,  147,  147,  153,  154,
  251,  147,  147,  256,  147,  155,  156,  271,  147,  147,
  147,  147,  147,  147,  147,  147,  147,  147,  147,  147,
  147,  262,  147,  145,  145,  265,  145,  145,  145,   85,
   86,   87,   88,   89,   90,  266,  276,  145,  145,  145,
  145,  145,  145,  274,  285,  287,  145,  145,  290,  145,
  291,  293,  300,  145,  145,  145,  145,  145,  145,  145,
  145,  145,  145,  145,  145,  145,  301,  145,  100,  100,
  302,  303,  100,  100,  304,  307,  308,  149,  149,    6,
   10,    5,  149,  149,  149,  149,  149,  149,    9,    1,
   47,  100,   86,   86,  100,   86,   86,   86,  100,  100,
  100,  118,  119,  100,  100,  210,  100,   78,  148,  100,
   82,    0,  100,    0,    0,  132,  132,    0,   86,  132,
  132,    0,   86,    0,    0,   86,   86,   86,    0,   86,
    0,   86,   86,   86,   86,    0,   86,  132,  132,  181,
    5,  132,    0,    7,    8,  132,    0,    0,    0,    0,
  132,    0,    0,  132,    0,    0,  132,  125,  125,  132,
    0,  125,  125,    0,    0,    0,    0,  129,  129,    9,
    0,  129,  129,    0,  120,    0,    0,  182,    5,    0,
   14,    7,    8,  121,  126,  126,    0,  125,  126,  126,
    0,    0,  125,    0,    0,  125,    0,  129,  125,    0,
    0,  125,  129,    0,    0,  129,    0,    9,  129,    0,
    0,  129,  120,    5,  126,    6,    7,    8,   14,  126,
    0,  121,  126,    0,    0,  126,    0,    0,  126,    5,
    0,    6,    7,    8,    0,    0,    0,    5,    0,    0,
    7,    8,    9,    0,    0,   10,   11,  120,    0,    0,
    0,  178,    0,   14,   15,    0,  121,    0,    9,    0,
    0,   10,   11,  120,    0,    0,    9,    0,    0,   14,
   15,  120,  121,    0,  182,    0,    0,   14,    4,    5,
  121,    6,    7,    8,    0,    0,    0,    0,    4,    5,
    0,    6,    7,    8,    0,    0,    0,    0,    4,    5,
    0,    6,    7,    8,    0,    0,    0,    0,    9,    0,
    0,   10,   11,   12,    0,    0,   13,    0,    9,   14,
   15,   10,   11,   12,    0,    0,    0,   60,    9,   14,
   15,   10,   11,   12,    0,    0,    0,   77,    0,   14,
   15,   99,    5,    0,    6,    7,    8,    0,    0,    0,
    0,    4,    5,    0,    6,    7,    8,    0,    0,    0,
    0,   55,    5,    0,    6,    7,    8,    0,    0,    0,
    0,    9,    0,    0,   10,   11,   12,    0,    0,    0,
  100,    9,   14,   15,   10,   11,   12,    0,    0,    0,
    0,    9,   14,   15,   10,   11,   12,    0,    0,    0,
   70,    5,   14,   15,    7,    8,    0,    0,    5,    0,
    0,    7,    8,    0,    0,    5,    0,    0,    7,    8,
    5,   40,    0,    7,    8,   41,   42,   43,    0,   44,
    9,    0,    0,    0,    0,   12,    0,    9,   71,    0,
    0,   14,   12,    0,    9,   71,    0,  231,   14,   12,
    0,    0,   71,    0,    0,   14,  114,    5,   40,    0,
    7,    8,   41,   42,   43,    0,   44,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  147,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
   67,   30,   67,   30,  130,   67,   67,   45,   59,   57,
  257,  146,   67,  260,  261,  150,   64,   58,   59,  191,
  192,  256,  279,  257,  256,  256,  282,  257,  257,  256,
  260,  261,  282,   81,  282,  292,   74,  276,   76,  295,
   78,  280,   80,   91,   71,  280,  296,  282,  278,  278,
  282,  282,  299,  225,  105,  282,  123,  108,  123,  194,
  294,  123,  123,  130,  105,  130,   93,  108,  123,  130,
  111,  256,  257,  258,   16,  260,  261,  262,  263,  264,
  257,  266,  120,  256,  257,  258,  256,  260,  261,  262,
  263,  264,   34,  266,  247,  248,  256,  282,  136,  257,
  138,  259,  260,  261,  278,  282,  279,  134,  265,  266,
  283,  282,  282,  281,   56,  182,  301,  182,  271,  146,
  182,  182,  282,  150,  165,  296,  174,  168,  301,  177,
  259,  289,  290,  256,  256,  257,  258,  175,  260,  261,
  262,  263,  264,  256,  266,  259,  294,  256,  274,  216,
  282,  216,  219,  273,  219,  216,  278,  219,  219,  282,
  289,  290,  269,    9,  296,  285,   12,  194,  278,  282,
  199,  285,  199,  282,  257,  289,  290,  260,  261,  301,
  247,  248,  247,  248,  205,  206,  247,  248,  209,  269,
   36,  257,  282,  277,  260,  261,  280,  256,  292,   45,
  280,  265,  266,  286,  271,  295,  271,  274,  291,  274,
  271,   57,  295,  274,  297,  279,  272,  300,   64,   65,
  286,  278,  243,  282,  280,  291,  265,  266,   74,  295,
   76,  297,   78,  257,   80,   81,  260,  261,   84,  272,
  279,  277,  282,  277,  280,   91,  280,  280,   94,   95,
   96,   97,  282,  256,  257,  258,  296,  260,  261,  262,
  263,  264,  286,  266,  259,  111,  296,  291,  282,  282,
  294,  256,  296,  297,  120,  278,  300,    2,  273,  256,
  257,  258,  296,  260,  261,  262,  263,  264,   13,  266,
  136,  272,  138,  257,  289,  290,  260,  261,  301,  280,
  279,  278,  256,  257,  258,  279,  260,  261,  262,  263,
  264,  282,  266,  292,  282,  282,  257,  282,  292,  260,
  261,  279,  168,  169,  301,  262,  263,  295,  174,  175,
  295,  177,  256,  257,  258,  279,  260,  261,  262,  263,
  264,  279,  266,  277,  280,  286,  280,  301,  256,  257,
  291,  259,  260,  261,  292,  282,  297,  265,  266,  267,
  268,  269,  270,  271,  272,  273,  274,  275,  295,  277,
  282,  279,  280,  278,  282,  287,  288,  301,  286,  287,
  288,  289,  290,  291,  292,  293,  294,  295,  296,  297,
  298,  278,  300,  256,  257,  279,  259,  260,  261,  279,
  282,  282,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,  295,  277,  282,  279,  280,  279,  282,
  262,  263,  264,  286,  287,  288,  289,  290,  291,  292,
  293,  294,  295,  296,  297,  298,  292,  300,  256,  257,
  293,  259,  260,  261,  296,  282,  282,  265,  266,  276,
  282,  276,  270,  271,  272,  273,  274,  275,  295,  295,
  279,  279,  280,  295,  282,  267,  268,  279,  286,  287,
  288,  289,  290,  291,  292,  293,  294,  295,  296,  297,
  298,  282,  300,  256,  257,  279,  259,  260,  261,  279,
  282,  282,  265,  266,  295,  262,  263,  270,  271,  272,
  273,  274,  275,  295,  295,  276,  279,  280,  296,  282,
  287,  288,  296,  286,  287,  288,  289,  290,  291,  292,
  293,  294,  295,  296,  297,  298,  282,  300,  256,  257,
  279,  259,  260,  261,  292,  278,  279,  265,  266,  282,
  262,  263,  270,  271,  272,  273,  274,  275,   94,   95,
  279,  279,  280,  279,  282,   96,   97,  292,  286,  287,
  288,  289,  290,  291,  292,  293,  294,  295,  296,  297,
  298,  282,  300,  256,  257,  282,  259,  260,  261,  270,
  271,  272,  273,  274,  275,  282,  296,  270,  271,  272,
  273,  274,  275,  293,  257,  257,  279,  280,  296,  282,
  296,  282,  282,  286,  287,  288,  289,  290,  291,  292,
  293,  294,  295,  296,  297,  298,  282,  300,  256,  257,
  257,  282,  260,  261,  282,  282,  282,  265,  266,    0,
    0,    0,  270,  271,  272,  273,  274,  275,    0,    0,
  279,  279,  256,  257,  282,  259,  260,  261,  286,  287,
  288,   66,   66,  291,  292,  168,  294,   36,   84,  297,
   44,   -1,  300,   -1,   -1,  256,  257,   -1,  282,  260,
  261,   -1,  286,   -1,   -1,  289,  290,  291,   -1,  293,
   -1,  295,  296,  297,  298,   -1,  300,  278,  279,  256,
  257,  282,   -1,  260,  261,  286,   -1,   -1,   -1,   -1,
  291,   -1,   -1,  294,   -1,   -1,  297,  256,  257,  300,
   -1,  260,  261,   -1,   -1,   -1,   -1,  256,  257,  286,
   -1,  260,  261,   -1,  291,   -1,   -1,  294,  257,   -1,
  297,  260,  261,  300,  256,  257,   -1,  286,  260,  261,
   -1,   -1,  291,   -1,   -1,  294,   -1,  286,  297,   -1,
   -1,  300,  291,   -1,   -1,  294,   -1,  286,  297,   -1,
   -1,  300,  291,  257,  286,  259,  260,  261,  297,  291,
   -1,  300,  294,   -1,   -1,  297,   -1,   -1,  300,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,  257,   -1,   -1,
  260,  261,  286,   -1,   -1,  289,  290,  291,   -1,   -1,
   -1,  295,   -1,  297,  298,   -1,  300,   -1,  286,   -1,
   -1,  289,  290,  291,   -1,   -1,  286,   -1,   -1,  297,
  298,  291,  300,   -1,  294,   -1,   -1,  297,  256,  257,
  300,  259,  260,  261,   -1,   -1,   -1,   -1,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  286,   -1,
   -1,  289,  290,  291,   -1,   -1,  294,   -1,  286,  297,
  298,  289,  290,  291,   -1,   -1,   -1,  295,  286,  297,
  298,  289,  290,  291,   -1,   -1,   -1,  295,   -1,  297,
  298,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,  286,   -1,   -1,  289,  290,  291,   -1,   -1,   -1,
  295,  286,  297,  298,  289,  290,  291,   -1,   -1,   -1,
   -1,  286,  297,  298,  289,  290,  291,   -1,   -1,   -1,
  256,  257,  297,  298,  260,  261,   -1,   -1,  257,   -1,
   -1,  260,  261,   -1,   -1,  257,   -1,   -1,  260,  261,
  257,  258,   -1,  260,  261,  262,  263,  264,   -1,  266,
  286,   -1,   -1,   -1,   -1,  291,   -1,  286,  294,   -1,
   -1,  297,  291,   -1,  286,  294,   -1,  296,  297,  291,
   -1,   -1,  294,   -1,   -1,  297,  256,  257,  258,   -1,
  260,  261,  262,  263,  264,   -1,  266,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  279,
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
"cuerpo_funcion : cuerpo_funcion sentencia_ejecutable_en_funcion",
"cuerpo_funcion : cuerpo_funcion sentencia_declarativa",
"cuerpo_funcion : sentencia_ejecutable_en_funcion",
"cuerpo_funcion : sentencia_declarativa",
"sentencia_ejecutable_en_funcion : sentencia_asignacion",
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
"parametro_real : expresion",
};

//#line 332 "gramatica.y"
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
//#line 792 "Parser.java"
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
case 53:
//#line 122 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "IF"); }
break;
case 55:
//#line 124 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 56:
//#line 125 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 57:
//#line 128 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 58:
//#line 129 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 59:
//#line 132 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 60:
//#line 133 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 61:
//#line 134 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta ';' al final de la sentencia"); }
break;
case 62:
//#line 137 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 63:
//#line 138 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 64:
//#line 139 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 65:
//#line 140 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 66:
//#line 141 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 67:
//#line 142 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 68:
//#line 143 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 69:
//#line 144 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 73:
//#line 152 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 75:
//#line 156 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 76:
//#line 157 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 80:
//#line 165 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 81:
//#line 166 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 82:
//#line 167 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 85:
//#line 170 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 86:
//#line 173 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 87:
//#line 176 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 88:
//#line 177 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 89:
//#line 178 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 90:
//#line 179 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 91:
//#line 180 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 92:
//#line 181 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 93:
//#line 182 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 94:
//#line 183 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 98:
//#line 191 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 100:
//#line 195 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 101:
//#line 198 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 102:
//#line 199 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 103:
//#line 200 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 104:
//#line 201 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 105:
//#line 202 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 106:
//#line 203 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 108:
//#line 207 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 109:
//#line 208 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 113:
//#line 216 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 114:
//#line 217 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 115:
//#line 218 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 116:
//#line 219 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 117:
//#line 220 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 118:
//#line 221 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 119:
//#line 222 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 120:
//#line 223 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 121:
//#line 226 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 122:
//#line 227 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 123:
//#line 230 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 124:
//#line 231 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 125:
//#line 232 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 126:
//#line 233 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 127:
//#line 234 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 128:
//#line 235 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 129:
//#line 236 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 130:
//#line 239 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 131:
//#line 240 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 132:
//#line 241 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 133:
//#line 242 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 134:
//#line 243 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 144:
//#line 261 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 149:
//#line 268 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta operador u operandos"); }
break;
case 155:
//#line 278 "gramatica.y"
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
case 157:
//#line 319 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 158:
//#line 320 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 159:
//#line 321 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
//#line 1428 "Parser.java"
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
