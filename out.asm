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
	_a_main REAL4 0.0
	_5f0s3 REAL4 5.0e3
.code
start:
	FLD _5f0s3
	FSTP _a_main
fld DWORD PTR [_a_main]
fstp QWORD PTR [_a_main]
invoke printf, cfm$("%.20Lf\n"), _a_main
invoke ExitProcess, 0
end start