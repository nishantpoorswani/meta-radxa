DEFAULT_PREFERENCE = "1"

include u-boot-rockpi.inc

SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=stable-4.4-rockpis; \
	file://0001-Use-local-command.h-file-instead-of-system-file.patch \
"
SRCREV = "912f34819b3e2d567a1f1255125ad53755f02937"
S = "${WORKDIR}/git"
