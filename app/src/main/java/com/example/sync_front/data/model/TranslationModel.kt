package com.example.sync_front.data.model

data class CheckLanguageResponse( // 언어 감지 결과
    val status: Int,
    val message: String,
    val data: LangCode
)

data class LangCode( // 언어 코드
    val langCode: String
)

data class CheckLanguageRequest(
    val query: String
)

data class TranslateRequest(
    val source: String,
    val target: String,
    val text: String
)

data class TranslateResponse(
    val status: Int,
    val message: String,
    val data: TranslateResponseData
)

data class TranslateResponseData(
    val srcLangType: String,
    val tarLangType: String,
    val translatedText: String
)
//
//data class TranslateResponseMessage(
//    val result: TranslateResponseResult
//)
//
//data class TranslateResponseResult(
//)