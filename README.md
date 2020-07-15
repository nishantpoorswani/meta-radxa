# Meta-radxa

# Table of contents

1. [Introduction](#introduction)
2. [Dependencies](#dependencies)
3. [List of Radxa Boards supported](#list_of_boards_supported)
4. [Using the meta-radxa layer](#meta_radxa_usage)
    1. [Step 1:Fetching the Source](#source_fetch)
    2. [Step 2:Setting up the Environment](#setup)
    3. [Step 3:Bblayers.conf Setup](#bblayers.conf_setup)
    4. [Step 4:Local.conf Setup](#local.conf_setup)
    5. [Step 5:Building the Image](#build_image)
5. [Serial Console](#serial_console)
6. [Login Details](#login)
7. [Networking](#networking)
    1. [Wifi Connectivity](#wifi)
8. [Release Info](#release_info)
9. [Contributing](#contributing)
10. [Reporting Bugs](#bugs)
11. [Maintainers](#maintainers)


## Introduction <a name="introduction"></a>

The OpenEmbedded/Yocto Project BSP layer for the Radxa machines

## Dependencies <a name="dependencies"></a>

The meta-radxa layer depends on:

	URI: git://git.yoctoproject.org/poky
	branch: warrior
    
	URI: git://git.openembedded.org/meta-openembedded
	layers: meta-oe meta-networking meta-python
	branch: warrior

## List of Radxa Boards supported <a name="list_of_boards_supported"></a>

1) RockPi-S
2) RockPi-4 (Community Tested)
3) RockPi-E

## Using the meta-radxa layer <a name="meta_radxa_usage"></a>

### Step 1: Fetching the source <a name="source_fetch"></a>

Fetch the source using the commands given below:

<pre><code>~ $ mkdir yocto
~ $ cd yocto
~/yocto $ git clone git://git.yoctoproject.org/poky -b warrior
~/yocto $ cd poky
~/yocto/poky $ git clone git://git.openembedded.org/meta-openembedded.git -b warrior
~/yocto/poky $ git clone https://github.com/nishantpoorswani/meta-radxa.git -b warrior
</code></pre>

### Step 2: Setting up the Environment <a name="setup"></a>

<pre><code> ~/yocto/poky $ source oe-init-build-env
</code></pre>

#### Step 3: Bblayers.conf Setup <a name="bblayers.conf_setup"></a>

* You can simply copy the bblayers.conf.sample present in meta-radxa/conf folder to the build/conf folder and rename it to bblayers.conf

<div align="center"><b>OR</b></div>

* Add the layers manually as given below to the bblayers.conf in the build/conf folder

<pre><code>  ${TOPDIR}/../meta \
  ${TOPDIR}/../meta-poky \
  ${TOPDIR}/../meta-yocto-bsp \
  ${TOPDIR}/../meta-openembedded/meta-oe \
  ${TOPDIR}/../meta-openembedded/meta-networking \
  ${TOPDIR}/../meta-openembedded/meta-python \
  ${TOPDIR}/../meta-radxa \
</code></pre>

### Step 4: Local.conf Setup <a name="local.conf_setup"></a>

* You can simply copy the local.conf.sample present in meta-radxa/conf folder to the build/conf folder and rename it to local.conf and uncomment the machine for which you want to build an image

<div align="center"><b>OR</b></div>

* Add the following lines in the build/conf/local.conf

```
MACHINE ?= "xxxx"
DISTRO_FEATURES_append = " pam systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"
PACKAGECONFIG_append_pn-systemd = " resolved networkd"
```

**Replace xxxx with the machine you want to build the image for. All supported machines can be found in meta-radxa/conf/machine.**

### Step 5: Building the Image <a name="build_image"></a>

* If you wish to build a minimal image use the command given below:
```
~/yocto/poky/build $ bitbake -k radxa-minimal-image
```
<div align="center"><b>OR</b></div>

* If you wish to build a console image use the command given below:

```
~/yocto/poky/build $ bitbake -k radxa-console-image
```

**At the end of a successful build, you should have a gpt.img image in build/tmp/deploy/images/MACHINE/ folder. The gpt.img can be directly flashed on the sd-card**

## Serial Console <a name="serial_console"></a>

The Serial Console on all 3 boards is enabled on UART-2.

**Helpful Links:**

+ GPIO Pinout for RockPi-S (https://wiki.radxa.com/RockpiS/hardware/gpio)
+ GPIO Pinout for RockPi-4 (https://wiki.radxa.com/Rockpi4/hardware/gpio)
+ GPIO Pinout for RockPi-E (https://wiki.radxa.com/RockpiE/hardware/rockpiE#gpio)
+ RockPi-4 Serial Console Setup (https://wiki.radxa.com/Rockpi4/dev/serial-console)

## Login Details <a name="login"></a>

```
Username: root
Password: rock
```

## Networking <a name="networking"></a>

**Network Devices available:**

+ Wifi
+ Ethernet

### Wifi Connectivity <a name="wifi"></a>

+ Using Commandline Based GUI(nmtui)

nmtui is a curses based GUI. You can start it by running the following command:

```
nmtui
```

+ Using Commandline Utility(nmcli)

nmcli is a command-line tool for controlling NetworkManager and reporting network status.

**List available devices**

```
nmcli dev
```

**Turn on Wifi**

```
nmcli r wifi on
```

**Scanning different devices**

```
nmcli dev wifi
```

**Connect to WiFi Hotspot**

```
nmcli dev wifi connect "SSID" password "PASSWORD"
```

***Note:You need to replace “SSID” and “Password” with your actual WiFi’s SSID and password.***

## Release Info <a name="release_info"></a>

1. RockPi-S

+ Kernel version: 4.4.143-39-daf243b9655a73ee14568e36cf76ac8a094e68e6
+ U-Boot version: 2017.09-c3d3bc84bef5ee95d7199c23e1a34e47ea8f0daf

2. RockPi-4

+ Kernel version: 4.4.154-a14f6502e0454a51626e3906f59637ab264bf53e
+ U-Boot version: 2017.09-6d910b7f12318e5a5bb8d1b2093fe5a9ba17dfce

3. RockPi-E

+ Kernel version: 4.4.194-12-615ae743115011bbe1cd1edc5c9118bf95527f54
+ U-Boot version: 2019.10-7b93f1b8bce4106266d4a38dde96fd8080faccea

## Contributing <a name="contributing"></a>

Please use github for pull requests: https://github.com/nishantpoorswani/meta-radxa/pulls

## Reporting bugs <a name="bugs"></a>

The github issue tracker (https://github.com/nishantpoorswani/meta-radxa/issues) is being used to keep track of bugs.

## Maintainers <a name="maintainers"></a>

* Nishant Poorswani <nishantpoorswani@gmail.com>
