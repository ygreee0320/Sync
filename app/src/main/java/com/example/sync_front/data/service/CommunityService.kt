package com.example.sync_front.data.service

import com.example.sync_front.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CommunityService {
    @POST
    fun getImage(
        @Url url: String,
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body image: RequestBody
    ): Call<String>

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

    // 게시글 검색
    @GET("community/post/search")
    fun searchCommunity(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String
    ): Call<CommunitySearchResponse>

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
    ): Call<CodeResetResponse>

    // 댓글 좋아요 취소
    @DELETE("community/comment/like/{commentId}")
    fun deleteCommentLike(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Path("commentId") commentId: Int
    ): Call<CodeResetResponse>
}