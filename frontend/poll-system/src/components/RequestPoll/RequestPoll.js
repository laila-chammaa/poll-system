import './RequestPoll.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png';
import {
  Button,
  Card,
  Form,
  Image,
} from 'react-bootstrap';
import { Link, useHistory } from 'react-router-dom';
import React, { useState } from 'react';
import { fetchPoll } from '../../api';

const RequestPoll = () => {
  let pollIDInput = React.createRef();
  let pinNumInput = React.createRef();

  const history = useHistory();
  const [displayIncorrectPollID, setIncorrectPollID] = useState(false);

  const checkPoll = async () => {
    let pollId = pollIDInput.current.value
    let pollResult = await fetchPoll(pollId)
    if(pollResult) {
      setIncorrectPollID(false);
      if(pinNumInput.current != null) {
        let pinNum = pinNumInput.current.value
        history.push(`/vote/${pollId}/${pinNum}`)
      }
      else {
        history.push(`/vote/${pollId}`)
      }
    }
    else{
      setIncorrectPollID(true);
    }
  };

  return (
    <div className="main-div">
      <Card className="card-title-div">
        <Card.Title className="card-title">
          Welcome Voter!
          <Link to="/">
            <Image src={homeicon} className="home-btn" />
          </Link>
        </Card.Title>
        <Card className="card-div-body">
          <Form id="request-form">
            <Form.Group>
              <Form.Text className="request-description">
                Please enter the ID of the poll you wish to access
              </Form.Text>
              <Form.Control
                type="text"
                className="request-box"
                aria-label="pollID"
                ref={pollIDInput}
              />
            </Form.Group>
            <Form.Group>
              <Form.Text className="request-description">
                pin# (optional)
              </Form.Text>
              <Form.Text id="pin-description">
                if you are returning to update your vote
              </Form.Text>
              <Form.Control
                type="text"
                className="request-box"
                aria-label="pinNum"
                ref={pinNumInput}
              />
            </Form.Group>
            <Button
              id="request-btn"
              onClick={async () => {
                if (pollIDInput.current != null) {
                  checkPoll()
                } else {
                  setIncorrectPollID(true);
                }
              }}
            >
              enter
            </Button>
          </Form>
          {displayIncorrectPollID ? (
            <Card.Text id="incorrectMessage">No such poll exists with that ID!</Card.Text>
          ) : (
            <Card.Text></Card.Text>
          )}
        </Card>
      </Card>
    </div>
  );
};

export default RequestPoll;
