import { useState } from "react";

function Tasks(){

    let[task,setTask]=useState("");
    let[taskList,setTaskList]=useState([]);


    const addTask = ()=>{
        let temp=[...taskList];
        temp.push(task);
        setTaskList(temp);
    }

    return(
        <div>
            <div className="container">


             <div className="row">
                <div className="col-md-12">
                    <br /><br /><br /><br /><br />
                </div>

             </div>
            <div className="row">
                <div className="col-md-6">
                    <h2>Task addition</h2>
                    <div className="card">
                        <div className="card-body" >
                        <label>Enter the task</label>
                        <textarea cols={30} onChange={($e)=>{setTask($e.target.value)}}></textarea>
                        <button className="btn btn-primary" onClick={()=>{addTask()}}>Add task</button>
                        </div>
                    </div>

                </div>
                <div className="col-md-2"></div>
                <div className="col-md-4">
                    <h2>Tasks added</h2>
                    {
                    taskList.map((e)=>(
                        <li key={e}>{e}<br/></li>
                    ))
                }
                </div>
            </div>

            </div>




        </div>

    )
}

export default Tasks;
