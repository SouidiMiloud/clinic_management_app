import React, { useEffect, useState } from "react";
import Navbar from '../navbar'
import './appointments.css'
import { connectWebSocket } from "../message/wSocket";
import fetchData from "../api";


const Appointments = ({username, notificationsNum, setNotificationsNum})=>{

    const [appointments, setAppointments] = useState([]);
    const [role, setRole] = useState('');
    const [usernameSearch, setUsernameSearch] = useState('');

    useEffect(()=>{connectWebSocket(username, setNotificationsNum)}, []);
    useEffect(()=>{
      
      loadAppointments();

    }, [notificationsNum]);

    const loadAppointments = ()=>{
      fetchData(`/user/appointments?username=${usernameSearch}`, 'GET')
      .then(data=>{
        setAppointments(data.appointments);
        setRole(data.role);
      }).catch(error=>{});
    }

    const checkAppointment = (appId, decision)=>{

      fetchData('/doctor/checkAppointment', 'POST', {id: appId, decision: decision})
      .then(data=>setNotificationsNum(notificationsNum + 1));
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
                        color: (app.status === 'CONFIRMED' ? '#0a5' : 
                        (app.status === 'REJECTED' ? '#DC3545' : '#444'))}}>
                      {app.status}
                    </td>}
                    {role === 'DOCTOR' && app.status === 'CONFIRMED' &&
                      <td className="status">
                        <button className="app_contact" onClick={()=>window.location.href=`/newRecord?appId=${app.appointmentId}`}>record</button>
                        <button className="app_contact" onClick={()=>window.location.href=`/message?participantUsername=${app.participantUsername}`}>message</button>
                      </td>}
                    {role === 'DOCTOR' && app.status === 'UNCHECKED' &&
                      <td className="status">
                        <button className="confirm" onClick={()=>checkAppointment(app.appointmentId, 'CONFIRMED')}>confirm</button>
                        <button className="reject" onClick={()=>checkAppointment(app.appointmentId, 'REJECTED')}>reject</button>
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