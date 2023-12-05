import React, {useState, useEffect} from "react";
import Navbar from "../navbar";
import './records.css'
import { connectWebSocket } from "../message/wSocket";
import fetchData from "../api";

const Records = ({username, notificationsNum, setNotificationsNum})=>{

    const [records, setRecords] = useState([]);
    const [role, setRole] = useState('');
    const [serverDown, setServerDown] = useState(true);

    useEffect(()=>{
      fetchData('/record/getRecords', 'GET')
      .then(data=>{
        setServerDown(false);
        setRole(data.role);
        setRecords(data.records);
      })
      .catch(error=>setServerDown(true));

      connectWebSocket(username, setNotificationsNum);

    }, []);

    return(
      <>
        <Navbar filter={false} notificationsNum={notificationsNum}/>
        {!serverDown && <div className="records_wrapper">
          <table>
            <thead>
              <tr>
                <th>{role === 'DOCTOR' ? 'patient' : 'doctor'}</th>
                <th>date of visit</th>
                <th>disease</th>
                <th>tests</th>
                <th>results</th>
                <th>medications</th>
                <th>more details</th>
              </tr>
            </thead>
            <tbody>
              {records.map(record=>(<tr>
                <td>{record.userName}</td>
                <td>{record.dateOfVisit}</td>
                <td>{record.disease}</td>
                <td>{record.tests}</td>
                <td>{record.results}</td>
                <td>{record.medications}</td>
                <td>{record.details}</td>
              </tr>))}
            </tbody>
          </table>
        </div>}
      </>
    );
}
export default Records;