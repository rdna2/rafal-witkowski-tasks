call runcrud
if "%ERRORLEVEL%" == "0" goto internetBrowser
echo.
echo Runcrud has errors – breaking work
goto fail

:internetBrowser
start opera http://localhost:8080/crud/v1/tasks

goto end

:fail
echo.
echo There were errors

:end
echo.
echo Work is finished