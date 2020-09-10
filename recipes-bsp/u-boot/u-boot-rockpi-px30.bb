DEFAULT_PREFERENCE = "1"

DESCRIPTION = "RockPi-PX30 U-Boot"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

include u-boot-rockpi.inc

SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=stable-4.4-px30; \
	file://0001-Use-local-command.h-file-instead-of-system-file.patch \
	file://0001-Makefile-disable-gcc9-s-address-of-packed-member-war.patch \
	file://${MACHINE}/boot.cmd \
	file://${MACHINE}/uEnv.txt \
"

SRCREV = "cfc37de87bc064b2d6d384566e24c5e4245f113a"
