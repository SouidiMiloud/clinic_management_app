import React, { useEffect, useState } from "react";
import Navbar from "../navbar";
import './messages.css'
import { connectWebSocket } from "./wSocket";


const Messages = ({username, notificationsNum, setNotificationsNum})=>{

    const [conversations, setConversations] = useState([]);

    useEffect(()=>{
      fetch('http://localhost:8090/message/getConversations', {
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
      .then(data=>setConversations(data))
      .catch(error=>{});

    }, [notificationsNum]);

    useEffect(()=>{connectWebSocket(username, setNotificationsNum)}, []);

    return(
      <div>
        <Navbar filter={false} notificationsNum={notificationsNum}/>
        {conversations && <div className="conv_container">
          <div className="conv_wrapper">
            {conversations.map(conv=>(
              <div className="conv_element" onClick={()=>window.location.href=`/message?participantUsername=${conv.participantUsername}`}>
                <img src={`images/${conv.participantImage ? conv.participantImage : 'default_user.png'}`} alt={conv.id}/>
                <div className="conv_name">
                  <h3>{conv.participantName}</h3>
                  <p>{conv.lastMsgContent}</p>
                </div>
                <div>
                  <div className="conv_date">{conv.lastMsgTime}</div>
                  {conv.unreadMsgsNum != 0 &&
                    <div className="conv_unread">{conv.unreadMsgsNum}</div>
                  }
                </div>
              </div>
            ))}
          </div>
        </div>}
      </div>);
}
export default Messages;