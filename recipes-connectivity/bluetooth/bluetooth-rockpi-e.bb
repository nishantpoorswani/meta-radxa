SUMMARY = "Disable loading of specific BT driver modules for ROCKPi-E"
DESCRIPTION = "Disable loading of specific BT driver modules for ROCKPi-E"
SECTION = "devel"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
	file://${MACHINE}/blacklist-bt.conf;name=blacklist-bt-conf \
"

SRC_URI[blacklist-bt-conf.md5sum] = "a6702394f1b71a3466edae4e20e3ba7e"

S = "${WORKDIR}"

inherit systemd

RDEPENDS_${PN} += "bluez5 usbutils"

do_install() {
	install -d ${D}${sysconfdir}
	install -d ${D}${sysconfdir}/modprobe.d
	install -m 0644 ${WORKDIR}/${MACHINE}/blacklist-bt.conf ${D}${sysconfdir}/modprobe.d/blacklist-bt.conf
}

FILES_${PN} += "/etc/modprobe.d/blacklist-bt.conf"
