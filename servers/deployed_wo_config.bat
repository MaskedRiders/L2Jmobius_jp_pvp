@echo off
set GS_DEPLOYED=
set DP_DEPLOYED=
set EXIT_F=

rem ���������ɍ��킹�ĕύX���Ă��������B�uC:\L2J�v�Ƃ��ł�OK
set DEPLOY_DIR=..\..\staging

:deployed_start
echo L2Jmobius_jp_pvp�̃f�v���C���J�n���܂��Bconfig�̓f�v���C�Ώۂ��珜�O���܂��B

:gsdeployed_start
set /P GS_DEPLOYED="�Q�[���T�[�o���f�v���C���܂����H(y/n) "
if /i %GS_DEPLOYED%==y (goto gs_deployed_run) else (goto gs_deployed_end)

:gs_deployed_run
echo �Q�[���T�[�o�̃f�v���C���J�n���܂�
robocopy ..\build\dist %DEPLOY_DIR%\ /s /xf General.properties > _gsdeploylog.txt
echo �Q�[���T�[�o�̃f�v���C���������܂���

:gs_deployed_end

:dp_deployed_start
set /P DP_DEPLOYED="�f�[�^�p�b�N���f�v���C���܂����H(y/n) "
if /i %DP_DEPLOYED%==y (goto dp_deployed_run) else (goto dp_deployed_end)

:dp_deployed_run
echo �f�[�^�p�b�N�̃f�v���C���J�n���܂�
robocopy dist\db_installer %DEPLOY_DIR%\db_installer\ /s > _dpdeploylog.txt
robocopy dist\doc %DEPLOY_DIR%\doc\ /s >> _dpdeploylog.txt
robocopy dist\libs %DEPLOY_DIR%\libs\ /s >> _dpdeploylog.txt
robocopy dist\login\data %DEPLOY_DIR%\login\data\ /s >> _dpdeploylog.txt
robocopy dist\game\data %DEPLOY_DIR%\game\data\ /s >> _dpdeploylog.txt
copy dist\game\*.* %DEPLOY_DIR%\game\ /Y >> _dpdeploylog.txt
copy dist\login\*.* %DEPLOY_DIR%\login\ /Y >> _dpdeploylog.txt

echo �f�[�^�p�b�N�̃f�v���C���������܂���

:dp_deployed_end
:exit_start
set /P EXIT_F="�I�����܂����H(y/n) "
if /i %EXIT_F%==y (goto exit_run) else (goto deployed_start)

:exit_run
echo �I�����܂��B
pause
exit
