SUMMARY = "Systemd service to setup BT for Broadcom Bluetooth chips"
DESCRIPTION = "Load Broadcom Bluetooth Chips Firmware"
SECTION = "devel"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
	file://broadcom-bluetooth/brcm-btfw-load.service;name=brcm-btfw-load-service \
	file://broadcom-bluetooth/brcm-btfw-update.service;name=brcm-btfw-update-service \
	file://broadcom-bluetooth/update-brcm-btfw-load-service.sh;name=update-brcm-btfw-load-service-script \
"
SRC_URI[brcm-btfw-load-service.md5sum] = "1f325873e96b7e02faf394e7823b75d3"
SRC_URI[brcm-btfw-update-service.md5sum] = "0762eb091d41d24a709a1679d1ce38f6"
SRC_URI[update-brcm-btfw-load-service-script.md5sum] = "bd4e2816381b53e6a18b0778cdf577dc"

inherit systemd

RDEPENDS_${PN} += "bluez5"

do_install() {
	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/broadcom-bluetooth/brcm-btfw-load.service ${D}${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/broadcom-bluetooth/brcm-btfw-update.service ${D}${systemd_system_unitdir}
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/broadcom-bluetooth/update-brcm-btfw-load-service.sh ${D}${bindir}/update-brcm-btfw-load-service.sh
}

SYSTEMD_SERVICE_${PN} = "brcm-btfw-load.service brcm-btfw-update.service"