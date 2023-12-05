import React, { useState } from "react";
import './doctorSignUp.css'


const DoctorSignUp = ()=>{

    const [info, setInfo] = useState({
        firstName: '',
        lastName: '',
        username: '',
        phone: '',
        password: '',
        confirmPwd: '',
        workStart: '',
        workEnd: '',
        specialty: 'CARDIOLOGY'
    });
    const [imagePath, setImagePath] = useState(null);
    const [specialtyOpen, setSpecialtyOpen] = useState(false);

    const updateInfo = (e)=>{
        const {name, value} = e.target;
        setInfo(prev=>({
            ...prev,
            [name]: value
        }))
    }

    const changeSpecialty = (newSpecialty)=>{
        setInfo({
            ...info,
            specialty: newSpecialty
        });
        setSpecialtyOpen(false);
    }

    const register = (e)=>{
        e.preventDefault();

        const formData = new FormData();
        formData.append('firstName', info.firstName);
        formData.append('lastName', info.lastName);
        formData.append('username', info.username);
        formData.append('phone', info.phone);
        formData.append('password', info.password);
        formData.append('confirmPwd', info.confirmPwd);
        formData.append('workStart', info.workStart);
        formData.append('workEnd', info.workEnd);
        formData.append('specialty', info.specialty);
        formData.append('image', imagePath);
        
        fetch('http://localhost:8090/doctor/register', {
            method: 'POST',
            body: formData
        })
        .then(response=>{
            if(response.status === 200){
                window.location.href='/login';
                return response.json();
            }
        })
    }

    return(
      <div style={{width: '100%', display: 'flex', justifyContent: 'center'}}>
        <form onSubmit={register}>
          <div className="wrapper">
            <div>
              <div className="inputField">
                <label>first name</label>
                <input type="text" name='firstName' value={info.firstName} onChange={updateInfo} placeholder="first name"/>
              </div>
              <div className="inputField">
                <label>username</label>
                <input type="email" name='username' value={info.username} onChange={updateInfo} placeholder="username"/>
              </div>
              <div className="inputField">
                <label>password</label>
                <input type="password" name='password' value={info.password} onChange={updateInfo} placeholder="password"/>
              </div>
              <div className="inputField">
                <label>workd start</label>
                <input type="time" name='workStart' value={info.workStart} onChange={updateInfo} placeholder="start of the work"/>
              </div>
              <div className="inputField">
                <label>specialty</label>
                <div style={{position: 'relative'}}>
                  <button type="button" className="specialty" onClick={()=>{setSpecialtyOpen(!specialtyOpen)}}>{info.specialty}</button>
              
                  {specialtyOpen &&
                    <div className="options">
                      <button type="button" onClick={()=>changeSpecialty('CARDIOLOGY')}>CARDIOLOGY</button>
                      <button type="button" onClick={()=>changeSpecialty('NEUROLOGY')}>NEUROLOGY</button>
                      <button type="button" onClick={()=>changeSpecialty('PULMONOLOGY')}>PULMONOLOGY</button>
                      <button type="button" onClick={()=>changeSpecialty('PSYCHIATRY')}>PSYCHIATRY</button>
                    </div>
                  }
                </div>
              </div>
            </div>

            <div>
              <div className="inputField">
                <label>last name</label>
                <input type="text" name='lastName' value={info.lastName} onChange={updateInfo} placeholder="last name"/>
              </div>
              <div className="inputField">
                <label>phone</label>
                <input type="text" name='phone' value={info.phone} onChange={updateInfo} placeholder="phone"/>
              </div>
              <div className="inputField">
                <label>confirm</label>
                <input type="password" name='confirmPwd' value={info.confirmPwd} onChange={updateInfo} placeholder="confirm your password"/>
              </div>
              <div className="inputField">
                <label>workd end</label>
                <input type="time" name='workEnd' value={info.workEnd} onChange={updateInfo} placeholder="end of the work"/>
              </div>
              <div className="inputField">
                <label>photo</label>
                <input type="file" onChange={(e)=>{setImagePath(e.target.files[0])}}/>
              </div>
            </div>
            
            <button className="submit_btn">sign up</button>
        </div>

        </form>
      </div>
    );
}
export default DoctorSignUp;