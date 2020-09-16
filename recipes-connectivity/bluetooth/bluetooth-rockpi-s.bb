SUMMARY = "Systemd service to setup BT for RockPi-S"
DESCRIPTION = "Load Realtek Bluetooth Driver and Firmware for RTL8723DS chip"
SECTION = "devel"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
	file://${MACHINE}/install-module-hci-uart.service;name=btmodule-rockpis-service \
	file://${MACHINE}/rtl8723ds-btfw-load.service;name=btfw-rockpis-service \
	file://${MACHINE}/install_module_hci_uart;name=btmodule-rockpis-script \
"
SRC_URI[btmodule-rockpis-service.md5sum] = "0dc7fac039b3c1625e17bccd3d406770"
SRC_URI[btfw-rockpis-service.md5sum] = "78a4765dfdab9bc044f01bc1e0dc20ff"
SRC_URI[btmodule-rockpis-script.md5sum] = "e141e4ed81aa3c0cd634808cc2e5c740"

S = "${WORKDIR}"

inherit systemd

RDEPENDS_${PN} += "bluez5"

do_install() {
	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/${MACHINE}/install-module-hci-uart.service ${D}${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/${MACHINE}/rtl8723ds-btfw-load.service ${D}${systemd_system_unitdir} 
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/${MACHINE}/install_module_hci_uart ${D}${bindir}/install_module_hci_uart
}

SYSTEMD_SERVICE_${PN} = "install-module-hci-uart.service rtl8723ds-btfw-load.service"
