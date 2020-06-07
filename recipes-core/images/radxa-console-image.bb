require radxa-minimal-image.bb

SUMMARY = "Basic console image for Radxa boards"

IMAGE_FEATURES += "package-management ssh-server-openssh hwcodecs"

CORE_IMAGE_BASE_INSTALL += " \
    packagegroup-radxa \
"
