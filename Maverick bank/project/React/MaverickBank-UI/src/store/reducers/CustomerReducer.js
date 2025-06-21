const allCustomersInitialState = {
    allCustomers:[]
}


const CustomerReducer =(state = allCustomersInitialState , action)=>{

    if(action.type==="FETCH_ALL_CUSTOMERS"){
        return {
            ...state,
            allCustomers:action.payload
        }
    }
    return state;

}

export default CustomerReducer;