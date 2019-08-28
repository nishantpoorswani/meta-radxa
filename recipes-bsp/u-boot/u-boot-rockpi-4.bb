DEFAULT_PREFERENCE = "1"

include u-boot-rockpi-4.inc

SRC_URI = " \
	git://github.com/u-boot/u-boot.git \
"
SRCREV = "50b4b80f597b9f59b98adbdbad691b1027bd501a"

S = "${WORKDIR}/git"
