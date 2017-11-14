package ru.org.adons.securedfiles

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.org.adons.securedfiles.ui.edit.DownloadItem
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class Simple {

    @Test
    @Throws(Exception::class)
    fun test() {
        val i1 = DownloadItem(File("/data/data/i1"))
        val i2 = DownloadItem(File("/data/data/i2"))
        val set = HashSet<DownloadItem>()
        set.add(i1)
        set.add(i2)
        set.remove(i1)
        assertEquals(1, set.size)
    }

}