require radxa-console-image.bb

SUMMARY = "Basic desktop image for Radxa boards"

IMAGE_FEATURES += "x11"

# make sure we boot to desktop
# by default and without x11-base in IMAGE_FEATURES we default to multi-user.target
SYSTEMD_DEFAULT_TARGET = "graphical.target"

CORE_IMAGE_BASE_INSTALL += " \
    openbox \
    packagegroup-xfce-extended \
    lxdm \
"
