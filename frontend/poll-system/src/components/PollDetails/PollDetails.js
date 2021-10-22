import './PollDetails.css';
import '../../Cards.css';
import { Button, Card, Col, Form, Row, Spinner } from 'react-bootstrap';
import { Link, useHistory } from 'react-router-dom';
import { updatePollStatus, fetchPoll } from '../../api';
import { useState, useEffect } from 'react';
import Choice from '../PollForm/Choice/Choice';

const PollDetails = () => {
  const [poll, setPoll] = useState(null);
  const [status, setStatus] = useState(null);

  useEffect(() => {
    const fetchCurrentPoll = async () => {
      setPoll(await fetchPoll());
    };

    fetchCurrentPoll();
  }, [status]);

  const history = useHistory();
  if (poll && poll.status === 'RELEASED') {
    history.push({
      pathname: '/results',
      state: { admin: true }
    });
  }

  return (
    <div className="main-div">
      {poll != null ? (
        <Card className="card-title-div">
          <Card.Title className="card-title">
            Your {poll.status.toLowerCase()} poll
          </Card.Title>
          <Card className="card-div-body">
            <Form className="form-style">
              <Form.Group as={Row} className="group-style" controlId="poll-title">
                <Form.Label className="label-style" column="lg" lg={4} >
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
                      {c.description}
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
                    updatePollStatus('running');
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
                    updatePollStatus('released');
                    setStatus('released');
                  }}
                >
                  release
                </Button>
                <Button
                  className="btn-1 clear"
                  onClick={() => {
                    updatePollStatus('cleared');
                    setStatus('cleared');
                  }}
                >
                  clear
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
  );
};

export default PollDetails;
