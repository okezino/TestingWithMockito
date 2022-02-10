package com.psdemo.todo

import com.nhaarman.mockitokotlin2.*
import com.psdemo.todo.add.AddViewModel
import com.psdemo.todo.data.TodoRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AddViewModelTest {
    private val now = System.currentTimeMillis()
    private  val day = 1000 * 60 * 60 * 24

    @Test
    fun saveTodo(){
        var repository : TodoRepository = mock()
        var model  = AddViewModel(repository)
        model.todo.title = "tutorial"


        val actual = model.save()

        assertNull(actual)
        verify(repository).insert(any())
    }

    @Test
    fun saveTodoWithDate(){
        var repository : TodoRepository = mock()
        var model  = AddViewModel(repository)
        val actualTitle = "tutorial"
        val actualDueDate = now + day
        model.todo.title = actualTitle
        model.todo.dueDate = actualDueDate


        val actual = model.save()

        assertNull(actual)
        verify(repository).insert(argThat {
            title == actualTitle && dueDate == actualDueDate
        })
    }

    @Test
    fun saveTodoNoTitle(){
        var repository : TodoRepository = mock()
        var model  = AddViewModel(repository)
        var expected = "Title is required"


        val actual = model.save()

        assertEquals(expected, actual)
        verify(repository, never()).insert(any())

    }
}