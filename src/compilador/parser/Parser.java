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
    7,   13,   13,   15,   14,   14,   14,   14,   16,   16,
   16,   16,   16,   20,   21,   18,   26,   26,   27,   23,
   23,   28,   28,    4,    4,    4,    4,   17,   29,   32,
   32,   34,   25,   35,   35,   35,   35,   35,   35,   33,
   33,   36,   36,   19,   19,   30,   22,   37,   37,   38,
   38,   38,   38,   31,   31,   24,   24,   39,   39,   39,
   40,   40,   40,   41,   41,   41,   41,   42,   42,   42,
   43,   44,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    2,    1,    1,    1,    2,
    3,    2,    1,    2,    1,    9,    1,    1,    1,    3,
    1,    3,    1,    1,    1,    1,    1,    1,    3,    3,
    4,    6,    5,    2,    2,    2,    1,    1,    2,    2,
    2,    1,    1,    2,    5,    7,    2,    1,    2,    4,
    1,    2,    1,    2,    2,    2,    1,    3,    7,    2,
    1,    2,    3,    1,    1,    1,    1,    1,    1,    4,
    1,    2,    1,    4,    4,    2,    8,    3,    3,    2,
    2,    2,    2,    3,    1,    4,    1,    3,    3,    1,
    3,    3,    1,    1,    1,    2,    1,    1,    1,    1,
    4,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   26,   19,   27,   28,    0,   18,
   17,    0,    0,    0,    0,    0,    7,    8,    9,    0,
    0,   13,    0,   23,    0,   25,    0,    0,    0,    0,
    0,   57,    0,   10,    0,    0,    0,    0,    0,    3,
    6,    0,    0,    0,    0,   14,    0,    0,   54,   56,
    0,   71,   76,   55,    2,    0,    0,    0,   98,   99,
  100,    0,    0,   94,    0,    0,   87,    0,   93,   95,
   97,    1,    0,    0,    0,    0,    0,   11,    0,    0,
   22,    0,   29,    0,    0,   38,    0,   37,    0,    0,
    0,   42,   43,    0,   73,    0,    0,    0,    0,   96,
    0,    0,    0,   66,   67,   64,   65,   68,   69,    0,
    0,    0,    0,   74,   75,   21,    0,    0,    0,    0,
    0,    0,    0,   31,   36,   35,   39,   40,   41,    0,
   51,   44,    0,   72,   78,   79,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   91,   92,    0,    0,    0,
   34,   33,    0,    0,    0,   53,    0,   70,    0,  101,
   86,    0,    0,    0,   20,   32,    0,    0,    0,   52,
    0,    0,    0,   59,    0,   60,    0,    0,   45,   50,
   80,   81,   83,   82,   77,   62,    0,    0,    0,   16,
    0,   47,   46,   49,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   19,   20,   21,   22,   23,  117,   24,
   25,   26,   27,   87,  120,  131,   28,   90,   29,   92,
   93,   30,  132,   65,   66,  189,  192,  157,   31,   32,
   80,  162,   53,  176,  110,   96,   57,  173,   67,   68,
   69,   70,   71,  139,
};
final static short yysindex[] = {                      -246,
  -68,  212,    0, -275,    0,    0,    0,    0, -238,    0,
    0, -209,  212, -200, -202,  149,    0,    0,    0, -162,
 -169,    0, -185,    0, -174,    0, -163, -146, -142,   -9,
 -138,    0,  159,    0,  -76, -234,  169, -244, -123,    0,
    0, -182, -179, -234,  -76,    0,  -76,  -91,    0,    0,
 -181,    0,    0,    0,    0,  -97, -107,  -88,    0,    0,
    0,  -24,  -84,    0,  101,  -75,    0, -232,    0,    0,
    0,    0,  -64, -206, -228,  -67, -228,    0, -111,  -60,
    0, -174,    0,  -47,  -35,    0, -133,    0,  -69,  -28,
  -27,    0,    0, -223,    0,  220,  -80, -234, -234,    0,
 -234,  274,  274,    0,    0,    0,    0,    0,    0, -234,
  -39,  274,  274,    0,    0,    0, -239, -228,  -76,  -32,
 -234, -234, -234,    0,    0,    0,    0,    0,    0,  -81,
    0,    0,  -26,    0,    0,    0,  -23, -111,  -16, -111,
  -15, -232, -232, -111,   -9,    0,    0,  -11, -228,    5,
    0,    0, -111,    7, -177,    0,  -83,    0, -101,    0,
    0,  -17,   -4,  -76,    0,    0,    2,   11,   13,    0,
  -66,  -61,   18,    0,   -9,    0, -128, -223,    0,    0,
    0,    0,    0,    0,    0,    0,   43,   15,    8,    0,
 -223,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  309,    0,    0,    0,    0,
    0,    0,   91,    0,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  310,    0,    0,    0,
    0,    0,  104,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -38,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -121,   40,
    0,   46,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   45,    0,    0,
  -41,  213,  248, -119,    0,    0,    0,    0,    0,    0,
    0,    0, -109,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   31,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   33,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  107,   92,  -42,  -30,  -65,  -18,    0,    0,    0,   20,
  283,  284,    0,    0,  215,  -45,  -44,    0,  -43,    0,
    0,  -40,  -85,  -29,  -73,    0,    0,    0,    0,    0,
    0,    0,  -93,    0,    0,    0,    0,    0,  233,  143,
  137,  276,    0,    0,
};
final static int YYTABLESIZE=540;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         52,
   24,   43,   88,   89,   91,   86,   34,   94,   74,  116,
    1,  119,    5,   58,   79,    7,    8,   59,   60,   61,
   95,   62,    5,   58,  137,    7,    8,   59,   60,   61,
    6,   62,  148,    5,  112,  113,    7,    8,   73,   35,
  149,  126,   89,   91,  125,   30,   94,    2,  154,   89,
   91,  163,  119,   94,   56,   64,   63,   64,  102,  103,
   10,   11,    9,   64,   81,  134,   63,   84,   36,  138,
  130,  140,  115,   14,   76,    5,   85,   38,    7,    8,
  144,  186,   39,  165,  156,   89,   91,  102,  103,   94,
   15,  153,  188,  155,    5,   77,   46,    7,    8,   44,
   45,  168,   78,   12,    9,  194,   47,   41,   33,   12,
   45,  170,   89,   91,   52,   14,   94,   64,   64,   37,
   64,   64,   64,    5,   41,    6,    7,    8,   41,   64,
   48,   64,   64,   89,   91,   49,   42,   94,  151,   50,
   64,   64,   64,   54,   52,  177,   89,   91,  187,   75,
   94,   45,    9,  102,  103,   10,   11,   84,   85,   63,
   85,  124,   63,   14,   15,    5,   85,    6,    7,    8,
   84,   97,   84,    5,   98,    5,    7,    8,    7,    8,
    5,  135,  136,    7,    8,  171,  172,    4,    5,   99,
    6,    7,    8,  101,    9,  181,  182,   10,   11,   84,
  183,  184,    9,  111,    9,   14,   15,   84,   85,   84,
  118,  169,  127,   14,  114,   14,   85,    9,   85,  121,
   10,   11,   12,   87,   87,   13,   90,   90,   14,   15,
  122,   90,   90,   90,   90,   90,   90,   59,   60,   61,
   90,   90,  123,   90,  142,  143,  152,    5,  146,  147,
    7,    8,  145,  128,  129,  158,   24,   24,  159,   24,
   24,   24,  160,  161,  164,   24,   24,   24,   24,   24,
   24,   24,   24,   24,   24,   24,    9,   24,  174,   24,
   24,   12,   24,  166,   51,  167,   24,   14,  175,   24,
   24,   24,  179,  178,  180,   24,  185,   24,   24,  190,
   24,   30,   30,  193,   30,   30,   30,  191,    5,    4,
   30,   30,   30,   30,   30,   30,   30,   30,   30,   30,
   30,   58,   30,  102,   30,   30,   61,   30,   48,   82,
   83,   30,  150,  141,   30,   30,   30,  100,    0,    0,
   30,    0,   30,   30,    0,   30,   15,   15,    0,   15,
   15,   15,    0,    0,    0,    0,    0,    0,    0,   12,
   12,    0,   12,   12,   12,  102,  103,    0,    0,    0,
  104,  105,  106,  107,  108,  109,   15,    0,    0,   15,
   15,   15,    0,    0,    0,   15,    0,   15,   15,   12,
   15,    0,   12,   12,   12,    0,    0,    0,   12,    0,
   12,   12,    0,   12,    4,    5,    0,    6,    7,    8,
    0,    0,    0,    0,    4,    5,    0,    6,    7,    8,
    0,    0,    0,    0,    4,    5,    0,    6,    7,    8,
    0,    0,    0,    0,    9,    0,    0,   10,   11,   12,
    0,    0,    0,   40,    9,   14,   15,   10,   11,   12,
    0,    0,    0,   55,    9,   14,   15,   10,   11,   12,
    0,    0,    0,   72,    0,   14,   15,    4,    5,    0,
    6,    7,    8,    0,    0,    0,    5,   88,   88,    7,
    8,    0,   88,   88,   88,   88,   88,   88,    0,    0,
    0,   88,   88,    0,   88,    0,    0,    9,    0,    0,
   10,   11,   12,    0,    0,    9,    0,    0,   14,   15,
   12,    0,   89,   89,  133,    0,   14,   89,   89,   89,
   89,   89,   89,    0,    0,    0,   89,   89,    0,   89,
    5,   58,    0,    7,    8,   59,   60,   61,    0,   62,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         30,
    0,   20,   48,   48,   48,   48,  282,   48,   38,   75,
  257,   77,  257,  258,   44,  260,  261,  262,  263,  264,
   51,  266,  257,  258,   98,  260,  261,  262,  263,  264,
  259,  266,  272,  257,  267,  268,  260,  261,  283,  278,
  280,   87,   87,   87,   87,    0,   87,  294,  122,   94,
   94,  145,  118,   94,   35,   36,  301,   38,  265,  266,
  289,  290,  286,   44,   45,   96,  301,  291,  278,   99,
  294,  101,  279,  297,  257,  257,  300,  278,  260,  261,
  110,  175,  285,  149,  130,  130,  130,  265,  266,  130,
    0,  121,  178,  123,  257,  278,  282,  260,  261,  269,
  280,  279,  282,    0,  286,  191,  281,   16,    2,  291,
  280,  157,  157,  157,  145,  297,  157,   98,   99,   13,
  101,  102,  103,  257,   33,  259,  260,  261,   37,  110,
  294,  112,  113,  178,  178,  282,  299,  178,  119,  282,
  121,  122,  123,  282,  175,  164,  191,  191,  277,  273,
  191,  280,  286,  265,  266,  289,  290,  291,  280,  279,
  282,  295,  282,  297,  298,  257,  300,  259,  260,  261,
  280,  269,  282,  257,  282,  257,  260,  261,  260,  261,
  257,  262,  263,  260,  261,  287,  288,  256,  257,  278,
  259,  260,  261,  278,  286,  262,  263,  289,  290,  291,
  262,  263,  286,  279,  286,  297,  298,  291,  300,  291,
  278,  295,  282,  297,  279,  297,  300,  286,  300,  280,
  289,  290,  291,  265,  266,  294,  265,  266,  297,  298,
  278,  270,  271,  272,  273,  274,  275,  262,  263,  264,
  279,  280,  278,  282,  102,  103,  279,  257,  112,  113,
  260,  261,  292,  282,  282,  282,  256,  257,  282,  259,
  260,  261,  279,  279,  276,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  275,  286,  277,  296,  279,
  280,  291,  282,  279,  294,  279,  286,  297,  293,  289,
  290,  291,  282,  292,  282,  295,  279,  297,  298,  257,
  300,  256,  257,  296,  259,  260,  261,  293,    0,    0,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  282,  277,  279,  279,  280,  296,  282,  296,   47,
   47,  286,  118,  101,  289,  290,  291,   62,   -1,   -1,
  295,   -1,  297,  298,   -1,  300,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,
  257,   -1,  259,  260,  261,  265,  266,   -1,   -1,   -1,
  270,  271,  272,  273,  274,  275,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,   -1,  295,   -1,  297,  298,  286,
  300,   -1,  289,  290,  291,   -1,   -1,   -1,  295,   -1,
  297,  298,   -1,  300,  256,  257,   -1,  259,  260,  261,
   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,
   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,
   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,  290,  291,
   -1,   -1,   -1,  295,  286,  297,  298,  289,  290,  291,
   -1,   -1,   -1,  295,  286,  297,  298,  289,  290,  291,
   -1,   -1,   -1,  295,   -1,  297,  298,  256,  257,   -1,
  259,  260,  261,   -1,   -1,   -1,  257,  265,  266,  260,
  261,   -1,  270,  271,  272,  273,  274,  275,   -1,   -1,
   -1,  279,  280,   -1,  282,   -1,   -1,  286,   -1,   -1,
  289,  290,  291,   -1,   -1,  286,   -1,   -1,  297,  298,
  291,   -1,  265,  266,  295,   -1,  297,  270,  271,  272,
  273,  274,  275,   -1,   -1,   -1,  279,  280,   -1,  282,
  257,  258,   -1,  260,  261,  262,  263,  264,   -1,  266,
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
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA",
"sentencia_seleccion_en_funcion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF",
"cuerpo_if_en_funcion : bloque_de_sent_ejecutables_en_funcion bloque_else_en_funcion",
"cuerpo_if_en_funcion : bloque_de_sent_ejecutables_en_funcion",
"bloque_else_en_funcion : ELSE bloque_de_sent_ejecutables_en_funcion",
"bloque_de_sent_ejecutables_en_funcion : BEGIN sentencias_ejecutable_en_funcion END PUNTO_Y_COMA",
"bloque_de_sent_ejecutables_en_funcion : sentencia_ejecutable_en_funcion",
"sentencias_ejecutable_en_funcion : sentencias_ejecutable_en_funcion sentencia_ejecutable_en_funcion",
"sentencias_ejecutable_en_funcion : sentencia_ejecutable_en_funcion",
"sentencia_ejecutable : sentencia_asignacion PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_seleccion PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_salida PUNTO_Y_COMA",
"sentencia_ejecutable : sentencia_control",
"sentencia_asignacion : lista_de_identificadores ASIGNACION lista_de_expresiones",
"sentencia_seleccion : IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF",
"cuerpo_if : bloque_de_sent_ejecutables bloque_else",
"cuerpo_if : bloque_de_sent_ejecutables",
"bloque_else : ELSE bloque_de_sent_ejecutables",
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
"sentencia_control : encabezado_for bloque_de_sent_ejecutables",
"encabezado_for : FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion PARENTESIS_R",
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

//#line 264 "gramatica.y"
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
//#line 586 "Parser.java"
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
//#line 62 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 24:
//#line 65 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 25:
//#line 66 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 26:
//#line 69 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 27:
//#line 70 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 28:
//#line 71 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 29:
//#line 74 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 30:
//#line 75 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 31:
//#line 78 "gramatica.y"
{ yyval.ival = val_peek(3).ival; }
break;
case 32:
//#line 81 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 33:
//#line 86 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                            }
break;
case 39:
//#line 101 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 40:
//#line 102 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 41:
//#line 103 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 42:
//#line 104 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 43:
//#line 105 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); }
break;
case 44:
//#line 108 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 45:
//#line 111 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 46:
//#line 114 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 54:
//#line 132 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 55:
//#line 133 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 56:
//#line 134 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "SALIDA"); }
break;
case 57:
//#line 135 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 58:
//#line 138 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 59:
//#line 141 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 74:
//#line 170 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 75:
//#line 171 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 76:
//#line 174 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 77:
//#line 177 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); }
break;
case 96:
//#line 210 "gramatica.y"
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
case 98:
//#line 251 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 99:
//#line 252 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 100:
//#line 253 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
//#line 968 "Parser.java"
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
