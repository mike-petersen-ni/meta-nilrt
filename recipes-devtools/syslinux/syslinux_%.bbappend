do_configure:append() {
	oe_runmake firmware="efi64" clean
}
do_compile:append() {
	oe_runmake firmware="efi64" installer
}
do_install:class-target:append() {
	oe_runmake firmware="efi64" install INSTALLROOT="${D}"
}
