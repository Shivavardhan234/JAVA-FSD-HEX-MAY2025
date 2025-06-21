import axios from "axios"

export const fetchAllCustomers= (dispatch)=>()=>{
    const token=localStorage.getItem('token');
    const baererToken="Bearer "+ token; 

    axios.get("http://localhost:9090/api/customer/get/all", {
        headers:{"Authorization":baererToken}
    }).then(function(response){
        dispatch({
            'payload':response.data,
            'type':"FETCH_ALL_CUSTOMERS"
        })

    });
}