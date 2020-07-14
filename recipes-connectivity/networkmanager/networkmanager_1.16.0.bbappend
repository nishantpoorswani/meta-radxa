FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://NetworkManager.conf"

FILES_${PN} += " \
	${sysconfdir}/NetworkManager/NetworkManager.conf \
"

do_install_append () {
	install -m 0644 ${WORKDIR}/NetworkManager.conf ${D}${sysconfdir}/NetworkManager/
}
