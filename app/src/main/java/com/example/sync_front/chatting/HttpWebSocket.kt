package com.example.sync_front.chatting

import android.util.Log
import okhttp3.*
import okio.ByteString

object HttpWebSocket {
    //private const val WEB_SOCKET_URL = "ws://192.168.0.10:8080/homepage/websocket.message"
    val WEB_SOCKET_URL = "ws://192.168.0.10:8080/homepage/websocket.message"

    val listener = object : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d("socket", "소켓 onClosing")
            webSocket.close(1000, null)
            webSocket.cancel()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.d("socket", "소켓 onFailure : $t")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d("socket", "text 데이터 확인 : $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Log.d("socket", "ByteString 데이터 확인 : $bytes")
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Log.d("socket", "전송 데이터 확인 : $webSocket : $response")
            webSocket.close(1000, null)
        }
    }


}