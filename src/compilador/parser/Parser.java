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
   40,   40,   41,   42,   43,   43,   43,   33,   33,   24,
   24,   24,   24,   45,   45,   45,   46,   46,   46,   46,
   46,   44,   44,   47,   47,   47,   47,   47,   47,   48,
   48,   48,
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
    6,    6,    1,    3,    2,    2,    1,    3,    1,    3,
    3,    1,    1,    3,    3,    1,    1,    1,    4,    3,
    1,    1,    1,    1,    2,    1,    1,    2,    2,    4,
    3,    5,
};
final static short yydefred[] = {                         0,
   41,   42,   43,    0,    0,    0,    0,   33,  153,   32,
   31,    0,    0,    0,    0,    0,   14,   15,   16,    0,
    0,   21,   22,   38,   40,    0,    0,   96,    0,    0,
   95,   97,    0,    0,    0,    0,    0,   17,  163,    0,
  172,  173,  176,    0,    0,  177,    0,  167,    0,    0,
  174,    0,  166,  168,  171,    0,    0,    0,    0,    4,
   13,    0,    0,    0,    0,   37,    0,    0,   92,  140,
    0,  128,  139,    0,    0,    0,    0,  112,    0,  142,
    0,    0,    0,    0,    0,    0,    0,    0,    5,    0,
  178,  179,  175,    0,    0,  117,    0,    0,  120,  121,
  118,  119,  122,  123,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   35,    0,    0,    0,    0,   44,    0,
    0,    0,   18,    0,    0,   36,    0,    0,   57,    0,
   56,    0,   61,   62,   63,   64,    0,    0,   93,   94,
    0,  130,    0,    0,  106,  102,    0,    0,  111,  114,
    0,    0,    0,    0,    0,    0,    0,    9,    0,    0,
    0,  181,    0,  107,    0,  170,    0,    0,    0,    0,
    0,    0,  164,  165,  138,  137,    0,    0,    0,    0,
    0,    0,    0,    0,   53,    0,   50,    0,   19,   20,
    0,    0,    0,    0,   46,   55,   54,    0,   58,   66,
    0,   89,   65,    0,    0,   82,    0,    0,  126,    0,
  129,  108,    0,   99,  113,    0,    0,    0,    0,    0,
  154,    0,    3,    8,    2,   12,    0,  180,    0,  169,
    0,  105,    0,  131,    0,  132,    0,   34,    0,    0,
    0,    0,    0,    0,   48,    0,    0,    0,    0,    0,
   59,   60,   91,    0,    0,   77,   73,    0,    0,   84,
   81,    0,  124,  100,  101,  145,    0,  146,    0,    0,
    0,  182,  103,  104,  133,  135,  134,  136,    0,    0,
    0,    0,    0,   47,   52,    0,    0,    0,    0,    0,
   90,   78,    0,   70,   83,  125,  127,  141,    0,    0,
    0,  152,  157,  151,  149,    0,    0,    0,    0,    0,
    0,    0,   76,    0,   67,    0,   86,   71,   72,  148,
  155,  156,    0,    0,    0,    0,   28,   74,   75,   68,
   69,   87,   88,   24,    0,   30,   29,   27,    0,   23,
   25,   26,
};
final static short yydgoto[] = {                          5,
   15,   16,   17,   18,   19,   20,   21,   22,   23,  115,
   24,   25,   26,  130,  188,  202,   27,  133,   28,  135,
  136,   29,  206,   49,   30,  207,   50,  208,  261,  254,
   31,   32,  125,   76,   77,  149,   78,  105,  143,   33,
   34,   84,  302,   51,   52,   53,   54,   55,
};
final static short yysindex[] = {                      -134,
    0,    0,    0,  781,    0,  698, -266,    0,    0,    0,
    0,   53, -253, -107, -242,  708,    0,    0,    0, -237,
   14,    0,    0,    0,    0, -259, -226,    0,  537,  114,
    0,    0,  174,   66,  -39,  824,  655,    0,    0, -233,
    0,    0,    0,  -74,  167,    0, -103,    0,  935,  832,
    0, -111,    0,    0,    0,   41, -232, -225,  141,    0,
    0, -249,  910,  167,  141,    0,  547, -267,    0,    0,
  890,    0,    0,  892, -105, -264,  848,    0,  167,    0,
   95,  141,  -83,  -65,  781, -207,  718,  781,    0,  106,
    0,    0,    0,  841,  120,    0,  472,  472,    0,    0,
    0,    0,    0,    0,  167,  883,  -63,  472,  472,  -32,
   18,    6,  -60,    0, -152, -232,  293, -242,    0,   34,
  -45,  -98,    0,  -30,   48,    0,  131,   54,    0,  527,
    0, -148,    0,    0,    0,    0,  463,  569,    0,    0,
   78,    0,  902,  848,    0,    0, -146,  892,    0,    0,
   69,  167,   86,  156,   77,  167,  761,    0,  -57,   26,
  771,    0,   12,    0,  107,    0,   64, -111, -111,  -30,
  130,  154,    0,    0,    0,    0, -144, -139,  168, -232,
  335,  179,  141,  -45,    0,  141,    0,  184,    0,    0,
  167,  167,  589,  167,    0,    0,    0,  -29,    0,    0,
  -37,    0,    0,  421,  200,    0, -246,  591,    0, -123,
    0,    0,   11,    0,    0,  206,  220,  226,  167,  212,
    0,  242,    0,    0,    0,    0,  248,    0,  247,    0,
  249,    0,  155,    0,  159,    0,  141,    0,  254,  141,
  141,   61,  257,  283,    0,  -30,  613,  633,  245,   68,
    0,    0,    0,  640,  591,    0,    0, -121,  421,    0,
    0,  163,    0,    0,    0,    0,  263,    0,    2, -156,
 -156,    0,    0,    0,    0,    0,    0,    0,  182,  141,
  490,  498,  290,    0,    0,  256,  259,  274, -117, -115,
    0,    0,  198,    0,    0,    0,    0,    0, -156,   77,
   77,    0,    0,    0,    0,  300,  582,  307,  309,  289,
  295,  296,    0,  205,    0,  215,    0,    0,    0,    0,
    0,    0,  299, -245,  301,  302,    0,    0,    0,    0,
    0,    0,    0,    0,  -88,    0,    0,    0,  240,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  204,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  483,    0,    0,    0,  572,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  288,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -192,    0,    0,    0,
    0,    0,    0,    0,    0,  594,  597,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   70,
    0,    0,    0,    0,    0,    0,    0,  246,    0,    0,
    0,    0,    0, -209,  -87,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -40,    0,    0,    0,  -34,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  598,  599,
    0,    0,    0,    0,    0,    0,    0,  330,  372,  414,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  310,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -10,    0,    0,
    0,    0,    0,    0,    0,    0,  485,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  327,    0, -177,    0,    0,    0,    0,
    0,    0,    0,    0,  152,    0,    0,    0,  153,    0,
    0,    0,    0,    0,    0,    0,  505,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  431,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    4,   13,  -11,  -64,  202,    3,  -19,    0,    0,  -48,
  -12,  549,    0,    0,  429,  -50,  -56,    0,  -46,    0,
    0,   -8, -106,  -18,   33, -153,  -38,  410,  364,    0,
    0,    0,    0,    5,  546,  479,  -23,    0,    0,    0,
    0,  543, -257,  -42,  349,  380,    0,    0,
};
final static int YYTABLESIZE=1210;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   63,   93,  129,    6,   61,   73,   94,  120,   66,  117,
  132,  335,  304,  305,  139,   38,  131,  146,   37,    1,
  134,   83,    2,    3,   56,   61,    8,  140,  121,   68,
  203,  147,   48,    8,   67,  257,  336,  113,   59,  249,
  151,  320,  153,   48,   90,  124,  159,  116,   87,  258,
   66,   48,  126,  150,  107,   69,   10,   11,  137,  114,
  114,   62,  118,   10,   11,  196,   48,  181,   48,   83,
  159,  163,  159,  132,   38,   61,  167,   48,  158,  197,
  132,  132,   48,  134,   48,   48,  170,  158,  193,  110,
  134,  134,   48,  286,  287,   48,   48,  157,  165,  138,
  161,  260,  158,  110,  158,   41,   42,  198,  187,  213,
  171,  233,  221,  217,   48,  220,  235,  222,  114,  179,
  150,  137,    1,  186,  215,    2,    3,  180,  137,  137,
  300,  301,  262,  199,  293,  214,  132,  234,  314,   48,
  316,   48,  236,   48,  132,   61,  134,  132,  260,   61,
  253,  132,  295,  247,  134,  108,  109,  134,  263,    4,
  294,  134,  138,  242,  315,   57,  317,  339,   98,  138,
  138,  187,  246,  244,   95,  250,  145,   58,   48,   48,
  269,   48,  238,  189,  137,  155,  186,   41,   42,   91,
  132,  132,  137,  340,   98,  137,  190,  132,  132,  137,
  134,  134,  132,  291,   97,   98,   48,  134,  134,   92,
  185,    1,  134,    8,    2,    3,  156,  279,  178,    1,
  281,  282,    2,    3,   38,  138,  303,  303,  303,   66,
   72,   72,  172,  138,   97,   98,  138,  224,  137,  137,
  138,  109,   38,   10,   11,  137,  137,  115,    9,  175,
  137,   72,  251,  127,   85,  109,  303,  321,  322,   13,
  307,  115,  128,   41,   42,  252,   66,  227,   66,   66,
    1,   80,  142,    2,    3,   72,   97,   98,   72,  138,
  138,  225,   64,  299,  177,   80,  138,  138,  300,  301,
  228,  138,  264,   65,   66,   72,  110,    1,   40,  176,
    2,    3,   41,   42,   43,  265,   44,   72,   39,    1,
   40,  184,    2,    3,   41,   42,   43,    1,   44,  111,
    2,    3,    1,  112,   46,    2,    3,  191,   97,   98,
   45,  194,   97,   98,  163,  163,   46,  283,   41,   42,
   65,   47,  230,   82,  211,   72,  289,  216,  163,   72,
   39,    1,   40,   47,    2,    3,   41,   42,   43,  209,
   44,   39,    1,   40,  218,    2,    3,   41,   42,   43,
    1,   44,  152,    2,    3,   39,    1,   40,   46,    2,
    3,   41,   42,   43,  162,   44,   39,    1,   40,   46,
    2,    3,   41,   42,   43,   47,   44,    1,  166,    9,
    2,    3,  229,   46,   12,   74,   47,   71,  192,   75,
   13,   39,    1,   40,   46,    2,    3,   41,   42,   43,
   47,   44,   39,    1,   40,  231,    2,    3,   41,   42,
   43,   47,   44,   79,   85,  232,  275,  219,    1,   46,
  277,    2,    3,  237,  296,  168,  169,   79,   85,  276,
   46,   79,   80,  278,  241,   81,   47,  297,  306,   39,
   39,   65,  245,   39,   39,   39,   39,   47,   39,   39,
   39,   39,   39,   39,   39,   39,   39,   39,   39,  318,
   39,  256,   39,   39,  266,   39,  330,  173,  174,   39,
   39,   39,  319,  270,   39,   39,  332,   39,  267,  331,
   39,   45,   45,   39,  268,   45,   45,   45,   45,  333,
   45,   45,   45,   45,   45,   45,   45,   45,   45,   45,
   45,  341,   45,  271,   45,   45,  272,   45,  273,  280,
  274,   45,   45,   45,  342,  284,   45,   45,  285,   45,
  288,  298,   45,  162,  162,   45,  310,  162,  162,  162,
  162,  311,  162,  162,  312,  313,  323,  162,  162,  162,
  162,  162,  162,  325,  182,  326,  162,  162,  183,  162,
  327,    7,  180,  162,  162,  162,  328,  329,  162,  162,
  334,  162,  337,  338,  162,  160,  160,  162,   51,  160,
  160,  160,  160,   11,  160,  160,    6,   10,    1,  160,
  160,  160,  160,  160,  160,   49,  239,  119,  160,  160,
  240,  160,  243,  255,  180,  160,  160,  160,  292,  144,
  160,  160,  212,  160,  154,    0,  160,  161,  161,  160,
    0,  161,  161,  161,  161,    0,  161,  161,    0,    0,
    0,  161,  161,  161,  161,  161,  161,    0,    0,    0,
  161,  161,    0,  161,    0,    0,    0,  161,  161,  161,
    0,    0,  161,  161,    0,  161,    0,    0,  161,  116,
  116,  161,    0,  116,  116,  116,  116,    1,    0,    0,
    2,    3,    0,    0,    0,    0,  150,  150,    0,    0,
  150,  150,  116,    0,    0,  116,    0,    0,    0,  116,
  116,  116,    0,    0,  116,  116,    9,  116,  150,  150,
  116,  127,  150,  116,  201,    0,  150,   13,  200,    1,
  128,  150,    2,    3,  150,    0,    0,  150,    1,   40,
  150,    2,    3,   41,   42,   43,    0,   44,  143,  143,
  147,  147,  143,  143,  147,  147,    1,    0,    9,    2,
    3,    0,    0,  127,    1,   46,  201,    2,    3,   13,
  144,  144,  128,    0,  144,  144,  308,    0,  143,   65,
  147,    0,   47,  143,  309,  147,  143,   65,  147,  143,
    0,  147,  143,    1,  147,    8,    2,    3,    0,    0,
  144,    0,   70,    1,    0,  144,    2,    3,  144,    0,
    0,  144,    0,    1,  144,    8,    2,    3,    0,    0,
    0,    0,    9,    0,    0,   10,   11,  127,    0,    0,
    0,  195,    9,   13,   14,    1,  128,   12,    2,    3,
   71,    0,    9,   13,    0,   10,   11,  127,    1,    0,
    0,    2,    3,   13,   14,    1,  128,    1,    2,    3,
    2,    3,    0,    0,    9,    0,    0,    0,  324,  127,
  204,   65,  201,    0,  205,   13,    0,  248,  128,    1,
    0,    0,    2,    3,    9,    0,    9,    0,    0,  127,
  204,  127,  201,  259,  201,   13,    0,   13,  128,    1,
  128,  164,    2,    3,    0,    0,    1,    0,    9,    2,
    3,    0,    0,  127,  204,    0,  201,    0,    0,   13,
    7,    1,  128,    8,    2,    3,    0,    0,    9,    0,
    0,    0,    0,  127,  204,    9,  201,    0,    0,   13,
  127,    0,  128,    0,  290,    0,   13,    0,    0,  128,
    9,    0,    0,   10,   11,   12,    0,    0,   88,   89,
    0,   13,   14,   35,    1,    0,    8,    2,    3,    0,
    0,    0,    0,    7,    1,    0,    8,    2,    3,    0,
    0,    0,    0,  159,    1,    0,    8,    2,    3,    0,
    0,    0,    0,    9,    0,    0,   10,   11,   12,    0,
    0,   36,    0,    9,   13,   14,   10,   11,   12,    0,
    0,    0,   60,    9,   13,   14,   10,   11,   12,    0,
    0,    0,  160,    0,   13,   14,    7,    1,    0,    8,
    2,    3,    0,    0,    0,    0,    7,    1,    0,    8,
    2,    3,    0,    0,    0,    0,    7,    1,    0,    8,
    2,    3,    0,    0,    0,    0,    9,    0,    0,   10,
   11,   12,    0,    0,    0,  223,    9,   13,   14,   10,
   11,   12,    0,    0,    0,  226,    9,   13,   14,   10,
   11,   12,    0,    0,    0,    0,    0,   13,   14,   86,
    1,    0,    8,    2,    3,    0,    0,    0,    1,    0,
    0,    2,    3,    0,    0,    0,    0,    1,    0,    0,
    2,    3,    0,    0,    1,    0,    0,    2,    3,    9,
  106,    0,   10,   11,   12,    0,    0,    9,    0,  164,
   13,   14,   12,   74,    0,   71,    9,    0,   13,    0,
    0,   12,   74,    9,   71,    0,    0,   13,   12,    1,
  148,   71,    2,    3,   13,    0,    1,    0,    1,    2,
    3,    2,    3,    0,    0,    0,    0,    0,    1,    0,
    0,    2,    3,    0,    0,  122,    1,    0,    9,    2,
    3,    0,    0,   12,   74,    9,   71,    9,    0,   13,
   12,    0,   12,    0,  141,   71,   13,    9,   13,   65,
   96,  123,   12,    0,    0,    0,  210,    0,   13,   97,
   98,    0,    0,    0,   99,  100,  101,  102,  103,  104,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
   20,   44,   67,    0,   16,   29,   45,  257,   21,   58,
   67,  257,  270,  271,  282,  282,   67,  282,    6,  257,
   67,   34,  260,  261,  278,   37,  259,  295,  278,  256,
  137,  296,   45,  259,  294,  282,  282,   56,  281,  193,
   79,  299,   81,   56,  278,   64,  256,  273,   36,  296,
   63,   64,   65,   77,   50,  282,  289,  290,   67,   57,
   58,  299,   59,  289,  290,  130,   79,  116,   81,   82,
  280,   90,  282,  130,  282,   87,   95,   90,  256,  130,
  137,  138,   95,  130,   97,   98,  105,  295,  127,  282,
  137,  138,  105,  247,  248,  108,  109,   85,   94,   67,
   88,  208,  280,  296,  282,  262,  263,  256,  121,  256,
  106,  256,  155,  152,  127,  154,  256,  156,  116,  272,
  144,  130,  257,  121,  148,  260,  261,  280,  137,  138,
  287,  288,  256,  282,  256,  282,  193,  282,  256,  152,
  256,  154,  282,  156,  201,  157,  193,  204,  255,  161,
  201,  208,  259,  192,  201,  267,  268,  204,  282,  294,
  282,  208,  130,  183,  282,  273,  282,  256,  256,  137,
  138,  184,  191,  186,  278,  194,  282,  285,  191,  192,
  219,  194,  180,  282,  193,  269,  184,  262,  263,  264,
  247,  248,  201,  282,  282,  204,  295,  254,  255,  208,
  247,  248,  259,  254,  265,  266,  219,  254,  255,  284,
  256,  257,  259,  259,  260,  261,  282,  237,  279,  257,
  240,  241,  260,  261,  282,  193,  269,  270,  271,  242,
   29,   30,  296,  201,  265,  266,  204,  295,  247,  248,
  208,  282,  282,  289,  290,  254,  255,  282,  286,  282,
  259,   50,  282,  291,  294,  296,  299,  300,  301,  297,
  280,  296,  300,  262,  263,  295,  279,  256,  281,  282,
  257,  282,   71,  260,  261,   74,  265,  266,   77,  247,
  248,  256,  269,  282,  279,  296,  254,  255,  287,  288,
  279,  259,  282,  280,  307,   94,  256,  257,  258,  282,
  260,  261,  262,  263,  264,  295,  266,  106,  256,  257,
  258,  278,  260,  261,  262,  263,  264,  257,  266,  279,
  260,  261,  257,  283,  284,  260,  261,  280,  265,  266,
  278,  278,  265,  266,  265,  266,  284,  277,  262,  263,
  280,  301,  279,  278,  143,  144,  279,  279,  279,  148,
  256,  257,  258,  301,  260,  261,  262,  263,  264,  282,
  266,  256,  257,  258,  279,  260,  261,  262,  263,  264,
  257,  266,  278,  260,  261,  256,  257,  258,  284,  260,
  261,  262,  263,  264,  279,  266,  256,  257,  258,  284,
  260,  261,  262,  263,  264,  301,  266,  257,  279,  286,
  260,  261,  296,  284,  291,  292,  301,  294,  278,  296,
  297,  256,  257,  258,  284,  260,  261,  262,  263,  264,
  301,  266,  256,  257,  258,  296,  260,  261,  262,  263,
  264,  301,  266,  282,  282,  282,  282,  282,  257,  284,
  282,  260,  261,  276,  282,   97,   98,  296,  296,  295,
  284,  278,  279,  295,  276,  282,  301,  295,  277,  256,
  257,  280,  279,  260,  261,  262,  263,  301,  265,  266,
  267,  268,  269,  270,  271,  272,  273,  274,  275,  282,
  277,  282,  279,  280,  279,  282,  282,  108,  109,  286,
  287,  288,  295,  282,  291,  292,  282,  294,  279,  295,
  297,  256,  257,  300,  279,  260,  261,  262,  263,  295,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  282,  277,  282,  279,  280,  279,  282,  282,  276,
  282,  286,  287,  288,  295,  279,  291,  292,  256,  294,
  296,  279,  297,  256,  257,  300,  257,  260,  261,  262,
  263,  296,  265,  266,  296,  282,  257,  270,  271,  272,
  273,  274,  275,  257,  272,  257,  279,  280,  276,  282,
  282,    0,  280,  286,  287,  288,  282,  282,  291,  292,
  282,  294,  282,  282,  297,  256,  257,  300,  279,  260,
  261,  262,  263,    0,  265,  266,    0,    0,    0,  270,
  271,  272,  273,  274,  275,  279,  272,   59,  279,  280,
  276,  282,  184,  204,  280,  286,  287,  288,  255,   74,
  291,  292,  144,  294,   82,   -1,  297,  256,  257,  300,
   -1,  260,  261,  262,  263,   -1,  265,  266,   -1,   -1,
   -1,  270,  271,  272,  273,  274,  275,   -1,   -1,   -1,
  279,  280,   -1,  282,   -1,   -1,   -1,  286,  287,  288,
   -1,   -1,  291,  292,   -1,  294,   -1,   -1,  297,  256,
  257,  300,   -1,  260,  261,  262,  263,  257,   -1,   -1,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,
  260,  261,  279,   -1,   -1,  282,   -1,   -1,   -1,  286,
  287,  288,   -1,   -1,  291,  292,  286,  294,  278,  279,
  297,  291,  282,  300,  294,   -1,  286,  297,  256,  257,
  300,  291,  260,  261,  294,   -1,   -1,  297,  257,  258,
  300,  260,  261,  262,  263,  264,   -1,  266,  256,  257,
  256,  257,  260,  261,  260,  261,  257,   -1,  286,  260,
  261,   -1,   -1,  291,  257,  284,  294,  260,  261,  297,
  256,  257,  300,   -1,  260,  261,  277,   -1,  286,  280,
  286,   -1,  301,  291,  277,  291,  294,  280,  294,  297,
   -1,  297,  300,  257,  300,  259,  260,  261,   -1,   -1,
  286,   -1,  256,  257,   -1,  291,  260,  261,  294,   -1,
   -1,  297,   -1,  257,  300,  259,  260,  261,   -1,   -1,
   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,   -1,
   -1,  295,  286,  297,  298,  257,  300,  291,  260,  261,
  294,   -1,  286,  297,   -1,  289,  290,  291,  257,   -1,
   -1,  260,  261,  297,  298,  257,  300,  257,  260,  261,
  260,  261,   -1,   -1,  286,   -1,   -1,   -1,  277,  291,
  292,  280,  294,   -1,  296,  297,   -1,  279,  300,  257,
   -1,   -1,  260,  261,  286,   -1,  286,   -1,   -1,  291,
  292,  291,  294,  293,  294,  297,   -1,  297,  300,  257,
  300,  279,  260,  261,   -1,   -1,  257,   -1,  286,  260,
  261,   -1,   -1,  291,  292,   -1,  294,   -1,   -1,  297,
  256,  257,  300,  259,  260,  261,   -1,   -1,  286,   -1,
   -1,   -1,   -1,  291,  292,  286,  294,   -1,   -1,  297,
  291,   -1,  300,   -1,  295,   -1,  297,   -1,   -1,  300,
  286,   -1,   -1,  289,  290,  291,   -1,   -1,  294,  295,
   -1,  297,  298,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,
   -1,  294,   -1,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,  286,  297,  298,  289,  290,  291,   -1,
   -1,   -1,  295,   -1,  297,  298,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,
  290,  291,   -1,   -1,   -1,   -1,   -1,  297,  298,  256,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,  257,   -1,
   -1,  260,  261,   -1,   -1,   -1,   -1,  257,   -1,   -1,
  260,  261,   -1,   -1,  257,   -1,   -1,  260,  261,  286,
  279,   -1,  289,  290,  291,   -1,   -1,  286,   -1,  279,
  297,  298,  291,  292,   -1,  294,  286,   -1,  297,   -1,
   -1,  291,  292,  286,  294,   -1,   -1,  297,  291,  257,
  293,  294,  260,  261,  297,   -1,  257,   -1,  257,  260,
  261,  260,  261,   -1,   -1,   -1,   -1,   -1,  257,   -1,
   -1,  260,  261,   -1,   -1,  256,  257,   -1,  286,  260,
  261,   -1,   -1,  291,  292,  286,  294,  286,   -1,  297,
  291,   -1,  291,   -1,  295,  294,  297,  286,  297,  280,
  256,  282,  291,   -1,   -1,   -1,  295,   -1,  297,  265,
  266,   -1,   -1,   -1,  270,  271,  272,  273,  274,  275,
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
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion_aritmetica PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion_aritmetica error PARENTESIS_R",
};

//#line 459 "gramatica.y"
private static int cantidadIdEnListaId = 0;
private static Lexer lex;
private static ArrayList<String> representacionPolaca;

// Pila de ambitos
public static final Stack<String> ambito = new Stack<>();

// Pila para bifurcaciones
public static final Stack<Integer> bfs = new Stack<>();

// Pila para almacenar acción del for
public static final Stack<String> aux = new Stack<>();

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

// Lista de expresiones
public static final List<String> listaExpresiones = new ArrayList<>();

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

public static void agregarUsoAIdentificador(String identificador, String uso) {
    TablaSimbolos.setUso(identificador, uso);
}

public static void agregarUsoAIdentificadores(List<String> identificadores, String uso) {
    for (String lexema: identificadores) {
        TablaSimbolos.setUso(lexema, uso);
    }
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
        String nombreCampo = lexema + "." + campo.nombre() + getAmbitoActual();
        CampoTablaSimbolos nuevoCampo = new CampoTablaSimbolos(false, campo.tipo());
        TablaSimbolos.agregarLexema(nombreCampo, nuevoCampo);
        agregarAmbitoAIdentificador(nombreCampo);
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
//#line 1002 "Parser.java"
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
                                                                                Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S");
                                                                                eliminarUltimosElementos(representacionPolaca, listaIdentificadores.size());
                                                                                agregarTipoAIdentificadores(val_peek(2).sval);
                                                                                agregarAmbitoAIdentificadores(listaIdentificadores);
                                                                                agregarUsoAIdentificadores(listaIdentificadores, "nombre de variable");
                                                                                listaIdentificadores.forEach((lexema)->TablaSimbolos.cambiarLexema(lexema, lexema + getAmbitoActual()));
                                                                                if (TablaSimbolos.esUnTipo(val_peek(2).sval) && TablaSimbolos.getTipo(val_peek(2).sval).equals("STRUCT")) {
                                                                                    for(String identificador: listaIdentificadores) {
                                                                                        crearCampo(val_peek(2).sval, identificador);
                                                                                    }
                                                                                };
                                                                                listaIdentificadores.clear();
                                                                            }
break;
case 19:
//#line 59 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 20:
//#line 60 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ';' al final de la sentencia"); }
break;
case 21:
//#line 61 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FUNCION"); }
break;
case 23:
//#line 65 "gramatica.y"
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
//#line 79 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 25:
//#line 80 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(10).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 26:
//#line 81 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(10).ival + ": Falta ';' al final de la sentencia"); }
break;
case 27:
//#line 82 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 28:
//#line 83 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta simbolos '<' y '>' en la declaración del STRUCT");}
break;
case 29:
//#line 84 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 30:
//#line 85 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 31:
//#line 89 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema().toUpperCase(); }
break;
case 32:
//#line 90 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema().toUpperCase(); }
break;
case 33:
//#line 91 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema(); }
break;
case 34:
//#line 94 "gramatica.y"
{ listaTipos.add(val_peek(0).sval); }
break;
case 35:
//#line 95 "gramatica.y"
{ listaTipos.add(val_peek(0).sval); }
break;
case 36:
//#line 98 "gramatica.y"
{ listaIdentificadores.add(val_peek(0).sval);}
break;
case 37:
//#line 99 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ',' entre las identificadores"); }
break;
case 38:
//#line 100 "gramatica.y"
{ yyval.ival = val_peek(0).ival; listaIdentificadores.add(val_peek(0).sval);}
break;
case 39:
//#line 103 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(0).obj).getLexema();
                                                       representacionPolaca.add(((Token) val_peek(0).obj).getLexema());
                                                       String idActual = ((Token) val_peek(0).obj).getLexema();
                                                       String idActualConAmbito = idActual + getAmbitoActual();
                                                       if (TablaSimbolos.existeLexema(idActualConAmbito)){
                                                           TablaSimbolos.aumentarUso(idActualConAmbito);
                                                           TablaSimbolos.deleteEntrada(idActual);
                                                       };}
break;
case 40:
//#line 111 "gramatica.y"
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
//#line 124 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 42:
//#line 125 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 43:
//#line 126 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 44:
//#line 129 "gramatica.y"
{ yyval.ival = ((Token) val_peek(2).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(2).obj).getLexema() + ((Token) val_peek(1).obj).getLexema() + val_peek(0).sval;}
break;
case 45:
//#line 130 "gramatica.y"
{ yyval.ival = ((Token) val_peek(2).obj).getNumeroDeLinea(); yyval.sval = ((Token) val_peek(2).obj).getLexema() + ((Token) val_peek(1).obj).getLexema() +((Token) val_peek(0).obj).getLexema();}
break;
case 46:
//#line 133 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                                ambito.pop();
                                                                                representacionPolaca.add("Fin " + aux.pop());
                                                                                representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()));
                                                                             }
break;
case 47:
//#line 145 "gramatica.y"
{
                                                                                                        String lexemaParametro = representacionPolaca.get(representacionPolaca.size()-1);
                                                                                                        eliminarUltimosElementos(representacionPolaca, 1);
                                                                                                        representacionPolaca.add("");
                                                                                                        bfs.push(representacionPolaca.size()-1);
                                                                                                        representacionPolaca.add("BI");
                                                                                                        representacionPolaca.add(((Token) val_peek(3).obj).getLexema().toString());
                                                                                                        aux.push(((Token) val_peek(3).obj).getLexema().toString());
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                        agregarAmbitoAIdentificador(lexema);
                                                                                                        agregarUsoAIdentificador(lexema, "nombre de funcion");
                                                                                                        agregarAmbitoAIdentificador(representacionPolaca.get(representacionPolaca.size()-1));
                                                                                                        ambito.push(":" + lexema);
                                                                                                        agregarAmbitoAIdentificador(lexemaParametro);
                                                                                                        TablaSimbolos.cambiarLexema(lexemaParametro, lexemaParametro + getAmbitoActual());
                                                                                                        TablaSimbolos.setCantidadDeParametros(lexema, 1);
                                                                                                        TablaSimbolos.setTipoParametro(lexema, val_peek(1).sval);
                                                                                                        TablaSimbolos.setTipoRetorno(lexema, val_peek(5).sval);
                                                                                                    }
break;
case 48:
//#line 166 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                             }
break;
case 49:
//#line 172 "gramatica.y"
{ yyval.sval = val_peek(1).sval; TablaSimbolos.cambiarTipo(val_peek(0).sval, val_peek(1).sval); agregarUsoAIdentificador(val_peek(0).sval, "nombre de parametro"); }
break;
case 50:
//#line 173 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 51:
//#line 174 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 52:
//#line 175 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": La funcion no puede tener mas de un parametro");}
break;
case 53:
//#line 176 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ yyval.ival + ": Falta el parametro de la funcion");}
break;
case 58:
//#line 185 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 59:
//#line 186 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 60:
//#line 187 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 63:
//#line 190 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 64:
//#line 191 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 65:
//#line 194 "gramatica.y"
{ yyval.ival = val_peek(1).ival;
                                                                                                  Parser.agregarEstructuraDetectadas(val_peek(1).ival, "FOR");
                                                                                                  representacionPolaca.add(aux.pop());
                                                                                                  representacionPolaca.add(aux.pop());
                                                                                                  representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()+2)); /* Se suma dos debido a los siguientes dos campos que se agregan en la polaca*/
                                                                                                  representacionPolaca.add(String.valueOf(bfs.pop()));
                                                                                                  representacionPolaca.add("BI");}
break;
case 66:
//#line 201 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 67:
//#line 204 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); representacionPolaca.add(((Token) val_peek(4).obj).getLexema()); }
break;
case 68:
//#line 205 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 69:
//#line 206 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 70:
//#line 209 "gramatica.y"
{ yyval.ival = val_peek(3).ival; Parser.agregarEstructuraDetectadas(val_peek(3).ival, "IF"); representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size())); }
break;
case 71:
//#line 210 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia de selección");}
break;
case 72:
//#line 211 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 73:
//#line 212 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el END_IF en la sentencia de selección"); }
break;
case 74:
//#line 213 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 75:
//#line 214 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 76:
//#line 215 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 77:
//#line 216 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 80:
//#line 221 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 81:
//#line 222 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 82:
//#line 225 "gramatica.y"
{yyval.ival = val_peek(0).ival;
                                                        representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size()+2));
                                                        representacionPolaca.add("");bfs.push(representacionPolaca.size()-1);
                                                        representacionPolaca.add("BI"); }
break;
case 84:
//#line 232 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ELSE en el cuerpo de la selección"); }
break;
case 85:
//#line 233 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 86:
//#line 236 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 87:
//#line 237 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 88:
//#line 238 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 92:
//#line 246 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 93:
//#line 247 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 94:
//#line 248 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 98:
//#line 254 "gramatica.y"
{
                                                                                            yyval.ival = val_peek(2).ival;
                                                                                            eliminarUltimosElementos(representacionPolaca, listaIdentificadores.size());
                                                                                            System.out.println("jeje " + listaExpresiones);
                                                                                            List<List<String>> expresiones = formatearLista(listaExpresiones);
                                                                                            System.out.println("jeje " + expresiones);

                                                                                            if (listaIdentificadores.size() == expresiones.size()) {
                                                                                                for (int i = 0; i < listaIdentificadores.size(); i++){
                                                                                                    representacionPolaca.add(listaIdentificadores.get(i));
                                                                                                    expresiones.get(i).forEach((elemento)->representacionPolaca.add(elemento));
                                                                                                    representacionPolaca.add(((Token) val_peek(1).obj).getLexema());
                                                                                                }
                                                                                            } else {
                                                                                                System.out.println("NO COINCIDEN LAS LONGITUDES");
                                                                                            }

                                                                                            listaExpresiones.clear();
                                                                                            listaIdentificadores.clear();
                                                                                         }
break;
case 99:
//#line 276 "gramatica.y"
{ yyval.ival = val_peek(3).ival; Parser.agregarEstructuraDetectadas(val_peek(3).ival, "IF"); representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size())); }
break;
case 100:
//#line 277 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia de selección");}
break;
case 101:
//#line 278 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta ';' al final de la sentencia"); }
break;
case 102:
//#line 279 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el END_IF en la sentencia de selección"); }
break;
case 103:
//#line 280 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 104:
//#line 281 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 105:
//#line 282 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 106:
//#line 283 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 107:
//#line 286 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();representacionPolaca.add("");bfs.push(representacionPolaca.size()-1); representacionPolaca.add("BF");}
break;
case 110:
//#line 291 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 111:
//#line 292 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta THEN en el cuerpo de la selección"); }
break;
case 112:
//#line 295 "gramatica.y"
{yyval.ival = val_peek(0).ival;
                                                            representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size()+2));
                                                            representacionPolaca.add("");bfs.push(representacionPolaca.size()-1);
                                                            representacionPolaca.add("BI"); }
break;
case 114:
//#line 302 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ELSE en el cuerpo de la selección"); }
break;
case 115:
//#line 303 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 116:
//#line 306 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 117:
//#line 307 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 118:
//#line 310 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 119:
//#line 311 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 120:
//#line 312 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 121:
//#line 313 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 122:
//#line 314 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 123:
//#line 315 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 124:
//#line 318 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 125:
//#line 319 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 126:
//#line 320 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
break;
case 127:
//#line 321 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 129:
//#line 325 "gramatica.y"
{ yyval.ival = ((Token) val_peek(1).obj).getNumeroDeLinea();}
break;
case 131:
//#line 329 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) val_peek(2).obj).getLexema()); representacionPolaca.add(((Token) val_peek(4).obj).getLexema());}
break;
case 132:
//#line 330 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(4).obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) val_peek(4).obj).getLexema());}
break;
case 133:
//#line 331 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 134:
//#line 332 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 135:
//#line 333 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 136:
//#line 334 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 137:
//#line 335 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 138:
//#line 336 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 139:
//#line 339 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "FOR");
                                                                            representacionPolaca.add(aux.pop());
                                                                            representacionPolaca.add(aux.pop());
                                                                            representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()+2)); /* Se suma dos debido a los siguientes dos campos que se agregan en la polaca*/
                                                                            representacionPolaca.add(String.valueOf(bfs.pop()));
                                                                            representacionPolaca.add("BI");}
break;
case 140:
//#line 345 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 141:
//#line 348 "gramatica.y"
{ yyval.ival = val_peek(5).ival;
                                                                                                                           representacionPolaca.add("");
                                                                                                                           bfs.push(representacionPolaca.size()-1);
                                                                                                                           representacionPolaca.add("BF");}
break;
case 142:
//#line 352 "gramatica.y"
{yyval.ival = val_peek(1).ival;
                                                                        representacionPolaca.add("");
                                                                        bfs.push(representacionPolaca.size()-1);
                                                                        representacionPolaca.add("BF");}
break;
case 143:
//#line 356 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 144:
//#line 357 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 145:
//#line 358 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 146:
//#line 359 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 147:
//#line 360 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 148:
//#line 363 "gramatica.y"
{ yyval.ival = val_peek(6).ival ;
                                                                                                                        representacionPolaca.remove(representacionPolaca.size()-1);}
break;
case 149:
//#line 365 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 150:
//#line 366 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta la acción en el encabezado del FOR"); }
break;
case 151:
//#line 367 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 152:
//#line 368 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(5).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 153:
//#line 371 "gramatica.y"
{yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); bfs.push(representacionPolaca.size());}
break;
case 154:
//#line 374 "gramatica.y"
{ representacionPolaca.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 155:
//#line 377 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); aux.push("UP"); aux.push(((Token) val_peek(0).obj).getLexema()); }
break;
case 156:
//#line 378 "gramatica.y"
{ listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); aux.push("DOWN"); aux.push(((Token) val_peek(0).obj).getLexema()); }
break;
case 157:
//#line 379 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 158:
//#line 382 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); }
break;
case 159:
//#line 383 "gramatica.y"
{ listaExpresiones.add(","); }
break;
case 160:
//#line 386 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 161:
//#line 387 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 162:
//#line 388 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 163:
//#line 389 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": Falta operador, operandos, o coma entre expresiones"); }
break;
case 164:
//#line 392 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 165:
//#line 393 "gramatica.y"
{ listaExpresiones.add(((Token) val_peek(1).obj).getLexema()); yyval.sval = val_peek(2).sval;}
break;
case 166:
//#line 394 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 167:
//#line 397 "gramatica.y"
{ yyval.sval = TablaSimbolos.getTipo(val_peek(0).sval);}
break;
case 168:
//#line 398 "gramatica.y"
{ yyval.sval = TablaSimbolos.getTipo(val_peek(0).sval); agregarUsoAIdentificador(val_peek(0).sval, "constante");}
break;
case 169:
//#line 399 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();  yyval.sval = "single"; }
break;
case 170:
//#line 400 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 171:
//#line 401 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 172:
//#line 404 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); listaExpresiones.add(((Token) val_peek(0).obj).getLexema());}
break;
case 173:
//#line 405 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); listaExpresiones.add(((Token) val_peek(0).obj).getLexema());}
break;
case 174:
//#line 408 "gramatica.y"
{ yyval.sval = val_peek(0).sval;}
break;
case 175:
//#line 409 "gramatica.y"
{ yyval.sval = ((Token) val_peek(1).obj).getLexema() + val_peek(0).sval; agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": la constante se va de rango. No se permiten constantes enteras negativas."); }
break;
case 176:
//#line 410 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); listaExpresiones.add(((Token) val_peek(0).obj).getLexema());}
break;
case 177:
//#line 411 "gramatica.y"
{ yyval.sval = ((Token) val_peek(0).obj).getLexema(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 178:
//#line 412 "gramatica.y"
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
case 179:
//#line 443 "gramatica.y"
{ yyval.obj = ((Token) val_peek(1).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 180:
//#line 446 "gramatica.y"
{
                                                                                                    yyval.sval = TablaSimbolos.getTipoRetorno(((Token) val_peek(3).obj).getLexema());
                                                                                                    if (!TablaSimbolos.getTipoRetorno(((Token) val_peek(3).obj).getLexema()).equals(val_peek(1).sval)) {
                                                                                                        agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea()  + ": Parametro real: " + val_peek(1).sval +". Parametro formal: " + TablaSimbolos.getTipoRetorno(((Token) val_peek(3).obj).getLexema()) + ".");
                                                                                                    }
                                                                                                    listaExpresiones.add(((Token) val_peek(3).obj).getLexema()); listaExpresiones.add("BI");

                                                                                                }
break;
case 181:
//#line 454 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 182:
//#line 455 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1934 "Parser.java"
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
