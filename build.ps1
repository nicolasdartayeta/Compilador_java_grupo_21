# Assemble the source code using MASM
& "\masm32\bin\ml" /c /Zd /coff out.asm

# Link the object file to create the executable
& "\masm32\bin\Link" /SUBSYSTEM:CONSOLE out.obj

Write-Host "Ejecucion del programa:"
& ".\out"

# Pause for user input before closing
Read-Host "Press Enter to exit..."
