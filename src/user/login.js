import React from "react";
import { useState } from "react";
import './login.css'
import ServerDown from "../serverDown";


const Login = ()=>{

    const [credentials, setCredentials] = useState({
      username: '',
      password: ''
    })

    const authenticateUser = (event)=>{
        event.preventDefault();
        fetch('http://localhost:8090/authentication/login', {
            headers: {
              'Content-Type': 'application/json'
            },
            method: 'POST',
            body: JSON.stringify(credentials)
        })
        .then(response=>{
          if(response.status === 200){
            return response.json();
          }
        })
        .then(data=>{
          localStorage.setItem('jwt', data.token);
          localStorage.setItem('name', data.user.firstName);
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
          <h2 className="log_h2">enter your credentials</h2>
          <div style={{marginTop: '5rem'}}>
          <div className="log_inputField">
            <label>username</label>
            <input type="email" name="username" value={credentials.username} onChange={updateCredentials} placeholder="username"/>
          </div>

          <div className="log_inputField">
            <label>password</label>
            <input type="password" name="password" value={credentials.password} onChange={updateCredentials} placeholder="password"/>
          </div>
          </div>
          
          <button className="log_submit_btn" type="submit">login</button>
          
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