package com.example.notessqlite.todo

class ToDo(
    var id: Int?= null,
    var taskName: String?= null,
    var taskDescription: String?= null,
    var time:String?= null){

    constructor() : this(null, null, null, null)

}