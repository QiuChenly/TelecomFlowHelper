package com.qiuchen.tianyicrack.TianYiApi

import java.security.Key
import javax.crypto.Cipher

/**
 * Created by qiuchen on 2018/2/6.
 */
class DesEncrypt {
    val defaultKey = "123456"
    val encryptKey = "t2b4h6y4l5"
    lateinit var encryptCipher: Cipher
    lateinit var decryptCipher: Cipher

    constructor(useTY: Boolean = false) {
        if (useTY)
            initKey(defaultKey)
        else
            initKey(encryptKey)

    }

    constructor(Str: String) {
        initKey(Str)
    }

    fun initKey(Str: String) {
        encryptCipher = Cipher.getInstance("DES")
        decryptCipher = Cipher.getInstance("DES")
        val key = getKey(Str.toByteArray())
        encryptCipher.init(Cipher.ENCRYPT_MODE, key)
        decryptCipher.init(Cipher.DECRYPT_MODE, key)

    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位
     * 不足8位时后面补0，超出8位只取前8位
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception
     */
    private fun getKey(toByteArray: ByteArray): Key {
        val arrB = ByteArray(8)
        var i = 0
        while (i < toByteArray.size && i < arrB.size) {
            arrB[i] = toByteArray[i]
            i++
        }
        return javax.crypto.spec.SecretKeySpec(arrB, "DES")
    }

    /**
     * 将byte数组转换为表示16进制值的字符串，
     * 如：byte[]{8,18}转换为：0813，
     * 和public static byte[] hexStr2ByteArr(String strIn)
     * 互为可逆的转换过程
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    fun byteArr2HexStr(byteArray: ByteArray): String {
        val iLen = byteArray.size
        val sb = StringBuffer(iLen * 2)
        for (byte in byteArray) {
            var tmp = byte.toInt()
            while (tmp < 0) {
                tmp += 256
            }
            if (tmp < 16) {
                sb.append("0")
            }
            sb.append(Integer.toString(tmp, 16))
        }
        return sb.toString()
    }

    /**
     * 将表示16进制值的字符串转换为byte数组，
     * 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    fun hexStr2ByteArr(Str: String): ByteArray {
        val a = Str.toByteArray()
        val len = a.size
        val outArr = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            val tmpStr = String(a, i, 2)
            outArr[i / 2] = Integer.parseInt(tmpStr, 16).toByte()
            i += 2
        }
        return outArr
    }

    /**
     * 开始DES加密
     */

    fun encrypt(Str: String): String {
        return byteArr2HexStr(encrypt(Str.toByteArray()))
    }


    fun encrypt(byteArray: ByteArray): ByteArray {
        return encryptCipher.doFinal(byteArray)
    }

    fun decrypt(Str: ByteArray): ByteArray {
        return decryptCipher.doFinal(Str)
    }

    fun decrypt(Str: String): String {
        return String(decrypt(hexStr2ByteArr(Str)))
    }

}

