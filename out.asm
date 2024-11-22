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
	_r.d_main dd 0
	_r.e_main REAL4 0.0
	_x_main dd 0
	_a_main dd 0
	@retValUlongint dd 0
	@aux3 dd 0
	@aux2 dd 0
	@aux1 dd 0
	_1 dd 1
	_5 dd 5
	_5f0 REAL4 5.0
	_50 dd 50
	_30 dd 30
	_20 dd 20
	_10 dd 10
	funcionActual dd 0
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
	errorRecursion db "Error: No se permite la recursion", 0
.code
start:
	MOV EAX, _1
	MOV _x_main,  EAX
	MOV EAX, _x_main
	MOV EBX, _5
	CMP EAX, EBX
	JAE Label15
	MOV EAX, _1
	ADD EAX, _a_main
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV _a_main,  EAX
	JMP Label21
Label15:
	MOV EAX, _a_main
	SUB EAX, _1
	JC _errorNegativo
	MOV @aux2, EAX
	MOV EAX, @aux2
	MOV _a_main,  EAX
Label21:
	MOV EAX, _a_main
	MOV EBX, _1
	CMP EAX, EBX
	JNE Label44
	MOV EAX, _x_main
	MOV EBX, _10
	CMP EAX, EBX
	JE Label37
	FLD _5f0
	FSTP _r.e_main
	JMP Label41
Label37:
	MOV EAX, _20
	MOV _a_main,  EAX
Label41:
	JMP Label45
Label44:
Label45:
	MOV EAX, _30
	MOV _x_main,  EAX
	MOV EAX, _x_main
	MOV EBX, _50
	CMP EAX, EBX
	JAE Label60
	MOV EAX, _5
	ADD EAX, _x_main
	MOV @aux3, EAX
	invoke printf, cfm$("%u\n"), @aux3
	JMP Label61
Label60:
Label61:
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