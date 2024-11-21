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
	_a_main dd 0
	_b_main dd 0
	@aux1 dd 0
	_5 dd 5
	_10 dd 10
	_p_main_myFunction dd 0
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
	@retValUlongint dd 0
	@retValSingle REAL4 0.0
.code
start:
	MOV EAX, _5
	MOV _a_main,  EAX
	JMP Label13
myFunction_main:
	MOV EAX, _5
	ADD EAX, _p_main_myFunction
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV @retValUlongint,  EAX
	RET  
Label13:
	MOV EAX, _5
	MOV _p_main_myFunction,  EAX
	CALL myFunction_main
	MOV EAX, @retValUlongint
	MOV _b_main,  EAX
	MOV EAX, _10
	MOV _p_main_myFunction,  EAX
	CALL myFunction_main
	MOV EAX, @retValUlongint
	MOV _a_main,  EAX
	invoke printf, cfm$("%u\n"), _b_main
	invoke printf, cfm$("%u\n"), _a_main
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