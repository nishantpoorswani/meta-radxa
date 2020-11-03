#!/bin/sh

FIRMWARE="/system/etc/firmware"

found=0
MMC=("mmc0" " mmc1" " mmc2")
for mmc in ${MMC[@]}
do
    if [[ -f /sys/bus/sdio/devices/${mmc}:0001:1/vendor ]] && [[ -f /sys/bus/sdio/devices/${mmc}:0001:1/device ]]; then
        VENDOR=$(cat /sys/bus/sdio/devices/${mmc}:0001:1/vendor)
        DEVICE=$(cat /sys/bus/sdio/devices/${mmc}:0001:1/device)
        found=1
        break
    fi
done

if [[ "$found" == "0" ]]; then
    echo "Do not need to load Bluetooth firmware."
    echo "Exit."
    exit 0
fi

echo "VENDOR=$VENDOR"
echo "DEVICE=$DEVICE"

BT_UART="$(strings /sys/firmware/fdt | grep bt_uart)"
if [[ -n ${BT_UART} ]]; then
    NUMBER=$(echo "${BT_UART}" | cut -d "_" -f2 | cut -d "t" -f2)
    BT_UART="/dev/ttyS${NUMBER}"
else
    exit 0
fi
echo "BT_UART: ${BT_UART}"

if [[ "$VENDOR" == "0x02d0" ]]; then
    # ap6236
    if [[ "$DEVICE" == "0xa9a6" ]]; then
        FIRMWARE="$FIRMWARE/BCM4343B0.hcd"
        echo "load ap6236 bt firmware"
    # ap6256
    elif [[ "$DEVICE" == "0xa9bf" ]]; then
        FIRMWARE="$FIRMWARE/BCM4345C5.hcd"
        echo "load ap6256 bt firmware"
    # ap6398s
    elif [[ "$DEVICE" == "0x4359" ]]; then
        FIRMWARE="$FIRMWARE/BCM4359C0.hcd"
        echo "load ap6398s bt firmware"
    fi
fi

echo "Update file /lib/systemd/system/brcm-btfw-load.service"

cat > /lib/systemd/system/brcm-btfw-load.service <<EOF
[Unit]
Description=Load Broadcom Bluetooth firmware
#Requires=display-manager.service
After=display-manager.service
[Service]
#Type=dbus
#blkusName=org.bluez
#ExecStartPre=/usr/sbin/rfkill block bluetooth
ExecStartPre=/usr/sbin/rfkill unblock bluetooth
ExecStart=/usr/bin/brcm_patchram_plus --enable_hci --no2bytes --use_baudrate_for_downloade --tosleep 200000 --baudrate 1500000 --patchram $FIRMWARE $BT_UART > /dev/null 2>&1 &
ExecStop=/usr/bin/killall brcm_patchram_plus
Restart=on-failure
RestartSec=2
StandardOutput=null
[Install]
WantedBy=multi-user.target
EOF
