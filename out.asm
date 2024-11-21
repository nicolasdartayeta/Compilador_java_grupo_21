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
	@retValSingle REAL4 0.0
	_o_main REAL4 0.0
	_aux_main REAL4 0.0
	@retValUlongint dd 0
	_r_main REAL4 0.0
	@aux1 REAL4 0.0
	_4f0s4 REAL4 4.0e4
	_1f0s38 REAL4 1.0e38
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
.code
start:
	FLD _1f0s38
	FSTP _r_main
	FLD _4f0s4
	FSTP _o_main
	FLD _r_main
	FLD _o_main
	FMUL
	FSTSW AX
	SAHF
	JO _errorOverflow
	FSTP @aux1
	FLD @aux1
	FSTP _aux_main
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