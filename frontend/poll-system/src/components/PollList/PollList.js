import './PollList.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png';
import { Button, Card, Col, Form, Image, Row, Spinner } from 'react-bootstrap';
import { Link, useHistory, useLocation } from 'react-router-dom';
import { updatePollStatus, fetchPoll, fetchPollsByCreator } from '../../api';
import React, { useState, useEffect } from 'react';
import PollView from './Poll/PollView';
import UnauthorizedView from '../UnautherizedView/UnauthorizedView';

const PollList = () => {
  const [polls, setPolls] = useState([]);

  let user = localStorage.getItem('email');

  useEffect(() => {
    const fetchPolls = async () => {
      setPolls(await fetchPollsByCreator(user));
    };
    fetchPolls();
  }, []);

  const history = useHistory();

  return (
    <div className="main-div">
      {user != null && user !== 'null' ? (
        <Card className="card-title-div">
          <Card.Title className="card-title">
            Welcome Admin!
            <Link to="/">
              <Image src={homeicon} className="home-btn" />
            </Link>
          </Card.Title>
          <Card className="card-div-body">
            <Button
              id=""
              onClick={async () => {
                history.push('/create');
              }}
            >
              create poll
            </Button>

            {polls.map((p, i) => (
              <PollView poll={p} />
            ))}
          </Card>
        </Card>
      ) : (
        <UnauthorizedView />
      )}
    </div>
  );
};

export default PollList;
