package com.cmdv.data.parsers

class FileParserFactory {

    fun newFileParser(fileType: FileType): FileParser =
        when (fileType) {
            FileType.EPUB -> EpubFileParser()
            FileType.PDF -> PdfFileParser()
        }

}