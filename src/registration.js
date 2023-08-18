import React, {useState} from "react";
import "./App.css";
import axios from "axios";

function Registration(){

    const [user, setUser] = useState({
        first_name: '',
        last_name: '',
        email: '',
        phone: '',
        password: '',
        confirm_password: ''
    });

    const [regResponse, setRegResponse] = useState({});

    const inputChange = (e)=>{
        const {name, value} = e.target;
        setUser((prevUser)=>({
            ...prevUser,
            [name]: value
        }));
    };

    const newUser = async(e)=>{
        e.preventDefault();
        try{
            const response = await axios.post('http://localhost:8090/newUser', user)
            window.location.href = '/login';          
        }catch(error){
            if(error.response)
                setRegResponse(error.response.data);
            else
                setRegResponse("une erreur est survenue");
            console.log(error);
        }
    }

    return (
        <div>
            <div class="wrapper">
                <div class="text-center name">
                    inscrivez-vous
                </div>
                <form onSubmit={newUser} class="p-3 mt-3">
                    <div class="form-field d-flex align-items-center">
                        <span class="far fa-user"></span>
                        <input type="text" onChange={inputChange} name="first_name" value={user.first_name} id="firstName" placeholder="prénom"/>
                    </div>
                    {'first_name' in regResponse && <div className="reg_msg">{regResponse.first_name}</div>}
                    <div class="form-field d-flex align-items-center">
                        <span class="fas fa-key"></span>
                        <input type="text" onChange={inputChange} name="last_name" value={user.last_name} id="lastName" placeholder="nom"/>
                    </div>
                    {'last_name' in regResponse && <div className="reg_msg">{regResponse.last_name}</div>}
                    <div class="form-field d-flex align-items-center">
                        <span class="fas fa-key"></span>
                        <input type="text" onChange={inputChange} name="email" value={user.email} id="email" placeholder="addresse inemail"/>
                    </div>
                    {'email' in regResponse && <div className="reg_msg">{regResponse.email}</div>}
                    <div class="form-field d-flex align-items-center">
                        <span class="fas fa-key"></span>
                        <input type="password" onChange={inputChange} name="password" value={user.password} id="password" placeholder="mot de passe"/>
                    </div>
                    {'password' in regResponse && <div className="reg_msg">{regResponse.password}</div>}
                    <div class="form-field d-flex align-items-center">
                        <span class="fas fa-key"></span>
                        <input type="password" onChange={inputChange} name="confirm_password" value={user.confirm_password} id="pwd" placeholder="confirmer mot de passe"/>
                    </div>
                    {'confirm_password' in regResponse && <div className="reg_msg">{regResponse.confirm_password}</div>}
                    <div class="form-field d-flex align-items-center">
                        {/*<span class="fas fa-key"></span>*/}
                        <input type="tel" onChange={inputChange} name="phone" value={user.phone} id="phone" placeholder="téléphone"/>
                    </div>
                    {'phone' in regResponse && <div className="reg_msg">{regResponse.phone}</div>}
                    <button type="submit" class="btn mt-5"><h4>s'inscrire</h4></button>
                </form>

            </div>
        </div>
    );
}

export default Registration;