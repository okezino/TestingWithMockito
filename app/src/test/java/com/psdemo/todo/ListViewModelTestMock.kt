package com.psdemo.todo

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.psdemo.todo.data.Todo
import com.psdemo.todo.data.TodoRepository
import com.psdemo.todo.list.ListViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.lang.IllegalArgumentException

class ListViewModelTestMock {

    val now = System.currentTimeMillis()
    val day = 1000 * 60 * 60 * 24

    @get:Rule
    val exceptionRule = ExpectedException.none()


    @Test
    fun test_getEmptyTodo(){
        val expected = 0
        var repository : TodoRepository = mock()

        whenever(repository.getAllTodos()).thenReturn(
            MutableLiveData(arrayListOf())
        )
        val model = ListViewModel(repository)
        val todos = model.allTodos.value

        Assert.assertNotNull(todos)
        Assert.assertEquals(expected, todos!!.size)
    }

    @Test
    fun test_getMultiple(){
        val expected = 3
        var repository : TodoRepository = mock(verboseLogging = true)

        whenever(repository.getAllTodos()).thenReturn(
            MutableLiveData(arrayListOf(Todo("2","Todo 2",now + day,false, now),
                Todo("3","Todo 3",now + day,false, now),
                Todo("4","Todo 4",now - day,false, now)
            ))
        )
        val model = ListViewModel(repository)
        val todos = model.allTodos.value

        Assert.assertNotNull(todos)
        Assert.assertEquals(expected, todos!!.size)
    }

    @Test
    fun test_todoCount(){
        val expected = 1
        var repository : TodoRepository = mock()

        whenever(repository.getUpcomingTodosCount()).thenReturn(
            MutableLiveData(1
            )
        )
        val model = ListViewModel(repository)
        val todos = model.upcomingTodosCount.value

        Assert.assertNotNull(todos)
        Assert.assertEquals(expected, todos)
    }

    @Test
    fun test_toggleTodo(){
        val id = "fake"
        val exceptionMessages = "Todo not found"
        var repository : TodoRepository = mock()

        whenever(repository.toggleTodo(id)).thenThrow(
            IllegalArgumentException(exceptionMessages)
        )

        val model = ListViewModel(repository)
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage(exceptionMessages)

        model.toggleTodo(id)

        verify(repository).toggleTodo(id)
    }

}