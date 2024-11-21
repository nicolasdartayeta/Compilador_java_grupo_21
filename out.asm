.386
.model flat, stdcall
.STACK 200h
option casemap :none
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG
.data
	@f64printVariable REAL8 0.0
	@retValSingle REAL4 0.0
	@retValUlongint dd 0
	_b_main REAL4 0.0
	@aux1 REAL4 0.0
	_1f1 REAL4 1.1
	_3f402823466s38 REAL4 3.402823466e38
	funcionActual dd 0
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
	errorRecursion db "Error: No se permite la recursion", 0
.code
start:
	FLD _3f402823466s38
	FLD _1f1
	FMUL
	FSTSW AX
	TEST AX, 0800h
	JNZ  _errorOverflow
	FSTP @aux1
	FLD @aux1
	FSTP _b_main
	FLD _b_main
	FSTP @f64printVariable
	invoke printf, cfm$("%.20Lf\n"), @f64printVariable
	JMP _quit
_errorNegativo:
	invoke printf, ADDR errorNegativoTxt 
	JMP _quit
_errorOverflow:
	invoke printf, ADDR errorOverflowTxt 
	JMP _quit
_errorRecursion:
	invoke printf, ADDR errorRecursion 
	JMP _quit
_quit:
	invoke ExitProcess, 0
end start