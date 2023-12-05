import React, { useEffect, useState } from "react";
import Navbar from "../navbar";
import './messages.css'
import { connectWebSocket } from "./wSocket";
import fetchData from "../api";
import Message from "./message";


const Messages = ({username, notificationsNum, setNotificationsNum})=>{

    const [conversations, setConversations] = useState([]);
    const [convChosen, setConvChosen] = useState(false);
    const [participUsername, setParticipUsername] = useState('');
    const [usernameSearch, setUsernameSearch] = useState(''); 

    const [messageSent, setMessageSent] = useState(false);

    const [containerStyle, setContainerStyle] = useState({
      justifyContent: 'center',
      marginLeft: 0
    })

    const [wrapperStyle, setWrapperStyle] = useState({
      width: '70%'
    })

    useEffect(()=>{
      loadMessages();

    }, [notificationsNum, participUsername, messageSent]);

    const loadMessages = ()=>{
      fetchData(`/message/getConversations?usernameSearch=${usernameSearch}`, 'GET')
      .then(data=>setConversations(data))
      .catch(error=>{});
    }

    const messaging = (participantUsername)=>{
      setParticipUsername(participantUsername);
      setContainerStyle({justifyContent: 'left'});
      setWrapperStyle({width: '55%', marginLeft: '1rem'});
      setConvChosen(true);
    }

    useEffect(()=>{connectWebSocket(username, setNotificationsNum)}, []);

    return(
      <div>
        {!convChosen && <Navbar notificationsNum={notificationsNum}/>}
        {convChosen && <Message setMessageSent={setMessageSent} rightPos={'-20rem'} username={username} notificationsNum={notificationsNum} setNotificationsNum={setNotificationsNum} participUsername={participUsername}/>}
        {conversations && <div className="conv_container" style={containerStyle}>
          <div className="conv_wrapper" style={wrapperStyle}>
            <div className="msgs_search">
              <input onChange={(e)=>setUsernameSearch(e.target.value)} placeholder="search by first or last name"/>
              <button onClick={()=>loadMessages()}>search</button>
            </div>
            <div className="convs">
              {conversations.map(conv=>(
                <div className="conv_element" onClick={()=>messaging(conv.participantUsername)}>
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
          </div>
        </div>}
      </div>);
}
export default Messages;