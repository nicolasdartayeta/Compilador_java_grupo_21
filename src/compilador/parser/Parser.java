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
    0,    0,    2,    2,    3,    3,    3,    4,    4,    4,
    4,    4,    9,    9,    9,    9,    9,    9,    9,    9,
    6,    6,    6,   10,   10,    7,    7,    7,   11,   11,
    1,    1,    1,   12,   12,    8,   13,   13,   15,   15,
   15,   15,   15,   14,   14,   14,   14,   16,   16,   16,
   16,   16,   16,   16,   20,   20,   21,   21,   21,   18,
   18,   18,   18,   18,   18,   18,   18,   18,   26,   26,
   27,   27,   23,   23,   23,   23,   28,   28,    5,    5,
    5,    5,    5,   17,   29,   29,   29,   29,   29,   29,
   29,   29,   29,   32,   32,   34,   34,   25,   25,   35,
   35,   35,   35,   35,   35,   33,   33,   33,   33,   33,
   36,   36,   19,   19,   19,   19,   19,   19,   19,   19,
   30,   30,   22,   22,   22,   22,   22,   22,   22,   37,
   37,   37,   37,   37,   38,   38,   39,   39,   39,   39,
   39,   39,   31,   31,   24,   24,   24,   40,   40,   40,
   40,   41,   41,   41,   42,   42,   42,   42,   43,   43,
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
    2,    1,    4,    5,    5,    1,    2,    1,    2,    2,
    1,    1,    1,    3,    8,    9,    7,    9,    7,    7,
    7,    6,    7,    2,    1,    2,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    4,    5,    3,    5,    1,
    2,    1,    5,    5,    6,    6,    6,    6,    4,    4,
    2,    2,    6,    2,    1,    5,    5,    5,    4,    7,
    6,    6,    6,    6,    3,    3,    2,    2,    2,    2,
    1,    1,    3,    1,    4,    3,    1,    3,    3,    1,
    1,    3,    3,    1,    1,    1,    2,    1,    1,    1,
    1,    4,    3,    5,
};
final static short yydefred[] = {                         0,
   41,   42,   43,    0,    0,    0,    0,   33,    0,   32,
   31,    0,    0,    0,    0,    0,   14,   15,   16,    0,
    0,   21,   22,   38,   40,    0,    0,   92,    0,   91,
   93,    0,    0,    0,    0,   17,    0,    0,    0,    0,
    0,  169,  170,  171,    0,    0,    0,  165,    0,    0,
    0,    0,  164,  166,  168,    0,    0,    0,    0,    4,
   13,    0,    0,    0,    0,   37,    0,   90,   89,  132,
    0,  120,  131,    0,  134,    0,    0,    0,    0,    0,
    5,    0,    0,    0,    0,  167,    0,    0,  112,  113,
  110,  111,  114,  115,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   35,    0,    0,    0,    0,
   44,    0,    0,    0,   18,  161,  154,    0,   36,    0,
    0,   57,    0,   56,    0,   61,   62,   63,   64,    0,
    0,  122,    0,    0,    0,    0,    0,    9,    0,    0,
    0,    0,    0,  145,  146,    0,  173,    0,    0,    0,
  156,    0,  108,    0,    0,    0,    0,    0,  162,  163,
  130,  129,    0,    0,    0,    0,    0,    0,    0,    0,
   53,    0,   50,    0,   19,   20,    0,    0,    0,    0,
   46,   55,   54,    0,   58,   66,    0,   86,   65,  118,
    0,  121,    0,    0,    0,    3,    8,    2,   12,    0,
    0,    0,    0,  172,    0,    0,    0,  155,    0,    0,
    0,  104,    0,  123,    0,  124,    0,   34,    0,    0,
    0,    0,    0,    0,   48,  153,    0,    0,    0,    0,
   59,   60,   88,    0,    0,  116,  137,    0,  138,  152,
  151,    0,    0,    0,  144,  143,  141,  174,    0,    0,
    0,    0,    0,  102,  106,  125,  127,  126,  128,    0,
    0,    0,    0,    0,   47,   52,    0,    0,    0,    0,
    0,    0,    0,   87,  117,  119,  133,  140,  147,  148,
  150,  149,  103,   99,    0,   97,  100,  101,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   79,    0,
    0,   67,    0,   83,    0,   95,    0,    0,    0,    0,
   28,    0,    0,    0,    0,    0,   81,   77,   68,   69,
   84,   85,   96,   98,   24,    0,   30,   29,   27,   78,
   74,    0,   72,   75,   76,    0,   23,    0,   70,   25,
   26,   71,   73,
};
final static short yydgoto[] = {                          5,
   15,   16,   17,   18,   19,   20,   21,   22,   23,  107,
   24,   25,   26,  123,  174,  188,   27,  126,   28,  128,
  129,   29,  270,   49,   50,  271,  299,  234,   30,   31,
  118,  155,  156,  212,   95,  133,   32,   39,  245,   51,
   52,   53,   54,   55,
};
final static short yysindex[] = {                      -163,
    0,    0,    0,  725,    0,  609, -251,    0,   18,    0,
    0,    7, -238, -168, -233,  619,    0,    0,    0, -147,
  392,    0,    0,    0,    0, -206, -249,    0,  743,    0,
    0,   62, -174,  735,  599,    0,  182, -120, -118,    0,
 -105,    0,    0,    0,  263,   60,  -95,    0,  362, -250,
  -37,  144,    0,    0,    0,  -39, -166, -111,  182,    0,
    0, -197,  -23,   96,  182,    0,  492,    0,    0,    0,
  790,    0,    0,   60,    0,   26,  725, -183,  662,  725,
    0,   37,  101,   60,   49,    0,  -97,  846,    0,    0,
    0,    0,    0,    0,   96, -134,  797,  809,  809,  809,
  809,  -94,  -82,  -75,  -21,    0,  187, -166,  289, -233,
    0,  -52,  110,  127,    0,    0,    0,  -34,    0,   73,
  -29,    0,  479,    0, -247,    0,    0,    0,    0, -101,
  -10,    0,  804,   -3,   60,   23,  672,    0,  152,   24,
  682,   60,   32,    0,    0,   43,    0,  -36,  752,  797,
    0, -150,    0,  797,   47,   54,  144,  144,    0,    0,
    0,    0, -236, -232,   99, -166,  330,  104,  182,  110,
    0,  182,    0,  115,    0,    0,   96,   60,   63,   96,
    0,    0,    0,  159,    0,    0,  557,    0,    0,    0,
 -224,    0,  122,  138,  157,    0,    0,    0,    0,  -32,
  -76,  -76,  174,    0,  745,  173,  199,    0,  205,  124,
  797,    0,  169,    0,  186,    0,  182,    0,  172,  182,
  182,   88,  230,  255,    0,    0,  113,  240,  547,  256,
    0,    0,    0, -154,  194,    0,    0,  264,    0,    0,
    0,  -76,  222,  257,    0,    0,    0,    0,  196, -102,
  266,  271,  278,    0,    0,    0,    0,    0,    0,  422,
  182,  452,  465,  305,    0,    0,  502,  547,  547,  274,
  272, -160, -155,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -138,    0,    0,    0,  316,  487,
  317,  320,  297,  540,  294,  298,  299,  547,    0,  303,
  195,    0,  201,    0,  211,    0,  311, -204,  321,  322,
    0,  325,  -93,  327,  329,  333,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -135,    0,    0,    0,    0,
    0, -117,    0,    0,    0,  228,    0,  236,    0,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  116,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  417,    0,    0,  580,    0,    0,    0,    0,  326,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  368,  200,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  616,  621,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -18,    0,    0,    0,    0,    0,    0,    0,  158,
    0,    0,    0,    0,    0,    0,    0,  -90,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  622,  627,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -30,  242,  284,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  340,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  430,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -22,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  365,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  437,    0,    0,
    0,  410,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   -5,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   50,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    4,    5,    1,  -51,  -14,  -13,  -19,    0,    0,  -24,
   -9,  586,    0,    0,  476,  -57,  -62,    0,  -59,    0,
    0,  -53, -124,  -26,  -33, -242,    0,    0,    0,    0,
    0, -131,  -27,    0,    0,    0,    0,  614, -180,  569,
  439,  451,  613,    0,
};
final static int YYTABLESIZE=1125;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         38,
   63,   73,   48,    6,  125,  189,   68,  127,  184,  124,
   35,   66,   87,  130,   72,  122,   61,  206,  207,  213,
  246,  247,  209,  215,  295,  296,  297,   38,   96,  105,
   36,  235,   69,  109,  185,   61,   48,  117,   79,   56,
  134,   97,  136,  106,  106,  214,   48,   59,  143,  216,
  146,  313,  326,   66,   48,  119,  132,  236,  148,  112,
  125,  278,  110,  127,   48,  183,   48,  125,  153,  130,
  127,  182,   48,  250,   48,   48,  130,  327,   48,   61,
  113,  137,   72,  167,  141,   48,  179,   67,   48,   48,
   48,   48,    8,    1,  106,  301,    2,    3,   36,  172,
  303,  194,    1,  173,   57,    2,    3,   36,  200,    1,
   48,  138,    2,    3,   98,   99,   58,  305,  192,   77,
  336,  302,   10,   11,  125,   48,  304,  127,  208,  233,
    4,    9,   48,  130,   72,   72,  120,   61,  338,   72,
  273,   61,   13,  306,  227,  121,  337,    8,   83,  222,
  226,   62,  218,  230,  186,    1,  172,  154,    2,    3,
  173,  108,  224,   84,  339,   94,  125,   48,   48,  127,
   48,  125,   85,  317,  127,  130,  274,   10,   11,  284,
  130,  149,   88,  255,    9,  240,  241,  161,  331,  120,
   72,   94,  187,  285,  150,   13,   72,  260,  121,  162,
  262,  263,  332,  163,  125,  125,  125,  127,  127,  127,
  243,  244,   66,  130,  130,  130,  102,    1,   41,  203,
    2,    3,   42,   43,   44,  170,   45,   98,   99,  240,
  241,  125,  114,    1,  127,  125,    2,    3,  127,  103,
  130,  290,  204,  104,  130,  177,  161,  161,  180,  242,
   66,  105,   66,   66,  243,  244,   65,  164,  115,  107,
  161,   47,   40,    1,   41,  105,    2,    3,   42,   43,
   44,  190,   45,  107,    1,  193,   80,    2,    3,  198,
   66,   40,    1,   41,   46,    2,    3,   42,   43,   44,
   80,   45,   40,    1,   41,   37,    2,    3,   42,   43,
   44,  195,   45,  135,  116,    1,   41,   47,    2,    3,
   42,   43,   44,  201,   45,   40,    1,   41,  142,    2,
    3,   42,   43,   44,  202,   45,   47,  147,   40,    1,
   41,   82,    2,    3,   42,   43,   44,   47,   45,   74,
   75,  228,  210,   76,    1,   82,  211,    2,    3,   47,
  178,  116,    1,   41,  229,    2,    3,   42,   43,   44,
   47,   45,  144,  145,  264,  171,    1,   65,    8,    2,
    3,   39,   39,   47,  217,   39,   39,   39,   39,  221,
   39,   39,   39,   39,   39,   39,   39,   39,   39,   39,
   39,  267,   39,  225,   39,   39,   47,   39,   10,   11,
  237,   39,   39,   39,  268,  254,   39,   39,  175,   39,
  100,  101,   39,   45,   45,   39,  238,   45,   45,   45,
   45,  176,   45,   45,   45,   45,   45,   45,   45,   45,
   45,   45,   45,   36,   45,  239,   45,   45,    1,   45,
  231,    2,    3,   45,   45,   45,  197,  261,   45,   45,
  256,   45,  248,  232,   45,  160,  160,   45,  165,  160,
  160,  160,  160,  257,  160,  160,  166,  258,  251,  160,
  160,  160,  160,  160,  160,  275,  319,  283,  160,  160,
  259,  160,  321,  279,  280,  160,  160,  160,  276,  320,
  160,  160,  323,  160,  252,  322,  160,  158,  158,  160,
  253,  158,  158,  158,  158,  324,  158,  158,  265,  340,
  266,  158,  158,  158,  158,  158,  158,  342,  281,  282,
  158,  158,  341,  158,   42,   43,   44,  158,  158,  158,
  343,  269,  158,  158,  272,  158,  157,  158,  158,  159,
  159,  158,  277,  159,  159,  159,  159,  286,  159,  159,
  159,  160,  287,  159,  159,  159,  159,  159,  159,  288,
  168,  293,  159,  159,  169,  159,  298,  300,  166,  159,
  159,  159,  307,  309,  159,  159,  310,  159,  311,    7,
  159,  109,  109,  159,  318,  109,  109,  109,  109,  314,
  161,  161,  325,  315,  316,  161,  161,  161,  161,  161,
  161,  219,  328,  329,  109,  220,  330,  109,  333,  166,
  334,  109,  109,  109,  335,   11,  109,  109,   51,  109,
    6,   10,  109,  157,  157,  109,    1,  157,  157,  157,
  157,   89,   90,   91,   92,   93,   94,  157,  157,  157,
  157,  157,  157,   49,  111,  223,  157,  157,    1,  157,
   82,    2,    3,  157,  157,  157,  152,   86,  157,  157,
   64,  157,    0,    0,  157,  142,  142,  157,    0,  142,
  142,   65,  135,  135,    0,    0,  135,  135,    1,    0,
    0,    2,    3,    0,    0,  139,  139,  142,  142,  139,
  139,  142,  136,  136,    0,  142,  136,  136,  289,    0,
  142,   65,  135,  142,    0,    0,  142,  135,    1,  142,
  135,    2,    3,  135,    0,  139,  135,    0,    0,    0,
  139,    1,  136,  139,    2,    3,  139,  136,  291,  139,
  136,   65,    0,  136,    0,    1,  136,    8,    2,    3,
    0,  292,    0,    1,   65,    0,    2,    3,    1,    0,
    8,    2,    3,    0,    0,    0,    0,    0,    1,    0,
    0,    2,    3,  308,    9,    0,   65,   10,   11,  120,
    0,    0,    0,  181,    0,   13,   14,    9,  121,    0,
   10,   11,  120,    0,    0,    0,    0,    9,   13,   14,
    0,  121,  120,  294,    0,  187,    1,    0,   13,    2,
    3,  121,    0,    1,    0,    0,    2,    3,    0,    0,
    0,    0,    0,    1,    0,    0,    2,    3,    0,    0,
    0,    0,    0,    0,    0,    9,    0,    0,    0,    0,
  120,    0,    9,  187,    0,  312,   13,  120,    0,  121,
  187,    0,    9,   13,    0,    0,  121,  120,    0,    0,
    0,    0,    0,   13,    7,    1,  121,    8,    2,    3,
    0,    0,    0,    0,   33,    1,    0,    8,    2,    3,
    0,    0,    0,    0,    7,    1,    0,    8,    2,    3,
    0,    0,    0,    0,    9,    0,    0,   10,   11,   12,
    0,    0,   80,   81,    9,   13,   14,   10,   11,   12,
    0,    0,   34,    0,    9,   13,   14,   10,   11,   12,
    0,    0,    0,   60,    0,   13,   14,  139,    1,    0,
    8,    2,    3,    0,    0,    0,    0,    7,    1,    0,
    8,    2,    3,    0,    0,    0,    0,    7,    1,    0,
    8,    2,    3,    0,    0,    0,    0,    9,    0,    0,
   10,   11,   12,    0,    0,    0,  140,    9,   13,   14,
   10,   11,   12,    0,    0,    0,  196,    9,   13,   14,
   10,   11,   12,    0,    0,    0,  199,    0,   13,   14,
    7,    1,    0,    8,    2,    3,    0,    0,    0,    0,
   78,    1,    0,    8,    2,    3,    0,    0,   70,    1,
    0,    1,    2,    3,    2,    3,    0,    0,    1,    0,
    9,    2,    3,   10,   11,   12,    0,    0,    0,    0,
    9,   13,   14,   10,   11,   12,    0,    0,    9,    0,
    9,   13,   14,   12,    0,   12,   71,    9,   71,   13,
  249,   13,   12,  205,    0,   71,    1,    0,   13,    2,
    3,    0,    0,    1,    0,    0,    2,    3,    0,    0,
    1,    0,    0,    2,    3,    1,   41,    0,    2,    3,
   42,   43,   44,    0,   45,    9,    0,    0,    0,    0,
   12,    0,    9,    0,  131,    0,   13,   12,    0,    9,
   71,    0,    0,   13,   12,    0,    0,    0,  191,    0,
   13,  116,    1,   41,    0,    2,    3,   42,   43,   44,
    0,   45,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  151,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   20,   29,   12,    0,   67,  130,  256,   67,  256,   67,
    6,   21,   46,   67,   29,   67,   16,  149,  150,  256,
  201,  202,  154,  256,  267,  268,  269,   37,  279,   56,
  282,  256,  282,   58,  282,   35,   46,   64,   34,  278,
   74,  292,   76,   57,   58,  282,   56,  281,   82,  282,
   84,  294,  257,   63,   64,   65,   71,  282,   85,  257,
  123,  242,   59,  123,   74,  123,   76,  130,   95,  123,
  130,  123,   82,  205,   84,   85,  130,  282,   88,   79,
  278,   77,   97,  108,   80,   95,  120,  294,   98,   99,
  100,  101,  259,  257,  108,  256,  260,  261,  282,  113,
  256,  135,  257,  113,  273,  260,  261,  282,  142,  257,
  120,  295,  260,  261,  265,  266,  285,  256,  133,  294,
  256,  282,  289,  290,  187,  135,  282,  187,  279,  187,
  294,  286,  142,  187,  149,  150,  291,  137,  256,  154,
  295,  141,  297,  282,  178,  300,  282,  259,  269,  169,
  177,  299,  166,  180,  256,  257,  170,  292,  260,  261,
  170,  273,  172,  282,  282,  256,  229,  177,  178,  229,
  180,  234,  278,  298,  234,  229,  234,  289,  290,  282,
  234,  279,  278,  211,  286,  262,  263,  282,  282,  291,
  205,  282,  294,  296,  292,  297,  211,  217,  300,  282,
  220,  221,  296,  279,  267,  268,  269,  267,  268,  269,
  287,  288,  222,  267,  268,  269,  256,  257,  258,  256,
  260,  261,  262,  263,  264,  278,  266,  265,  266,  262,
  263,  294,  256,  257,  294,  298,  260,  261,  298,  279,
  294,  261,  279,  283,  298,  280,  265,  266,  278,  282,
  260,  282,  262,  263,  287,  288,  280,  279,  282,  282,
  279,  301,  256,  257,  258,  296,  260,  261,  262,  263,
  264,  282,  266,  296,  257,  279,  282,  260,  261,  256,
  290,  256,  257,  258,  278,  260,  261,  262,  263,  264,
  296,  266,  256,  257,  258,  278,  260,  261,  262,  263,
  264,  279,  266,  278,  256,  257,  258,  301,  260,  261,
  262,  263,  264,  282,  266,  256,  257,  258,  282,  260,
  261,  262,  263,  264,  282,  266,  301,  279,  256,  257,
  258,  282,  260,  261,  262,  263,  264,  301,  266,  278,
  279,  279,  296,  282,  257,  296,  293,  260,  261,  301,
  278,  256,  257,  258,  292,  260,  261,  262,  263,  264,
  301,  266,  262,  263,  277,  256,  257,  280,  259,  260,
  261,  256,  257,  301,  276,  260,  261,  262,  263,  276,
  265,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  279,  277,  279,  279,  280,  301,  282,  289,  290,
  279,  286,  287,  288,  292,  282,  291,  292,  282,  294,
  267,  268,  297,  256,  257,  300,  279,  260,  261,  262,
  263,  295,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,  282,  277,  279,  279,  280,  257,  282,
  282,  260,  261,  286,  287,  288,  295,  276,  291,  292,
  282,  294,  279,  295,  297,  256,  257,  300,  272,  260,
  261,  262,  263,  295,  265,  266,  280,  282,  296,  270,
  271,  272,  273,  274,  275,  282,  282,  282,  279,  280,
  295,  282,  282,  262,  263,  286,  287,  288,  295,  295,
  291,  292,  282,  294,  296,  295,  297,  256,  257,  300,
  296,  260,  261,  262,  263,  295,  265,  266,  279,  282,
  256,  270,  271,  272,  273,  274,  275,  282,  262,  263,
  279,  280,  295,  282,  262,  263,  264,  286,  287,  288,
  295,  292,  291,  292,  279,  294,   98,   99,  297,  256,
  257,  300,  279,  260,  261,  262,  263,  282,  265,  266,
  100,  101,  282,  270,  271,  272,  273,  274,  275,  282,
  272,  257,  279,  280,  276,  282,  293,  296,  280,  286,
  287,  288,  257,  257,  291,  292,  257,  294,  282,    0,
  297,  256,  257,  300,  282,  260,  261,  262,  263,  296,
  265,  266,  282,  296,  296,  270,  271,  272,  273,  274,
  275,  272,  282,  282,  279,  276,  282,  282,  282,  280,
  282,  286,  287,  288,  282,    0,  291,  292,  279,  294,
    0,    0,  297,  256,  257,  300,    0,  260,  261,  262,
  263,  270,  271,  272,  273,  274,  275,  270,  271,  272,
  273,  274,  275,  279,   59,  170,  279,  280,  257,  282,
   37,  260,  261,  286,  287,  288,   88,   45,  291,  292,
  269,  294,   -1,   -1,  297,  256,  257,  300,   -1,  260,
  261,  280,  256,  257,   -1,   -1,  260,  261,  257,   -1,
   -1,  260,  261,   -1,   -1,  256,  257,  278,  279,  260,
  261,  282,  256,  257,   -1,  286,  260,  261,  277,   -1,
  291,  280,  286,  294,   -1,   -1,  297,  291,  257,  300,
  294,  260,  261,  297,   -1,  286,  300,   -1,   -1,   -1,
  291,  257,  286,  294,  260,  261,  297,  291,  277,  300,
  294,  280,   -1,  297,   -1,  257,  300,  259,  260,  261,
   -1,  277,   -1,  257,  280,   -1,  260,  261,  257,   -1,
  259,  260,  261,   -1,   -1,   -1,   -1,   -1,  257,   -1,
   -1,  260,  261,  277,  286,   -1,  280,  289,  290,  291,
   -1,   -1,   -1,  295,   -1,  297,  298,  286,  300,   -1,
  289,  290,  291,   -1,   -1,   -1,   -1,  286,  297,  298,
   -1,  300,  291,  292,   -1,  294,  257,   -1,  297,  260,
  261,  300,   -1,  257,   -1,   -1,  260,  261,   -1,   -1,
   -1,   -1,   -1,  257,   -1,   -1,  260,  261,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  286,   -1,   -1,   -1,   -1,
  291,   -1,  286,  294,   -1,  296,  297,  291,   -1,  300,
  294,   -1,  286,  297,   -1,   -1,  300,  291,   -1,   -1,
   -1,   -1,   -1,  297,  256,  257,  300,  259,  260,  261,
   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,
   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,
   -1,   -1,   -1,   -1,  286,   -1,   -1,  289,  290,  291,
   -1,   -1,  294,  295,  286,  297,  298,  289,  290,  291,
   -1,   -1,  294,   -1,  286,  297,  298,  289,  290,  291,
   -1,   -1,   -1,  295,   -1,  297,  298,  256,  257,   -1,
  259,  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,
  259,  260,  261,   -1,   -1,   -1,   -1,  256,  257,   -1,
  259,  260,  261,   -1,   -1,   -1,   -1,  286,   -1,   -1,
  289,  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,
  289,  290,  291,   -1,   -1,   -1,  295,  286,  297,  298,
  289,  290,  291,   -1,   -1,   -1,  295,   -1,  297,  298,
  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,
  256,  257,   -1,  259,  260,  261,   -1,   -1,  256,  257,
   -1,  257,  260,  261,  260,  261,   -1,   -1,  257,   -1,
  286,  260,  261,  289,  290,  291,   -1,   -1,   -1,   -1,
  286,  297,  298,  289,  290,  291,   -1,   -1,  286,   -1,
  286,  297,  298,  291,   -1,  291,  294,  286,  294,  297,
  296,  297,  291,  292,   -1,  294,  257,   -1,  297,  260,
  261,   -1,   -1,  257,   -1,   -1,  260,  261,   -1,   -1,
  257,   -1,   -1,  260,  261,  257,  258,   -1,  260,  261,
  262,  263,  264,   -1,  266,  286,   -1,   -1,   -1,   -1,
  291,   -1,  286,   -1,  295,   -1,  297,  291,   -1,  286,
  294,   -1,   -1,  297,  291,   -1,   -1,   -1,  295,   -1,
  297,  256,  257,  258,   -1,  260,  261,  262,  263,  264,
   -1,  266,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  279,
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
"PARENTESIS_R","COMA","PUNTO","PUNTO_Y_COMA","INLINE_STRING","ERROR","STRUCT",
"FOR","UP","DOWN","SINGLE","ULONGINT","IF","THEN","ELSE","BEGIN","END","END_IF",
"OUTF","TYPEDEF","FUN","RET","TOS",
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
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA",
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R error PUNTO_Y_COMA",
"sentencia_retorno : RET PARENTESIS_L expresion PARENTESIS_R error END",
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
"sentencia_ejecutable : sentencia_asignacion error",
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
"bloque_de_sent_ejecutables : BEGIN END PUNTO_Y_COMA",
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
"accion : CONSTANTE_OCTAL",
"accion : CONSTANTE_DECIMAL",
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
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion error PARENTESIS_R",
};

//#line 342 "gramatica.y"
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
  parsingConErrores = true;
  System.out.println("Error: " + string );
}
//#line 837 "Parser.java"
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
{ Parser.agregarEstructuraDetectadas( yyval.ival = val_peek(3).ival, "PROGRAMA"); }
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
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ( yyval.ival = val_peek(2).ival +1) + ": Falta un BEGIN despues del identificador del programa"); }
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
{ Parser.agregarEstructuraDetectadas(val_peek(2).ival, "VARIABLE/S"); }
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
                                                                                                                                                }
break;
case 24:
//#line 56 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
break;
case 25:
//#line 57 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(10).obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
break;
case 26:
//#line 58 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(10).ival + ": Falta ';' al final de la sentencia"); }
break;
case 27:
//#line 59 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
break;
case 28:
//#line 60 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(7).obj).getNumeroDeLinea() + ": Falta simbolos '<' y '>' en la declaración del STRUCT");}
break;
case 29:
//#line 61 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
break;
case 30:
//#line 62 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}
break;
case 31:
//#line 66 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 32:
//#line 67 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 33:
//#line 68 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 37:
//#line 76 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ',' entre las variables"); }
break;
case 38:
//#line 77 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 39:
//#line 80 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 40:
//#line 81 "gramatica.y"
{ yyval.ival = val_peek(0).ival; }
break;
case 41:
//#line 84 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 42:
//#line 85 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 43:
//#line 86 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 44:
//#line 89 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 45:
//#line 90 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 46:
//#line 93 "gramatica.y"
{
                                                                                yyval.ival = val_peek(3).ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
break;
case 47:
//#line 102 "gramatica.y"
{
                                                                                                        yyval.ival = val_peek(5).ival;
                                                                                                        String lexema = ((Token) val_peek(3).obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
break;
case 48:
//#line 107 "gramatica.y"
{
                                                                                yyval.ival = val_peek(4).ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el identificador de la funcion");
                                                                             }
break;
case 50:
//#line 114 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el tipo del parametro de la funcion"); }
break;
case 51:
//#line 115 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el identificador del parametro de la funcion"); }
break;
case 52:
//#line 116 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": La funcion no puede tener mas de un parametro");}
break;
case 53:
//#line 117 "gramatica.y"
{agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ yyval.ival + ": Falta el parametro de la funcion");}
break;
case 58:
//#line 126 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 59:
//#line 127 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 60:
//#line 128 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 61:
//#line 129 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "IF"); }
break;
case 63:
//#line 131 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 64:
//#line 132 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "RET"); returnEncontrado = true; }
break;
case 65:
//#line 135 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 66:
//#line 136 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 67:
//#line 139 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); }
break;
case 68:
//#line 140 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 69:
//#line 141 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 70:
//#line 144 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 71:
//#line 145 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 72:
//#line 146 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
break;
case 73:
//#line 147 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
break;
case 74:
//#line 148 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 75:
//#line 149 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 76:
//#line 150 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 77:
//#line 151 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 78:
//#line 152 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 82:
//#line 160 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 84:
//#line 164 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 85:
//#line 165 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 89:
//#line 173 "gramatica.y"
{ yyval.ival = val_peek(1).ival; Parser.agregarEstructuraDetectadas(val_peek(1).ival, "ASIGNACION"); }
break;
case 90:
//#line 174 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ';' al final de la sentencia"); }
break;
case 93:
//#line 178 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(0).ival, "FOR"); }
break;
case 94:
//#line 181 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 95:
//#line 184 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 96:
//#line 185 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia de selección");}
break;
case 97:
//#line 186 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
break;
case 98:
//#line 187 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 99:
//#line 188 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 100:
//#line 189 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 101:
//#line 190 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 102:
//#line 191 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 103:
//#line 192 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 107:
//#line 200 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 109:
//#line 204 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 110:
//#line 207 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 111:
//#line 208 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 112:
//#line 209 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 113:
//#line 210 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 114:
//#line 211 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 115:
//#line 212 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 117:
//#line 216 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 118:
//#line 217 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
break;
case 119:
//#line 218 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 123:
//#line 226 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 124:
//#line 227 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
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
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 129:
//#line 232 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 130:
//#line 233 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 131:
//#line 236 "gramatica.y"
{ yyval.ival = val_peek(1).ival; }
break;
case 132:
//#line 237 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 133:
//#line 240 "gramatica.y"
{yyval.ival = val_peek(5).ival;}
break;
case 134:
//#line 241 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 135:
//#line 242 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 136:
//#line 243 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 137:
//#line 244 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 138:
//#line 245 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 139:
//#line 246 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 140:
//#line 249 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 141:
//#line 250 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 142:
//#line 251 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 143:
//#line 252 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 144:
//#line 253 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 151:
//#line 264 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 152:
//#line 265 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 155:
//#line 272 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 156:
//#line 273 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 161:
//#line 280 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + " :Falta operador u operandos"); }
break;
case 167:
//#line 290 "gramatica.y"
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
case 169:
//#line 331 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 170:
//#line 332 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 171:
//#line 333 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 173:
//#line 337 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 174:
//#line 338 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1537 "Parser.java"
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
