DEFAULT_PREFERENCE = "1"

include u-boot-rockpi.inc

SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=rk3399-pie-gms-express-baseline; \
	file://0001-Use-local-command.h-file-instead-of-system-file.patch \
	file://0001-Makefile-disable-gcc9-s-address-of-packed-member-war.patch \
	file://0002-Suppress-maybe-uninitialized-warning.patch \
"
SRCREV = "04d66f4b45a47531b5ff6cdbddcdc2cc35fa7aea"

S = "${WORKDIR}/git"
