@echo off
rem ���������ɍ��킹�ĕύX���Ă��������B�uC:\L2J�v�Ƃ��ł�OK
set DEPLOY_DIR=..\..\staging

robocopy dist\game\data\scripts %DEPLOY_DIR%\game\data\scripts /s > _scriptsdeploylog.txt
echo �X�N���v�g�̃f�v���C���������܂���
pause
exit
