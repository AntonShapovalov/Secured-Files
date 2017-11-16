package ru.org.adons.securedfiles

import org.junit.Test
import rx.Observable

/**
 * Example local unit test, which will execute on the development machine (host).
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class Simple {

    @Test
    @Throws(Exception::class)
    fun test() {
        val list1 = Observable.just(listOf(1, 2, 3))
        val list2 = Observable.just(listOf(7, 8, 9))
        list1.concatWith(list2).subscribe { it.forEach { println(it) } }
    }

}