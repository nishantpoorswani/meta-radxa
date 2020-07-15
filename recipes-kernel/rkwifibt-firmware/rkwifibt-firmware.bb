# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd

SUMMARY = "Rockchip WIFI/BT firmware files"
SECTION = "kernel"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/nishantpoorswani/rkwifibt.git"

S = "${WORKDIR}/git"

inherit allarch deploy

do_install() {
	install -d ${D}/system/etc/firmware/
	install -m 0644 ${S}/firmware/broadcom/AP6256/*/* \
		-t ${D}/system/etc/firmware/
	install -d ${D}/lib/firmware/rtlbt/
	install -m 0644 ${S}/realtek/RTL8723DS/* -t ${D}/lib/firmware/rtlbt/
	install -m 0644 ${S}/realtek/RTL8723DU/* -t ${D}/lib/firmware/
	install -m 0644 ${S}/realtek/RTL8821CU/* -t ${D}/lib/firmware/
}

PACKAGES =+ " \
	${PN}-ap6256-wifi \
	${PN}-ap6256-bt \
	${PN}-rtl8723ds-bt \
	${PN}-rtl8723du-bt \
	${PN}-rtl8821cu-bt \
"

FILES_${PN}-ap6256-wifi = " \
	system/etc/firmware/fw_bcm43456c5_ag.bin \
	system/etc/firmware/fw_bcm43456c5_ag_p2p.bin \
	system/etc/firmware/nvram_ap6256.txt \
"
FILES_${PN}-ap6256-bt = " \
	system/etc/firmware/BCM4345C5.hcd \
"

FILES_${PN}-rtl8723ds-bt = " \
	lib/firmware/rtlbt/rtl8723d_config \
	lib/firmware/rtlbt/rtl8723d_fw \
"

FILES_${PN}-rtl8723du-bt = " \
	lib/firmware/rtl8723du_config \
	lib/firmware/rtl8723du_fw \
"

FILES_${PN}-rtl8821cu-bt = " \
	lib/firmware/rtl8821cu_config \
	lib/firmware/rtl8821cu_fw \
"

FILES_${PN} = "*"

# Make it depend on all of the split-out packages.
python () {
    pn = d.getVar('PN')
    firmware_pkgs = oe.utils.packages_filter_out_system(d)
    d.appendVar('RDEPENDS_' + pn, ' ' + ' '.join(firmware_pkgs))
}

INSANE_SKIP_${PN} += "arch"
