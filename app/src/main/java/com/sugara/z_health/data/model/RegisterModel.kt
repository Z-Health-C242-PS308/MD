package com.sugara.z_health.data.model

import com.google.gson.annotations.SerializedName

data class RegisterModel(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("profile_img")
	val profileImg: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("confirmPass")
	val confirmPass: String? = null,

	@field:SerializedName("birthdate")
	val birthdate: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
