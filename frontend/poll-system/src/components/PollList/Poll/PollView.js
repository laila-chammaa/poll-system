import './PollView.css';
import { Button } from 'react-bootstrap';
import React from 'react';

const PollView = ({ poll, onClick, deletePoll }) => {
  return (
    <div className="poll-main-div">
      <Button className="delete-button" onClick={deletePoll}>
        -
      </Button>
      <div className="poll-box" onClick={onClick}>
        <div>Poll ID: {poll.id}</div>
        <div>Name: {poll.name}</div>
        <div>Status: {poll.status}</div>
      </div>
    </div>
  );
};

export default PollView;
