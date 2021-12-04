import './PollDetails.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png';
import { Button, Card, Col, Form, Image, Row, Spinner } from 'react-bootstrap';
import { Link, useHistory, useParams } from 'react-router-dom';
import { updatePollStatus, fetchPoll } from '../../api';
import React, { useState, useEffect } from 'react';
import UnauthorizedView from '../UnautherizedView/UnauthorizedView';

const PollDetails = () => {
  const [poll, setPoll] = useState(null);
  const [status, setStatus] = useState(null);
  const { pollId } = useParams();
  let user = localStorage.getItem('email');

  useEffect(() => {
    const fetchCurrentPoll = async () => {
      setPoll(await fetchPoll(pollId));
    };
    if (notLoggedIn) {
      fetchCurrentPoll();
    }
  }, [status]);

  const history = useHistory();
  if (poll && poll.status === 'RELEASED') {
    history.push({
      pathname: `/results/${pollId}`,
      state: { admin: true }
    });
  }

  const notLoggedIn = () => {
    return user != null && user !== 'null';
  };

  return (
    <div className="main-div">
      {notLoggedIn ? (
        <div>
          {poll != null ? (
            <Card className="card-title-div">
              <Card.Title className="card-title">
                Your {poll.status.toLowerCase()} poll
                <Link to="/">
                  <Image src={homeicon} className="home-btn" />
                </Link>
              </Card.Title>
              <Card className="card-div-body">
                <Form className="form-style">
                  <Form.Group
                    as={Row}
                    className="group-style"
                    controlId="poll-id"
                  >
                    <Form.Label className="label-style" column="lg" lg={4}>
                      Poll ID
                    </Form.Label>
                    <Col sm={8}>
                      <Card.Text className="details-style">{poll.id}</Card.Text>
                    </Col>
                  </Form.Group>
                  <Form.Group
                    as={Row}
                    className="group-style"
                    controlId="poll-title"
                  >
                    <Form.Label className="label-style" column="lg" lg={4}>
                      Title
                    </Form.Label>
                    <Col sm={8}>
                      <Card.Text className="details-style">
                        {poll.name}
                      </Card.Text>
                    </Col>
                  </Form.Group>
                  <Form.Group
                    as={Row}
                    className="group-style"
                    controlId="poll-description"
                  >
                    <Form.Label className="label-style" column="lg" lg={4}>
                      Question
                    </Form.Label>
                    <Col sm={8}>
                      <Card.Text className="details-style">
                        {poll.question}
                      </Card.Text>
                    </Col>
                  </Form.Group>
                  <fieldset>
                    <Form.Label className="label-style" column="lg" lg={12}>
                      Choices
                    </Form.Label>
                    <Form.Label className="label-style" column="lg" lg={4}>
                      Name
                    </Form.Label>
                    <Form.Label className="label-style" column="lg" lg={8}>
                      Description
                    </Form.Label>
                    {poll.choices.map((c, i) => (
                      <Form.Group
                        as={Row}
                        className="group-style"
                        controlId="poll-choice1"
                        key={i}
                      >
                        <Col lg={4} className="choice">
                          <Card.Text className="choice-name">
                            {c.text}
                          </Card.Text>
                        </Col>
                        <Col lg={8} className="choice">
                          <Card.Text className="choice-description">
                            {c.description === '' ? 'N/A' : c.description}
                          </Card.Text>
                        </Col>
                      </Form.Group>
                    ))}
                  </fieldset>
                </Form>
                {poll.status === 'CREATED' ? (
                  <div>
                    <Link
                      className="btn-1 edit"
                      to={{
                        pathname: '/create',
                        state: { currentPoll: poll }
                      }}
                    >
                      edit
                    </Link>
                    <Button
                      className="btn-1 run"
                      onClick={() => {
                        updatePollStatus('running', pollId);
                        setStatus('running');
                      }}
                    >
                      run
                    </Button>
                  </div>
                ) : poll.status === 'RUNNING' ? (
                  <div>
                    <Link
                      className="btn-1 edit"
                      to={{
                        pathname: '/create',
                        state: { currentPoll: poll }
                      }}
                    >
                      edit
                    </Link>
                    <Button
                      className="btn-1 run"
                      onClick={() => {
                        updatePollStatus('released', pollId);
                        setStatus('released');
                      }}
                    >
                      release
                    </Button>
                    <Button
                      className="btn-1 clear"
                      onClick={() => {
                        updatePollStatus('cleared', pollId);
                        setStatus('cleared');
                      }}
                    >
                      clear
                    </Button>
                  </div>
                ) : poll.status === 'ARCHIVED' ? (
                  <div>
                    <Button
                      className="btn-1 run"
                      onClick={() => {
                        history.push({
                          pathname: `/results/${pollId}`,
                          state: { admin: false }
                        });
                      }}
                    >
                      view results
                    </Button>
                  </div>
                ) : null}
              </Card>
            </Card>
          ) : (
            <Card className="card-title-div">
              <Card.Title className="card-title">Loading your poll</Card.Title>
              <Spinner animation="border" role="status">
                <span className="visually-hidden">Loading...</span>
              </Spinner>
            </Card>
          )}
        </div>
      ) : (
        <UnauthorizedView />
      )}
    </div>
  );
};

export default PollDetails;
