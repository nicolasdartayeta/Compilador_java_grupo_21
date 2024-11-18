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
	_a_main REAL4 0.0
	@aux1 REAL4 0.0
	_4f0s20 REAL4 4.0e20
	_4f0s30 REAL4 4.0e30
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
.code
start:
	FLD _4f0s30
	FMUL _4f0s20
	FSTSW AX
	SAHF
	JO _errorOverflow
	FSTP @aux1
	FLD @aux1
	FSTP _a_main
	FLD _a_main
	FSTP @f64printVariable
	invoke printf, cfm$("%.20Lf\n"), @f64printVariable
	JMP _quit
_errorNegativo:
	invoke printf, ADDR errorNegativoTxt 
	JMP _quit
_errorOverflow:
	invoke printf, ADDR errorOverflowTxt 
	JMP _quit
_quit:
	invoke ExitProcess, 0
end start