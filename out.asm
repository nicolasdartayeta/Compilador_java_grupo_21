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
	_x_main dd 0
	@retValSingle REAL4 0.0
	_aux_main dd 0
	@retValUlongint dd 0
	@aux1 dd 0
	_20 dd 20
	_10 dd 10
	funcionActual dd 0
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
	errorRecursion db "Error: No se permite la recursion", 0
.code
start:
	MOV EAX, _10
	MOV _aux_main,  EAX
	MOV EAX, _20
	MOV _x_main,  EAX
	MOV EAX, _aux_main
	SUB EAX, _x_main
	JC _errorNegativo
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV _aux_main,  EAX
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