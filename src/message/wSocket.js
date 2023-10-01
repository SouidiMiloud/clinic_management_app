import SockJS from "sockjs-client";
import Stomp from 'stompjs'


export function connectWebSocket(username, setNotificationsNum){
    const socket = new SockJS('http://localhost:8090/ws');
    const stomp = Stomp.over(socket);
    stomp.connect({}, ()=>{
      stomp.subscribe(`/user/${username}/private`, (content)=>{
        setNotificationsNum(content.body)
      });
    });
    return(()=>{
      if(stomp.connected)
        stomp.disconnect();
    });
}