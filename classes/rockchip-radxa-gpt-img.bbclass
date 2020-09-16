# Create an image that can be written onto a SD card using dd.

inherit image_types

# Use an uncompressed ext4 by default as rootfs
IMG_ROOTFS_TYPE = "ext4"
IMG_ROOTFS = "${IMGDEPLOYDIR}/${IMAGE_BASENAME}-${MACHINE}.${IMG_ROOTFS_TYPE}"

# This image depends on the rootfs image
IMAGE_TYPEDEP_rockchip-radxa-gpt-img = "${IMG_ROOTFS_TYPE}"

GPTIMG = "${IMAGE_BASENAME}-${MACHINE}-gpt.img"
BOOT_IMG = "${IMAGE_BASENAME}-${MACHINE}-boot.img"
IDBLOADER = "idbloader.img"

# Get From radxa-binary loader
DDR_BIN = "radxa-binary/ddr.bin"
LOADER_BIN = "radxa-binary/loader.bin"
MINILOADER_BIN = "radxa-binary/miniloader.bin"
ATF_BIN = "radxa-binary/atf.bin"
BL31_ELF = "radxa-binary/bl31.elf"
TRUST_IMG = "trust.img"
# Not from radxa-binary
UBOOT_IMG = "u-boot.img"

GPTIMG_APPEND_px30 = "console=tty1 console=ttyS1,1500000n8 rw \
	root=PARTUUID=b921b045-1d rootfstype=ext4 init=/sbin/init rootwait"
GPTIMG_APPEND_rk3308 = "console=tty1 console=ttyFIQ0,1500000n8 rw \
	root=PARTUUID=b921b045-1d rootfstype=ext4 init=/sbin/init rootwait"
GPTIMG_APPEND_rk3328 = "console=tty1 console=ttyS2,1500000n8 rw \
	root=PARTUUID=b921b045-1d rootfstype=ext4 init=/sbin/init rootwait"
GPTIMG_APPEND_rk3399 = "console=tty1 console=ttyFIQ0,1500000n8 rw \
	root=PARTUUID=b921b045-1d rootfstype=ext4 init=/sbin/init rootwait"

# default partitions [in Sectors]
# More info at http://rockchip.wikidot.com/partitions
LOADER1_SIZE = "8000"
RESERVED1_SIZE = "128"
RESERVED2_SIZE = "8192"
LOADER2_SIZE = "8192"
ATF_SIZE = "8192"
BOOT_SIZE = "229376"

# WORKROUND: miss recipeinfo
do_image_rockchip_radxa_gpt_img[depends] += " \
	radxa-binary-loader:do_populate_lic \
	virtual/bootloader:do_populate_lic"

do_image_rockchip_radxa_gpt_img[depends] += " \
	parted-native:do_populate_sysroot \
	mtools-native:do_populate_sysroot \
	gptfdisk-native:do_populate_sysroot \
	dosfstools-native:do_populate_sysroot \
	radxa-binary-native:do_populate_sysroot \
	radxa-binary-loader:do_deploy \
	virtual/kernel:do_deploy \
	virtual/bootloader:do_deploy"

PER_CHIP_IMG_GENERATION_COMMAND_px30 = "generate_px30_loader_image"
PER_CHIP_IMG_GENERATION_COMMAND_rk3308 = "generate_rk3308_loader_image"
PER_CHIP_IMG_GENERATION_COMMAND_rk3328 = "generate_rk3328_loader_image"
PER_CHIP_IMG_GENERATION_COMMAND_rk3399 = "generate_rk3399_loader_image"

IMAGE_CMD_rockchip-radxa-gpt-img () {
	# Change to image directory
	cd ${DEPLOY_DIR_IMAGE}

	# Remove the existing image
	rm -f "${GPTIMG}"
	rm -f "${BOOT_IMG}"

	create_rk_image

	${PER_CHIP_IMG_GENERATION_COMMAND}

	cd ${DEPLOY_DIR_IMAGE}
	if [ -f ${WORKDIR}/${BOOT_IMG} ]; then
		cp ${WORKDIR}/${BOOT_IMG} ./
	fi
}

create_rk_image () {

	# last dd rootfs will extend gpt image to fit the size,
	# but this will overrite the backup table of GPT
	# will cause corruption error for GPT
	IMG_ROOTFS_SIZE=$(stat -L --format="%s" ${IMG_ROOTFS})

	GPTIMG_MIN_SIZE=$(expr $IMG_ROOTFS_SIZE + \( ${LOADER1_SIZE} + ${RESERVED1_SIZE} + ${RESERVED2_SIZE} + ${LOADER2_SIZE} + ${ATF_SIZE} + ${BOOT_SIZE} + 35 \) \* 512 )

	GPT_IMAGE_SIZE=$(expr $GPTIMG_MIN_SIZE \/ 1024 \/ 1024 + 2)

	# Initialize sdcard image file
	dd if=/dev/zero of=${GPTIMG} bs=1M count=0 seek=$GPT_IMAGE_SIZE

	# Create partition table
	parted -s ${GPTIMG} mklabel gpt

	# Create vendor defined partitions
	LOADER1_START=64
	RESERVED1_START=$(expr ${LOADER1_START} + ${LOADER1_SIZE})
	RESERVED2_START=$(expr ${RESERVED1_START} + ${RESERVED1_SIZE})
	LOADER2_START=$(expr ${RESERVED2_START} + ${RESERVED2_SIZE})
	ATF_START=$(expr ${LOADER2_START} + ${LOADER2_SIZE})
	BOOT_START=$(expr ${ATF_START} + ${ATF_SIZE})
	ROOTFS_START=$(expr ${BOOT_START} + ${BOOT_SIZE})

 	# Make 5 partitions only for rocki-4b 
	if [ "${SOC_FAMILY}" = "rk3399" ]; then

		parted -s ${GPTIMG} unit s mkpart loader1 ${LOADER1_START} $(expr ${RESERVED1_START} - 1)
		# parted -s ${GPTIMG} unit s mkpart reserved1 ${RESERVED1_START} $(expr ${RESERVED2_START} - 1)
		# parted -s ${GPTIMG} unit s mkpart reserved2 ${RESERVED2_START} $(expr ${LOADER2_START} - 1)
		parted -s ${GPTIMG} unit s mkpart loader2 ${LOADER2_START} $(expr ${ATF_START} - 1)
		parted -s ${GPTIMG} unit s mkpart trust ${ATF_START} $(expr ${BOOT_START} - 1)	
		BOOT_PART=4
		ROOT_PART=5
	else
		BOOT_PART=1
		ROOT_PART=2
	fi

	# Create boot partition and mark it as bootable
	parted -s ${GPTIMG} unit s mkpart boot ${BOOT_START} $(expr ${ROOTFS_START} - 1)
	parted -s ${GPTIMG} set ${BOOT_PART} boot on

	# Create rootfs partition
	parted -s ${GPTIMG} -- unit s mkpart rootfs ${ROOTFS_START} -34s

	parted ${GPTIMG} print

	# mark the boot partition as UEFI boot
	BOOT_UUID="C12A7328-F81F-11D2-BA4B-00A0C93EC93B"

	# Change boot partuuid
	gdisk ${GPTIMG} <<EOF
x
c
${BOOT_PART}
${BOOT_UUID}
w
y
EOF

	# the root partition is always this, because aarch64

	ROOT_UUID="B921B045-1DF0-41C3-AF44-4C6F280D3FAE"

	# Change rootfs partuuid
	gdisk ${GPTIMG} <<EOF
x
c
${ROOT_PART}
${ROOT_UUID}
w
y
EOF

	# Delete the boot image to avoid trouble with the build cache
	rm -f ${WORKDIR}/${BOOT_IMG}
	# Create boot partition image
	BOOT_BLOCKS=$(LC_ALL=C parted -s ${GPTIMG} unit b print | awk "/ ${BOOT_PART} / { print substr(\$4, 1, length(\$4 -1)) / 512 /2 }")
	BOOT_BLOCKS=$(expr $BOOT_BLOCKS / 63 \* 63)

	mkfs.vfat -n "boot" -S 512 -C ${WORKDIR}/${BOOT_IMG} $BOOT_BLOCKS
	mcopy -i ${WORKDIR}/${BOOT_IMG} -s ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${MACHINE}.bin ::${KERNEL_IMAGETYPE}
	DTB_NAME=""
	DTB_NAME=$(echo "${KERNEL_DEVICETREE}" | cut -d '/' -f 2)

	mcopy -i ${WORKDIR}/${BOOT_IMG} -s ${DEPLOY_DIR_IMAGE}/${DTB_NAME} ::${DTB_NAME}

	# Create extlinux config file
	cat >${WORKDIR}/extlinux.conf <<EOF
default Yocto

label Yocto
	kernel /${KERNEL_IMAGETYPE}
	devicetree /${DTB_NAME}
	append ${GPTIMG_APPEND}
EOF

	mmd -i ${WORKDIR}/${BOOT_IMG} ::/extlinux
	mcopy -i ${WORKDIR}/${BOOT_IMG} -s ${WORKDIR}/extlinux.conf ::/extlinux/
	if [ -d ${DEPLOY_DIR_IMAGE}/overlays ]; then
		mmd -i ${WORKDIR}/${BOOT_IMG} ::/overlays
		mcopy -i ${WORKDIR}/${BOOT_IMG} -s ${DEPLOY_DIR_IMAGE}/overlays/* ::/overlays/
	fi
	if [ -e ${DEPLOY_DIR_IMAGE}/hw_intfc.conf ]; then
		mcopy -i ${WORKDIR}/${BOOT_IMG} -s ${DEPLOY_DIR_IMAGE}/hw_intfc.conf ::/
	fi
	if [ -e ${DEPLOY_DIR_IMAGE}/uEnv.txt ]; then
		mcopy -i ${WORKDIR}/${BOOT_IMG} -s ${DEPLOY_DIR_IMAGE}/uEnv.txt ::/
		mcopy -i ${WORKDIR}/${BOOT_IMG} -s ${DEPLOY_DIR_IMAGE}/boot.scr ::/
		mcopy -i ${WORKDIR}/${BOOT_IMG} -s ${DEPLOY_DIR_IMAGE}/boot.cmd ::/
	fi

	# Burn Boot Partition
	dd if=${WORKDIR}/${BOOT_IMG} of=${GPTIMG} conv=notrunc,fsync seek=${BOOT_START}

	# Burn Rootfs Partition
	dd if=${IMG_ROOTFS} of=${GPTIMG} conv=notrunc,fsync seek=${ROOTFS_START}

}

generate_px30_loader_image () {
	LOADER1_START=64
	RESERVED1_START=$(expr ${LOADER1_START} + ${LOADER1_SIZE})
	RESERVED2_START=$(expr ${RESERVED1_START} + ${RESERVED1_SIZE})
	LOADER2_START=$(expr ${RESERVED2_START} + ${RESERVED2_SIZE})
	ATF_START=$(expr ${LOADER2_START} + ${LOADER2_SIZE})
	BOOT_START=$(expr ${ATF_START} + ${ATF_SIZE})
	ROOTFS_START=$(expr ${BOOT_START} + ${BOOT_SIZE})

	# Burn bootloader
	loaderimage --pack --uboot ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.bin ${DEPLOY_DIR_IMAGE}/${UBOOT_IMG} 0x200000 --size 1024 1

	${DEPLOY_DIR_IMAGE}/mkimage -n ${SOC_FAMILY} -T rksd -d ${DEPLOY_DIR_IMAGE}/${DDR_BIN} ${DEPLOY_DIR_IMAGE}/${IDBLOADER}
	cat ${DEPLOY_DIR_IMAGE}/${MINILOADER_BIN} >>${DEPLOY_DIR_IMAGE}/${IDBLOADER}
	cat >${DEPLOY_DIR_IMAGE}/trust.ini <<EOF
[VERSION]
MAJOR=1
MINOR=0
[BL30_OPTION]
SEC=0
[BL31_OPTION]
SEC=1
PATH=${BL31_ELF}
ADDR=0x00010000
[BL32_OPTION]
SEC=0
[BL33_OPTION]
SEC=0
[OUTPUT]
PATH=trust.img
EOF
	trust_merger --size 1024 1 ${DEPLOY_DIR_IMAGE}/trust.ini

	dd if=${DEPLOY_DIR_IMAGE}/${IDBLOADER} of=${GPTIMG} conv=notrunc,fsync seek=${LOADER1_START}
	dd if=${DEPLOY_DIR_IMAGE}/${UBOOT_IMG} of=${GPTIMG} conv=notrunc,fsync seek=${LOADER2_START}
	dd if=${DEPLOY_DIR_IMAGE}/${TRUST_IMG} of=${GPTIMG} conv=notrunc,fsync seek=${ATF_START}
}

generate_rk3308_loader_image () {
	LOADER1_START=64
	RESERVED1_START=$(expr ${LOADER1_START} + ${LOADER1_SIZE})
	RESERVED2_START=$(expr ${RESERVED1_START} + ${RESERVED1_SIZE})
	LOADER2_START=$(expr ${RESERVED2_START} + ${RESERVED2_SIZE})
	ATF_START=$(expr ${LOADER2_START} + ${LOADER2_SIZE})
	BOOT_START=$(expr ${ATF_START} + ${ATF_SIZE})
	ROOTFS_START=$(expr ${BOOT_START} + ${BOOT_SIZE})

	# Burn bootloader
	loaderimage --pack --uboot ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.bin ${DEPLOY_DIR_IMAGE}/${UBOOT_IMG} 0x600000 --size 1024 1

	${DEPLOY_DIR_IMAGE}/mkimage -n ${SOC_FAMILY} -T rksd -d ${DEPLOY_DIR_IMAGE}/${DDR_BIN} ${DEPLOY_DIR_IMAGE}/${IDBLOADER}
	cat ${DEPLOY_DIR_IMAGE}/${MINILOADER_BIN} >>${DEPLOY_DIR_IMAGE}/${IDBLOADER}
	cat >${DEPLOY_DIR_IMAGE}/trust.ini <<EOF
[VERSION]
MAJOR=1
MINOR=0
[BL30_OPTION]
SEC=0
[BL31_OPTION]
SEC=1
PATH=${BL31_ELF}
ADDR=0x00010000
[BL32_OPTION]
SEC=0
[BL33_OPTION]
SEC=0
[OUTPUT]
PATH=trust.img
EOF
	trust_merger --size 1024 1 ${DEPLOY_DIR_IMAGE}/trust.ini

	dd if=${DEPLOY_DIR_IMAGE}/${IDBLOADER} of=${GPTIMG} conv=notrunc,fsync seek=${LOADER1_START}
	dd if=${DEPLOY_DIR_IMAGE}/${UBOOT_IMG} of=${GPTIMG} conv=notrunc,fsync seek=${LOADER2_START}
	dd if=${DEPLOY_DIR_IMAGE}/${TRUST_IMG} of=${GPTIMG} conv=notrunc,fsync seek=${ATF_START}
}

generate_rk3328_loader_image () {
	LOADER1_START=64
	RESERVED1_START=$(expr ${LOADER1_START} + ${LOADER1_SIZE})
	RESERVED2_START=$(expr ${RESERVED1_START} + ${RESERVED1_SIZE})
	LOADER2_START=$(expr ${RESERVED2_START} + ${RESERVED2_SIZE})
	ATF_START=$(expr ${LOADER2_START} + ${LOADER2_SIZE})
	BOOT_START=$(expr ${ATF_START} + ${ATF_SIZE})
	ROOTFS_START=$(expr ${BOOT_START} + ${BOOT_SIZE})

	# Burn bootloader
	loaderimage --pack --uboot ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.bin ${DEPLOY_DIR_IMAGE}/${UBOOT_IMG} 0x200000 --size 1024 1

	${DEPLOY_DIR_IMAGE}/mkimage -n ${SOC_FAMILY} -T rksd -d ${DEPLOY_DIR_IMAGE}/${DDR_BIN} ${DEPLOY_DIR_IMAGE}/${IDBLOADER}
	cat ${DEPLOY_DIR_IMAGE}/${MINILOADER_BIN} >>${DEPLOY_DIR_IMAGE}/${IDBLOADER}
	cat >${DEPLOY_DIR_IMAGE}/trust.ini <<EOF
[VERSION]
MAJOR=1
MINOR=2
[BL30_OPTION]
SEC=0
[BL31_OPTION]
SEC=1
PATH=${BL31_ELF}
ADDR=0x10000
[BL32_OPTION]
SEC=0
[BL33_OPTION]
SEC=0
[OUTPUT]
PATH=trust.img
EOF

	trust_merger --size 1024 1 ${DEPLOY_DIR_IMAGE}/trust.ini

	dd if=${DEPLOY_DIR_IMAGE}/${IDBLOADER} of=${GPTIMG} conv=notrunc,fsync seek=${LOADER1_START}
	dd if=${DEPLOY_DIR_IMAGE}/${UBOOT_IMG} of=${GPTIMG} conv=notrunc,fsync seek=${LOADER2_START}
	dd if=${DEPLOY_DIR_IMAGE}/${TRUST_IMG} of=${GPTIMG} conv=notrunc,fsync seek=${ATF_START}
}

generate_rk3399_loader_image () {
	LOADER1_START=64
	RESERVED1_START=$(expr ${LOADER1_START} + ${LOADER1_SIZE})
	RESERVED2_START=$(expr ${RESERVED1_START} + ${RESERVED1_SIZE})
	LOADER2_START=$(expr ${RESERVED2_START} + ${RESERVED2_SIZE})
	ATF_START=$(expr ${LOADER2_START} + ${LOADER2_SIZE})
	BOOT_START=$(expr ${ATF_START} + ${ATF_SIZE})
	ROOTFS_START=$(expr ${BOOT_START} + ${BOOT_SIZE})

	# Burn bootloader
	loaderimage --pack --uboot ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.bin ${DEPLOY_DIR_IMAGE}/${UBOOT_IMG} 0x200000 --size 1024 1

	${DEPLOY_DIR_IMAGE}/mkimage -n ${SOC_FAMILY} -T rksd -d ${DEPLOY_DIR_IMAGE}/${DDR_BIN} ${DEPLOY_DIR_IMAGE}/${IDBLOADER}
	cat ${DEPLOY_DIR_IMAGE}/${MINILOADER_BIN} >>${DEPLOY_DIR_IMAGE}/${IDBLOADER}
	cat >${DEPLOY_DIR_IMAGE}/trust.ini <<EOF
[VERSION]
MAJOR=1
MINOR=0
[BL30_OPTION]
SEC=0
[BL31_OPTION]
SEC=1
PATH=${BL31_ELF}
ADDR=0x10000
[BL32_OPTION]
SEC=0
[BL33_OPTION]
SEC=0
[OUTPUT]
PATH=trust.img
EOF

	trust_merger --size 1024 1 ${DEPLOY_DIR_IMAGE}/trust.ini

	dd if=${DEPLOY_DIR_IMAGE}/${IDBLOADER} of=${GPTIMG} conv=notrunc,fsync seek=${LOADER1_START}
	dd if=${DEPLOY_DIR_IMAGE}/${UBOOT_IMG} of=${GPTIMG} conv=notrunc,fsync seek=${LOADER2_START}
	dd if=${DEPLOY_DIR_IMAGE}/${TRUST_IMG} of=${GPTIMG} conv=notrunc,fsync seek=${ATF_START}
}
