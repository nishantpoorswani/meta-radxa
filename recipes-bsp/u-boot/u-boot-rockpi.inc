require recipes-bsp/u-boot/u-boot.inc

DESCRIPTION = "RockPi U-Boot"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
COMPATIBLE_MACHINE = "(rk3036|rk3066|rk3188|rk3288|rk3328|rk3399|rk3308)"

DEPENDS = "dtc-native bc-native swig-native bison-native"

# u-boot will build native python module
inherit pythonnative

do_compile_prepend () {
	export STAGING_INCDIR=${STAGING_INCDIR_NATIVE};
	export STAGING_LIBDIR=${STAGING_LIBDIR_NATIVE};
}

do_compile_append () {
	# copy to default search path
	if [ ${SPL_BINARY} ]; then
		cp ${B}/spl/${SPL_BINARY} ${B}/
	fi
}

do_deploy_append() {
    cp -a ${B}/tools/mkimage ${DEPLOY_DIR_IMAGE}
}
