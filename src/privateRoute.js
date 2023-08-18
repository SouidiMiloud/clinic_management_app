import React, { useState } from "react";
import {Navigate} from "react-router-dom"


const PrivateRoute = (({child})=>{
    //const [jwt, setJwt] = useState('');
    const jwt = localStorage.getItem('jwt');
    return (jwt ? child : <Navigate to="/login"/>);
});

export default PrivateRoute;