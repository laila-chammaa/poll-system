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
  const [generatedPin, setGeneratedPin] = useState(null);
  const { pollId } = useParams();
  let { inputtedPin } = useParams();

  useEffect(() => {
    const fetchCurrentPoll = async () => {
      setPoll(await fetchPoll(pollId));
    };

    fetchCurrentPoll();
  }, []);

  const history = useHistory();

  if (poll != null && poll.status === 'RELEASED') {
    history.push({
      pathname: `/results/${pollId}`,
      state: { admin: false }
    });
  }

  const pollIsRunning = () => {
    return poll != null && poll.status === 'RUNNING';
  };

  const pollIsClosed = () => {
    return poll != null && poll.status === 'ARCHIVED';
  };

  const isPinIncorrect = (generatedPin, inputtedPin) => {
    return generatedPin !== inputtedPin;
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
          <Card.Title className="card-description">
            {poll.question}
            <Card.Text className="poll-id">ID: {pollId}</Card.Text>
          </Card.Title>
          <Card className="card-div-body">
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
                    vote(pollId, chosenAnswer, inputtedPin).then(
                      (responseData) => {
                        setGeneratedPin(responseData);
                      }
                    );
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
                {isPinIncorrect(generatedPin, inputtedPin) ? (
                  <div className="no-results">
                    Pin not found. Your new generated pin # to revote for this
                    poll is: <br /> {generatedPin}
                  </div>
                ) : (
                  <div className="no-results">
                    Your pin # to revote for this poll is: <br /> {generatedPin}
                  </div>
                )}
                <Button
                  className="btn-1"
                  onClick={() => {
                    setVoted(false);
                    setChosenAnswer(null);
                    history.push(`/vote/${pollId}/${generatedPin}`);
                  }}
                >
                  revote
                </Button>
                <Link className="btn-1 change-poll" to="/requestPoll">
                  change poll
                </Link>
              </div>
            )}
          </Card>
        </Card>
      ) : !pollIsClosed() ? (
        <Card className="card-title-div">
          <Card.Title className="card-title">
            Poll is not open
            <Link to="/">
              <Image src={homeicon} className="home-btn" />
            </Link>
          </Card.Title>
          <Card className="card-div-body">
            <div className="no-results">
              The poll with the ID: {pollId} is not open yet. Please come back
              later!
            </div>
          </Card>
        </Card>
      ) : (
        <Card className="card-title-div">
          <Card.Title className="card-title">
            Poll is closed
            <Link to="/">
              <Image src={homeicon} className="home-btn" />
            </Link>
          </Card.Title>
          <Card className="card-div-body">
            <div className="no-results">
              The poll with the ID: {pollId} is closed. Please vote for another
              poll!
            </div>
          </Card>
        </Card>
      )}
    </div>
  );
};

export default VoteForm;
