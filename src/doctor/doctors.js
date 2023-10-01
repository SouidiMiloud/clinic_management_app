import React, { useEffect, useState } from "react";
import Navbar from '../navbar';
import './doctors.css';
import {connectWebSocket} from '../message/wSocket'


const Doctors = ({username, notificationsNum, setNotificationsNum})=>{

    const [doctorsList, setDoctorList] = useState([]);
    const [specialty, setSpecialty] = useState('ALL');

    const updateSpecialty = (new_specialty)=>{
      setSpecialty(new_specialty);
    }

    const jwt = localStorage.getItem('jwt');

    useEffect(()=>{
      fetch(`http://localhost:8090/doctor/doctors?specialty=${specialty}`, {
          headers: {
              'Content-Type': 'application/json',
              authorization: `Bearer ${jwt}`
          },
          method: 'GET'
      })
      .then(response=>{
          if(response.status === 200){
              return response.json();
          }
      })
      .then(data=>{
          setDoctorList(data);
      }).catch(error=>{});
    }, [specialty]);

    useEffect(()=>{connectWebSocket(username, setNotificationsNum);}, []);

    return(
      <div>
        <Navbar filter={true} updateSpecialty={updateSpecialty} notificationsNum={notificationsNum}/>
        <div className="doctors_wrapper">
          <div className="doctor_grid">
            {doctorsList.map((doctor)=>(
              <div className="grid_element">
                <img src={`images/${doctor.profileImagePath ? doctor.profileImagePath : 'default_user.png'}`} alt={doctor.firstName}/>
                <p>{doctor.firstName + ' ' + doctor.lastName}</p>
                <p>{doctor.specialty}</p>
                <div className="interact">
                  <button onClick={()=>window.location.href=`/appointment?doctor=${encodeURIComponent(JSON.stringify(doctor))}`}>appointment</button>
                  <button onClick={()=>window.location.href=`/message?participantUsername=${doctor.username}`}>message</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
}
export default Doctors;