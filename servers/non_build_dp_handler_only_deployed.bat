@echo off
rem ���������ɍ��킹�ĕύX���Ă��������B�uC:\L2J�v�Ƃ��ł�OK
set DEPLOY_DIR=..\..\staging

robocopy dist\game\data\scripts\handlers %DEPLOY_DIR%\game\data\scripts\handlers /s > _handlersdeploylog.txt
echo �n���h���̃f�v���C���������܂���
pause
exit
