@ECHO OFF
cls
rem mshta vbscript:msgbox("是否确定？",36,"第一个")(window.close)
echo cd %cd%
echo -----------------------------
echo ** Initialize
SETLOCAL ENABLEEXTENSIONS
REM Initialize Constants
REM SET TSVN_INFO_FILE=./TSVN_INFO.tmp

rem SET TSVN_INFO_FILE=%4
REM Initialize script arguments
echo  workDir %1
rem ./src VersionInfo.tpl ./src/META-INF/version-info.properties TSVN_INFO.tmp
rem SET workDir=%1
rem SET template=%2
rem SET target=%3

SET workDir=./src
SET template=./VersionInfo.tpl
SET target=./src/META-INF/version-info.properties
SET TSVN_INFO_FILE=./TSVN_INFO.tmp

REM Goto main entry
GOTO MAIN
echo =============================
echo -----------------------------
::Main entry
:MAIN
pushd %workDir%
rem SET workDir=./
REM 检查参数
IF %workDir%=="" GOTO ARGUMENT_ERROR
IF %template%=="" GOTO ARGUMENT_ERROR
IF %target%=="" GOTO ARGUMENT_ERROR
echo 查询注册表
echo TSVN_INFO_FILE %TSVN_INFO_FILE%
reg query HKEY_LOCAL_MACHINE\SOFTWARE\TortoiseSVN /v Directory > %TSVN_INFO_FILE% 2>NUL
REM 查找 TSVN 路径
FOR /F "tokens=*" %%i IN (%TSVN_INFO_FILE%) DO (
  ECHO %%i | find "Directory" > NUL
  IF %ERRORLEVEL% == 0 (
    ECHO %%i > %TSVN_INFO_FILE%
    echo ----------------****
    ECHO %%i
  )
)

SET /P TSVN_PATH= < %TSVN_INFO_FILE%
SET /P TSVN_PATH= < %TSVN_PATH%

echo TSVN_PATH %TSVN_PATH%
SET TSVN_PATH=%TSVN_PATH:~23,-1%

echo TSVN_PATH %TSVN_PATH%


echo 调用 TSVN 替换模板
echo ERRORLEVEL %ERRORLEVEL%
echo TSVN_PATH %TSVN_PATH%
IF NOT %ERRORLEVEL% == 0 GOTO UNKNOW_ERROR
echo work dir %workDir%
"%TSVN_PATH%bin/SubWCRev.exe" %workDir% %template% %target% >NUL
IF NOT %ERRORLEVEL% == 0 GOTO UNKNOW_ERROR
GOTO SUCESSED
echo =============================
echo -----------------------------
echo ** Error handlers
:ARGUMENT_ERROR
ECHO 传入的参数无效。
GOTO FAIL
:NOT_FOUND_TSVN
echo 查询TortoiseSVN 的安装信息失败。
GOTO FAIL
:UNKNOW_ERROR
ECHO 生成程序集信息出现未知错误。
:FAIL
echo =============================
echo -----------------------------
echo ** Program exit
:FAIL
DEL /Q %TSVN_INFO_FILE% 2>NUL
echo 生成程序集信息失败。
popd
pause
EXIT 1
:SUCESSED
DEL /Q %TSVN_INFO_FILE% 2>NUL
ECHO 生成程序集信息成功。
popd
pause
EXIT 0
::=============================