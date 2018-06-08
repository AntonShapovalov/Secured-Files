package mobile.addons.securedfiles

import mobile.addons.securedfiles.ext.extToLoverCase
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class Simple {

    @Test
    @Throws(Exception::class)
    fun test() {
        var name = "name.EXT"
        Assert.assertEquals("name.ext", name.extToLoverCase())
        name = "name"
        Assert.assertEquals("name", name.extToLoverCase())
        name = "name."
        Assert.assertEquals("name.", name.extToLoverCase())
        name = "."
        Assert.assertEquals(".", name.extToLoverCase())
        name = ".EXT"
        Assert.assertEquals(".ext", name.extToLoverCase())
        name = "name.EXT.EXT"
        Assert.assertEquals("name.EXT.ext", name.extToLoverCase())
    }

}