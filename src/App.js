import React, {useState, useEffect} from "react";
import Login from "./user/login";
import Registration from "./user/registration"
import Home from "./home";
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import DoctorSignUp from "./doctor/doctorSignUp";
import Doctors from "./doctor/doctors";
import Appointment from "./user/appointment";
import PrivateRoute from './privateRoute'
import Appointments from "./user/appointments";
import Message from "./message/message";
import Messages from "./message/messages";
import ServerDown from "./serverDown";
import Record from "./record/record";
import Records from './record/records'


function App() {

  const [username, setUsername] = useState(localStorage.getItem('username'));
  const [notif, setNotif] = useState(false);

  const [notificationsNum, setNotificationsNum] = useState(0);
  
  useEffect(()=>{
    setUsername(localStorage.getItem('username'));
  }, [localStorage.getItem('username')]);

  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home username={username} notificationsNum={notificationsNum} setNotificationsNum={setNotificationsNum}/>}/>
          <Route path='/login' element={<Login/>}/>
          <Route path='/register' element={<Registration/>}/>
          <Route path="/newDoctor" element={<DoctorSignUp/>}/>
          <Route path="/serverDown" element={<ServerDown/>}/>
          <Route path="/doctors" element={
            <PrivateRoute>
              <Doctors username={username} notificationsNum={notificationsNum} setNotificationsNum={setNotificationsNum}/>
            </PrivateRoute>
            }/>
          <Route path="/appointment" element={
            <PrivateRoute>
              <Appointment username={username} notificationsNum={notificationsNum} setNotificationsNum={setNotificationsNum}/>
            </PrivateRoute>
          }/>
          <Route path="/appointments" element={
            <PrivateRoute>
              <Appointments username={username} notificationsNum={notificationsNum} setNotificationsNum={setNotificationsNum}/>
            </PrivateRoute>
          }/>
          <Route path="/messages" element={
            <PrivateRoute>
              <Messages username={username} notificationsNum={notificationsNum} setNotificationsNum={setNotificationsNum}/>
            </PrivateRoute>
          }/>
          <Route path="/message" element={
            <PrivateRoute>
              <Message username={username} notificationsNum={notificationsNum} setNotificationsNum={setNotificationsNum}/>
            </PrivateRoute>
          }/>
          <Route path="/newRecord" element={
            <PrivateRoute>
              <Record username={username} notificationsNum={notificationsNum} setNotificationsNum={setNotificationsNum}/>
            </PrivateRoute>
          }/>
          <Route path="/records" element={
            <PrivateRoute>
              <Records username={username} notificationsNum={notificationsNum} setNotificationsNum={setNotificationsNum}/>
            </PrivateRoute>
          }/>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;