@echo off
rem ここを環境に合わせて変更してください。「C:\L2J」とかでもOK
set DEPLOY_DIR=..\..\staging

robocopy dist\game\data\scripts %DEPLOY_DIR%\game\data\scripts /s > _scriptsdeploylog.txt
echo スクリプトのデプロイが完了しました
pause
exit
