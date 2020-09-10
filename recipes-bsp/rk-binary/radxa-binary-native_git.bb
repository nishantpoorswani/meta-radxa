inherit native deploy

DESCRIPTION = "Rockchip-Radxa binary tools"

LICENSE = "BINARY"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=564e729dd65db6f65f911ce0cd340cf9"
NO_GENERIC_LICENSE[BINARY] = "LICENSE.TXT"

SRC_URI = "git://github.com/radxa/rkbin.git;branch=master;"
SRCREV = "fb4ce829b87a9a5738ff6ebc9951768c86ad75c1"

S = "${WORKDIR}/git"

do_install () {
	install -d ${D}/${bindir}
	install -m 0755 "${S}/tools/trust_merger" ${D}/${bindir}
	install -m 0755 "${S}/tools/firmwareMerger" ${D}/${bindir}

	install -m 0755 "${S}/tools/kernelimage" ${D}/${bindir}
	install -m 0755 "${S}/tools/loaderimage" ${D}/${bindir}

	install -m 0755 "${S}/tools/mkkrnlimg" ${D}/${bindir}
	install -m 0755 "${S}/tools/resource_tool" ${D}/${bindir}

}
