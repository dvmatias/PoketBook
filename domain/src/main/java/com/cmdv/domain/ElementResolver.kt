package com.cmdv.domain

/*
* Converts relative href to absolute
*/
class HrefResolver(parentFileName: String?) {
    /*
     * path to file holding the href
     */
    private val mParentPath: String = Utility.extractPath(parentFileName)

    fun toAbsolute(relativeHref: String): String = Utility.concatPath(mParentPath, relativeHref)

}