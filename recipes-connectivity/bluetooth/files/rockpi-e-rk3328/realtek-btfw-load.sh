#!/bin/sh
#
# realtek-btfw-load
#
# description: Download Realtek bluetooth driver and firmware service.
#              Included chips: RTL8723DU, RTL8821CU.
#
PROGRAM=${0##*/}

# we must be root
[ $(whoami) = "root" ] || { echo "E: You must be root" && exit 1; }

function get_bt_chip_type()
{
    lsusb | grep "0bda:d723"
    [ $? == "0" ] && export BT_CHIP="rtl8723du"

    lsusb | grep "0bda:c820"
    [ $? == "0" ] && export BT_CHIP="rtl8821cu"
}

function start()
{
    get_bt_chip_type
    sleep 2
    rmmod btusb || true
    rmmod btrtl || true
    rmmod btbcm || true
    rmmod btintel || true
    rmmod bt_rtl8723du || true
    rmmod bt_rtl8821cu || true

    sleep 2

    [ "${BT_CHIP}" == "rtl8723du" ] && modprobe bt_rtl8723du
    [ "${BT_CHIP}" == "rtl8821cu" ] && modprobe bt_rtl8821cu

    hciconfig hci0 up
}

function stop()
{
    get_bt_chip_type
    if [ "${BT_CHIP}" == "rtl8723du" ]; then
        rmmod bt_rtl8723du || true
    elif [ "${BT_CHIP}" == "rtl8821cu" ]; then
        rmmod bt_rtl8821cu || true
    fi
}

case "$1" in
start)
    start
    ;;
stop)
    stop
    ;;
restart|reload)
    stop
    sleep 1
    start
    ;;
*)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
esac

exit 0
