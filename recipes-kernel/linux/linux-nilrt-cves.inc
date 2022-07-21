# The following CVEs have been fixed upstream
# -------------------------------------------

# release 5.15.15, possibly commit a482c5e00a9b5a194085bcd372ac36141028becb?
CVE_CHECK_WHITELIST += " CVE-2021-3773"

# these CVEs' database entries are useless for the checker: they don't specify the affected versions for the kernel
# commit 7892032cfe67f4bde6fc2ee967e45a8fbaf33756, release 4.10
CVE_CHECK_WHITELIST += " CVE-2017-5897"
# commit 69ae4f6aac1578575126319d3f55550e7e440449, release 5.2
CVE_CHECK_WHITELIST += " CVE-2019-10126"
