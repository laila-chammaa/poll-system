import './VoteForm.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png';
import { Link, useHistory, useParams } from 'react-router-dom';
import { Card, Form, Button, Image } from 'react-bootstrap';
import React, { useState, useEffect } from 'react';
import { fetchPoll, vote } from '../../api';

const VoteForm = () => {
  const [voted, setVoted] = useState(false);
  const [chosenAnswer, setChosenAnswer] = useState(null);
  const [poll, setPoll] = useState(null);
  const { pollId } = useParams();

  useEffect(() => {
    const fetchCurrentPoll = async () => {
      setPoll(await fetchPoll(pollId));
    };

    fetchCurrentPoll();
  }, []);

  const history = useHistory();

  if (poll != null && poll.status === 'RELEASED') {
    history.push({
      pathname: '/results',
      state: { admin: false }
    });
  }

  const pollIsRunning = () => {
    return poll != null && poll.status === 'RUNNING';
  };

  return (
    <div className="main-div">
      {pollIsRunning() ? (
        <Card className="card-title-div">
          <Card.Title className="card-title">
            {poll.name}
            <Link to="/">
              <Image src={homeicon} className="home-btn" />
            </Link>
          </Card.Title>
          <Card.Title className="card-description">{poll.question}</Card.Title>
          <Card className="card-body">
            {!voted ? (
              <div>
                <Form className="choice-list">
                  {poll.choices.map((c, i) => (
                    <div key={i} className="choice mb-2">
                      <Form.Check
                        name="choice"
                        type="radio"
                        id="default-radio"
                        label={c.text}
                        onClick={() => {
                          setChosenAnswer(c);
                        }}
                      />
                      <div className="choice-description">{c.description}</div>
                    </div>
                  ))}
                </Form>
                <Button
                  className="btn-1"
                  disabled={chosenAnswer == null}
                  onClick={() => {
                    setVoted(true);
                    vote(pollId, chosenAnswer);
                  }}
                >
                  vote
                </Button>
              </div>
            ) : (
              <div>
                <Form className="choice-list">
                  <div key="default-radio" className="choice mb-2">
                    <Form.Check
                      checked
                      disabled
                      type="radio"
                      id="default-radio"
                      label={chosenAnswer.text}
                    />
                    <div className="choice-description disabled">
                      {chosenAnswer.description}
                    </div>
                  </div>
                </Form>
                <div className="no-results">
                  Your vote was counted. The results are not yet released.
                </div>
                <Button
                  className="btn-1"
                  onClick={() => {
                    setVoted(false);
                    setChosenAnswer(null);
                  }}
                >
                  revote
                </Button>
              </div>
            )}
          </Card>
        </Card>
      ) : (
        <Card className="card-title-div">
          <Card.Title className="card-title">
            No Open Poll
            <Link to="/">
              <Image src={homeicon} className="home-btn" />
            </Link>
          </Card.Title>
          <Card className="card-body">
            <div className="no-results">
              There isn't a running poll yet. Come back later!
            </div>
          </Card>
        </Card>
      )}
    </div>
  );
};

export default VoteForm;
