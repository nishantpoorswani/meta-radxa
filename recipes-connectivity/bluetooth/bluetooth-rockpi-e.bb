SUMMARY = "Systemd service to setup BT for RockPi-E"
DESCRIPTION = "Load Realtek Bluetooth Driver and Firmware for RTL8723DU and RTL8821CU chips"
SECTION = "devel"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
	file://${MACHINE}/realtek-btfw-load.service \
	file://${MACHINE}/realtek-btfw-load.sh \
"

S = "${WORKDIR}"

inherit systemd

RDEPENDS_${PN} += "bluez5 usbutils"

do_install() {
	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/${MACHINE}/realtek-btfw-load.service ${D}${systemd_system_unitdir} 
	install -d ${D}${exec_prefix}/local/bin
	install -m 0755 ${WORKDIR}/${MACHINE}/realtek-btfw-load.sh ${D}${exec_prefix}/local/bin/realtek-btfw-load.sh
}

SYSTEMD_SERVICE_${PN} = "realtek-btfw-load.service"

FILES_${PN} += "${exec_prefix}/local/bin/realtek-btfw-load.sh"
