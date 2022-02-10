package com.psdemo.todo

import android.app.Application
import com.psdemo.todo.data.TodoRepository
import com.psdemo.todo.data.TodoRoomDatabase
import com.psdemo.todo.data.TodoRoomRepository

class TodoApplication : Application() {

    val todoRepository: TodoRepository
        get() = TodoRoomRepository(TodoRoomDatabase.getInstance(this.applicationContext)!!.todoDao())
}