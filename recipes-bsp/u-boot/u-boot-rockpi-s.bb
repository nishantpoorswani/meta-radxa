DEFAULT_PREFERENCE = "1"
require recipes-bsp/u-boot/u-boot.inc

DESCRIPTION = "RockPi-S U-Boot"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
COMPATIBLE_MACHINE = "(rk3036|rk3066|rk3188|rk3288|rk3328|rk3399|rk3308)"

DEPENDS = "dtc-native bc-native swig-native bison-native"

SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=stable-4.4-rockpis; \
	file://0001-Use-local-command.h-file-instead-of-system-file.patch \
	file://rockpi-s/boot.cmd \
	file://rockpi-s/uEnv.txt \
"
SRCREV = "c3d3bc84bef5ee95d7199c23e1a34e47ea8f0daf"
S = "${WORKDIR}/git"

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
	cp ${WORKDIR}/rockpi-s/boot.cmd ${WORKDIR}/boot.cmd
	${B}/tools/mkimage -C none -A arm -T script -d ${WORKDIR}/boot.cmd ${WORKDIR}/boot.scr
}

do_deploy_append() {
	cp -a ${B}/tools/mkimage ${DEPLOY_DIR_IMAGE}
        install -D -m 644 ${WORKDIR}/rockpi-s/uEnv.txt ${DEPLOYDIR}/
        install -D -m 644 ${WORKDIR}/boot.scr ${DEPLOYDIR}/
	install -D -m 644 ${WORKDIR}/boot.cmd ${DEPLOYDIR}/
    
}
