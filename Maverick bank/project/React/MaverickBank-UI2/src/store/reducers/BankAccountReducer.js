
const bankAccountReducerInitialState ={
    account:{}
}



const bankAccountReducer=(state = bankAccountReducerInitialState, action)=>{

    if(action.type === "GET_ACCOUNT_BY_ID"){
        
        return{
            ...state,
            account:action.payload
        }
    }
    return state;

}

export default bankAccountReducer;