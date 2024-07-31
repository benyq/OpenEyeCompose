package com.benyq.compose.open.eye.model

open class CommonResult<T : Any>(
    val itemList: MutableList<T> = mutableListOf(),
    val nextPageUrl: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommonResult<*>

        if (itemList != other.itemList) return false
        if (nextPageUrl != other.nextPageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemList.hashCode()
        result = 31 * result + (nextPageUrl?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CommonResult(itemList=$itemList, nextPageUrl=$nextPageUrl)"
    }


}