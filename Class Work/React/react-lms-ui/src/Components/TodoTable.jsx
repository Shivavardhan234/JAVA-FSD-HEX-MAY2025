import { useState } from "react";
import { todo } from "../Sample_Data/todo";
function TodoTable() {
  let [todoList, settTodoList] = useState(todo)
  return (
    <div>
      <div className="container">
        <div className="row">
          <div className="col-md-12">
            <table className="table table-striped-columns">
              <thead>
                <tr>
                  <th scope="col">#</th>
                  <th scope="col">UserId</th>
                  <th scope="col">Id</th>
                  <th scope="col">Title</th>
                  <th scope="col">Status</th>
                </tr>
              </thead>
              <tbody>
                {
                  todoList.map((e, index) => (
                    <tr key={e.id}>
                      <th scope="row" >{index + 1}</th>
                      <td>{e.userId}</td>
                      <td>{e.id}</td>
                      <td>{e.title}</td>
                      <td>{e.completed == true ? "Completed" : "Not-Completed"}</td>
                    </tr>
                  ))
                }


              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>
  )

}

export default TodoTable;