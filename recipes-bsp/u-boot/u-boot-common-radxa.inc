HOMEPAGE = "https://github.com/radxa/u-boot"
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"
PE = "1"

SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=stable-4.4-rockpie; \
	file://0001-Use-local-command.h-file-instead-of-system-file.patch \
"
SRCREV = "7b93f1b8bce4106266d4a38dde96fd8080faccea"

S = "${WORKDIR}/git"
