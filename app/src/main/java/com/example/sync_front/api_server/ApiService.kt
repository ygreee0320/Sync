package com.example.sync_front.api_server

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface LoginService {

    // 소셜 로그인
    @POST("auth/signin")
    fun signIn(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Header("fcmToken") fcmToken: String,
        @Body platform: Platform
    ): Call<LogInResponse>

    // 회원 가입

}

interface CommunityService {
    //게시글 조회
    @GET("community/post")
    fun getCommunity(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Query("postType") postType: String
    ): Call<CommunityResponse>

    //게시글 상세 조회
    @GET("community/post/{postId}")
    fun getCommunityDetail(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<CommunityDetailResponse>

    //게시글 생성
    @Multipart
    @POST("community/post")
    fun postCommunity(
        @Header("Authorization") accessToken: String,
        @Part images: List<MultipartBody.Part>?,
        @Part("requestDto") request: RequestBody
    ): Call<AddCommunityResponse>

    //댓글 조회
    @GET("community/comment/{postId}")
    fun getComment(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<CommentResponse>

    // 댓글 작성
    @POST("community/comment/{postId}")
    fun postComment(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int,
        @Body content: Content
    ): Call<WriteCommentResponse>

    // 게시글 좋아요 생성
    @POST("community/post/like/{postId}")
    fun postCommunityLike(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<LikeResponse>

    // 게시글 좋아요 취소
    @DELETE("community/post/like/{postId}")
    fun deleteCommunityLike(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<LikeResponse>

    // 댓글 좋아요 생성
    @POST("community/comment/like/{commentId}")
    fun postCommentLike(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Path("commentId") commentId: Int
    ): Call<Void>

    // 댓글 좋아요 취소
    @DELETE("community/comment/like/{commentId}")
    fun deleteCommentLike(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Path("commentId") commentId: Int
    ): Call<Void>
}

interface GoogleService {
    // 구글 액세스 토큰 발급
    @POST("oauth2/v4/token")
    fun getAccessToken(
        @Body request: LoginGoogleRequestModel
    ): Call<LoginGoogleResponseModel>
}

interface CountriesService { //국가 조회
    @POST("auth/countries")
    fun getCountries(
        @Header("Content-Type") application: String,
        @Body request: CountriesRequestModel
    ): Call<CountriesResponse>
}

interface EmailService { // 학교 이메일 인증
    @POST("auth/school-emails/verification-requests")
    fun sendEmail(
        @Header("Content-Type") application: String,
        @Body request: EmailRequest
    ): Call<EmailResponse>

    @POST("auth/school-emails/verifications")
    fun sendCode(
        @Header("Content-Type") application: String,
        @Body request: CodeRequest
    ): Call<CodeResponse>

    @POST("auth/school-emails/reset")
    fun sendReset(
        @Header("Content-Type") application: String
    ): Call<CodeResetResponse>
}