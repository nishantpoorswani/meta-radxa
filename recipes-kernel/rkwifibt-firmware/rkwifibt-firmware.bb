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
	install -m 0644 ${S}/firmware/broadcom/all/*/* \
		-t ${D}/system/etc/firmware/
	install -d ${D}/lib/firmware/rtlbt/
	install -m 0644 ${S}/realtek/RTL*/* -t ${D}/lib/firmware/rtlbt/
}

PACKAGES =+ " \
	${PN}-ap6181-wifi \
	${PN}-ap6212a1-wifi \
	${PN}-ap6212a1-bt \
	${PN}-ap6236-wifi \
	${PN}-ap6236-bt \
	${PN}-ap6255-wifi \
	${PN}-ap6255-bt \
	${PN}-ap6256-wifi \
	${PN}-ap6256-bt \
	${PN}-ap6354-wifi \
	${PN}-ap6354-bt \
	${PN}-rtl8723ds-bt \
	${PN}-rtl8723du-bt \
	${PN}-rtl8821cs-bt \
	${PN}-rtl8821cu-bt \
"

FILES_${PN}-ap6181-wifi = " \
	system/etc/firmware/fw_bcm40181a2_apsta.bin \
	system/etc/firmware/fw_bcm40181a2.bin \
	system/etc/firmware/nvram_ap6181.txt \
"

FILES_${PN}-ap6212a1-wifi = " \
	system/etc/firmware/fw_bcm43438a1_apsta.bin \
	system/etc/firmware/fw_bcm43438a1.bin \
	system/etc/firmware/nvram_ap6212a.txt \
"
FILES_${PN}-ap6212a1-bt = " \
	system/etc/firmware/bcm43438a1.hcd \
"

FILES_${PN}-ap6236-wifi = " \
	system/etc/firmware/fw_bcm43436b0_apsta.bin \
	system/etc/firmware/fw_bcm43436b0.bin \
	system/etc/firmware/nvram_ap6236.txt \
"
FILES_${PN}-ap6236-bt = " \
	system/etc/firmware/BCM4343B0.hcd \
"

FILES_${PN}-ap6255-wifi = " \
	system/etc/firmware/fw_bcm43455c0_ag.bin \
	system/etc/firmware/nvram_ap6255.txt \
"
FILES_${PN}-ap6255-bt = " \
	system/etc/firmware/BCM4345C0_ap.hcd \
	system/etc/firmware/BCM4345C0.hcd \
"

FILES_${PN}-ap6256-wifi = " \
	system/etc/firmware/fw_bcm43456c5_ag.bin \
	system/etc/firmware/fw_bcm43456c5_ag_p2p.bin \
	system/etc/firmware/nvram_ap6256.txt \
"
FILES_${PN}-ap6256-bt = " \
	system/etc/firmware/BCM4345C5.hcd \
"

FILES_${PN}-ap6354-wifi = " \
	system/etc/firmware/fw_bcm4354a1_ag.bin \
	system/etc/firmware/nvram_ap6354.txt \
"
FILES_${PN}-ap6354-bt = " \
	system/etc/firmware/bcm4354a1.hcd \
"

FILES_${PN}-rtl8723ds-bt = " \
	lib/firmware/rtlbt/rtl8723d_config \
	lib/firmware/rtlbt/rtl8723d_fw \
"

FILES_${PN}-rtl8723du-bt = " \
	lib/firmware/rtlbt/rtl8723du_config \
	lib/firmware/rtlbt/rtl8723du_fw \
"

FILES_${PN}-rtl8821cs-bt = " \
	lib/firmware/rtlbt/rtl8821c_config \
	lib/firmware/rtlbt/rtl8821c_fw \
"

FILES_${PN}-rtl8821cu-bt = " \
	lib/firmware/rtlbt/rtl8821cu_config \
	lib/firmware/rtlbt/rtl8821cu_fw \
"

FILES_${PN} = "*"

# Make it depend on all of the split-out packages.
python () {
    pn = d.getVar('PN')
    firmware_pkgs = oe.utils.packages_filter_out_system(d)
    d.appendVar('RDEPENDS_' + pn, ' ' + ' '.join(firmware_pkgs))
}

INSANE_SKIP_${PN} += "arch"
