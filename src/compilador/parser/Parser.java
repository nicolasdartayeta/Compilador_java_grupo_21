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
    1,    1,    1,   12,   12,    8,   13,   13,   15,   15,
   15,   15,   15,   14,   14,   14,   14,   16,   16,   16,
   16,   16,   16,   16,   20,   20,   21,   21,   21,   18,
   18,   18,   18,   18,   18,   18,   18,   18,   26,   26,
   27,   27,   23,   23,   23,   23,   28,   28,    5,    5,
    5,    5,    5,    5,   17,   29,   29,   29,   29,   29,
   29,   29,   29,   29,   32,   32,   34,   34,   25,   25,
   35,   35,   35,   35,   35,   35,   33,   33,   33,   33,
   33,   36,   36,   19,   19,   19,   19,   19,   19,   19,
   19,   30,   30,   22,   22,   22,   22,   22,   22,   22,
   37,   37,   37,   37,   37,   38,   38,   39,   39,   39,
   39,   39,   39,   31,   31,   24,   40,   40,   40,   40,
   41,   41,   41,   42,   42,   42,   42,   42,   43,   43,
   43,   43,   43,   43,   43,   43,   44,   44,   44,
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
    2,    1,    4,    5,    5,    1,    2,    1,    2,    3,
    3,    1,    1,    1,    3,    8,    9,    7,    9,    7,
    7,    7,    6,    7,    2,    1,    2,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    4,    5,    3,    5,
    1,    2,    1,    5,    5,    6,    6,    6,    6,    4,
    4,    2,    2,    6,    2,    1,    5,    5,    5,    4,
    7,    6,    6,    6,    6,    3,    3,    2,    2,    2,
    2,    1,    1,    3,    1,    1,    3,    3,    1,    1,
    3,    3,    1,    1,    1,    4,    3,    1,    1,    1,
    1,    1,    2,    2,    2,    2,    4,    3,    5,
};
final static short yydefred[] = {                         0,
   41,   42,   43,    0,    0,    0,    0,   33,    0,   32,
   31,    0,    0,    0,    0,    0,   14,   15,   16,    0,
    0,   21,   22,   38,   40,    0,    0,   93,    0,   92,
   94,    0,    0,    0,    0,   17,    0,    0,    0,    0,
    0,  169,  170,  171,    0,    0,  172,    0,  164,    0,
    0,    0,    0,  163,  165,  168,    0,    0,    0,    0,
    4,   13,    0,    0,    0,    0,   37,    0,    0,   89,
  133,    0,  121,  132,    0,  135,    0,    0,    0,    0,
    0,    5,    0,    0,    0,    0,  173,  174,  175,  176,
    0,    0,  113,  114,  111,  112,  115,  116,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   35,
    0,    0,    0,    0,   44,    0,    0,    0,   18,  160,
  155,    0,   36,    0,    0,   57,    0,   56,    0,   61,
   62,   63,   64,    0,   90,   91,    0,  123,    0,    0,
    0,    0,    0,    9,    0,    0,    0,    0,    0,  146,
  147,    0,  178,    0,    0,    0,  167,    0,  109,    0,
    0,    0,    0,    0,  161,  162,  131,  130,    0,    0,
    0,    0,    0,    0,    0,    0,   53,    0,   50,    0,
   19,   20,    0,    0,    0,    0,   46,   55,   54,    0,
   58,   66,    0,   86,   65,  119,    0,  122,    0,    0,
    0,    3,    8,    2,   12,    0,    0,    0,    0,  177,
    0,    0,    0,  166,    0,    0,    0,  105,    0,  124,
    0,  125,    0,   34,    0,    0,    0,    0,    0,    0,
   48,  154,    0,    0,    0,    0,   59,   60,   88,    0,
    0,  117,  138,    0,  139,  153,  152,    0,    0,    0,
  145,  144,  142,  179,    0,    0,    0,    0,    0,  103,
  107,  126,  128,  127,  129,    0,    0,    0,    0,    0,
   47,   52,    0,    0,    0,    0,    0,    0,    0,   87,
  118,  120,  134,  141,  148,  149,  151,  150,  104,  100,
    0,   98,  101,  102,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   79,    0,    0,   67,    0,   83,
    0,   96,    0,    0,    0,    0,   28,    0,    0,    0,
    0,    0,   81,   77,   68,   69,   84,   85,   97,   99,
   24,    0,   30,   29,   27,   78,   74,    0,   72,   75,
   76,    0,   23,    0,   70,   25,   26,   71,   73,
};
final static short yydgoto[] = {                          5,
   15,   16,   17,   18,   19,   20,   21,   22,   23,  111,
   24,   25,   26,  127,  180,  194,   27,  130,   28,  132,
  133,   29,  276,   50,   51,  277,  305,  240,   30,   31,
  122,  161,  162,  218,   99,  139,   32,   39,  251,   52,
   53,   54,   55,   56,
};
final static short yysindex[] = {                      -219,
    0,    0,    0,  776,    0,  693, -253,    0,   98,    0,
    0,   16, -229, -241, -195,  703,    0,    0,    0, -159,
  164,    0,    0,    0,    0, -197, -230,    0,  827,    0,
    0,  276,  -99,  819,  650,    0,  120, -155, -164,    0,
 -157,    0,    0,    0,   45,  142,    0, -135,    0,  439,
 -162, -100,   18,    0,    0,    0,    5, -153, -170,  120,
    0,    0, -244,   93,  156,  120,    0,  574, -110,    0,
    0,  191,    0,    0,  142,    0,   35,  776,  -81,  713,
  776,    0,   64,   69,  142,   77,    0,    0,    0,    0,
  -40,  106,    0,    0,    0,    0,    0,    0,  156, -137,
  843,  486,  486,  486,  486,  -90,  -50,  -61,  -36,    0,
  -57, -153,  -39, -195,    0,  -56,   55,  -12,    0,    0,
    0,  -31,    0,  131,   -3,    0,  561,    0, -223,    0,
    0,    0,    0,  497,    0,    0,  -18,    0,  670,    2,
  142,   23,  756,    0,    8,   48,  766,  142,   60,    0,
    0,   75,    0, -145,  834,  843,    0, -115,    0,  843,
   78,   86,   18,   18,    0,    0,    0,    0, -220, -172,
  110, -153,  155,  125,  120,   55,    0,  120,    0,  103,
    0,    0,  156,  142,   26,  156,    0,    0,    0,   28,
    0,    0,  637,    0,    0,    0, -156,    0,  132,  151,
  190,    0,    0,    0,    0,  -82,  -42,  -42,  192,    0,
  836,  178,  182,    0,  187,  212,  843,    0,   65,    0,
   89,    0,  120,    0,  223,  120,  120,  504,  234,  260,
    0,    0,  104,  226,  622,  240,    0,    0,    0,  629,
  128,    0,    0,  241,    0,    0,    0,  -42,  174,  176,
    0,    0,    0,    0,  243, -242,  246,  248,  254,    0,
    0,    0,    0,    0,    0,  547,  120,  569,  878,  274,
    0,    0,  584,  622,  622,  251,  245, -149, -144,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -134,    0,    0,    0,  288,  884,  289,  296,  278,  -98,
  265,  266,  270,  622,    0,  285,  146,    0,  147,    0,
  152,    0,  290, -252,  291,  301,    0,  304, -235,  305,
  306,  313,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -133,    0,    0,    0,    0,    0, -129,    0,    0,
    0,  194,    0,  229,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  193,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  499,    0,    0,  570,    0,    0,    0,    0,  403,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  445,  277,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  578,  596,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -35,    0,    0,    0,    0,
    0,    0,    0,  235,    0,    0,    0,    0,    0,    0,
    0, -121,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  597,  600,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -183,  319,  361,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  323,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  512,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -114,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  324,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  519,    0,    0,    0,  -44,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -112,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -109,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   20,    1,   11,  -60,  -14,  -48,  -19,    0,    0,  -41,
   -9,  544,    0,    0,  432,  -62,  -64,    0,  -49,    0,
    0,  -46, -125,  -26,  -32, -250,    0,    0,    0,    0,
    0, -139,  -27,    0,    0,    0,    0,  572, -103,  520,
  249,  341,    0,    0,
};
final static int YYTABLESIZE=1164;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         38,
   64,   74,   49,  129,  332,  128,   35,  126,  195,  110,
  110,   67,  116,   91,   73,  212,  213,  113,  131,    6,
  215,  134,  301,  302,  303,   69,   62,   38,   36,  333,
  109,   58,  190,  117,   80,  219,   49,    1,  121,  290,
    2,    3,  140,   59,  142,   62,  337,   49,   57,  319,
  149,   70,  152,  291,   67,   49,  123,  138,  191,  154,
  338,  220,  129,  110,  189,   49,  188,   49,  178,  129,
  173,  256,  159,   49,    4,   49,   49,  131,  143,  114,
  134,  147,   49,  221,  131,   60,   73,  134,    8,   49,
   62,  185,   49,   49,   49,   49,   68,    1,  106,  241,
    2,    3,  112,  252,  253,    8,  307,  179,  200,  222,
  209,  309,  106,   84,   49,  206,  100,   85,   10,   11,
   86,  311,  342,  224,  198,  242,  344,  178,  129,  101,
  239,   49,  308,  210,   95,   10,   11,  310,   49,   63,
   73,   73,   92,  131,  284,   73,  134,  312,  343,  102,
  103,  233,  345,   62,  160,  228,  232,   62,    1,  236,
   95,    2,    3,  214,  102,  103,  179,  108,  230,   80,
  129,  135,   82,   49,   49,  129,   49,  280,  323,  246,
  247,  108,   36,   80,  136,  131,   82,    9,  134,  261,
  131,  167,  124,  134,   78,  193,   73,  318,   13,  248,
   36,  125,   73,  266,  249,  250,  268,  269,  129,  129,
  129,  143,  143,  144,  171,  143,  143,  169,   67,  246,
  247,  176,  172,  131,  131,  131,  134,  134,  134,  160,
  160,  168,  174,  143,  143,  129,  175,  143,  155,  129,
  172,  143,  170,  160,  249,  250,  143,  296,  183,  143,
  131,  156,  143,  134,  131,  143,   67,  134,   67,   67,
  106,    1,   41,  196,    2,    3,   42,   43,   44,  181,
   45,   40,    1,   41,  186,    2,    3,   42,   43,   44,
  199,   45,  182,  107,  104,  105,   67,  108,   47,   36,
   40,    1,   41,   46,    2,    3,   42,   43,   44,   47,
   45,  201,  203,  204,  234,   48,   87,   88,   89,  237,
  177,    1,  141,    8,    2,    3,   48,  235,   47,   40,
    1,   41,  238,    2,    3,   42,   43,   44,   90,   45,
  150,  151,  120,    1,   41,   48,    2,    3,   42,   43,
   44,  207,   45,   10,   11,  148,  262,   47,  118,    1,
  163,  164,    2,    3,    1,  153,  208,    2,    3,  263,
   47,  120,    1,   41,   48,    2,    3,   42,   43,   44,
  264,   45,   66,  216,  119,   37,    1,   48,  217,    2,
    3,  231,  273,  265,  157,  223,   40,    1,   41,   47,
    2,    3,   42,   43,   44,  274,   45,   40,    1,   41,
  227,    2,    3,   42,   43,   44,   48,   45,  184,  281,
  243,  120,    1,   41,   47,    2,    3,   42,   43,   44,
    1,   45,  282,    2,    3,   47,  225,  325,  327,  244,
  226,   48,   65,  329,  172,  285,  286,  287,  288,   47,
  326,  328,   48,   66,  165,  166,  330,    1,   39,   39,
    2,    3,   39,   39,   39,   39,   48,   39,   39,   39,
   39,   39,   39,   39,   39,   39,   39,   39,  245,   39,
  254,   39,   39,  257,   39,  346,    9,  258,   39,   39,
   39,   12,  259,   39,   39,  137,   39,   13,  347,   39,
   45,   45,   39,  260,   45,   45,   45,   45,  267,   45,
   45,   45,   45,   45,   45,   45,   45,   45,   45,   45,
  348,   45,  271,   45,   45,  272,   45,  275,  278,  283,
   45,   45,   45,  349,  289,   45,   45,  292,   45,  293,
  299,   45,  159,  159,   45,  294,  159,  159,  159,  159,
  306,  159,  159,  304,  313,  315,  159,  159,  159,  159,
  159,  159,  316,   75,   76,  159,  159,   77,  159,  317,
  320,  321,  159,  159,  159,  322,  324,  159,  159,    7,
  159,  331,  334,  159,  157,  157,  159,   11,  157,  157,
  157,  157,  335,  157,  157,  336,  339,  340,  157,  157,
  157,  157,  157,  157,  341,    6,   10,  157,  157,    1,
  157,   51,   49,  115,  157,  157,  157,  229,   83,  157,
  157,  158,  157,    0,    0,  157,  158,  158,  157,    0,
  158,  158,  158,  158,    0,  158,  158,    0,    0,    0,
  158,  158,  158,  158,  158,  158,    0,    0,    0,  158,
  158,    0,  158,    0,    0,    0,  158,  158,  158,    0,
    0,  158,  158,    0,  158,    0,    0,  158,  110,  110,
  158,    0,  110,  110,  110,  110,    0,  160,  160,    0,
    0,    0,  160,  160,  160,  160,  160,  160,    0,    0,
    0,  110,    0,    0,  110,    0,    0,    0,  110,  110,
  110,    0,    0,  110,  110,    0,  110,    0,    0,  110,
  156,  156,  110,    0,  156,  156,  156,  156,   93,   94,
   95,   96,   97,   98,  156,  156,  156,  156,  156,  156,
    0,    0,    0,  156,  156,    0,  156,    0,    0,    0,
  156,  156,  156,    0,    0,  156,  156,    0,  156,    0,
    0,  156,    1,   41,  156,    2,    3,   42,   43,   44,
    0,   45,  192,    1,  136,  136,    2,    3,  136,  136,
    1,    0,    0,    2,    3,    0,    0,  140,  140,   47,
    0,  140,  140,    0,  137,  137,    0,    0,  137,  137,
  270,    0,    9,   66,  136,    0,   48,  124,    0,  136,
  193,    0,  136,   13,    0,  136,  125,  140,  136,    0,
    0,    0,  140,    1,  137,  140,    2,    3,  140,  137,
    0,  140,  137,    0,    0,  137,    0,    1,  137,    8,
    2,    3,    0,  295,    0,    1,   66,    0,    2,    3,
    1,    0,    8,    2,    3,    0,    0,    0,    0,    0,
    1,    0,    0,    2,    3,  297,    9,    0,   66,   10,
   11,  124,    0,    0,    0,  187,    0,   13,   14,    9,
  125,    0,   10,   11,  124,    0,    0,    0,    0,    9,
   13,   14,    0,  125,  124,  300,    0,  193,    1,    0,
   13,    2,    3,  125,    0,    1,    0,    0,    2,    3,
    0,    0,    0,    1,    0,    0,    2,    3,    0,    0,
    0,    0,    0,    0,    0,    7,    1,    9,    8,    2,
    3,    0,  124,    0,    9,  193,    0,    0,   13,  124,
    0,  125,    9,  279,    0,   13,    1,  124,  125,    2,
    3,    0,    0,   13,    0,    9,  125,    0,   10,   11,
   12,    0,    0,   81,   82,    0,   13,   14,   33,    1,
    0,    8,    2,    3,    0,    9,    0,    0,    7,    1,
   12,    8,    2,    3,  197,    0,   13,    0,  145,    1,
    0,    8,    2,    3,    0,    0,    0,    0,    9,    0,
    0,   10,   11,   12,    0,    0,   34,    0,    9,   13,
   14,   10,   11,   12,    0,    0,    0,   61,    9,   13,
   14,   10,   11,   12,    0,    0,    0,  146,    0,   13,
   14,    7,    1,    0,    8,    2,    3,    0,    0,    0,
    0,    7,    1,    0,    8,    2,    3,    0,    0,    0,
    0,    7,    1,    0,    8,    2,    3,    0,    0,    0,
    0,    9,    0,    0,   10,   11,   12,    0,    0,    0,
  202,    9,   13,   14,   10,   11,   12,    0,    0,    0,
  205,    9,   13,   14,   10,   11,   12,    0,    0,    0,
    0,    0,   13,   14,   79,    1,    0,    8,    2,    3,
    0,    0,   71,    1,    0,    0,    2,    3,    0,    0,
    1,    0,    1,    2,    3,    2,    3,    0,    0,    1,
    0,    0,    2,    3,    9,    0,    0,   10,   11,   12,
    0,    0,    9,    0,    0,   13,   14,   12,    0,    9,
   72,    9,    0,   13,   12,  211,   12,   72,    9,   72,
   13,  255,   13,   12,    1,    0,   72,    2,    3,   13,
    1,    0,    0,    2,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  298,    0,    0,   66,    0,    0,
  314,    0,    0,   66,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   20,   29,   12,   68,  257,   68,    6,   68,  134,   58,
   59,   21,  257,   46,   29,  155,  156,   59,   68,    0,
  160,   68,  273,  274,  275,  256,   16,   37,  282,  282,
   57,  273,  256,  278,   34,  256,   46,  257,   65,  282,
  260,  261,   75,  285,   77,   35,  282,   57,  278,  300,
   83,  282,   85,  296,   64,   65,   66,   72,  282,   86,
  296,  282,  127,  112,  127,   75,  127,   77,  117,  134,
  112,  211,   99,   83,  294,   85,   86,  127,   78,   60,
  127,   81,   92,  256,  134,  281,  101,  134,  259,   99,
   80,  124,  102,  103,  104,  105,  294,  257,  282,  256,
  260,  261,  273,  207,  208,  259,  256,  117,  141,  282,
  256,  256,  296,  269,  124,  148,  279,  282,  289,  290,
  278,  256,  256,  172,  139,  282,  256,  176,  193,  292,
  193,  141,  282,  279,  256,  289,  290,  282,  148,  299,
  155,  156,  278,  193,  248,  160,  193,  282,  282,  265,
  266,  184,  282,  143,  292,  175,  183,  147,  257,  186,
  282,  260,  261,  279,  265,  266,  176,  282,  178,  282,
  235,  282,  282,  183,  184,  240,  186,  240,  304,  262,
  263,  296,  282,  296,  295,  235,  296,  286,  235,  217,
  240,  282,  291,  240,  294,  294,  211,  296,  297,  282,
  282,  300,  217,  223,  287,  288,  226,  227,  273,  274,
  275,  256,  257,  295,  272,  260,  261,  279,  228,  262,
  263,  278,  280,  273,  274,  275,  273,  274,  275,  265,
  266,  282,  272,  278,  279,  300,  276,  282,  279,  304,
  280,  286,  279,  279,  287,  288,  291,  267,  280,  294,
  300,  292,  297,  300,  304,  300,  266,  304,  268,  269,
  256,  257,  258,  282,  260,  261,  262,  263,  264,  282,
  266,  256,  257,  258,  278,  260,  261,  262,  263,  264,
  279,  266,  295,  279,  267,  268,  296,  283,  284,  282,
  256,  257,  258,  278,  260,  261,  262,  263,  264,  284,
  266,  279,  295,  256,  279,  301,  262,  263,  264,  282,
  256,  257,  278,  259,  260,  261,  301,  292,  284,  256,
  257,  258,  295,  260,  261,  262,  263,  264,  284,  266,
  262,  263,  256,  257,  258,  301,  260,  261,  262,  263,
  264,  282,  266,  289,  290,  282,  282,  284,  256,  257,
  102,  103,  260,  261,  257,  279,  282,  260,  261,  295,
  284,  256,  257,  258,  301,  260,  261,  262,  263,  264,
  282,  266,  280,  296,  282,  278,  257,  301,  293,  260,
  261,  279,  279,  295,  279,  276,  256,  257,  258,  284,
  260,  261,  262,  263,  264,  292,  266,  256,  257,  258,
  276,  260,  261,  262,  263,  264,  301,  266,  278,  282,
  279,  256,  257,  258,  284,  260,  261,  262,  263,  264,
  257,  266,  295,  260,  261,  284,  272,  282,  282,  279,
  276,  301,  269,  282,  280,  262,  263,  262,  263,  284,
  295,  295,  301,  280,  104,  105,  295,  257,  256,  257,
  260,  261,  260,  261,  262,  263,  301,  265,  266,  267,
  268,  269,  270,  271,  272,  273,  274,  275,  279,  277,
  279,  279,  280,  296,  282,  282,  286,  296,  286,  287,
  288,  291,  296,  291,  292,  295,  294,  297,  295,  297,
  256,  257,  300,  282,  260,  261,  262,  263,  276,  265,
  266,  267,  268,  269,  270,  271,  272,  273,  274,  275,
  282,  277,  279,  279,  280,  256,  282,  292,  279,  279,
  286,  287,  288,  295,  282,  291,  292,  282,  294,  282,
  257,  297,  256,  257,  300,  282,  260,  261,  262,  263,
  296,  265,  266,  293,  257,  257,  270,  271,  272,  273,
  274,  275,  257,  278,  279,  279,  280,  282,  282,  282,
  296,  296,  286,  287,  288,  296,  282,  291,  292,    0,
  294,  282,  282,  297,  256,  257,  300,    0,  260,  261,
  262,  263,  282,  265,  266,  282,  282,  282,  270,  271,
  272,  273,  274,  275,  282,    0,    0,  279,  280,    0,
  282,  279,  279,   60,  286,  287,  288,  176,   37,  291,
  292,   92,  294,   -1,   -1,  297,  256,  257,  300,   -1,
  260,  261,  262,  263,   -1,  265,  266,   -1,   -1,   -1,
  270,  271,  272,  273,  274,  275,   -1,   -1,   -1,  279,
  280,   -1,  282,   -1,   -1,   -1,  286,  287,  288,   -1,
   -1,  291,  292,   -1,  294,   -1,   -1,  297,  256,  257,
  300,   -1,  260,  261,  262,  263,   -1,  265,  266,   -1,
   -1,   -1,  270,  271,  272,  273,  274,  275,   -1,   -1,
   -1,  279,   -1,   -1,  282,   -1,   -1,   -1,  286,  287,
  288,   -1,   -1,  291,  292,   -1,  294,   -1,   -1,  297,
  256,  257,  300,   -1,  260,  261,  262,  263,  270,  271,
  272,  273,  274,  275,  270,  271,  272,  273,  274,  275,
   -1,   -1,   -1,  279,  280,   -1,  282,   -1,   -1,   -1,
  286,  287,  288,   -1,   -1,  291,  292,   -1,  294,   -1,
   -1,  297,  257,  258,  300,  260,  261,  262,  263,  264,
   -1,  266,  256,  257,  256,  257,  260,  261,  260,  261,
  257,   -1,   -1,  260,  261,   -1,   -1,  256,  257,  284,
   -1,  260,  261,   -1,  256,  257,   -1,   -1,  260,  261,
  277,   -1,  286,  280,  286,   -1,  301,  291,   -1,  291,
  294,   -1,  294,  297,   -1,  297,  300,  286,  300,   -1,
   -1,   -1,  291,  257,  286,  294,  260,  261,  297,  291,
   -1,  300,  294,   -1,   -1,  297,   -1,  257,  300,  259,
  260,  261,   -1,  277,   -1,  257,  280,   -1,  260,  261,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,   -1,   -1,
  257,   -1,   -1,  260,  261,  277,  286,   -1,  280,  289,
  290,  291,   -1,   -1,   -1,  295,   -1,  297,  298,  286,
  300,   -1,  289,  290,  291,   -1,   -1,   -1,   -1,  286,
  297,  298,   -1,  300,  291,  292,   -1,  294,  257,   -1,
  297,  260,  261,  300,   -1,  257,   -1,   -1,  260,  261,
   -1,   -1,   -1,  257,   -1,   -1,  260,  261,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  286,  259,  260,
  261,   -1,  291,   -1,  286,  294,   -1,   -1,  297,  291,
   -1,  300,  286,  295,   -1,  297,  257,  291,  300,  260,
  261,   -1,   -1,  297,   -1,  286,  300,   -1,  289,  290,
  291,   -1,   -1,  294,  295,   -1,  297,  298,  256,  257,
   -1,  259,  260,  261,   -1,  286,   -1,   -1,  256,  257,
  291,  259,  260,  261,  295,   -1,  297,   -1,  256,  257,
   -1,  259,  260,  261,   -1,   -1,   -1,   -1,  286,   -1,
   -1,  289,  290,  291,   -1,   -1,  294,   -1,  286,  297,
  298,  289,  290,  291,   -1,   -1,   -1,  295,  286,  297,
  298,  289,  290,  291,   -1,   -1,   -1,  295,   -1,  297,
  298,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,   -1,
   -1,  286,   -1,   -1,  289,  290,  291,   -1,   -1,   -1,
  295,  286,  297,  298,  289,  290,  291,   -1,   -1,   -1,
  295,  286,  297,  298,  289,  290,  291,   -1,   -1,   -1,
   -1,   -1,  297,  298,  256,  257,   -1,  259,  260,  261,
   -1,   -1,  256,  257,   -1,   -1,  260,  261,   -1,   -1,
  257,   -1,  257,  260,  261,  260,  261,   -1,   -1,  257,
   -1,   -1,  260,  261,  286,   -1,   -1,  289,  290,  291,
   -1,   -1,  286,   -1,   -1,  297,  298,  291,   -1,  286,
  294,  286,   -1,  297,  291,  292,  291,  294,  286,  294,
  297,  296,  297,  291,  257,   -1,  294,  260,  261,  297,
  257,   -1,   -1,  260,  261,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  277,   -1,   -1,  280,   -1,   -1,
  277,   -1,   -1,  280,
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
"factor : TOS PARENTESIS_L expresion_aritmetica PARENTESIS_R",
"factor : TOS PARENTESIS_L PARENTESIS_R",
"factor : invocacion_a_funcion",
"constante : CONSTANTE_DECIMAL",
"constante : CONSTANTE_OCTAL",
"constante : CONSTANTE_SINGLE",
"constante : TOKERROR",
"constante : RESTA CONSTANTE_DECIMAL",
"constante : RESTA CONSTANTE_OCTAL",
"constante : RESTA CONSTANTE_SINGLE",
"constante : RESTA TOKERROR",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L PARENTESIS_R",
"invocacion_a_funcion : IDENTIFICADOR_FUN PARENTESIS_L expresion error PARENTESIS_R",
};

//#line 337 "gramatica.y"
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

public static void main(String[] args) {
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
//#line 857 "Parser.java"
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
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta ',' entre las identificadores"); }
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
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 91:
//#line 175 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(2).ival + ": Falta ';' al final de la sentencia"); }
break;
case 95:
//#line 181 "gramatica.y"
{ yyval.ival = val_peek(2).ival; }
break;
case 96:
//#line 184 "gramatica.y"
{ yyval.ival = ((Token) val_peek(7).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) val_peek(7).obj).getNumeroDeLinea(), "IF"); }
break;
case 97:
//#line 185 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia de selección");}
break;
case 98:
//#line 186 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
break;
case 99:
//#line 187 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(8).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 100:
//#line 188 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
break;
case 101:
//#line 189 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
break;
case 102:
//#line 190 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
break;
case 103:
//#line 191 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
break;
case 104:
//#line 192 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(6).obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
break;
case 108:
//#line 200 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
break;
case 110:
//#line 204 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
break;
case 111:
//#line 207 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 112:
//#line 208 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 113:
//#line 209 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 114:
//#line 210 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 115:
//#line 211 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 116:
//#line 212 "gramatica.y"
{ yyval.ival = ((Token) val_peek(0).obj).getNumeroDeLinea(); }
break;
case 118:
//#line 216 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 119:
//#line 217 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
break;
case 120:
//#line 218 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 124:
//#line 226 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 125:
//#line 227 "gramatica.y"
{ yyval.ival = ((Token) val_peek(4).obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(val_peek(4).ival, "OUTF"); }
break;
case 126:
//#line 228 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 127:
//#line 229 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 128:
//#line 230 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 129:
//#line 231 "gramatica.y"
{ yyval.ival = ((Token) val_peek(5).obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
break;
case 130:
//#line 232 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
break;
case 131:
//#line 233 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(3).obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
break;
case 132:
//#line 236 "gramatica.y"
{ Parser.agregarEstructuraDetectadas(val_peek(1).ival, "FOR"); }
break;
case 133:
//#line 237 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(1).ival + ": Falta el cuerpo del FOR"); }
break;
case 134:
//#line 240 "gramatica.y"
{ yyval.ival = val_peek(5).ival; }
break;
case 135:
//#line 241 "gramatica.y"
{yyval.ival = val_peek(1).ival;}
break;
case 136:
//#line 242 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(0).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 137:
//#line 243 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 138:
//#line 244 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta un ';' en el encabezado del FOR"); }
break;
case 139:
//#line 245 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(4).ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
break;
case 140:
//#line 246 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ val_peek(3).ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
break;
case 141:
//#line 249 "gramatica.y"
{ yyval.ival = ((Token) val_peek(6).obj).getNumeroDeLinea(); }
break;
case 142:
//#line 250 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
break;
case 143:
//#line 251 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
break;
case 144:
//#line 252 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 145:
//#line 253 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(5).obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
break;
case 152:
//#line 264 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 153:
//#line 265 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
break;
case 160:
//#line 278 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": Falta operador, operandos, o coma entre expresiones"); }
break;
case 166:
//#line 288 "gramatica.y"
{yyval.ival = ((Token) val_peek(3).obj).getNumeroDeLinea();}
break;
case 167:
//#line 289 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea() + ": Falta la expresión"); }
break;
case 169:
//#line 293 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 170:
//#line 294 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 171:
//#line 295 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); }
break;
case 172:
//#line 296 "gramatica.y"
{ yyval.obj = ((Token) val_peek(0).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(0).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 173:
//#line 297 "gramatica.y"
{ yyval.obj = ((Token) val_peek(1).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": la constante se va de rango. No se permiten constantes long negativas."); }
break;
case 174:
//#line 298 "gramatica.y"
{ yyval.obj = ((Token) val_peek(1).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": la constante se va de rango. No se permiten constantes long negativas."); }
break;
case 175:
//#line 299 "gramatica.y"
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
                                                            }
                                                        }
break;
case 176:
//#line 328 "gramatica.y"
{ yyval.obj = ((Token) val_peek(1).obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(1).obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
break;
case 178:
//#line 332 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(2).obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
break;
case 179:
//#line 333 "gramatica.y"
{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) val_peek(4).obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
break;
//#line 1560 "Parser.java"
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
