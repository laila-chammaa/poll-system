import './App.css';
import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Roles from './components/Roles/Roles';
import AdminLogin from './components/AdminLogin/AdminLogin';
import VoteForm from './components/VoteForm/VoteForm';
import PollResults from './components/PollResults/PollResults';

function App() {
  return (
    <Router>
      <div className="App">
        <Switch>
          <Route exact path="/">
            <Roles />
          </Route>
          <Route path="/login">
            <AdminLogin />
          </Route>
          <Route path="/vote">
            <VoteForm />
          </Route>
          <Route path="/results">
            <PollResults />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
