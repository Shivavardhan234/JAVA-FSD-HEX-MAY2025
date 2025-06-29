import axios from "axios";


export const getCustomer =(dispatch)=>(id)=>{

    const token = localStorage.getItem('token');
    const bearerAuthString = "Bearer "+ token;
    axios.get(`http://localhost:9090/api/customer/get/by-id/${id}`,{headers:{"Authorization" : bearerAuthString}}).then(function(response){
        
            dispatch({
                'payload':response.data,
                'type':"GET_BY_ID"
                
            })

    })


}

