DEFAULT_PREFERENCE = "1"

DESCRIPTION = "RockPi-S U-Boot"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

include u-boot-rockpi.inc

SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=stable-4.4-rockpis; \
	file://0001-Use-local-command.h-file-instead-of-system-file.patch \
	file://0001-Makefile-disable-gcc9-s-address-of-packed-member-war.patch \
	file://${MACHINE}/boot.cmd \
	file://${MACHINE}/uEnv.txt \
"

SRCREV = "233a23e3ed0b3e5250253ee455c3c5df2080f99c"
