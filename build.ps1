# Get the relative path to the source assembly file
param (
    [string]$sourceFile
)

# Check if the source file exists
if (-Not (Test-Path $sourceFile)) {
    Write-Host "The specified source file does not exist: $sourceFile"
    exit
}

# Get the directory where the source file is located
$sourceDir = [System.IO.Path]::GetDirectoryName($sourceFile)

# Assemble the source code using MASM
& "\masm32\bin\ml" /c /Zd /coff $sourceFile

# Get the base name of the file (without extension)
$baseName = [System.IO.Path]::GetFileNameWithoutExtension($sourceFile)

# Link the object file to create the executable
& "\masm32\bin\Link" /SUBSYSTEM:CONSOLE "$baseName.obj"

# Move the generated files (.obj and .exe) to the source directory
Move-Item "$baseName.obj" $sourceDir
Move-Item "$baseName.exe" $sourceDir

Write-Host "Ejecucion del programa:"

# Execute the generated executable from the source directory
& "$sourceDir\$baseName.exe"

# Pause for user input before closing
Read-Host "Press Enter to exit..."