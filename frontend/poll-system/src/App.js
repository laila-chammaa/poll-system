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
  let poll = {
    title: 'hi',
    description: 'lol',
    choices: [
      { text: 'l', description: '1' },
      { text: '', description: '3' },
      { text: '', description: '' }
    ]
  };
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
            <PollForm poll={poll} />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
