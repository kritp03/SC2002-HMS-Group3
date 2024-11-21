@echo off
echo Generating JavaDoc documentation for HMS...

:: Create docs directory if it doesn't exist
if not exist "docs" mkdir docs

:: Run javadoc command with all source files
javadoc -d docs ^
-sourcepath HMS/src ^
HMS/src/app/*.java ^
HMS/src/appointment/*.java ^
HMS/src/authorisation/*.java ^
HMS/src/exceptions/*.java ^
HMS/src/io/*.java ^
HMS/src/medicalrecordsPDT/*.java ^
HMS/src/medication/*.java ^
HMS/src/prescription/*.java ^
HMS/src/ui/*.java ^
HMS/src/user/*.java ^
HMS/src/utils/*.java ^
-author ^
-version ^
-use ^
-windowtitle "HMS - Hospital Management System" ^
-doctitle "HMS - Hospital Management System Documentation" ^
-header "<h1>HMS</h1>" ^
-link https://docs.oracle.com/en/java/javase/17/docs/api/ ^
-private ^
-splitindex ^
-encoding UTF-8 ^
-charset UTF-8

echo JavaDoc generation complete. Documentation is available in the docs directory.
pause
