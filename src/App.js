<<<<<<< HEAD
import React from "react";
import { BrowserRouter, Routes, Route} from "react-router-dom";
import HomeComponent from './home';
import Login from './login'
import Registration from './registration'
import MailConfirmation from './mailConfirmation'
import Contact from './contact';
import About from './about';
import Materials from './products/materials';
import ProductListing from './products/productlisting';
import PrivateRoute from "./privateRoute";

=======
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from './pages/home';
import Login from './pages/login';
import About from './pages/about';
import Details from "./pages/materiel/details";
import Register from './pages/register';
import Reservations from "./pages/materiel/Reservations";
import StudentReservations from "./pages/materiel/studentReservations";
>>>>>>> 61625863e07f0dd9d5698b61854d0edd8ad74b07

import Students from "./pages/students";
import Contact from './pages/contact';
import MaterielForm from "./pages/materiel/materielForm";
import Materials from "./pages/materiel/materiel.js"
import PrivateRoute from "./pages/privateRoute";

function App() {

  return (
    <BrowserRouter>
      <Routes>
<<<<<<< HEAD
        <Route path="/" element={<HomeComponent/>}></Route>
        <Route path="/login" element ={<Login/>}></Route>
        <Route path="/register" element={<Registration/>}></Route>
        <Route path="/confirm" element={<MailConfirmation/>}></Route>
        <Route path="/contact" element={<Contact/>}></Route>
        <Route path="/about" element={<About/>}></Route>
        <Route path="/materials" element={
          <PrivateRoute>
            <Materials/>
          </PrivateRoute>}
        />
        <Route path="/ProductListing" element={<ProductListing />}></Route>
      </Routes>
    </BrowserRouter>
     
=======
        <Route path="/" element={<Home />}></Route>
        <Route path="/login" element={<Login />}></Route>
        <Route path="/about" element={<About />}></Route>
        <Route path="/register" element={<Register />}></Route>

        <Route path="/contact" element={<Contact />}></Route>
        <Route path="/students" element={<Students/>}/>

        <Route path="/materiel" element={
          <PrivateRoute>
            <Materials />
          </PrivateRoute>
        }/>

        <Route path="/materiel/new" element={<MaterielForm/>}/>
        <Route path="/reservations" element={<Reservations/>}/>
        <Route path="/materiel/details" element={<Details/>}/>
        <Route path="/demandes" element={<StudentReservations/>}/>

      </Routes>
    </BrowserRouter>
>>>>>>> 61625863e07f0dd9d5698b61854d0edd8ad74b07
  );
}

export default App;
