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
    3,    3,    3,    3,    8,    8,    8,    8,    8,    8,
    5,    5,    5,    9,    9,    6,    6,   10,   10,   11,
   11,   11,   12,   12,    7,   13,   13,   15,   15,   15,
   14,   14,   14,   14,   16,   16,   16,   16,   16,   20,
   20,   21,   18,   18,   18,   18,   18,   18,   26,   26,
   27,   27,   23,   23,   28,   28,    4,    4,    4,    4,
    4,    4,   17,   29,   29,   29,   29,   29,   29,   32,
   32,   34,   34,   25,   35,   35,   35,   35,   35,   35,
   33,   33,   36,   36,   19,   19,   19,   30,   30,   22,
   22,   22,   22,   22,   22,   22,   37,   37,   37,   37,
   37,   38,   38,   39,   39,   39,   39,   31,   31,   24,
   24,   24,   40,   40,   40,   41,   41,   41,   42,   42,
   42,   42,   43,   43,   43,   44,   45,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    2,    1,    1,    1,    2,
    3,    2,    1,    1,   10,    9,    9,    9,    9,    8,
    1,    1,    1,    3,    1,    3,    1,    1,    1,    1,
    1,    1,    3,    3,    4,    6,    5,    2,    1,    1,
    2,    2,    1,    1,    2,    2,    2,    1,    1,    2,
    2,    5,    7,    7,    6,    6,    5,    6,    2,    1,
    2,    1,    4,    1,    2,    1,    2,    1,    2,    1,
    2,    1,    3,    7,    7,    6,    6,    5,    6,    2,
    1,    2,    1,    3,    1,    1,    1,    1,    1,    1,
    4,    1,    2,    1,    4,    4,    3,    2,    2,    6,
    2,    1,    5,    5,    5,    4,    7,    6,    6,    6,
    6,    3,    3,    2,    2,    2,    2,    3,    1,    4,
    3,    1,    3,    3,    1,    3,    3,    1,    1,    1,
    2,    1,    1,    1,    1,    4,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   30,   23,   31,   32,    0,   22,
   21,    0,    0,    0,    0,    0,    7,    8,    9,    0,
    0,   13,   14,   27,    0,   29,    0,    0,    0,    0,
    0,   72,    0,    0,   10,    0,    0,    0,    0,  133,
  134,  135,    0,    0,    0,  129,    0,    0,  122,    0,
  128,  130,  132,    0,    0,    0,    0,    3,    6,    0,
    0,    0,    0,    0,    0,   67,   71,   99,    0,   92,
   98,   69,    0,  101,    0,    2,    0,    0,    0,    0,
  131,    0,    0,    0,    0,   87,   88,   85,   86,   89,
   90,    0,    0,    0,    0,    0,    1,   97,    0,    0,
    0,   25,    0,    0,    0,    0,    0,   11,    0,    0,
   26,    0,   33,    0,    0,   44,    0,   43,    0,    0,
    0,   48,   49,    0,   94,    0,    0,    0,    0,    0,
    0,  112,  113,    0,    0,    0,    0,    0,  121,    0,
    0,    0,    0,    0,    0,    0,    0,  126,  127,   95,
   96,    0,    0,    0,    0,    0,    0,    0,   39,    0,
    0,    0,    0,    0,   35,   42,   41,   45,   46,   47,
   51,    0,   64,   50,    0,   93,    0,    0,    0,    0,
    0,    0,  136,    0,    0,  120,    0,   78,    0,   80,
    0,    0,   24,    0,    0,    0,   38,   37,    0,    0,
    0,    0,    0,   66,    0,   91,  104,    0,  105,    0,
    0,    0,  111,  110,  108,   79,    0,   76,   77,   82,
    0,    0,    0,    0,   36,    0,    0,    0,    0,    0,
    0,    0,   65,  100,  107,  114,  115,  117,  116,   75,
   74,    0,    0,    0,    0,    0,    0,    0,    0,   59,
   57,   52,   63,    0,   20,    0,    0,    0,   58,    0,
   55,   56,   61,   19,   16,    0,   18,   54,   53,   15,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   19,   20,   21,   22,   23,  103,   24,
   25,   26,   27,  117,  160,  173,   28,  120,   29,  122,
  123,   30,  229,   47,   48,  230,  250,  205,   31,   32,
  110,  146,  147,  190,   92,  126,   33,   38,  213,   49,
   50,   51,   52,   53,  136,
};
final static short yysindex[] = {                      -197,
  697,  770,    0, -252,    0,    0,    0,    0,   98,    0,
    0,  289,  770, -240, -185,  707,    0,    0,    0, -247,
 -119,    0,    0,    0, -241,    0, -198, -177, -161,  335,
 -155,    0,   90,  717,    0,   25, -136, -142, -164,    0,
    0,    0,   67,  -56, -131,    0,  236, -250,    0,  -24,
    0,    0,    0,  760, -181, -160, -172,    0,    0, -168,
  -82,  -56,   25,   25,  598,    0,    0,    0,  467,    0,
    0,    0,  -56,    0,  447,    0, -106,   47,  -56,  -56,
    0, -189,  299,  587,  587,    0,    0,    0,    0,    0,
    0,  -56, -139,  455,  587,  587,    0,    0, -116,  -25,
 -152,    0, -236, -152, -157, -105, -242,    0,   -2, -100,
    0, -241,    0,  499,  -94,    0,  582,    0,  -93,  -88,
  -73,    0,    0,  511,    0,  787,  -58,  -56,  -54,  -56,
  -71,    0,    0,  -51,   -2,  -42,  -37,  455,    0,   -2,
   21,  -24,  -24,   -2,  455,  -19,  -45,    0,    0,    0,
    0,  -98,  -17, -152,  -81,    3, -242,   25,    0,   29,
  -56,  -56, -186,  -56,    0,    0,    0,    0,    0,    0,
    0,  652,    0,    0,   22,    0,   43,   45,   81,  -68,
   66,   66,    0,  778,   53,    0,   71,    0,  455,    0,
   25,   25,    0,  118,   25,  121,    0,    0,   -2, -184,
  129,  640,  -14,    0,  650,    0,    0,  156,    0,   66,
  112,  142,    0,    0,    0,    0, -245,    0,    0,    0,
  -48,  -31,   25,  137,    0,  147,  640,  640,  150,  148,
  167,  175,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -233,  193,  168,  201,  633,  163,  166,  640,    0,
    0,    0,    0,  182,    0,  183,  209,  198,    0, -228,
    0,    0,    0,    0,    0,  202,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  488,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,  390,    0,    0,
  403,    0,  518,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   91,
    0,    0,    0,  489,    0,    0,    0,    0,    0,    0,
  226,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  284,  345,
    0,   46,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  211,    0,    0,    0,    0,    0,
  133,  136,  181,  -64,    0,    0, -225,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  212,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  538,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -223,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  327,    0,
    0,    0,    0,    0,    0,    0,    0,  570,    0,  -44,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -222,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -221,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  239,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  157,   70,  -53,  -10,  -35,  -20,    0,    0,  -34,  659,
  428,  429,    0,    0,  337,  -60,  -63,    0,  -61,    0,
    0,  -59, -117,  -30,  -36, -201,    0,    0,    0,    0,
    0, -129,  -27,    0,    0,    0,    0,  461,  -33,  420,
  328,  324,  462,    0,    0,
};
final static int YYTABLESIZE=1084;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         61,
   28,  119,   71,  121,  118,  124,  174,   82,  185,    5,
  240,  116,    7,    8,    5,  187,    6,    7,    8,   70,
  102,  102,  105,  254,  100,  247,  248,  268,   93,   35,
   81,  109,   83,   60,   62,  153,  127,   55,  129,   64,
  131,   94,  134,  154,  260,   34,   10,   11,  255,  135,
  241,   60,  140,  119,  217,  121,  167,  124,  125,    1,
  119,  144,  121,  166,  124,  102,  152,  269,  102,  155,
   81,  158,   83,   60,   62,    5,   39,  163,    7,    8,
   40,   41,   42,   70,   43,   59,    6,   56,  106,  137,
  125,  178,  201,  180,  226,   65,    2,   98,    6,   57,
  104,   99,  138,   59,   66,  202,    6,  227,  119,  107,
  121,  204,  124,   80,  156,  176,   10,   11,  193,   45,
   67,  158,  154,   59,  101,  200,   72,   70,   10,   11,
  199,  263,   78,  203,   70,  123,   10,   11,  119,   79,
  121,  119,  124,  121,  233,  124,   83,  214,  215,   62,
    5,   39,  145,    7,    8,   40,   41,   42,   34,   43,
   63,  220,  150,  119,  119,  121,  121,  124,  124,   54,
  221,  222,  157,   70,  224,  130,  235,  191,   70,  161,
  124,  154,  119,  164,  121,  119,  124,  121,  168,  124,
  194,   84,   84,  169,   45,   84,   84,   63,  154,  108,
    5,   39,  244,    7,    8,   40,   41,   42,  170,   43,
  181,  109,  109,  210,   84,  109,  109,   84,  211,  212,
  177,   84,   84,   84,  179,   12,   84,   84,  242,   84,
  182,   63,   84,  109,  109,   84,  183,  109,   17,   84,
   85,  109,   95,   96,   45,  243,  109,  189,   63,  109,
   84,   85,  109,  151,  184,  109,   28,   28,  192,   28,
   28,   28,   84,   85,  231,   28,   28,   28,   28,   28,
   28,   28,   28,   28,   28,   28,  188,   28,  195,   28,
   28,    5,   28,  119,    7,    8,   28,   28,   28,   28,
   28,   28,   28,   28,   28,   28,   28,   28,   28,  186,
   28,   34,   34,  206,   34,   34,   34,  198,  132,  133,
   34,   34,   34,   34,   34,   34,   34,   34,   34,   34,
   34,  207,   34,  208,   34,   34,  118,   34,   40,   41,
   42,   34,   34,   34,   34,   34,   34,   34,   34,   34,
   34,   34,   34,   34,   73,   34,  125,  125,  218,  125,
  125,  125,  211,  212,    5,  125,  125,    7,    8,  209,
  125,  125,  125,  125,  125,  125,  219,   73,   74,  125,
  125,   75,  125,  236,  237,   36,  125,  125,  125,  125,
  125,  125,  125,  125,  125,  125,  125,  125,  125,   68,
  125,  123,  123,  223,  123,  123,  123,  122,  122,  225,
  123,  123,   70,  238,  239,  123,  123,  123,  123,  123,
  123,  142,  143,  245,  123,  123,   63,  123,  148,  149,
  228,  123,  123,  123,  123,  123,  123,  123,  123,  123,
  123,  123,  123,  123,  234,  123,  124,  124,  246,  124,
  124,  124,  249,  251,  257,  124,  124,   63,  252,  256,
  124,  124,  124,  124,  124,  124,  253,  258,  261,  124,
  124,  262,  124,  264,  265,  266,  124,  124,  124,  124,
  124,  124,  124,  124,  124,  124,  124,  124,  124,  267,
  124,   12,   12,  270,   12,   12,   12,    5,    4,  137,
   40,  112,  113,  196,   17,   17,   77,   17,   17,   17,
   84,   85,  141,    0,   81,   86,   87,   88,   89,   90,
   91,   12,    0,    0,   12,   12,   12,    0,    0,    0,
   12,    0,   12,   12,   17,   12,    0,   17,   17,   17,
    0,    0,    0,   17,    0,   17,   17,    0,   17,  119,
  119,    0,  119,  119,  119,    5,   39,    0,    7,    8,
   40,   41,   42,    0,   43,    5,   39,    0,    7,    8,
   40,   41,   42,  119,   43,  119,   44,    0,    0,  119,
    0,    0,  119,  119,  119,    0,  119,  139,  119,  119,
  119,  119,  118,  118,    0,  118,  118,  118,    0,   45,
   68,    5,    0,    0,    7,    8,    0,    0,    0,   45,
   73,   73,    0,   73,   73,   73,  118,    0,  118,    0,
    0,    0,  118,    0,    0,  118,  118,  118,    0,  118,
    9,  118,  118,  118,  118,   12,   73,    0,   69,    0,
   73,   14,    0,   73,   73,   73,    0,   73,    0,   73,
   73,   73,   73,    0,    0,   68,   68,    0,   68,   68,
   68,    0,    0,    0,    0,    0,    0,    0,   70,   70,
    0,   70,   70,   70,    0,    0,    0,   37,    0,    0,
   46,    0,    0,    0,    0,   68,    0,    0,   68,   68,
   68,    0,   68,    0,   68,   68,   68,   68,   70,    0,
    0,   70,   70,   70,   37,   70,    0,   70,   70,   70,
   70,    0,   46,    5,   39,    0,    7,    8,   40,   41,
   42,    5,   43,   46,    7,    8,    0,    0,    0,    0,
   46,  111,    0,    5,  128,    0,    7,    8,    0,    0,
    0,   46,    0,   46,    0,   46,    0,   46,   46,    0,
    9,   46,   46,   46,    0,   12,    0,   45,   69,    0,
   46,   14,    9,   46,   46,    5,   39,   12,    7,    8,
   40,   41,   42,   14,   43,  159,  171,    5,    0,    0,
    7,    8,   46,  102,  102,    0,  162,  102,  102,    0,
    0,    0,    0,    0,    0,    0,   46,    0,   46,    0,
    0,    0,    0,  106,  106,    0,    9,  106,  106,   45,
    0,  114,    0,  102,  172,    0,    0,   14,  102,    0,
  115,  102,    0,    0,  102,  159,  197,  102,    0,   46,
   46,    0,   46,  106,    0,  103,  103,    0,  106,  103,
  103,  106,    0,    0,  106,    0,    0,  106,    5,    0,
    6,    7,    8,    5,   39,    0,    7,    8,   40,   41,
   42,    0,   43,    0,    5,  103,    6,    7,    8,    0,
  103,    0,    0,  103,    0,    0,  103,    9,    0,  103,
   10,   11,  114,    0,    0,    0,  165,    0,   14,   15,
    0,  115,    0,    9,    0,    0,   10,   11,  114,    5,
    0,    0,    7,    8,   14,   15,    5,  115,    0,    7,
    8,    0,    0,    0,    0,    0,    5,    0,    5,    7,
    8,    7,    8,    0,    0,    0,    0,    0,    9,    0,
    0,    0,    0,  114,    0,    9,  172,    0,  259,   14,
  114,    0,  115,  172,    0,    9,   14,    9,    0,  115,
  114,    0,  114,    0,  232,    0,   14,    0,   14,  115,
    0,  115,    4,    5,    0,    6,    7,    8,    0,    0,
    0,    0,    4,    5,    0,    6,    7,    8,    0,    0,
    0,    0,    4,    5,    0,    6,    7,    8,    0,    0,
    0,    0,    9,    0,    0,   10,   11,   12,    0,    0,
   13,    0,    9,   14,   15,   10,   11,   12,    0,    0,
    0,   58,    9,   14,   15,   10,   11,   12,    0,    0,
    0,   76,    0,   14,   15,    4,    5,    0,    6,    7,
    8,    0,    0,    0,    0,    4,    5,    0,    6,    7,
    8,    0,    0,    0,    5,    0,    0,    7,    8,    0,
    0,    0,    0,    5,    0,    9,    7,    8,   10,   11,
   12,    0,    0,    0,   97,    9,   14,   15,   10,   11,
   12,    0,    0,    9,    0,    0,   14,   15,   12,    0,
    0,   69,    9,  216,   14,    0,    0,   12,    0,    0,
    0,  175,    0,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   65,   30,   65,   65,   65,  124,   44,  138,  257,
  256,   65,  260,  261,  257,  145,  259,  260,  261,   30,
   56,   57,   57,  257,   55,  227,  228,  256,  279,  282,
  256,   62,  256,  256,  256,  272,   73,  278,   75,  281,
   77,  292,   79,  280,  246,    0,  289,  290,  282,   80,
  296,  299,   83,  117,  184,  117,  117,  117,   69,  257,
  124,   92,  124,  117,  124,  101,  101,  296,  104,  104,
  296,  107,  296,  296,  296,  257,  258,  114,  260,  261,
  262,  263,  264,   94,  266,   16,  259,  273,  257,  279,
    0,  128,  279,  130,  279,  294,  294,  279,  259,  285,
  273,  283,  292,   34,  282,  292,  259,  292,  172,  278,
  172,  172,  172,  278,  272,  126,  289,  290,  154,  301,
  282,  157,  280,   54,  285,  162,  282,  138,  289,  290,
  161,  249,  269,  164,  145,    0,  289,  290,  202,  282,
  202,  205,  202,  205,  205,  205,  278,  181,  182,  269,
  257,  258,  292,  260,  261,  262,  263,  264,    2,  266,
  280,  189,  279,  227,  228,  227,  228,  227,  228,   13,
  191,  192,  278,  184,  195,  282,  210,  276,  189,  280,
    0,  280,  246,  278,  246,  249,  246,  249,  282,  249,
  272,  256,  257,  282,  301,  260,  261,  280,  280,  282,
  257,  258,  223,  260,  261,  262,  263,  264,  282,  266,
  282,  256,  257,  282,  279,  260,  261,  282,  287,  288,
  279,  286,  287,  288,  279,    0,  291,  292,  277,  294,
  282,  280,  297,  278,  279,  300,  279,  282,    0,  265,
  266,  286,  267,  268,  301,  277,  291,  293,  280,  294,
  265,  266,  297,  279,  292,  300,  256,  257,  276,  259,
  260,  261,  265,  266,  279,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  275,  296,  277,  276,  279,
  280,  257,  282,    0,  260,  261,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,  279,
  300,  256,  257,  282,  259,  260,  261,  279,  262,  263,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  279,  277,  279,  279,  280,    0,  282,  262,  263,
  264,  286,  287,  288,  289,  290,  291,  292,  293,  294,
  295,  296,  297,  298,    0,  300,  256,  257,  296,  259,
  260,  261,  287,  288,  257,  265,  266,  260,  261,  279,
  270,  271,  272,  273,  274,  275,  296,  278,  279,  279,
  280,  282,  282,  262,  263,  278,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,    0,
  300,  256,  257,  276,  259,  260,  261,  265,  266,  279,
  265,  266,    0,  262,  263,  270,  271,  272,  273,  274,
  275,   84,   85,  277,  279,  280,  280,  282,   95,   96,
  292,  286,  287,  288,  289,  290,  291,  292,  293,  294,
  295,  296,  297,  298,  279,  300,  256,  257,  292,  259,
  260,  261,  293,  296,  277,  265,  266,  280,  282,  257,
  270,  271,  272,  273,  274,  275,  282,  257,  296,  279,
  280,  296,  282,  282,  282,  257,  286,  287,  288,  289,
  290,  291,  292,  293,  294,  295,  296,  297,  298,  282,
  300,  256,  257,  282,  259,  260,  261,    0,    0,  279,
  279,   64,   64,  157,  256,  257,   36,  259,  260,  261,
  265,  266,   83,   -1,   43,  270,  271,  272,  273,  274,
  275,  286,   -1,   -1,  289,  290,  291,   -1,   -1,   -1,
  295,   -1,  297,  298,  286,  300,   -1,  289,  290,  291,
   -1,   -1,   -1,  295,   -1,  297,  298,   -1,  300,  256,
  257,   -1,  259,  260,  261,  257,  258,   -1,  260,  261,
  262,  263,  264,   -1,  266,  257,  258,   -1,  260,  261,
  262,  263,  264,  280,  266,  282,  278,   -1,   -1,  286,
   -1,   -1,  289,  290,  291,   -1,  293,  279,  295,  296,
  297,  298,  256,  257,   -1,  259,  260,  261,   -1,  301,
  256,  257,   -1,   -1,  260,  261,   -1,   -1,   -1,  301,
  256,  257,   -1,  259,  260,  261,  280,   -1,  282,   -1,
   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,  293,
  286,  295,  296,  297,  298,  291,  282,   -1,  294,   -1,
  286,  297,   -1,  289,  290,  291,   -1,  293,   -1,  295,
  296,  297,  298,   -1,   -1,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,    9,   -1,   -1,
   12,   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,  290,
  291,   -1,  293,   -1,  295,  296,  297,  298,  286,   -1,
   -1,  289,  290,  291,   36,  293,   -1,  295,  296,  297,
  298,   -1,   44,  257,  258,   -1,  260,  261,  262,  263,
  264,  257,  266,   55,  260,  261,   -1,   -1,   -1,   -1,
   62,   63,   -1,  257,  278,   -1,  260,  261,   -1,   -1,
   -1,   73,   -1,   75,   -1,   77,   -1,   79,   80,   -1,
  286,   83,   84,   85,   -1,  291,   -1,  301,  294,   -1,
   92,  297,  286,   95,   96,  257,  258,  291,  260,  261,
  262,  263,  264,  297,  266,  107,  256,  257,   -1,   -1,
  260,  261,  114,  256,  257,   -1,  278,  260,  261,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  128,   -1,  130,   -1,
   -1,   -1,   -1,  256,  257,   -1,  286,  260,  261,  301,
   -1,  291,   -1,  286,  294,   -1,   -1,  297,  291,   -1,
  300,  294,   -1,   -1,  297,  157,  158,  300,   -1,  161,
  162,   -1,  164,  286,   -1,  256,  257,   -1,  291,  260,
  261,  294,   -1,   -1,  297,   -1,   -1,  300,  257,   -1,
  259,  260,  261,  257,  258,   -1,  260,  261,  262,  263,
  264,   -1,  266,   -1,  257,  286,  259,  260,  261,   -1,
  291,   -1,   -1,  294,   -1,   -1,  297,  286,   -1,  300,
  289,  290,  291,   -1,   -1,   -1,  295,   -1,  297,  298,
   -1,  300,   -1,  286,   -1,   -1,  289,  290,  291,  257,
   -1,   -1,  260,  261,  297,  298,  257,  300,   -1,  260,
  261,   -1,   -1,   -1,   -1,   -1,  257,   -1,  257,  260,
  261,  260,  261,   -1,   -1,   -1,   -1,   -1,  286,   -1,
   -1,   -1,   -1,  291,   -1,  286,  294,   -1,  296,  297,
  291,   -1,  300,  294,   -1,  286,  297,  286,   -1,  300,
  291,   -1,  291,   -1,  295,   -1,  297,   -1,  297,  300,
   -1,  300,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,   -1,  286,   -1,   -1,  289,  290,  291,   -1,   -1,
  294,   -1,  286,  297,  298,  289,  290,  291,   -1,   -1,
   -1,  295,  286,  297,  298,  289,  290,  291,   -1,   -1,
   -1,  295,   -1,  297,  298,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,  257,   -1,   -1,  260,  261,   -1,
   -1,   -1,   -1,  257,   -1,  286,  260,  261,  289,  290,
  291,   -1,   -1,   -1,  295,  286,  297,  298,  289,  290,
  291,   -1,   -1,  286,   -1,   -1,  297,  298,  291,   -1,
   -1,  294,  286,  296,  297,   -1,   -1,  291,   -1,   -1,
   -1,  295,   -1,  297,
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

//#line 308 "gramatica.y"
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
//#line 759 "Parser.java"
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
case 10:
//#line 33 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)val_peek(0).obj).getNumeroDeLinea() + ": Syntax Error");}
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
//#line 42 "gramatica.y"
{
                                                                                                                                                    yyval.ival = ((Token) val_peek(9).obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) val_peek(9).obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) val_peek(1).obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                }
break;
case 16:
//#line 48 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 17:
//#line 49 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 18:
//#line 50 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 19:
//#line 51 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 20:
//#line 52 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 21:
//#line 56 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 22:
//#line 57 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 23:
//#line 58 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 27:
//#line 67 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 28:
//#line 70 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 29:
//#line 71 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 30:
//#line 74 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 31:
//#line 75 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 32:
//#line 76 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 33:
//#line 79 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 34:
//#line 80 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 35:
//#line 83 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 36:
//#line 92 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 37:
//#line 97 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                            }
break;
case 39:
//#line 104 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 40:
//#line 105 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 45:
//#line 114 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 46:
//#line 115 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 47:
//#line 116 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 48:
//#line 117 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 49:
//#line 118 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 50:
//#line 121 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 51:
//#line 122 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 52:
//#line 125 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 53:
//#line 128 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 54:
//#line 129 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 55:
//#line 130 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 56:
//#line 131 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 57:
//#line 132 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 58:
//#line 133 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 62:
//#line 141 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 67:
//#line 152 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 68:
//#line 153 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ';' al final de la sentencia");}
break;
case 69:
//#line 154 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "IF"); }
break;
case 70:
//#line 155 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta ';' al final de la sentencia");}
break;
case 71:
//#line 156 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "SALIDA"); }
break;
case 72:
//#line 157 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 73:
//#line 160 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 74:
//#line 163 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 75:
//#line 164 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 76:
//#line 165 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 77:
//#line 166 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 78:
//#line 167 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 79:
//#line 168 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 83:
//#line 176 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 95:
//#line 198 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 96:
//#line 199 "gramatica.y"
{ yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea(); }
break;
case 97:
//#line 200 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 98:
//#line 203 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 99:
//#line 204 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 100:
//#line 207 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 101:
//#line 208 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 102:
//#line 209 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 103:
//#line 210 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 104:
//#line 211 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 105:
//#line 212 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 106:
//#line 213 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 107:
//#line 216 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 108:
//#line 217 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 109:
//#line 218 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 110:
//#line 219 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 111:
//#line 220 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 121:
//#line 238 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 131:
//#line 254 "gramatica.y"
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
case 133:
//#line 295 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 134:
//#line 296 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 135:
//#line 297 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
//#line 1291 "Parser.java"
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
