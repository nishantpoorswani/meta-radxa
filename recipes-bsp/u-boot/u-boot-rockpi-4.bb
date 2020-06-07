DEFAULT_PREFERENCE = "1"

include u-boot-rockpi.inc

SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=stable-4.4-rockpi4; \
	file://0001-Use-local-command.h-file-instead-of-system-file.patch \
"
SRCREV = "6d910b7f12318e5a5bb8d1b2093fe5a9ba17dfce"

S = "${WORKDIR}/git"
