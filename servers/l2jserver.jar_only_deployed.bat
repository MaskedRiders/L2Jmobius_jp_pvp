@echo off
rem ���������ɍ��킹�ĕύX���Ă��������B�uC:\L2J�v�Ƃ��ł�OK
set DEPLOY_DIR=..\..\staging

copy ..\build\dist\game\GameServer.jar %DEPLOY_DIR%\game\ /Y
echo �Q�[���T�[�o�̃f�v���C���������܂���
pause
exit
