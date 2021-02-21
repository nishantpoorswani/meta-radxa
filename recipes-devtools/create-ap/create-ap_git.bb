DESCRIPTION = "Create AP"
SECTION = "devel"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=47819091dc3777f6899ac4e6dbff2613"

SRC_URI = "git://github.com/oblique/create_ap.git;branch=master;"
SRCREV = "${AUTOREV}"

SRC_URI[md5sum] = "dc6c5a027508ffa09d2c3c2ed152e14a"
SRC_URI[sha256sum] = "d86f0d46f044bf0f2b76e8e90e02e4f778d4e873aec82cffa5914839eb356744"

S = "${WORKDIR}/git"

inherit systemd

RDEPENDS_${PN} += "bash util-linux procps hostapd iw dnsmasq"

do_install() {
	install -d ${D}/${bindir}
	install -d ${D}/${bindir}/create_ap
	install -m 0755 "${S}/create_ap" ${D}/${bindir}/create_ap/create_ap

	install -d ${D}/${sysconfdir}
	install -m 0644 "${S}/create_ap.conf" ${D}/${sysconfdir}/create_ap.conf

	install -d ${D}/${systemd_system_unitdir}
	install -m 0644 "${S}/create_ap.service" ${D}/${systemd_system_unitdir}/create_ap.service

	install -d ${D}/${datadir}
	install -d ${D}/${datadir}/bash-completion/completions
	install -m 0644 "${S}/bash_completion" ${D}/${datadir}/bash-completion/completions/create_ap

	install -d ${D}/${docdir}
	install -d ${D}/${docdir}/create_ap
	install -m 0644 "${S}/README.md" ${D}/${docdir}/create_ap/README.md
}

FILES_${PN} += "\
	${datadir}/bash-completion/* \
	"

SYSTEMD_SERVICE_${PN} = "create_ap.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
