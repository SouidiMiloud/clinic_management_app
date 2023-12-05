import React, { useEffect, useState } from "react";
import Navbar from '../navbar.js'
import './record.css'
import { connectWebSocket } from "../message/wSocket.js";
import fetchData from "../api.js";

const Record = ({username, notificationsNum, setNotificationsNum})=>{

    const params = new URLSearchParams(window.location.search);
    const appointmentId = params.get('appId');

    const [record, setRecord] = useState({
        disease: '',
        tests: '',
        results: '',
        medications: '',
        details: ''
    });
    const [serverDown, setServerDown] = useState(true);

    const updateRecord = (event)=>{
        const {name, value} = event.target;
        setRecord(prev=>({
            ...prev,
            [name]: value
        }));
    };

    useEffect(()=>{
        
        fetchData('/user/getUsername', 'GET')
        .then(response=>{
              setServerDown(false);
      }).catch(error=>setServerDown(true));


        connectWebSocket(username, setNotificationsNum);

    }, []);

    const saveRecord = ()=>{
        fetchData(`/record/saveRecord?appointmentId=${appointmentId}`, 'POST', record)
        .then(data=>{
          window.location.href = '/newRecord';
        })
        .catch(error=>{setServerDown(true)});
    }

    return(
      <>
        <Navbar filter={false} notificationsNum={notificationsNum}/>
        {!serverDown && <div className="record_wrapper">
          <h3 style={{color: '#104F5F'}}>Add a new record</h3>
          <div>
            <label>disease</label>
            <input name="disease" value={record.disease} onChange={updateRecord} placeholder="disease"/>
          </div>
          <div>
            <label>tests</label>
            <input name="tests" value={record.tests} onChange={updateRecord} placeholder="tests passed"/>
          </div>
          <div>
            <label>results</label>
            <input name="results" value={record.results} onChange={updateRecord} placeholder="tests results"/>
          </div>
          <div>
            <label>medications</label>
            <input name="medications" value={record.medications} onChange={updateRecord} placeholder="medications required"/>
          </div>
          <div>
            <label>details</label>
            <textarea name="details" value={record.details} onChange={updateRecord}/>
          </div>
          <button onClick={()=>saveRecord()}>save record</button>
        </div>
}
      </>
    );
}
export default Record;