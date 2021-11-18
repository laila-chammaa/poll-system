import './PollList.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png';
import { Button, Card, Image } from 'react-bootstrap';
import { Link, useHistory } from 'react-router-dom';
import { fetchPollsByCreator, deletePoll } from '../../api';
import React, { useState, useEffect } from 'react';
import PollView from './Poll/PollView';
import UnauthorizedView from '../UnautherizedView/UnauthorizedView';

const PollList = () => {
  const [polls, setPolls] = useState([]);
  const history = useHistory();
  let user = localStorage.getItem('email');

  useEffect(() => {
    const fetchPolls = async () => {
      let listOfPolls = await fetchPollsByCreator(user);

      if (listOfPolls !== undefined) {
        setPolls(listOfPolls);
      }
    };
    fetchPolls();
  }, [polls]);

  return (
    <div className="main-div poll-list">
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
              className="create-button"
              onClick={() => {
                history.push('/create');
              }}
            >
              create poll
            </Button>
            <div className="polls-list">
              {polls.map((p, i) => (
                <PollView
                  key={i}
                  className="poll"
                  poll={p}
                  deletePoll={() => {
                    deletePoll(p.id);
                    setPolls(polls.filter((poll) => poll.id !== p.id));
                  }}
                  onClick={() => {
                    history.push(`/details/${p.id}`);
                  }}
                />
              ))}
            </div>
          </Card>
        </Card>
      ) : (
        <UnauthorizedView />
      )}
    </div>
  );
};

export default PollList;
