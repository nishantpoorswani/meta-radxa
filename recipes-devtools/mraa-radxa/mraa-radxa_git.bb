SUMMARY = "Linux Library for low speed I/O Communication"
HOMEPAGE = "https://github.com/radxa/mraa"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=91e7de50a8d3cf01057f318d72460acd"

SRCREV = "06b2be27c77abf5b5b7af4644151049d60e54053"
PV = "2.1.0+git${SRCPV}"

SRC_URI = "git://github.com/radxa/mraa.git;protocol=http \
           "

S = "${WORKDIR}/git"

# CMakeLists.txt checks the architecture, only x86 and ARM supported for now
COMPATIBLE_HOST = "(x86_64.*|i.86.*|aarch64.*|arm.*)-linux"

inherit cmake distutils3-base

DEPENDS += "json-c"

EXTRA_OECMAKE_append = " -DINSTALLTOOLS:BOOL=ON -DFIRMATA=ON -DCMAKE_SKIP_RPATH=ON \
                         -DPYTHON3_PACKAGES_PATH:PATH=${baselib}/python${PYTHON_BASEVERSION}/site-packages \
			 -DBUILDSWIGNODE=OFF \
                       "

# Prepend mraa-utils to make sure bindir ends up in there
PACKAGES =+ "${PN}-utils"

FILES_${PN}-doc += "${datadir}/mraa/examples/"

FILES_${PN}-utils = "${bindir}/"

PACKAGECONFIG ??= "python"

PACKAGECONFIG[python] = "-DBUILDSWIGPYTHON=ON, -DBUILDSWIGPYTHON=OFF, swig-native ${PYTHON_PN},"
PACKAGECONFIG[ft4222] = "-DUSBPLAT=ON -DFTDI4222=ON, -DUSBPLAT=OFF -DFTDI4222=OFF,, libft4222"

FILES_${PYTHON_PN}-${PN} = "${PYTHON_SITEPACKAGES_DIR}/"
RDEPENDS_${PYTHON_PN}-${PN} += "${PYTHON_PN}"
