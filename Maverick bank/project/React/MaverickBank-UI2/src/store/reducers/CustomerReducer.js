
const initialState ={
    customer:{}
}



const customerReducer=(state = initialState, action)=>{

    if(action.type === "GET_BY_ID"){
        
        return{
            ...state,
            customer:action.payload
        }
    }
    return state;

}

export default customerReducer;