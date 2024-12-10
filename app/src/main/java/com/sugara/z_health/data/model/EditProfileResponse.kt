package com.sugara.z_health.data.model

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(

	@field:SerializedName("user_detail")
	val userDetail: UserDetail? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserDetail(

	@field:SerializedName("profile_img")
	val profileImg: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("birthdate")
	val birthdate: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
