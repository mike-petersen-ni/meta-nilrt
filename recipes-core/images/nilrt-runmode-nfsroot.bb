DESCRIPTION = "NI Linux RT runmode nfsroot rootfs archive"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += "\
	file://default-pxelinux.cfg \
	file://README.nfsroot \
	file://nfsroot.postinst \
	"

DEPENDS += "\
	nilrt-runmode-rootfs \
	syslinux \
	"

IMAGE_FSTYPES = "tar.gz tar.bz2"
IMAGE_INSTALL = "" # TODO even with this empty, a bunch of extra stuff gets added somewhere; maybe I'm abusing the image recipe

nfsroot_fixup() {
	# README
	install "${THISDIR}/files/README.nfsroot" "${IMAGE_ROOTFS}/README.nfsroot"

	# NFS root directory
	install -d "${IMAGE_ROOTFS}/srv/nfs/nilrt"

	# Recovery initial contents
	# TODO?

	# Runmode rootfs contents
	install -d "${IMAGE_ROOTFS}/srv/nfs/nilrt"
	tar -xzf "${DEPLOY_DIR_IMAGE}/nilrt-runmode-rootfs-${PACKAGE_ARCH}.tar.gz" -C "${IMAGE_ROOTFS}/srv/nfs/nilrt"

	# Don't bother with /boot/tmp
	mv "${IMAGE_ROOTFS}/srv/nfs/nilrt/boot/tmp/runmode" "${IMAGE_ROOTFS}/srv/nfs/nilrt/boot/"
	rmdir "${IMAGE_ROOTFS}/srv/nfs/nilrt/boot/tmp"

	# Syslinux PXE bootloader
	# BIOS
	install -d "${IMAGE_ROOTFS}/srv/tftp/bios"
	install -m 0644 "${WORKDIR}/recipe-sysroot/usr/share/syslinux/ldlinux.c32" "${IMAGE_ROOTFS}/srv/tftp/bios/ldlinux.c32"
	install -m 0644 "${WORKDIR}/recipe-sysroot/usr/share/syslinux/pxelinux.0" "${IMAGE_ROOTFS}/srv/tftp/bios/pxelinux.0"
        # x86_64 EFI
	install -d "${IMAGE_ROOTFS}/srv/tftp/efi64"
	install -m 0644 "${WORKDIR}/recipe-sysroot/usr/share/syslinux/efi64/ldlinux.e64" "${IMAGE_ROOTFS}/srv/tftp/efi64/ldlinux.e64"
	install -m 0755 "${WORKDIR}/recipe-sysroot/usr/share/syslinux/efi64/syslinux.efi" "${IMAGE_ROOTFS}/srv/tftp/efi64/syslinux.efi"

	# Configuration for pxelinux
	install -d "${IMAGE_ROOTFS}/srv/tftp/pxelinux.cfg/default"
	install -m 0644 "${THISDIR}/files/default-pxelinux.cfg" "${IMAGE_ROOTFS}/srv/tftp/pxelinux.cfg/default"

	# Link to kernel (don't need *everything* accessible by tftp)
	ln -s /srv/nfs/nilrt/boot/runmode/bzImage "${IMAGE_ROOTFS}/srv/tftp/bzImage"

	# Postinst for first boot
	install -d "${IMAGE_ROOTFS}/srv/nfs/nilrt/etc/ipk-postinsts"
	install -m 0755 "${THISDIR}/files/nfsroot.postinst" "${IMAGE_ROOTFS}/srv/nfs/nilrt/etc/ipk-postinsts/nfsroot.postinst"
}

IMAGE_PREPROCESS_COMMAND += " nfsroot_fixup; rootfs_update_timestamp; "

inherit core-image