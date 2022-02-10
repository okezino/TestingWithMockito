package com.psdemo.todo

import com.psdemo.todo.list.determineCardColor
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ListUtilClassTest(
    private val expected : Int,
    private val dueDate : Long?,
    private val done : Boolean,
    private val scenario : String
) {

    companion object{
        private val now = System.currentTimeMillis()
        private const val day = 1000 * 60 * 60 * 24


        @JvmStatic
        @Parameterized.Parameters(name = "determineCardColor: {3}")
        fun todo() = listOf(
            arrayOf(R.color.todoDone, null, true, "done, no date"),
            arrayOf(R.color.todoNotDue, null, false, "not done, no date"),
            arrayOf(R.color.todoOverDue, now - day, false, "not done, due yesterday")
        )
    }



    @Test
    fun test_determineCardColor(){
        var testResult = determineCardColor(dueDate,done)
        assertEquals(expected, testResult)
    }
}