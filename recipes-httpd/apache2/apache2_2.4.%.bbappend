FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-ssl-clear-the-error-queue-before-SSL_read-write-acce.patch \
    file://mod_session-SessionExpiryUpdateInterval.patch \
"