import React, { useEffect, useState } from "react";
import Navbar from "../navbar";
import './appointment.css'
import { connectWebSocket } from "../message/wSocket";


const Appointment = ({username, notificationsNum, setNotificationsNum})=>{
    
    const params = new URLSearchParams(window.location.search);
    const doctor_ = params.get('doctor');
    const doctor = JSON.parse(decodeURIComponent(doctor_));

    const [appointment, setAppointment] = useState({
      doctorUsername: doctor.username,
      start: '',
      duration: '',
      description: ''
    });
    const [serverMessage, setServerMessage] = useState('');
    const [req_status, setReq_status] = useState(0);

    useEffect(()=>{

      connectWebSocket(username, setNotificationsNum);
    
    }, []);

    const updateAppointment = (event)=>{

      const {name, value} = event.target;
      setAppointment(prev=>({
        ...prev,
        [name]: value
      }));
    }

    const confirmAction = ()=>{
      setServerMessage('');
      window.location.reload();
    }

    const updateDuration = (event)=>{
      let value = event.target.value;
      const character = value.charAt(value.length - 1);
      if(character > '9' || character < '0')
        value = value.replace(character, '');
      if(value === '0')
        value = '';
      setAppointment(prev=>({
        ...prev,
        ['duration']: value
      }));
    }

    const helpSaveFunc = (endPoint)=>{
      fetch(endPoint, {
        headers: {
          'Content-Type': 'application/json',
          authorization: `Bearer ${localStorage.getItem('jwt')}`
        },
        method: 'POST',
        body: JSON.stringify(appointment)
      })
      .then(response=>{
        setReq_status(response.status);
        return response.text();
      })
      .then(data=>{
        setServerMessage(data);
      })
    }

    const saveAppointment = (e)=>{
      e.preventDefault();
      helpSaveFunc('http://localhost:8090/user/saveAppointment')
    }

    const takeAppointment = (e)=>{
      e.preventDefault();
      if(!appointment.start || !appointment.duration)
        return;
      helpSaveFunc('http://localhost:8090/user/appointment');
    }

    return(
        <div>
          <Navbar filter={false} notificationsNum={notificationsNum}/>
          <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
            <div className="app_wrapper">
              <h3>Schedule an appointment with your doctor</h3>
              <div className="doctor_info">
                <img src={`images/${doctor.profileImagePath ? doctor.profileImagePath : 'default_user.png'}`}/>
                <div>
                  <div className="detail">
                    <p>doctor</p>
                    <h3>{` ${doctor.firstName} ${doctor.lastName}`}</h3>
                  </div>
                  <div className="detail">
                    <p>specialty:</p>
                    <h3>{doctor.specialty}</h3>
                  </div>
                  <div className="detail">
                    <p>availability:</p>
                    <h3>{`${doctor.workStart} - ${doctor.workEnd}`}</h3>
                  </div>
                </div>
              </div>

              <div className="appointment">
                <div className="app_element">
                  <label>start time</label>
                  <input type="datetime-local" name='start' value={appointment.start} onChange={updateAppointment}/>
                  <label>duration(minutes)</label>
                  <input type='text' name='duration' value={appointment.duration} onChange={updateDuration}/>
                </div>
                <div className="app_element">
                  <label>description</label>
                  <textarea className="description" name="description" value={appointment.description} onChange={updateAppointment}/>
                </div>
              </div>
              <button className="app_btn" onClick={takeAppointment}>Schedule Appointment</button>
            </div>

            {serverMessage &&
              <div className="server_msg">
                {req_status === 201 &&
                  <p style={{color: '#155724'}}>{serverMessage}</p>
                }
                {req_status === 202 &&
                  <div>
                    <p style={{color: '#856404'}}>{serverMessage}</p>
                    <div className="app_warning">
                      <button onClick={saveAppointment}>confirm anyway</button>
                      <button onClick={()=>setServerMessage('')}>new appointment</button>
                    </div>
                  </div>
                }
                {req_status === 400 &&
                  <p style={{color: '#721C24'}}>{`${serverMessage}, try again`}</p>
                }
                {req_status !== 202 &&
                  <button onClick={()=>confirmAction()}>OK</button>
                }
                </div>
            }
          </div>
        </div>
    );
}
export default Appointment;