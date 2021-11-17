import './App.css';
import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import {
  Roles,
  AdminLogin,
  VoteForm,
  PollResults,
  PollForm,
  PollDetails,
  PollList
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
          <Route path="/vote/:pollid">
            <VoteForm />
          </Route>
          <Route path="/results/:pollid">
            <PollResults />
          </Route>
          <Route path="/create">
            <PollForm />
          </Route>
          <Route path="/details/:pollid">
            <PollDetails />
          </Route>
          <Route path="/userPolls">
            <PollList />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
