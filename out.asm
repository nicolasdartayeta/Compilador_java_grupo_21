.386
.model flat, stdcall
.STACK 200h
option casemap :none

include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
includelib \masm32\lib\kernel32.lib

.data
	_x_main dd 0
	_a_main dw 0
	@aux3 dd 0
	@aux2 dd 0
	@aux1 dw 0
.code
start:
	FLD 5.0
	FADD 4.4
	FSTP @aux1
	FLD @aux1
	FSTP _a_main
	MOV EAX, 6
	MUL 8
	MOV @aux2, EAX
	MOV @aux3, EDX
	MOV EAX, @aux2
	MOV _x_main,  EAX
invoke ExitProcess, 0
end start