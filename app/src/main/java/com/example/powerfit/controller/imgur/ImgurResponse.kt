package com.example.powerfit.controller.imgur

data class ImgurResponse(
    val data: ImgurData,
    val success: Boolean,
    val status: Int
)

data class ImgurData(
    val link: String
)

