package com.example.wsamad5.core

import org.junit.Assert.assertEquals
import org.junit.Test


internal class ValidationsTest{
    @Test
    fun `email is null and return EMPTY`(){
        val email = null
        val result = Validations.email(email)
        assertEquals(Validations.ValidationValues.EMPTY,result)
    }
    @Test
    fun `email is empty and return EMPTY`(){
        val email = ""
        val result = Validations.email(email)
        assertEquals(Validations.ValidationValues.EMPTY,result)
    }
    @Test
    fun `email is different to the regex return REGEX_ERROR`(){
        val email = "healthy@gmail.com"
        val result = Validations.email(email)
        assertEquals(Validations.ValidationValues.REGEX_ERROR,result)
    }
    @Test
    fun `email is correct and return CORRECT`(){
        val email = "healthy@wsa.com"
        val result = Validations.email(email)
        assertEquals(Validations.ValidationValues.CORRECT,result)
    }
    @Test
    fun `password is null return false`(){
        val password = null
        val result = Validations.password(password)
        assertEquals(false,result)
    }
    @Test
    fun `password is empty return false`(){
        val password = ""
        val result = Validations.password(password)
        assertEquals(false,result)
    }
    @Test
    fun `password is correct return true`(){
        val password = "1234"
        val result = Validations.password(password)
        assertEquals(true,result)
    }
}