import React, {useState, useEffect} from "react";
import './navbar.css'
import ServerDown from "./serverDown";


const Navbar = (props)=>{

    const [serverDown, setServerDown] = useState(false);

    const [userNotif, setUserNotif] = useState({
      role: '',
      username: '',
      appointmentsNotifNum: '0',
      messagesNum: '0'
    });
    const [openBtn, setOpenBtn] = useState(false);
    const [openFilter, setOpenFilter] = useState(false);

    const jwt = localStorage.getItem('jwt');
    const authenticatedUser = (jwt ? localStorage.getItem('name') : 'user');

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

    const logout = ()=>{
      localStorage.removeItem('jwt');
      localStorage.removeItem('name');
      localStorage.removeItem('username');
      window.location.href='/';
    }

    useEffect(()=>{
      fetch('http://localhost:8090/user/getUserNotifs', {
        headers: {
          'Content-Type': 'application/json',
          authorization: `Bearer ${localStorage.getItem('jwt')}`
        },
        method: 'GET'
      })
      .then(response=>{
        if(response.status === 200)
          return response.json();
      })
      .then(data=>{
        setUserNotif(data);
      })
    .catch(error=>{setServerDown(true);});

    }, [props.notificationsNum])

    return (
      <>
        {!serverDown && <div className="custom_nav">
          <button className="nav_btn" onClick={()=>window.location.href='/'}>home</button>
          <button className="nav_btn" onClick={()=>window.location.href=`/appointments`}>
            {`appointments(${jwt ? userNotif.appointmentsNotifNum : ''})`}
          </button>
          <button className="nav_btn" onClick={()=>window.location.href = '/records'}>records</button>

          {(!jwt || (!props.filter && userNotif.role !== 'DOCTOR')) &&
            <button className="nav_btn" onClick={()=>window.location.href='/doctors'}>doctors</button>
          }
          {props.filter &&
          <div>
            <button className="filter_btn" onClick={clickedSpecialty}>{specialty}</button>
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

          <button className="nav_btn" onClick={()=>window.location.href=`/messages`}>
            {`messages(${jwt ? userNotif.messagesNum : ''})`}
          </button>
          
          <div>
            <button className="nav_btn" onClick={clickedDropBtn}>{authenticatedUser}</button>
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