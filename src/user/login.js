import React from "react";
import { useState } from "react";
import './login.css'
import fetchData from "../api";


const Login = ()=>{

    const [credentials, setCredentials] = useState({
      username: '',
      password: ''
    })

    const authenticateUser = (event)=>{
        event.preventDefault();
        fetchData('/authentication/login', 'POST', credentials)
        .then(data=>{
          localStorage.setItem('jwt', data.token);
          localStorage.setItem('name', data.user.firstName + ' ' + data.user.lastName);
          localStorage.setItem('username', credentials.username);
          
          window.location.href='/';
        });
    }

    const updateCredentials = (event)=>{
      const {name, value} = event.target;
      setCredentials((prev)=>({
        ...prev,
        [name]: value
      }));
    }

    return(
      <div style={{width: '100%', display: 'flex', justifyContent: 'center'}}>
        <div className="log_wrapper">
        <form onSubmit={authenticateUser}>
          <h2 className="log_h2">Enter your credentials</h2>
          <div style={{marginTop: '5rem'}}>
          <div className="log_inputField">
            <label>username</label>
            <input type="email" name="username" value={credentials.username} onChange={updateCredentials} placeholder="username"/>
          </div>

          <div className="log_inputField">
            <label>password</label>
            <input type="password" name="password" value={credentials.password} onChange={updateCredentials} placeholder="password"/>
          </div>
          <button type="submit">login</button>
          </div>
          
          <br/>
          <div className="forgot_pwd">
            <a className="link" href="/#">forgot password?</a>
            <a className="link" href="/register">sign up</a>
          </div>

        </form>
        </div>
      </div>
    );
}
export default Login;