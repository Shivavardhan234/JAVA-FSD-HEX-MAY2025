import axios from "axios";


export const getLoanClosureRequest =(dispatch)=>(id)=>{

    const token = localStorage.getItem('token');
    const bearerAuthString = "Bearer "+ token;
    axios.get(`http://localhost:9090/api/loan-closure/get/by-id/${id}`,{headers:{"Authorization" : bearerAuthString}}).then(function(response){
        
            dispatch({
                'payload':response.data,
                'type':"GET_LOAN_CLOSURE_REQUEST_BY_ID"
                
            })

    })


}