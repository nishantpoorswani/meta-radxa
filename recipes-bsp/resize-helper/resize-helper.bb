SUMMARY = "Resize root filesystem to fit available disk space"
DESCRIPTION = "Resize root filesystem to fit available disk space"
SECTION = "admin"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
	file://resize-helper.service;name=resize-helper-service \
	file://resize-helper;name=resize-helper-script \
"
SRC_URI[resize-helper-service] = "007b61d7e194deec006531ede89c393a"
SRC_URI[resize-helper-script] = "9e359c1cce25f89114befc48213f7ea8"

inherit systemd

RDEPENDS_${PN} += "e2fsprogs-resize2fs gptfdisk parted util-linux udev"

do_install() {
	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/resize-helper.service ${D}${systemd_system_unitdir}
	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/resize-helper ${D}${sbindir}
}

SYSTEMD_SERVICE_${PN} = "resize-helper.service"