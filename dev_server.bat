call ant release

if "%ERRORLEVEL%"=="0" (
    call %GAE_HOME%\bin\dev_appserver.cmd  --jvm_flag=-Ddatastore.default_high_rep_job_policy_unapplied_job_pct=20 --jvm_flag=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 .\release\war
    
    exit
)

echo Build failed
pause