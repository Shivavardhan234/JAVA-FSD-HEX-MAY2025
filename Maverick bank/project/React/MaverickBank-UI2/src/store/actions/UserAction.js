import axios from "axios"


export const getUserDetails = (dispatch)=>()=>{
    const token = localStorage.getItem('token');

     const baererAuthString = "Bearer " + token;
    axios.get("http://localhost:9090/api/user/get/details", {
                headers: { "Authorization": baererAuthString }
            }).then(function(response){
                console.log("in actor"+token);
                dispatch({
                    'payload':response.data,
                    'type':"GET_USER_DETAILS"
                })
            })

}

export const logOutAction= (dispatch)=>()=>{
    dispatch({
        'payload':{},
        'type':"LOG_OUT"
        })
    
}