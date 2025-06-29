
const initialState ={
    cio:{}
}



const cioReducer=(state = initialState, action)=>{

    if(action.type === "GET_BY_ID"){
        
        return{
            ...state,
            cio:action.payload
        }
    }
    return state;

}

export default cioReducer;