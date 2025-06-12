import axios from "axios";
import { useEffect, useState } from "react";

function AddPost() {

    let[userId,setUserId]=useState(undefined)
    let[body,setBody]=useState("")
    let[title,setTitle]=useState("")
    let[message,setMessage]=useState("")
    

    useEffect(() => {
        
        const addPosts=async ()=>{
            try{
           let response=await axios.post("https://jsonplaceholder.typicode.com/posts",
            {
                'userId':userId,
                'title':title,
                'body':body
            }
           );
            setMessage("operation success,post added..");
           
           
        }
        catch(err){
            setMessage("operation failes!!! try again");
        console.log(err);
    }
    }
    
        addPosts();
            
    }, []);


    return (

        <div>
            <div className="container-fluid">
                <div className="row">
                    {
                        postList.map((post) => (
                            <div className="col-md-3" key={post.id}>
                                <div className="card" style={{height:300,marginBottom:30}}>
                                    <div className="card-header" style={{color:'green'}}>
                                        {post.title}
                                    </div>
                                    <div className="card-body">
                                        <figure>
                                            <blockquote className="blockquote">
                                                <p>User Id:{post.userId}</p>
                                                <p>Post Id:{post.id}</p>
                                            </blockquote>
                                            <label >{post.body}</label>
                                        </figure>
                                    </div>
                                </div>
                            </div>

                        ))
                    }

                </div>

            </div>
        </div>

    )
}
export default AddPost;