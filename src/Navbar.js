import React, {useState, useEffect} from "react";
import './navbar.css'
import ServerDown from "./serverDown";
import fetchData from "./api";


const Navbar = (props)=>{
    const active = {
      color: '#ddd',
      fontWeight: 'bold',
      borderBottom: 'solid 2px #fff',
      transition: '.4s'
    };
    const [serverDown, setServerDown] = useState(false);
    const [activeButton, setActiveButton] = useState('home');
    const [userNotif, setUserNotif] = useState({
      role: '',
      username: '',
      appointmentsNotifNum: '0',
      messagesNum: '0'
    });
    const [openBtn, setOpenBtn] = useState(false);
    const [openFilter, setOpenFilter] = useState(false);

    const jwt = localStorage.getItem('jwt');
    const authenticatedUser = (jwt ? localStorage.getItem('name') : '');

    const [specialty, setSpecialty] = useState('specialty')

    const changeSpecialty = (specialty_)=>{
      setSpecialty(specialty_);
      props.updateSpecialty(specialty_);
      setOpenFilter(false);
    }
    const clickedDropBtn = ()=>{
      setOpenBtn(!openBtn);
      setOpenFilter(false);
    }
    const clickedSpecialty = ()=>{
      setOpenFilter(!openFilter);
      setOpenBtn(false);
    }
    const clickButton = (path, e)=>{
      e.preventDefault();
      window.location.href = path;
    }

    const logout = ()=>{
      localStorage.removeItem('jwt');
      localStorage.removeItem('name');
      localStorage.removeItem('username');
      window.location.href='/';
    }

    useEffect(()=>{
      const path = window.location.pathname;
      const buttonMap = {
        '/': 'home',
        '/appointments': 'appointments',
        '/records': 'record',
        '/doctors': 'doctors',
        '/messages': 'messages'
      };
      setActiveButton(buttonMap[path]);
    });

    useEffect(()=>{
      fetchData('/user/getUserNotifs', 'GET')
      .then(data=>{
        setUserNotif(data);
      })
      .catch(error=>{setServerDown(true);});
    }, [props.notificationsNum])

    return (
      <>
        {!serverDown && <div className="custom_nav">
          <button className="nav_btn" style={activeButton==='home'?active:{}} onClick={(e)=>clickButton('/', e)}>Home</button>
        
          <button className="nav_btn" style={activeButton==='appointments'?active:{}} onClick={(e)=>clickButton(`/appointments`, e)}>
            <p>Appointments</p>
            {(jwt && userNotif.appointmentsNotifNum !== '0') && <div className="notif_num">{userNotif.appointmentsNotifNum}</div>}
          </button>

          <button className="nav_btn" style={activeButton==='record'?active:{}} onClick={(e)=>clickButton('/records', e)}>Records</button>

          {(!jwt || (!props.filter && userNotif.role !== 'DOCTOR')) &&
            <button className="nav_btn" style={activeButton==='doctors'?active:{}} onClick={(e)=>clickButton('/doctors', e)}>Doctors</button>
          }
          {props.filter &&
          <div>
            <button className="filter_btn" style={activeButton==='doctors'?active:{}} onClick={clickedSpecialty}>{specialty}</button>
            {openFilter && <div className="drop_filter">
            <button className="drop_btn" onClick={()=>changeSpecialty('ALL')}>ALL</button>
              <button className="drop_btn" onClick={()=>changeSpecialty('CARDIOLOGY')}>CARDIOLOGY</button>
              <button className="drop_btn" onClick={()=>changeSpecialty('NEUROLOGY')}>NEUROLOGY</button>
              <button className="drop_btn" onClick={()=>changeSpecialty('PULMONOLOGY')}>PULMONOLOGY</button>
              <button className="drop_btn" onClick={()=>changeSpecialty('PSYCHIATRY')}>PSYCHIATRY</button>
            </div>
          }
          </div>
        }
          <button className="nav_btn" style={activeButton==='messages'?active:{}} onClick={(e)=>clickButton(`/messages`, e)}>
            <p>Messages</p>
            {(jwt && userNotif.messagesNum !== '0') && <div className="notif_num">{userNotif.messagesNum}</div>}
          </button>
          <div>
            <button className="nav_btn nav_btn_user" onClick={clickedDropBtn}>
              {authenticatedUser}
            </button>
            {openBtn &&
            <div className="drop_div">
              <button>profile</button>
              <button>settings</button>
              <button
                onClick={jwt ? logout : ()=>window.location.href = '/login'}>{jwt ? 'logout' : 'login'}
              </button> 
            </div>
        }
          </div>
        </div>}
        {serverDown && <ServerDown/>}
        </>
    );
}
export default Navbar;