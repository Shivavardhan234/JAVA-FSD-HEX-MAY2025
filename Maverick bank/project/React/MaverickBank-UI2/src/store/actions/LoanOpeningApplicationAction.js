import axios from "axios";


export const getLoanOpeningApplication =(dispatch)=>(id)=>{

    const token = localStorage.getItem('token');
    const bearerAuthString = "Bearer "+ token;
    axios.get(`http://localhost:9090/api/loan-opening-application/get/by-id/${id}`,{headers:{"Authorization" : bearerAuthString}}).then(function(response){
        
            dispatch({
                'payload':response.data,
                'type':"GET_LOAN_OPENING_APPLICATION_BY_ID"
                
            })

    })


}