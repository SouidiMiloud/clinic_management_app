import React, { useEffect, useState } from "react";
import Navbar from '../navbar'
import './appointments.css'
import { connectWebSocket } from "../message/wSocket";


const Appointments = ({username, notificationsNum, setNotificationsNum})=>{

    const [appointments, setAppointments] = useState([]);
    const [role, setRole] = useState('');
    const [usernameSearch, setUsernameSearch] = useState('');

    useEffect(()=>{connectWebSocket(username, setNotificationsNum)}, []);
    useEffect(()=>{
      
      loadAppointments();

    }, [notificationsNum]);

    const loadAppointments = ()=>{
      fetch(`http://localhost:8090/user/appointments?username=${usernameSearch}`, {
        headers: {
            'Content-Type': 'application/json',
            authorization: `Bearer ${localStorage.getItem('jwt')}`
        },
        method: 'GET',
      })
      .then(response=>{
        if(response.status === 200)
            return response.json();
      })
      .then(data=>{
        setAppointments(data.appointments);
        setRole(data.role);
      }).catch(error=>{})
    }

    const checkAppointment = (appId, decision)=>{
      fetch('http://localhost:8090/doctor/checkAppointment', {
        headers: {
          'Content-Type': 'application/json',
          authorization: `Bearer ${localStorage.getItem('jwt')}`
        },
        method: 'POST',
        body: JSON.stringify({id: appId, decision: decision})
      })
      .then(response=>{
        if(response.status === 200)
          setNotificationsNum(notificationsNum + 1);
      })
    }

    return(
        <div>
          <div>
          <Navbar filter={false} notificationsNum={notificationsNum}/>
          {role && <div className="apps_wrapper">
            <div className="apps_search">
              <input onChange={(e)=>setUsernameSearch(e.target.value)} placeholder="search for email"/>
              <button onClick={()=>loadAppointments()}>search</button>
            </div>
            <table>
              <thead>
                <tr>
                  <th>{role === 'PATIENT' ? 'doctor' : 'patient'}</th>
                  <th>email</th>
                  <th>start</th>
                  <th>duration</th>
                  <th>description</th>
                  <th>status</th>
                </tr>
              </thead>
              <tbody>
                {appointments && appointments.map(app=>(
                  <tr>
                    <td>{app.name}</td>
                    <td>{app.participantUsername}</td>
                    <td>{app.start}</td>
                    <td>{`${app.duration} min`}</td>
                    <td>{app.description}</td>
                    {(role === 'PATIENT' || app.status === 'REJECTED') && <td
                      style={{width: '17%', fontStyle: 'italic', 
                        color: (app.status === 'CONFIRMED' ? '#393' : 
                        (app.status === 'REJECTED' ? '#933' : '#aa0'))}}>
                      {app.status}
                    </td>}
                    {role === 'DOCTOR' && app.status === 'CONFIRMED' &&
                      <td className="status">
                        <button className="app_contact" onClick={()=>window.location.href=`/newRecord?appId=${app.id}`}>record</button>
                        <button className="app_contact" onClick={()=>window.location.href=`/message?participantUsername=${app.participantUsername}`}>message</button>
                      </td>}
                    {role === 'DOCTOR' && app.status === 'UNCHECKED' &&
                      <td className="status">
                        <button className="confirm" onClick={()=>checkAppointment(app.id, 'CONFIRMED')}>confirm</button>
                        <button className="reject" onClick={()=>checkAppointment(app.id, 'REJECTED')}>reject</button>
                      </td>
                    }
                  </tr>
                ))}
              </tbody>
            </table>
          </div>}
          </div>
        </div>
    );
}
export default Appointments;