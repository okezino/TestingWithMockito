package com.psdemo.todo

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.*
import com.psdemo.todo.data.Todo
import com.psdemo.todo.data.TodoDao
import com.psdemo.todo.data.TodoRoomDatabase
import com.psdemo.todo.data.TodoRoomRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.VerificationCollector
import java.lang.RuntimeException

@RunWith(AndroidJUnit4::class)
class TodoRoomRepositoryTest {

    @get:Rule
    val collector : VerificationCollector = MockitoJUnit.collector()

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    val exceptionRule = ExpectedException.none()

    val now = System.currentTimeMillis()
    val day = 1000 * 60 * 60 * 24

    lateinit var  db : TodoRoomDatabase

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TodoRoomDatabase::class.java)
            .allowMainThreadQueries().build()
    }


    @Test
    fun test_getUpcomingTodoCountEmpty(){
        var dao = spy(db.todoDao())
        val repository = TodoRoomRepository(dao)
        val expected = 0

        val actual = repository.getUpcomingTodosCount().test().value()

        assertEquals(expected, actual)
        verify(dao)
            .getDateCount(any())

    }

    @Test
    fun test_upcomingSingleTestTodo(){
        var dao = spy(db.todoDao())
        val repository = TodoRoomRepository(dao)
        val expected = 2

        db.todoDao().insert(Todo("2","Todo 2",now + day,false, now))
        db.todoDao().insert(Todo("4","Todo 4",now - day,false, now))
        db.todoDao().insert(Todo("3","Todo 3",now + day,false, now))

        val actual = repository.getUpcomingTodosCount().test().value()

        assertEquals(expected, actual)

        verify(dao)
            .getDateCount(any())

    }

    @Test
    fun test_getAllTodoMultiple(){
        var dao = spy(db.todoDao())
        val repository = TodoRoomRepository(dao)
        val expected = 3

        db.todoDao().insert(Todo("2","Todo 2",now + day,false, now))
        db.todoDao().insert(Todo("4","Todo 4",now - day,false, now))
        db.todoDao().insert(Todo("3","Todo 3",now + day,false, now))

        val actual = repository.getAllTodos().test().value()

        assertEquals(expected, actual.size)

        verify(dao).getAllTodos()
        assertTrue(actual.contains(Todo("2","Todo 2",now + day,false, now)))

    }

    @Test
    fun test_toggleTodoGoodId(){
        val dao = mock<TodoDao>{
            on(it.toggleTodo(any())).doAnswer {
                val id = it.arguments[0]
                require(id != "bad") {"bad id"}
                1
            }
        }

        val repo = TodoRoomRepository(dao)
        val id = "good"

        repo.toggleTodo(id)

        //validate the argument of id was passed to the repository
        verify(dao).toggleTodo(id)
    }

    @Test
    fun test_toggleTodoBadId(){
        val dao = mock<TodoDao>{
            on(it.toggleTodo(any())).doAnswer {
                val id = it.arguments[0]
                require(id != "bad") {"bad id"}
                1
            }
        }

        val repo = TodoRoomRepository(dao)
        val id = "bad"

        exceptionRule.expect(RuntimeException::class.java)

        repo.toggleTodo(id)

        //validate the argument of id was passed to the repository
        verify(dao).toggleTodo(id)
    }

    @Test
    fun test_insert(){
        //we can test by adding to the bd and check to see if it is dere
       val dao : TodoDao= mock()
       val repository = TodoRoomRepository(dao)
        val expected = Todo("2","Todo 2",now + day,false, now)
        val expected2 = Todo("1","Todo 1",now + day,false, now)


        repository.insert(expected)
        repository.insert(expected2)

       argumentCaptor<Todo>().apply {
           verify(dao, atLeast(2)).insert(capture())
           assertEquals(expected2, firstValue)
           assertEquals(expected2, secondValue)

       }

        //another way to use check for argument
        verify(dao).insert(argThat {
            id == "1"
        })
    }

    @After
    fun tearDown(){
        db.close()
    }


}