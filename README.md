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
7. [Contributing](#contributing)
8. [Reporting Bugs](#bugs)
9. [Maintainers](#maintainers)


## Introduction <a name="introduction"></a>

The OpenEmbedded/Yocto Project BSP layer for the Radxa machines

## Dependencies <a name="dependencies"></a>

The meta-radxa layer depends on:

	URI: git://git.yoctoproject.org/poky
	branch: thud
    
	URI: git://git.openembedded.org/meta-openembedded
	layers: meta-oe meta-networking meta-python
	branch: thud

## List of Radxa Boards supported <a name="list_of_boards_supported"></a>

1) RockPi-S
2) RockPi-4
3) RockPi-E

## Using the meta-radxa layer <a name="meta_radxa_usage"></a>

### Step 1: Fetching the source <a name="source_fetch"></a>

Fetch the source using the commands given below:

<pre><code>~ $ mkdir yocto
~ $ cd yocto
~/yocto $ git clone git://git.yoctoproject.org/poky -b thud
~/yocto $ cd poky
~/yocto/poky $ git clone git://git.openembedded.org/meta-openembedded.git -b thud
~/yocto/poky $ git clone https://github.com/nishantpoorswani/meta-radxa.git -b thud
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

* You can simply copy the local.conf.sample present in meta-radxa/conf folder to the build/conf foldermage and rename it to local.conf and uncomment the machine for which you want to build an image

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

**At the end of a successful build, you should have a gpt.img image in build/tmp/deploy/images/MACHINE/ folder.The gpt.img can be directly flashed on the sd-card**

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

## Contributing <a name="contributing"></a>

Please use github for pull requests: https://github.com/nishantpoorswani/meta-radxa/pulls

## Reporting bugs <a name="bugs"></a>

The github issue tracker (https://github.com/nishantpoorswani/meta-radxa/issues) is being used to keep track of bugs.

## Maintainers <a name="maintainers"></a>

* Nishant Poorswani <nishantpoorswani@gmail.com>
