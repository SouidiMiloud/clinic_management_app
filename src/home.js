import React, { useEffect } from "react";
import Navbar from "./navbar";
import { connectWebSocket } from "./message/wSocket";
import './home.css'


const Home = ({username, notificationsNum, setNotificationsNum})=>{

  useEffect(() => {
    connectWebSocket(username, setNotificationsNum);
  }, []);

  const jwt = localStorage.getItem('jwt');

  return (
    <>
      <Navbar filter={false} notificationsNum={notificationsNum}/>
      <div className="home_wrapper">
        <h1>Discover a world of health and wellness with our expert medical team</h1>
        {!jwt && <div>
          <button onClick={()=>window.location.href='/login'}>login</button>
          <button onClick={()=>window.location.href='/register'}>register</button>
        </div>}
      </div>
    </>
  );
}
export default Home;