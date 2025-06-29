import axios from "axios";


export const getCio =(dispatch)=>(id)=>{

    const token = localStorage.getItem('token');
    const bearerAuthString = "Bearer "+ token;
    axios.get(`http://localhost:9090/api/cio/get/by-id/${id}`,{headers:{"Authorization" : bearerAuthString}}).then(function(response){
        
            dispatch({
                'payload':response.data,
                'type':"GET_BY_ID"
                
            })

    })


}

