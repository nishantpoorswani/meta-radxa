FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://0001-Add-support-reboot-loader-command.patch \
"

BUSYBOX_SPLIT_SUID = "0"

