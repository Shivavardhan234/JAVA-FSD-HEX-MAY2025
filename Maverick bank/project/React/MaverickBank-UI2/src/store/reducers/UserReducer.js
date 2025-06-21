


const userInitialState ={
    userDetails:{}
}



const UserReducer = (state = userInitialState,action)=>{

    if(action.type === "GET_USER_DETAILS"){
        console.log("in user reducer"+ action.payload)
        return{

            ...state,
            userDetails:action.payload
        }
    }


    if(action.type === "LOG_OUT"){
        return {
            ...state,
            userDetails:{}

        }
    }


    return state;

}

export default UserReducer;