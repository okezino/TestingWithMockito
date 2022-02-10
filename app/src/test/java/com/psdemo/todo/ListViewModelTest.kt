package com.psdemo.todo

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.psdemo.todo.data.Todo
import com.psdemo.todo.data.TodoRepository
import com.psdemo.todo.list.ListViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException


class ListViewModelTest {

    @get:Rule
    val exceptionRule = ExpectedException.none()

 lateinit var repository: TodoRepository

    @Before
    fun setup(){
        val now = System.currentTimeMillis()
        val day = 1000 * 60 * 60 * 24

        val todo = ArrayList<Todo>()
        var todos = Todo("1","Todo 1",null,false, now)
        todo.add(todos)
        todos = Todo("2","Todo 2",now + day,false, now)
        todo.add(todos)
        todos = Todo("3","Todo 3",now + day,false, now)
        todo.add(todos)
        todos = Todo("4","Todo 4",now - day,false, now)
        todo.add(todos)
     repository = TodoTestRepository(todo)
    }

    @Test
    fun test_allTodos(){
        val expected = 4
        val model = ListViewModel(repository)

        val todos = model.allTodos.value

        assertNotNull(todos)
        assertEquals(expected, todos!!.size)
    }

    @Test
    fun upcomingTodos(){
        val expected = 2
        val model = ListViewModel(repository)

        val todos = model.upcomingTodosCount.value

        assertNotNull(todos)
        assertEquals(expected, todos)
    }

    @Test
    fun test_toggleTodo(){
        val id = "fake"
        val model = ListViewModel(repository)

        exceptionRule.expect(NotImplementedError::class.java)

        model.toggleTodo(id)
    }


}