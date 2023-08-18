import React, { useState } from "react";
import "./App.css"
import logoImg from "./logo.png"
import axios from "axios";

function Login(){

    const [credentials, setCredentials] = useState({
        email: '',
        password: ''
    });

    const change = (event)=>{
        const {name, value} = event.target;
        setCredentials((prevCred)=>({
            ...prevCred,
            [name]: value
        }));
    };

    function submit(){
        
        fetch("http://localhost:8090/authentication/login", {
            headers: {
                "Content-Type": "application/json",
            },
            method: "post",
            body: JSON.stringify(credentials),
        })
        .then((response)=>{
            if(response.status === 200)
                return Promise.all([response.json(), response.headers])
            else
                return Promise.reject("email ou mot de passe incorrects");
        })
        .then(([body, headers])=>{
            localStorage.setItem('jwt', headers.get("authorization"));
            window.location.href='/materials';
        });
    }
    
    return (
        <div >
            <div class="wrapper">
                <div class="logo">
                    <img src={logoImg} alt=""/>
                </div>
                <div class="text-center name">
                    Connectez-vous
                </div>
                <form onSubmit={submit} class="p-3 mt-3">
                    <div class="form-field d-flex align-items-center">
                        <span class="far fa-user"></span>
                        <input type="email" onChange={change} name="email" value={credentials.email} id="userName" placeholder="Username"/>
                    </div>
                    <div class="form-field d-flex align-items-center">
                        <span class="fas fa-key"></span>
                        <input type="password" onChange={change} name="password" value={credentials.password} id="pwd" placeholder="Password"/>
                    </div>
                    
                    <button type="submit" class="btn mt-3 pt-3"><h4>se connecter</h4></button>
                </form>
                <div class="text-center fs-6">
                    <a href="#"><h5>mot de passe oublié?</h5></a> ou <a href="/register"><h5>s'inscrire</h5></a>
                </div>
            </div>
        </div>
    );
}

export default Login