SUMMARY = "Organize packages to avoid duplication across all images"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

# contains basic dependencies, that can work without graphics/display
RDEPENDS_packagegroup-radxa-console = "\
    alsa-utils-aplay \
    coreutils \
    cpufrequtils \
    hostapd \
    htop \
    iptables \
    kernel-modules \
    networkmanager \
    networkmanager-nmtui \
    openssh-sftp-server \
    bluez5 \
    dialog \
    "
