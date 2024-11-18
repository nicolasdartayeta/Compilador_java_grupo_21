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
	@f64printVariable REAL8 0.0
	_a_main REAL4 0.0
	@aux1 REAL4 0.0
	_5f0s3 REAL4 5.0e3
	_3f0s1 REAL4 3.0e1
.code
start:
	FLD _5f0s3
	FMUL _3f0s1
	FSTP @aux1
	FLD @aux1
	FSTP _a_main
	fld _a_main
	fstp @f64printVariable
	invoke printf, cfm$("%.20Lf\n"), @f64printVariable
invoke ExitProcess, 0
end start