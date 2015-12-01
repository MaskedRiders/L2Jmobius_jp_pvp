@echo off
rem ここを環境に合わせて変更してください。「C:\L2J」とかでもOK
set DEPLOY_DIR=..\..\staging

robocopy dist\game\data\scripts\handlers %DEPLOY_DIR%\game\data\scripts\handlers /s > _handlersdeploylog.txt
echo ハンドラのデプロイが完了しました
pause
exit
