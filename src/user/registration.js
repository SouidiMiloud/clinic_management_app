import React, { useState } from "react";
import './registration.css'
import ServerDown from "../serverDown";


const Registration = ()=>{

    const [info, setInfo] = useState({
        firstName: '',
        lastName: '',
        username: '',
        phone: '',
        password: '',
        confirmPwd: ''
    });

    const [imagePath, setImagePath] = useState(null);

    const updateInfo = (event)=>{
        const {name, value} = event.target;
        setInfo(prev=>({
            ...prev,
            [name]: value
        }));
    }

    const registerUser = (e)=>{
        e.preventDefault();

        const formData = new FormData();
        formData.append('image', imagePath);
        formData.append('firstName', info.firstName);
        formData.append('lastName', info.lastName);
        formData.append('username', info.username);
        formData.append('phone', info.phone);
        formData.append('password', info.password);
        formData.append('confirmPwd', info.confirmPwd);

        fetch('http://localhost:8090/user/register', {
            method: 'POST',
            body: formData
        })
        .then((response)=>{
          if(response.status === 200){
            window.location.href='/login';
            return response.json();
          }
        })
    }

    return (
      <div style={{width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
        <form onSubmit={registerUser}>
          <div className="reg_wrapper">
          <h2>create your account here</h2>
            <div style={{marginTop: '4rem'}}>
              <div className="reg_inputField">
                <label>first name</label>
                <input type="text" name="firstName" value={info.firstName} onChange={updateInfo} placeholder="enter your first name"/>
              </div>
              <div className="reg_inputField">
                <label>username</label>
                <input type="email" name="username" value={info.username} onChange={updateInfo} placeholder="enter your username"/>
            </div>
            <div className="reg_inputField">
              <label>password</label>
              <input type="password" name="password" value={info.password} onChange={updateInfo} placeholder="enter your password"/>
            </div>
            <div className="reg_inputField">
              <label>photo</label>
              <input type="file" onChange={e=>{setImagePath(e.target.files[0])}}/>
            </div>
          </div>

          <div style={{marginTop: '4rem'}}>
            <div className="reg_inputField">
              <label>last name</label>
              <input type="text" name="lastName" value={info.lastName} onChange={updateInfo} placeholder="enter your last name"/>
            </div>
            <div className="reg_inputField">
              <label>phone</label>
              <input type="text" name="phone" value={info.phone} onChange={updateInfo} placeholder="enter your phone number"/>
            </div>
            <div className="reg_inputField">
              <label>confirm</label>
              <input type="password" name="confirmPwd" value={info.confirmPwd} onChange={updateInfo} placeholder="confirm your password"/>
            </div>
          </div>

            <button className="reg_submit_btn">register</button>
            </div>
          </form>
      </div>
    );
}
export default Registration;

