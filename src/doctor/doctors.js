import React, { useEffect, useState } from "react";
import Navbar from '../navbar';
import './doctors.css';
import {connectWebSocket} from '../message/wSocket'
import fetchData from "../api";


const Doctors = ({username, notificationsNum, setNotificationsNum})=>{

    const [doctorsList, setDoctorList] = useState([]);
    const [specialty, setSpecialty] = useState('ALL');

    const updateSpecialty = (new_specialty)=>{
      setSpecialty(new_specialty);
    }

    useEffect(()=>{
      fetchData(`/doctor/doctors?specialty=${specialty}`, 'GET')
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
                
                <div className="specialty_div">
                  <p>{doctor.specialty}</p>
                </div>

                <p>{doctor.firstName + ' ' + doctor.lastName}</p>
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