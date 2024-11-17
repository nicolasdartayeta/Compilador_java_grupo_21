.386
.model flat, stdcall
option casemap :none

include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
includelib \masm32\lib\kernel32.lib

.data
	a:main dd 0
.code
start:
MOV EAX, _5
MOV _a:main,  EAX
invoke ExitProcess, 0
end start