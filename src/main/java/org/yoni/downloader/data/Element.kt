package org.yoni.downloader.data

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * Created by yoni on 05/05/16.
 */
open class Element(val name: String) {

    override fun equals(other: Any?): Boolean {
        return EqualsBuilder()
            .append(name, (other as Element).name)
            .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder()
            .append(name)
            .toHashCode()
    }
}