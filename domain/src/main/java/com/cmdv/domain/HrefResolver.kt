package com.cmdv.domain

/*
* Converts relative href to absolute
*/
class HrefResolver(parentFileName: String?) {
    /*
     * path to file holding the href
     */
    private val mParentPath: String
    fun ToAbsolute(relativeHref: String): String {
        return Utility.concatPath(mParentPath, relativeHref)
    }

    init {
        mParentPath = Utility.extractPath(parentFileName)
    }
}