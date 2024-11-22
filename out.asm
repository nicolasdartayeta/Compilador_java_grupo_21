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
	_c.a_main dd 0
	@retValSingle REAL4 0.0
	_d.e_main REAL4 0.0
	_d.d.b_main REAL4 0.0
	_c.b_main REAL4 0.0
	@f64printVariable REAL8 0.0
	_d.d.a_main dd 0
	@retValUlongint dd 0
	@_Programa_terminado db "Programa terminado", 0
	_5f0 REAL4 5.0
	funcionActual dd 0
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
	errorRecursion db "Error: No se permite la recursion", 0
.code
start:
	FLD _5f0
	FSTP _d.e_main
	FLD _d.d.b_main
	FSTP @f64printVariable
	invoke printf, cfm$("%.20Lf\n"), @f64printVariable
	FLD _d.e_main
	FSTP @f64printVariable
	invoke printf, cfm$("%.20Lf\n"), @f64printVariable
	invoke printf, ADDR @_Programa_terminado
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