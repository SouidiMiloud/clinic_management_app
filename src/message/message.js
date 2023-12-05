import React, { useEffect, useState, useRef } from "react";
import Navbar from "../navbar";
import './message.css';
import { connectWebSocket } from "./wSocket";
import fetchData from "../api";

const Message = ({setMessageSent, rightPos, username, notificationsNum, setNotificationsNum, participUsername})=>{

    const styles = {
      width: '28rem',
      position: 'relative',
      top: '4.5rem',
      right: rightPos,
    }

    const messageRef = useRef(null);
    const [serverDown, setServerDown] = useState(true);
    const [message, setMessage] = useState('');
    const [userId, setUserId] = useState('');
    const [messages, setMessages] = useState([]);
    const [participant, setParticipant] = useState({});

    if(!participUsername){
      const params = new URLSearchParams(window.location.search);
      participUsername = params.get('participantUsername');
    }
    const [messageElements, setMessageElements] = useState({
      username: participUsername,
      message: ''
    })

    const scrollBottom = ()=>{
      setTimeout(()=>{
        if(messageRef.current){
        messageRef.current.scrollTop = messageRef.current.scrollHeight;
      }
    }, 100);
    }

    const loadMessages = ()=>{
      fetchData(`/message/getMessages?participantUsername=${participUsername}`, 'GET')
      .then(data=>{
        setServerDown(false);
        setUserId(data.userId);
        setMessages(data.messages);
        setParticipant(data.participant);
      })
      .catch(error=>setServerDown(true));
      scrollBottom();
    }

    useEffect(()=>{connectWebSocket(username, setNotificationsNum)}, [])

    useEffect(()=>{
      loadMessages();

    }, [notificationsNum, participUsername]);

    const updateMessage = (e)=>{
      const {name, value} = e.target;
      setMessageElements(prev=>({
        ...prev,
        [name]: value
      }));
    };

    const sendMessage = (e)=>{
        e.preventDefault();
        
        fetchData('/message/sendMessage', 'POST', {username: participUsername, message: message})
        .then(data=>{
          setMessageElements(prev=>({
            ...prev, ['message']: ''
          }))
          setMessage('');
          if(data !== 'empty message'){
            loadMessages();
            setMessageSent(prevState=>!prevState);
          }
        }).catch(error=>setServerDown(true));
    }

    return(
      <>
      <Navbar filter={false} notificationsNum={notificationsNum}/>
      {!serverDown && <div>
      <div className="msg_container">
        <div style={styles}>
          <div className="msg_header">
            <img src={`/images/${participant.imagePath ? participant.imagePath : 'default_user.png'}`}/>
            <h5>{participant.name}</h5>
          </div>
          <div className="msg_body" ref={messageRef}>
            {messages.map((message, index)=>(
              <div className="msg_content" key={index}>
                {(index === 0 || message.date !== messages[index - 1].date) &&
                  <div className="msg_date"><p>{message.date}</p></div>
                }
                <div className={userId == message.senderId ? 'msg_out' : 'msg_in'}>
                  <div>
                    <p>{message.content}</p>
                    <span>{message.time}</span>
                  </div>
                </div>
              </div>
              
            ))}
          </div>
          <div className="msg_bottom">
            <input value={message} onChange={(e)=>setMessage(e.target.value)} placeholder="write a message..."/>
            <button onClick={(e)=>sendMessage(e)}>send</button>
          </div>
        </div>
      </div>
      </div>}
      </>
    );
}
export default Message;