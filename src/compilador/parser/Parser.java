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
    1,    1,    1,    8,   12,   12,   14,   14,   14,   14,
   14,   13,   13,   13,   13,   15,   15,   15,   15,   15,
   15,   15,   19,   19,   20,   20,   20,   17,   17,   17,
   17,   17,   17,   17,   17,   17,   25,   25,   26,   26,
   22,   22,   22,   22,   27,   27,    5,    5,    5,    5,
    5,    5,   16,   28,   28,   28,   28,   28,   28,   28,
   28,   28,   31,   31,   33,   33,   24,   24,   34,   34,
   34,   34,   34,   34,   32,   32,   32,   32,   32,   35,
   35,   18,   18,   18,   18,   18,   18,   18,   18,   29,
   29,   21,   21,   21,   21,   21,   21,   21,   36,   36,
   36,   36,   36,   37,   38,   38,   38,   30,   30,   23,
   23,   23,   23,   40,   40,   40,   41,   41,   41,   41,
   41,   39,   39,   42,   42,   42,   42,   42,   42,   43,
   43,   43,
};
final static short yylen[] = {                            2,
    4,    5,    5,    3,    3,    3,    2,    5,    4,    4,
    3,    5,    2,    1,    1,    1,    2,    3,    4,    4,
    1,    1,   10,    9,   11,   11,    9,    8,    9,    9,
    1,    1,    1,    3,    1,    3,    2,    1,    1,    3,
    1,    1,    1,    4,    6,    5,    2,    1,    1,    3,
    1,    2,    2,    1,    1,    2,    3,    3,    1,    1,
    1,    1,    2,    2,    5,    6,    6,    8,    9,    7,
    9,    7,    7,    7,    6,    7,    2,    1,    2,    1,
    4,    5,    5,    1,    2,    1,    2,    3,    3,    1,
    1,    1,    3,    8,    9,    7,    9,    7,    7,    7,
    6,    7,    2,    1,    2,    1,    3,    2,    1,    1,
    1,    1,    1,    1,    4,    5,    3,    5,    1,    2,
    1,    5,    5,    6,    6,    6,    6,    4,    4,    2,
    2,    6,    2,    1,    5,    5,    5,    4,    7,    6,
    6,    6,    6,    3,    2,    2,    1,    3,    1,    3,
    3,    1,    1,    3,    3,    1,    1,    1,    4,    3,
    1,    1,    1,    1,    2,    1,    1,    2,    2,    4,
    3,    5,
};
final static short yydefred[] = {                         0,
   41,   42,   43,    0,    0,    0,    0,   33,    0,   32,
   31,    0,    0,    0,   39,    0,   14,   15,   16,    0,
    0,   21,   22,    0,    0,    0,   91,    0,   90,   92,
    0,    0,    0,    0,   17,    0,    0,    0,  153,    0,
  162,  163,  166,    0,    0,  167,    0,    0,    0,    0,
  164,    0,  156,  158,  161,    0,    0,    0,    4,   13,
    0,    0,    0,    0,    0,    0,    0,    0,   87,  131,
    0,  119,  130,    0,  133,    0,    0,    0,    0,    0,
    5,    0,    0,    0,    0,  168,  169,  165,    0,    0,
  108,    0,    0,  111,  112,  109,  110,  113,  114,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   35,    0,
    0,    0,    0,    0,    0,   18,    0,    0,    0,    0,
    0,    0,   55,    0,   54,    0,   59,   60,   61,   62,
    0,   88,   89,    0,  121,    0,    0,    0,    0,    0,
    9,    0,    0,    0,    0,    0,  144,    0,  171,    0,
    0,    0,  160,    0,    0,    0,    0,    0,    0,    0,
  154,  155,  129,  128,    0,    0,    0,    0,    0,    0,
    0,    0,   51,    0,    0,    0,   19,   20,    0,    0,
    0,    0,   44,   53,   52,    0,   56,   64,    0,   84,
   63,  117,    0,  120,    0,    0,    0,    3,    8,    2,
   12,    0,    0,    0,    0,  170,    0,    0,    0,  159,
    0,    0,    0,  103,    0,  122,    0,  123,    0,   34,
    0,    0,    0,    0,    0,    0,   46,    0,    0,    0,
    0,    0,   57,   58,   86,    0,    0,  115,  136,    0,
  137,    0,    0,    0,  143,  147,  142,  140,  172,    0,
    0,    0,    0,    0,  101,  105,  124,  126,  125,  127,
    0,    0,    0,    0,    0,   45,   50,    0,    0,    0,
    0,    0,    0,    0,   85,  116,  118,  132,  139,  145,
  146,  102,   98,    0,   96,   99,  100,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   77,    0,    0,
   65,    0,   81,    0,   94,    0,    0,    0,    0,   28,
    0,    0,    0,    0,    0,   79,   75,   66,   67,   82,
   83,   95,   97,   24,    0,   30,   29,   27,   76,   72,
    0,   70,   73,   74,    0,   23,    0,   68,   25,   26,
   69,   71,
};
final static short yydgoto[] = {                          5,
   15,   16,   17,   18,   19,   20,   21,   22,   23,  110,
   24,   25,  124,  176,  190,   26,  127,   27,  129,  130,
   28,  271,   49,   50,  272,  298,  236,   29,   30,  118,
  159,  160,  214,  100,  136,   31,   38,  245,   51,   52,
   53,   54,   55,
};
final static short yysindex[] = {                      -223,
    0,    0,    0,  679,    0,  563, -242,    0,  254,    0,
    0,    1, -220,  -71,    0,  573,    0,    0,    0, -212,
  407,    0,    0, -193, -178, -236,    0,   31,    0,    0,
    4,  -38,  689,  553,    0,  106,  178, -163,    0, -152,
    0,    0,    0,   91,  -24,    0, -114, -193,  838, -268,
    0,  -47,    0,    0,    0, -107, -189, -145,    0,    0,
  -95,  794,  -24,  106, -193,  106,  464, -260,    0,    0,
  119,    0,    0,  -24,    0,   12,  679, -184,  616,  679,
    0,   37,  -14,  -24,   48,    0,    0,    0,  -81,   73,
    0,  389,  389,    0,    0,    0,    0,    0,    0,  -24,
 -117,  706,  389,  389,  -63,  -36,  -49,  -69,    0, -241,
 -189,  172,  -28,  745,  -29,    0,   50,    9, -193, -193,
   84,    3,    0,  444,    0, -200,    0,    0,    0,    0,
  380,    0,    0,   36,    0,  530,   47,  -24,   64,  626,
    0,  -11,  105,  636,  -24,   82,    0,   87,    0,  216,
  697,  706,    0,   93,  -47,  -47,   50,  706,   75,  109,
    0,    0,    0,    0, -159, -157,  123, -189,  218,  130,
  106,  745,    0,  106, -193,  143,    0,    0,  -24,  -24,
  -66,  -24,    0,    0,    0,   25,    0,    0,  508,    0,
    0,    0, -135,    0,  179,  185,  195,    0,    0,    0,
    0,  449, -154, -154,  204,    0,  699,  193,  210,    0,
  220,  243,  706,    0,   78,    0,  122,    0,  106,    0,
  224,  106,  106,  779,  249, -230,    0,   50,  177,  239,
  338,  174,    0,    0,    0,  506,  191,    0,    0,  257,
    0, -154,  -14,  -14,    0,    0,    0,    0,    0,  260,
 -186,  266,  274,  276,    0,    0,    0,    0,    0,    0,
  801,  106,  803,  812,  310,    0,    0,  486,  338,  338,
  281,  244, -124, -121,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -96,    0,    0,    0,  322,  825,  325,
  327,  303,  488,  294,  300,  301,  338,    0,  318,  206,
    0,  248,    0,  271,    0,  319, -165,  320,  321,    0,
  324,   69,  329,  330,  332,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -90,    0,    0,    0,    0,    0,
  -89,    0,    0,    0,  275,    0,  283,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  751,    0,    0,    0,    0,    0,    0,
  400,    0,    0,  607,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  163,    0,    0,
    0,  205,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  761,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  615,  620,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  258,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -252,  -57,  788,  121,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  621,  633,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  247,  289,  331,    0,    0,   74,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  337,  364,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  402,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  115,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  365,    0, -239,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  422,
    0,  348,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  145,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  150,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  654,   27,   -2,  -44,  -12,  -50,  -19,    0,    0,  -48,
   -9,    0,    0,  493,  -58,  -62,    0,  -52,    0,    0,
  -46, -129,   30,  -32,  -88,    0,    0,    0,    0,    0,
 -133,  -22,    0,    0,    0,    0,  634, -101,  -15,  231,
  469,    0,    0,
};
final static int YYTABLESIZE=1113;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   62,  191,   48,  149,  126,   73,  109,  109,  125,  112,
  101,   65,   89,   60,  128,   72,  148,  208,  209,   68,
  131,  132,  123,  102,  211,  267,   37,  149,   88,  149,
  167,   60,   34,    1,  133,   48,    2,    3,  168,   35,
  148,  137,  148,  139,    1,   69,   48,    2,    3,  146,
   66,  148,   65,   48,  119,  186,  120,   56,  135,   79,
  109,  126,  169,  174,   48,  185,   48,  147,  126,    8,
    4,  128,   48,  251,   48,   48,   60,  131,  128,  184,
   48,  187,   48,   48,  131,  108,   61,   66,  181,   72,
   48,  325,  117,   48,   48,  283,  215,   35,  217,   10,
   11,  247,  248,  140,  175,  196,  144,   41,   42,  284,
  141,   48,  202,    8,  150,   67,  326,  220,   84,  154,
  237,  174,  216,  194,  218,   85,  126,  111,   48,  157,
  235,  300,  243,  244,  302,   48,  128,   60,   72,   72,
  279,   60,  131,   10,   11,   72,  238,  229,  105,    1,
   40,  224,    2,    3,   41,   42,   43,  301,   44,  304,
  303,  113,  175,   90,  226,  335,  337,  316,  126,   48,
   48,  106,   48,  126,  158,  107,   46,  275,  128,  294,
  295,  296,  114,  128,  131,  305,  246,  246,  246,  131,
  256,  336,  338,   47,   72,   92,   93,  151,   93,  261,
   72,   57,  263,  264,  312,  126,  126,  126,  228,  166,
  152,  232,  230,   58,   65,  128,  128,  128,  163,  103,
  104,  131,  131,  131,   93,  231,  246,  280,  281,  165,
  126,   39,    1,   40,  126,    2,    3,   41,   42,   43,
  128,   44,  289,   35,  128,  164,  131,   41,   42,  172,
  131,   65,  177,   65,   65,   77,   39,    1,   40,   46,
    2,    3,   41,   42,   43,  178,   44,   39,    1,   40,
   35,    2,    3,   41,   42,   43,   47,   44,   45,   65,
  182,   74,   75,  199,   46,   76,   70,    1,  179,  138,
    2,    3,   39,    1,   40,   46,    2,    3,   41,   42,
   43,   47,   44,   39,    1,   40,  233,    2,    3,   41,
   42,   43,   47,   44,   92,   93,    9,  192,  145,  234,
   46,   12,  155,  156,   71,  195,  149,   13,   39,    1,
   40,   46,    2,    3,   41,   42,   43,   47,   44,   39,
    1,   40,  197,    2,    3,   41,   42,   43,   47,   44,
  330,  153,   41,   42,   86,  104,   46,   92,   93,  257,
  200,  180,    1,  203,  331,    2,    3,   46,  204,  104,
  212,  210,  258,   47,   87,    1,   40,   40,    2,    3,
   40,   40,   40,   40,   47,   40,   40,   40,   40,   40,
   40,   40,   40,   40,   40,   40,  106,   40,  219,   40,
   40,  213,   40,  259,    9,  223,   40,   40,   40,   12,
  106,   40,   40,  134,   40,   13,  260,   40,  157,  157,
   40,  227,  157,  157,  157,  157,   78,  157,  157,  157,
  157,   80,  157,  157,  157,  157,  157,  157,   92,   93,
   78,  157,  157,  170,  157,   80,   83,  171,  157,  157,
  157,  168,  273,  157,  157,  268,  157,  239,   66,  157,
  152,  152,  157,  240,  152,  152,  152,  152,  269,  152,
  152,  205,  276,  241,  152,  152,  152,  152,  152,  152,
   92,   93,  249,  152,  152,  277,  152,  318,  252,  221,
  152,  152,  152,  222,  206,  152,  152,  168,  152,  262,
  319,  152,  150,  150,  152,  253,  150,  150,  150,  150,
    1,  150,  150,    2,    3,  254,  150,  150,  150,  150,
  150,  150,  153,  153,  255,  150,  150,  266,  150,  320,
  270,   36,  150,  150,  150,  278,  153,  150,  150,  299,
  150,  282,  321,  150,  151,  151,  150,  285,  151,  151,
  151,  151,  322,  151,  151,  286,  339,  287,  151,  151,
  151,  151,  151,  151,  341,  323,  292,  151,  151,  340,
  151,  161,  162,  297,  151,  151,  151,  342,  306,  151,
  151,  308,  151,  309,  310,  151,  107,  107,  151,  313,
  107,  107,  107,  107,    1,  314,  315,    2,    3,  317,
  324,  327,  328,  141,  141,  329,    7,  141,  141,  107,
  332,  333,  107,  334,   11,   49,  107,  107,  107,    6,
   10,  107,  107,    9,  107,  141,  141,  107,  121,  141,
  107,  189,    1,  141,   13,  188,    1,  122,  141,    2,
    3,  141,   48,   47,  141,    1,   40,  141,    2,    3,
   41,   42,   43,    6,   44,  134,  134,  138,  138,  134,
  134,  138,  138,    1,  225,    9,    2,    3,    0,   82,
  121,    0,   46,  189,    0,   63,   13,  135,  135,  122,
    0,  135,  135,    0,    0,  134,   64,  138,    0,   47,
  134,    0,  138,  134,    0,  138,  134,    0,  138,  134,
    1,  138,    8,    2,    3,    0,    0,  135,    0,    0,
   41,   42,  135,    0,    0,  135,    0,    0,  135,    0,
    1,  135,    8,    2,    3,    0,    0,    0,    0,    9,
  242,    0,   10,   11,  121,  243,  244,    0,  183,    0,
   13,   14,    1,  122,    1,    2,    3,    2,    3,    9,
    0,    0,   10,   11,  121,    0,    0,    0,    0,    0,
   13,   14,    1,  122,    1,    2,    3,    2,    3,    0,
    0,    9,    0,    9,    0,    0,  121,  293,  121,  189,
    0,  189,   13,  311,   13,  122,    1,  122,    0,    2,
    3,    9,    0,    9,    0,    0,  121,    0,  121,    0,
  274,    0,   13,    0,   13,  122,    0,  122,    7,    1,
    0,    8,    2,    3,    0,    9,    0,    0,   32,    1,
   12,    8,    2,    3,  193,    0,   13,    0,    7,    1,
    0,    8,    2,    3,    0,    0,    0,    0,    9,    0,
    0,   10,   11,   12,    0,    0,   80,   81,    9,   13,
   14,   10,   11,   12,    0,    0,   33,    0,    9,   13,
   14,   10,   11,   12,    0,    0,    0,   59,    0,   13,
   14,  142,    1,    0,    8,    2,    3,    0,    0,    0,
    0,    7,    1,    0,    8,    2,    3,    0,    0,    0,
    0,    7,    1,    0,    8,    2,    3,    0,    0,    0,
    0,    9,    0,    0,   10,   11,   12,    0,    0,    0,
  143,    9,   13,   14,   10,   11,   12,    0,    0,    0,
  198,    9,   13,   14,   10,   11,   12,    0,    0,    0,
  201,    0,   13,   14,    7,    1,    0,    8,    2,    3,
    0,    0,    0,    0,   78,    1,    0,    8,    2,    3,
    0,    0,    0,    1,    0,    1,    2,    3,    2,    3,
    0,    0,    1,    0,    9,    2,    3,   10,   11,   12,
    0,    0,    0,    0,    9,   13,   14,   10,   11,   12,
    0,    0,    9,    0,    9,   13,   14,   12,  207,   12,
   71,    9,   71,   13,  250,   13,   12,    0,    0,   71,
  173,    1,   13,    8,    2,    3,   38,   38,    0,    0,
   38,   38,    0,    0,    0,    0,   37,   37,    0,   38,
   37,   37,    0,    0,    0,    0,    0,   38,    0,   37,
   38,    0,   38,   10,   11,    1,    0,   37,    2,    3,
   37,    0,   37,   36,   36,    0,    0,   36,   36,  115,
    1,    0,    0,    2,    3,  265,   36,    1,   64,    1,
    2,    3,    2,    3,   36,    0,    0,   36,    1,   36,
    0,    2,    3,   64,    0,  116,    0,  288,    0,  290,
   64,    1,   64,    0,    2,    3,    0,    0,  291,    0,
    0,   64,    0,   91,    0,    0,    0,    0,    0,    0,
    0,  307,   92,   93,   64,    0,    0,   94,   95,   96,
   97,   98,   99,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   20,  131,   12,  256,   67,   28,   57,   58,   67,   58,
  279,   21,   45,   16,   67,   28,  256,  151,  152,  256,
   67,  282,   67,  292,  158,  256,   36,  280,   44,  282,
  272,   34,    6,  257,  295,   45,  260,  261,  280,  282,
  280,   74,  282,   76,  257,  282,   56,  260,  261,   82,
  281,   84,   62,   63,   64,  256,   66,  278,   71,   33,
  111,  124,  111,  114,   74,  124,   76,   83,  131,  259,
  294,  124,   82,  207,   84,   85,   79,  124,  131,  124,
   90,  282,   92,   93,  131,   56,  299,  281,  121,  102,
  100,  257,   63,  103,  104,  282,  256,  282,  256,  289,
  290,  203,  204,   77,  114,  138,   80,  262,  263,  296,
  295,  121,  145,  259,   85,  294,  282,  168,  282,   90,
  256,  172,  282,  136,  282,  278,  189,  273,  138,  100,
  189,  256,  287,  288,  256,  145,  189,  140,  151,  152,
  242,  144,  189,  289,  290,  158,  282,  180,  256,  257,
  258,  171,  260,  261,  262,  263,  264,  282,  266,  256,
  282,  257,  172,  278,  174,  256,  256,  297,  231,  179,
  180,  279,  182,  236,  292,  283,  284,  236,  231,  268,
  269,  270,  278,  236,  231,  282,  202,  203,  204,  236,
  213,  282,  282,  301,  207,  265,  266,  279,  256,  219,
  213,  273,  222,  223,  293,  268,  269,  270,  179,  279,
  292,  182,  279,  285,  224,  268,  269,  270,  282,  267,
  268,  268,  269,  270,  282,  292,  242,  243,  244,  279,
  293,  256,  257,  258,  297,  260,  261,  262,  263,  264,
  293,  266,  262,  282,  297,  282,  293,  262,  263,  278,
  297,  261,  282,  263,  264,  294,  256,  257,  258,  284,
  260,  261,  262,  263,  264,  295,  266,  256,  257,  258,
  282,  260,  261,  262,  263,  264,  301,  266,  278,  289,
  278,  278,  279,  295,  284,  282,  256,  257,  280,  278,
  260,  261,  256,  257,  258,  284,  260,  261,  262,  263,
  264,  301,  266,  256,  257,  258,  282,  260,  261,  262,
  263,  264,  301,  266,  265,  266,  286,  282,  282,  295,
  284,  291,   92,   93,  294,  279,  279,  297,  256,  257,
  258,  284,  260,  261,  262,  263,  264,  301,  266,  256,
  257,  258,  279,  260,  261,  262,  263,  264,  301,  266,
  282,  279,  262,  263,  264,  282,  284,  265,  266,  282,
  256,  278,  257,  282,  296,  260,  261,  284,  282,  296,
  296,  279,  295,  301,  284,  257,  256,  257,  260,  261,
  260,  261,  262,  263,  301,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  275,  282,  277,  276,  279,
  280,  293,  282,  282,  286,  276,  286,  287,  288,  291,
  296,  291,  292,  295,  294,  297,  295,  297,  256,  257,
  300,  279,  260,  261,  262,  263,  282,  265,  266,  267,
  268,  282,  270,  271,  272,  273,  274,  275,  265,  266,
  296,  279,  280,  272,  282,  296,  269,  276,  286,  287,
  288,  280,  279,  291,  292,  279,  294,  279,  281,  297,
  256,  257,  300,  279,  260,  261,  262,  263,  292,  265,
  266,  256,  282,  279,  270,  271,  272,  273,  274,  275,
  265,  266,  279,  279,  280,  295,  282,  282,  296,  272,
  286,  287,  288,  276,  279,  291,  292,  280,  294,  276,
  295,  297,  256,  257,  300,  296,  260,  261,  262,  263,
  257,  265,  266,  260,  261,  296,  270,  271,  272,  273,
  274,  275,  265,  266,  282,  279,  280,  279,  282,  282,
  292,  278,  286,  287,  288,  279,  279,  291,  292,  296,
  294,  282,  295,  297,  256,  257,  300,  282,  260,  261,
  262,  263,  282,  265,  266,  282,  282,  282,  270,  271,
  272,  273,  274,  275,  282,  295,  257,  279,  280,  295,
  282,  103,  104,  293,  286,  287,  288,  295,  257,  291,
  292,  257,  294,  257,  282,  297,  256,  257,  300,  296,
  260,  261,  262,  263,  257,  296,  296,  260,  261,  282,
  282,  282,  282,  256,  257,  282,    0,  260,  261,  279,
  282,  282,  282,  282,    0,  279,  286,  287,  288,    0,
    0,  291,  292,  286,  294,  278,  279,  297,  291,  282,
  300,  294,    0,  286,  297,  256,  257,  300,  291,  260,
  261,  294,  279,  279,  297,  257,  258,  300,  260,  261,
  262,  263,  264,    0,  266,  256,  257,  256,  257,  260,
  261,  260,  261,  257,  172,  286,  260,  261,   -1,   36,
  291,   -1,  284,  294,   -1,  269,  297,  256,  257,  300,
   -1,  260,  261,   -1,   -1,  286,  280,  286,   -1,  301,
  291,   -1,  291,  294,   -1,  294,  297,   -1,  297,  300,
  257,  300,  259,  260,  261,   -1,   -1,  286,   -1,   -1,
  262,  263,  291,   -1,   -1,  294,   -1,   -1,  297,   -1,
  257,  300,  259,  260,  261,   -1,   -1,   -1,   -1,  286,
  282,   -1,  289,  290,  291,  287,  288,   -1,  295,   -1,
  297,  298,  257,  300,  257,  260,  261,  260,  261,  286,
   -1,   -1,  289,  290,  291,   -1,   -1,   -1,   -1,   -1,
  297,  298,  257,  300,  257,  260,  261,  260,  261,   -1,
   -1,  286,   -1,  286,   -1,   -1,  291,  292,  291,  294,
   -1,  294,  297,  296,  297,  300,  257,  300,   -1,  260,
  261,  286,   -1,  286,   -1,   -1,  291,   -1,  291,   -1,
  295,   -1,  297,   -1,  297,  300,   -1,  300,  256,  257,
   -1,  259,  260,  261,   -1,  286,   -1,   -1,  256,  257,
  291,  259,  260,  261,  295,   -1,  297,   -1,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  286,   -1,
   -1,  289,  290,  291,   -1,   -1,  294,  295,  286,  297,
  298,  289,  290,  291,   -1,   -1,  294,   -1,  286,  297,
  298,  289,  290,  291,   -1,   -1,   -1,  295,   -1,  297,
  298,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,  286,   -1,   -1,  289,  290,  291,   -1,   -1,   -1,
  295,  286,  297,  298,  289,  290,  291,   -1,   -1,   -1,
  295,  286,  297,  298,  289,  290,  291,   -1,   -1,   -1,
  295,   -1,  297,  298,  256,  257,   -1,  259,  260,  261,
   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,
   -1,   -1,   -1,  257,   -1,  257,  260,  261,  260,  261,
   -1,   -1,  257,   -1,  286,  260,  261,  289,  290,  291,
   -1,   -1,   -1,   -1,  286,  297,  298,  289,  290,  291,
   -1,   -1,  286,   -1,  286,  297,  298,  291,  292,  291,
  294,  286,  294,  297,  296,  297,  291,   -1,   -1,  294,
  256,  257,  297,  259,  260,  261,  256,  257,   -1,   -1,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  269,
  260,  261,   -1,   -1,   -1,   -1,   -1,  277,   -1,  269,
  280,   -1,  282,  289,  290,  257,   -1,  277,  260,  261,
  280,   -1,  282,  256,  257,   -1,   -1,  260,  261,  256,
  257,   -1,   -1,  260,  261,  277,  269,  257,  280,  257,
  260,  261,  260,  261,  277,   -1,   -1,  280,  257,  282,
   -1,  260,  261,  280,   -1,  282,   -1,  277,   -1,  277,
  280,  257,  280,   -1,  260,  261,   -1,   -1,  277,   -1,
   -1,  280,   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  277,  265,  266,  280,   -1,   -1,  270,  271,  272,
  273,  274,  275,
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
"identificador : identificador PUNTO identificador",
"identificador_simple : IDENTIFICADOR_GENERICO",
"identificador_simple : IDENTIFICADOR_ULONGINT",
"identificador_simple : IDENTIFICADOR_SINGLE",
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

//#line 332 "gramatica.y"
private static int cantidadIdEnListaId = 0;
private static Lexer lex;
private static ArrayList<String> representacionPolaca;

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
//#line 864 "Parser.java"
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
{ Parser.agregarEstructuraDetectadas(((Token) val_peek(3).obj).getNumeroDeLinea(), "PROGRAMA"); representacionPolaca.remove(0); }
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
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + (((Token) val_peek(2).obj).getNumeroDeLinea() +1) + ": Falta un BEGIN despues del identificador del programa"); }
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
{ Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S"); eliminarUltimosElementos(representacionPolaca, cantidadIdEnListaId); cantidadIdEnListaId = 0;}
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
                                                                                                                                                    eliminarUltimosElementos(representacionPolaca, cantidadIdEnListaId);
                                                                                                                                                    cantidadIdEnListaId = 0;
                                                                                                                                                }
break;
case 24:
//#line 58 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 25:
//#line 59 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(10).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 26:
//#line 60 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(10).ival + ": Falta ';' al final de la sentencia"); }
break;
case 27:
//#line 61 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 28:
//#line 62 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta simbolos '<' y '>' en la declaración del STRUCT");}
break;
case 29:
//#line 63 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 30:
//#line 64 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 31:
//#line 68 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 32:
//#line 69 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 33:
//#line 70 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 36:
//#line 77 "gramatica.y"
{cantidadIdEnListaId++;}
break;
case 37:
//#line 78 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ',' entre las identificadores"); }
break;
case 38:
//#line 79 "gramatica.y"
{ yyval.ival = val_peek(0).ival; cantidadIdEnListaId++;}
break;
case 39:
//#line 82 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 40:
//#line 83 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 41:
//#line 86 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 42:
//#line 87 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 43:
//#line 88 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 44:
//#line 92 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 45:
//#line 101 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 46:
//#line 106 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                             }
break;
case 48:
//#line 113 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 49:
//#line 114 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 50:
//#line 115 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": La funcion no puede tener mas de un parametro");}
break;
case 51:
//#line 116 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ yyval.ival + ": Falta el parametro de la funcion");}
break;
case 56:
//#line 125 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 57:
//#line 126 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 58:
//#line 127 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 61:
//#line 130 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 62:
//#line 131 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 63:
//#line 134 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 64:
//#line 135 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 65:
//#line 138 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 66:
//#line 139 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 67:
//#line 140 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 68:
//#line 143 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 69:
//#line 144 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 70:
//#line 145 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
break;
case 71:
//#line 146 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 72:
//#line 147 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 73:
//#line 148 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 74:
//#line 149 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 75:
//#line 150 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 76:
//#line 151 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 80:
//#line 159 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 82:
//#line 163 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 83:
//#line 164 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 87:
//#line 172 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 88:
//#line 173 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 89:
//#line 174 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 93:
//#line 180 "gramatica.y"
{ yyval.ival = val_peek(2).ival; representacionPolaca.add(((Token) val_peek(1).obj).getLexema());cantidadIdEnListaId = 0;}
break;
case 94:
//#line 183 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 95:
//#line 184 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia de selección");}
break;
case 96:
//#line 185 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
break;
case 97:
//#line 186 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 98:
//#line 187 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 99:
//#line 188 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 100:
//#line 189 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 101:
//#line 190 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 102:
//#line 191 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 106:
//#line 199 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 107:
//#line 202 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 108:
//#line 203 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 109:
//#line 206 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 110:
//#line 207 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 111:
//#line 208 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 112:
//#line 209 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 113:
//#line 210 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 114:
//#line 211 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 116:
//#line 215 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 117:
//#line 216 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
break;
case 118:
//#line 217 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 122:
//#line 225 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); }
break;
case 123:
//#line 226 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); }
break;
case 124:
//#line 227 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 125:
//#line 228 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 126:
//#line 229 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 127:
//#line 230 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 128:
//#line 231 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 129:
//#line 232 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 130:
//#line 235 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "FOR"); }
break;
case 131:
//#line 236 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 132:
//#line 239 "gramatica.y"
{ yyval.ival = val_peek(5).ival; }
break;
case 133:
//#line 240 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 134:
//#line 241 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 135:
//#line 242 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 136:
//#line 243 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 137:
//#line 244 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 138:
//#line 245 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 139:
//#line 248 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 140:
//#line 249 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 141:
//#line 250 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 142:
//#line 251 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 143:
//#line 252 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 144:
//#line 255 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 145:
//#line 258 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 146:
//#line 259 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 147:
//#line 260 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 150:
//#line 267 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 151:
//#line 268 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 153:
//#line 270 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": Falta operador, operandos, o coma entre expresiones"); }
break;
case 154:
//#line 273 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 155:
//#line 274 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 159:
//#line 280 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 160:
//#line 281 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 162:
//#line 285 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 163:
//#line 286 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 164:
//#line 289 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj);}
break;
case 165:
//#line 290 "gramatica.y"
{ yyval.obj = ((Token) val_peek(1).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": la constante se va de rango. No se permiten constantes enteras negativas."); }
break;
case 166:
//#line 291 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); representacionPolaca.add(((Token) val_peek(0).obj).getLexema());}
break;
case 167:
//#line 292 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 168:
//#line 293 "gramatica.y"
{
                                                            yyval.obj = ((Token) val_peek(1).obj);
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
case 169:
//#line 323 "gramatica.y"
{ yyval.obj = ((Token) val_peek(1).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 171:
//#line 327 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 172:
//#line 328 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1594 "Parser.java"
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
