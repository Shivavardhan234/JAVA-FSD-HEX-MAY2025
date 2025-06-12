import axios from "axios";
import { useEffect, useState } from "react";

function Posts() {

    let [postList, setPostList] = useState([]);
    

    useEffect(() => {
        
        const getPosts=async ()=>{
            try{
           let response=await axios.get("https://jsonplaceholder.typicode.com/posts");
           //console.log(response);
           setPostList(response.data);
        }
        catch(err){
        console.log(err);
    }
    }
    
        getPosts();
            
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
export default Posts;