package com.cmdv.data.parsers

interface FileParser {

    fun <T> parse(fileName: String): T?

}