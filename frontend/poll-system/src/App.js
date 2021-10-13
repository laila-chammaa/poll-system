import './App.css';
import React from "react";
import {
    BrowserRouter as Router,
    Switch,
    Route
} from "react-router-dom";
import Roles from './components/Roles/Roles';
import AdminLogin from "./components/AdminLogin/AdminLogin";

function App() {
  return (
    <Router>
        <div className="App">
            <Switch>
                <Route exact path="/">
                    <Roles/>
                </Route>
                <Route path="/AdminLogin">
                    <AdminLogin/>
                </Route>
            </Switch>
        </div>
    </Router>
  );
}

export default App;
