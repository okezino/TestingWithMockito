package com.psdemo.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.psdemo.todo.data.Todo
import com.psdemo.todo.data.TodoRepository

class TodoTestRepository(private val todo : ArrayList<Todo>) : TodoRepository {


    override fun getAllTodos(): LiveData<List<Todo>> {
        return MutableLiveData(todo)
    }

    override fun insert(todo: Todo) {

    }

    override fun toggleTodo(id: String) {
        TODO("Not yet implemented")
    }


    override fun getUpcomingTodosCount(): LiveData<Int> {
        val count = todo.count {
            !it.completed &&
                    it.dueDate != null &&
                    it.dueDate !! >= System.currentTimeMillis()
        }
        return  MutableLiveData(count)
    }
}