DEFAULT_PREFERENCE = "1"

DESCRIPTION = "RockPi-E U-Boot"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"

include u-boot-rockpi.inc

SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=stable-4.4-rockpie; \
	file://0001-Use-local-command.h-file-instead-of-system-file.patch \
	file://${MACHINE}/boot.cmd \
	file://${MACHINE}/uEnv.txt \
"

SRCREV = "7b93f1b8bce4106266d4a38dde96fd8080faccea"
