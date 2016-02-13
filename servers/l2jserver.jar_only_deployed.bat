@echo off
rem ここを環境に合わせて変更してください。「C:\L2J」とかでもOK
set DEPLOY_DIR=..\..\staging

copy ..\build\dist\game\GameServer.jar %DEPLOY_DIR%\game\ /Y
echo ゲームサーバのデプロイが完了しました
pause
exit
