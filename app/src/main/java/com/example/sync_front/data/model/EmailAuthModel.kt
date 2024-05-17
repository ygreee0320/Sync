package com.example.sync_front.data.model


data class EmailRequest( // 인증 코드 전송 시 요청 데이터
    val email: String,
    val univName: String
)

data class EmailResponse( // 인증 코드 전송 시 받는 데이터
    val status: Int,
    val message: String
)

data class CodeRequest( // 인증 코드 검증 요청
    val email: String,
    val univName: String,
    val code: String
)

data class CodeResponseData(
    val success: Boolean,
    val univName: String,
    val certified_email: String,
    val certified_date: String
)

data class CodeResponse( // 인증 코드 검증 완료
    val status: Int,
    val message: String,
    val data: CodeResponseData
)

data class CodeResetResponse(
    val status: Int,
    val message: String,
    val data: Boolean
)

data class UnivName(
    val univName: String
)