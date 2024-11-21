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
	@aux6 dd 0
	@aux5 REAL4 0.0
	@aux4 REAL4 0.0
	_c_main REAL4 0.0
	@retValSingle REAL4 0.0
	_j_main dd 0
	_x_main dd 0
	@retValUlongint dd 0
	@aux3 dd 0
	@aux2 dd 0
	@aux1 dd 0
	@_Programa_terminado db "Programa terminado", 0
	_0 dd 0
	_1 dd 1
	_100 dd 100
	_2 dd 2
	_3 dd 3
	_5 dd 5
	_6 dd 6
	_30 dd 30
	_10 dd 10
	funcionActual dd 0
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
	errorRecursion db "Error: No se permite la recursion", 0
.code
start:
	MOV EAX, _10
	MOV _x_main,  EAX
Label3:
	MOV EAX, _x_main
	MOV EBX, _0
	CMP EAX, EBX
	JB Label39
	MOV EAX, _5
	MOV _j_main,  EAX
Label12:
	MOV EAX, _j_main
	MOV EBX, _0
	CMP EAX, EBX
	JBE Label33
	MOV EAX, _x_main
	MOV EBX, _30
	CMP EAX, EBX
	JE Label33
	MOV EAX, _1
	ADD EAX, _x_main
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV _x_main,  EAX
	MOV EAX, _j_main
	SUB EAX, _2
	JC _errorNegativo
	MOV @aux2, EAX
	MOV EAX, @aux2
	MOV _j_main,  EAX
	JMP Label12
Label33:
	MOV EAX, _x_main
	SUB EAX, _1
	JC _errorNegativo
	MOV @aux3, EAX
	MOV EAX, @aux3
	MOV _x_main,  EAX
	JMP Label3
Label39:
	MOV EAX, _10
	MOV _x_main,  EAX
Label43:
	MOV EAX, _x_main
	MOV EBX, _0
	CMP EAX, EBX
	JBE Label66
	MOV EAX, _x_main
	MOV EBX, _100
	CMP EAX, EBX
	JAE Label66
	FLD _6
	FDIV _3
	FSTP @aux4
	FLD _x_main
	FSUB @aux4
	FSTP @aux5
	MOV EAX, @aux5
	MOV _x_main,  EAX
	MOV EAX, _2
	ADD EAX, _x_main
	MOV @aux6, EAX
	MOV EAX, @aux6
	MOV _x_main,  EAX
	JMP Label43
Label66:
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