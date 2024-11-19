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
	_a_main dd 0
	_z_main dd 0
	@aux2 REAL4 0.0
	@aux1 dd 0
	_3f14 REAL4 3.14
.code
start:
	MOV EAX, _1
	MOV _x_main,  EAX
	MOV EAX, _1
	ADD EAX, _a_main
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV _a_main,  EAX
	JMP Label21
Label15:
	FLD _a_main
	FSUB _1
	FSTP @aux2
	MOV EAX, @aux2
	MOV _a_main,  EAX
Label21:
	MOV EAX, _3.14
	MOV _z_main,  EAX
	JMP Label41
Label37:
	MOV EAX, _20
	MOV _a_main,  EAX
Label41:
	JMP Label45
Label44:
Label45:
invoke ExitProcess, 0
end start