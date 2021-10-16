import './App.css';
import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import {
  Roles,
  AdminLogin,
  VoteForm,
  PollResults,
  PollForm
} from './components';

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
          <Route path="/create">
            <PollForm />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
