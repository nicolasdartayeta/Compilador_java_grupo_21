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
	_y_main dd 0
	@retValSingle REAL4 0.0
	_d_main REAL4 0.0
	_r_main_myFunction2 dd 0
	_x_main_myFunction dd 0
	_x_main dd 0
	@retValUlongint dd 0
	@aux3 dd 0
	@aux2 dd 0
	@aux1 dd 0
	_o_main_myFunction dd 0
	_z_main dd 0
	@_Programa_terminado_ db "Programa terminado ", 0
	_20f1 REAL4 20.1
	_0 dd 0
	_1 dd 1
	_2 dd 2
	_3 dd 3
	_3f2s3 REAL4 3.2e3
	_4 dd 4
	_5 dd 5
	_5f0 REAL4 5.0
	_d_main_myFunction2 REAL4 0.0
	_p_main_myFunction dd 0
	_g_main_myFunction_mySubFunction dd 0
	funcionActual dd 0
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
	errorRecursion db "Error: No se permite la recursion", 0
.code
start:
	JMP Label40
myFunction_main:
	FLD _5f0
	FSTP _d_main
	MOV EAX, _2
	MOV _x_main_myFunction,  EAX
	JMP Label18
mySubFunction_main_myFunction:
	MOV EAX, _3
	MOV @retValUlongint,  EAX
	RET  
Label18:
	MOV EAX, _p_main_myFunction
	MOV EBX, _0
	CMP EAX, EBX
	JNE Label28
	MOV EAX, _2
	MOV @retValUlongint,  EAX
	RET  
	JMP Label37
Label28:
	MOV EAX, _p_main_myFunction
	SUB EAX, _1
	JC _errorNegativo
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV _p_main_myFunction,  EAX
	CMP funcionActual, 1
	JE _errorRecursion
	MOV funcionActual, 1
	CALL myFunction_main
	MOV funcionActual, 0
	MOV EAX, _2
	MOV EBX, @retValUlongint
	MUL EBX
	MOV @aux2, EAX
	MOV @aux3, EDX
	MOV EAX, @aux2
	MOV @retValUlongint,  EAX
	RET  
Label37:
Label40:
	MOV EAX, _4
	MOV _z_main,  EAX
	MOV EAX, _5
	MOV _p_main_myFunction,  EAX
	CMP funcionActual, 1
	JE _errorRecursion
	MOV funcionActual, 1
	CALL myFunction_main
	MOV funcionActual, 0
	MOV EAX, @retValUlongint
	MOV _y_main,  EAX
	FLD _3f2s3
	FSTP _d_main
	JMP Label60
myFunction2_main:
	MOV EAX, _r_main_myFunction2
	MOV @retValUlongint,  EAX
	RET  
Label60:
	FLD _20f1
	FSTP _d_main
	MOV EAX, _4
	MOV _x_main,  EAX
	invoke printf, ADDR @_Programa_terminado_
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