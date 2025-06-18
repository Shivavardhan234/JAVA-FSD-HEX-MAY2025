
export const deleteUserDetails=(dispatch)=>{
    dispatch({
        'payload': "",
        'type': "DELETE_USER_DETAILS"
    })
}

export const setUserDetails=(dispatch)=>(user)=>{
    dispatch({
        'payload':user,
        'type': "SET_USER_DETAILS"
    })

}




