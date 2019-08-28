require recipes-bsp/u-boot/u-boot.inc

DESCRIPTION = "RockPi-4 U-Boot"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"
COMPATIBLE_MACHINE = "(rk3036|rk3066|rk3188|rk3288|rk3328|rk3399|rk3308)"

DEPENDS = "dtc-native bc-native swig-native bison-native"

# u-boot will build native python module
inherit pythonnative

do_configure () {
    if [ -z "${UBOOT_CONFIG}" ]; then
        if [ -n "${UBOOT_MACHINE}" ]; then
            oe_runmake -C ${S} O=${B} ${UBOOT_MACHINE}
        else
            oe_runmake -C ${S} O=${B} oldconfig
        fi
	echo ${PWD}
        ${S}/scripts/kconfig/merge_config.sh -m .config ${@" ".join(find_cfgs(d))}
        cml1_do_configure
    fi
}

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
