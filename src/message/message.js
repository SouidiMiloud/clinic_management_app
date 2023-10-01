import React, { useEffect, useState, useRef } from "react";
import Navbar from "../navbar";
import './message.css';
import { connectWebSocket } from "./wSocket";


const Message = ({username, notificationsNum, setNotificationsNum})=>{

    const messageRef = useRef(null);

    const [serverDown, setServerDown] = useState(true);
    const [message, setMessage] = useState('');
    const [userId, setUserId] = useState('');
    const [messages, setMessages] = useState([]);
    const [participant, setParticipant] = useState({});

    const params = new URLSearchParams(window.location.search);
    const participantUsername = params.get('participantUsername');

    const scrollBottom = ()=>{
      setTimeout(()=>{
        if(messageRef.current){
        messageRef.current.scrollTop = messageRef.current.scrollHeight;
      }
    }, 100);
    }

    const loadMessages = ()=>{
      fetch(`http://localhost:8090/message/getMessages?participantUsername=${participantUsername}`, {
        headers: {
          'Content-Type': 'application/json',
          authorization: `Bearer ${localStorage.getItem('jwt')}`
        },
        method: 'GET',
      })
      .then(response=>{
        if(response.status === 200){
          setServerDown(false);
          return response.json();
        }
      })
      .then(data=>{
        setUserId(data.userId);
        setMessages(data.messages);
        setParticipant(data.participant);
      }).catch(error=>setServerDown(true));
      scrollBottom();
    }

    useEffect(()=>{connectWebSocket(username, setNotificationsNum)}, [])

    useEffect(()=>{
      loadMessages();

    }, [notificationsNum]);

    const sendMessage = (e)=>{
        e.preventDefault();
        fetch('http://localhost:8090/message/sendMessage', {
            headers: {
              'Content-Type': 'application/json',
              authorization: `Bearer ${localStorage.getItem('jwt')}`
            },
            method: 'POST',
            body: JSON.stringify({username: participantUsername, message: message})
        })
        .then(response=>{
          if(response.status === 200){
            return response.text();
          }
        })
        .then(data=>{
          setMessage('');
          if(data !== 'empty message'){
            loadMessages();
          }
        }).catch(error=>setServerDown(true));
    }

    return(
      <>
      <Navbar filter={false} notificationsNum={notificationsNum}/>
      {!serverDown && <div>
      <div className="msg_container">
      <div className="msg_wrapper">
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