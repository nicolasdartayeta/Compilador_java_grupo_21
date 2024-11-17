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
.code
start:
MOV EAX, 5
MOV _a_main,  EAX
invoke printf, cfm$("%u\n"), 20
invoke ExitProcess, 0
end start