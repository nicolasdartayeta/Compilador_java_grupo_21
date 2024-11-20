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
	_j_main dd 0
	_x_main dd 0
	@aux3 dd 0
	@aux2 dd 0
	@aux1 dd 0
	_Programa terminado db "Programa terminado", 0
	errorNegativoTxt db "Error: La resta da un resultado menor que 0", 0
	errorOverflowTxt db "Error: La multiplicacion se va de rango", 0
.code
start:
	MOV EAX, 10
	MOV _x_main,  EAX
Label3:
	MOV EAX, _x_main
	MOV EBX, 0
	CMP EAX, EBX
	JB Label39
	MOV EAX, 5
	MOV _j_main,  EAX
Label12:
	MOV EAX, _j_main
	MOV EBX, 0
	CMP EAX, EBX
	JBE Label33
	MOV EAX, _x_main
	MOV EBX, 30
	CMP EAX, EBX
	JE Label33
	MOV EAX, 1
	ADD EAX, _x_main
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV _x_main,  EAX
	MOV EAX, _j_main
	SUB EAX, 2
	JC _errorNegativo
	MOV @aux2, EAX
	MOV EAX, @aux2
	MOV _j_main,  EAX
	JMP Label12
Label33:
	MOV EAX, _x_main
	SUB EAX, 1
	JC _errorNegativo
	MOV @aux3, EAX
	MOV EAX, @aux3
	MOV _x_main,  EAX
	JMP Label3
Label39:
	MOV EAX, 10
	MOV _x_main,  EAX
Label43:
	MOV EAX, _x_main
	MOV EBX, 0
	CMP EAX, EBX
	JBE Label66
	MOV EAX, _x_main
	MOV EBX, 100
	CMP EAX, EBX
	JAE Label66
	FLD 6
	FDIV 3
	FSTP @aux4
	FLD _x_main
	FSUB @aux4
	FSTP @aux5
	MOV EAX, @aux5
	MOV _x_main,  EAX
	MOV EAX, 2
	ADD EAX, _x_main
	MOV @aux6, EAX
	MOV EAX, @aux6
	MOV _x_main,  EAX
	JMP Label43
Label66:
	invoke printf, ADDR _Programa terminado
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