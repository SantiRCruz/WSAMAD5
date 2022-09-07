package com.example.wsamad5.core

import java.util.regex.Pattern

object Validations {
    enum class ValidationValues {
        EMPTY,
        REGEX_ERROR,
        CORRECT

    }
    fun email(s:String?):ValidationValues{
        val regex = Pattern.compile("^([a-zA-Z]{1,}@wsa[.]com)")
        return if (s.isNullOrEmpty()){
            ValidationValues.EMPTY
        }else if (!regex.matcher(s).matches()){
            ValidationValues.REGEX_ERROR
        }else{
            ValidationValues.CORRECT
        }
    }
    fun password(s:String?):Boolean{
        return !s.isNullOrEmpty()
    }
}