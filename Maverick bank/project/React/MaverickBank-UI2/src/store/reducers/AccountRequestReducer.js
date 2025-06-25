
const initialState ={
    accountRequest:{}
}



const accountRequestReducer=(state = initialState, action)=>{

    if(action.type === "GET_BY_ID"){
        
        return{
            ...state,
            accountRequest:action.payload
        }
    }
    return state;

}

export default accountRequestReducer;