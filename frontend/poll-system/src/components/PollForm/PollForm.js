import './PollForm.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png';
import { Button, Card, Col, Form, Image, Row } from 'react-bootstrap';
import React, { useState, useEffect } from 'react';
import Choice from './Choice/Choice';
import { Link, useHistory, useLocation } from 'react-router-dom';
import { createPoll } from '../../api';
import UnauthorizedView from '../UnautherizedView/UnauthorizedView';

const PollForm = ({
  currentPoll = {
    name: '',
    question: '',
    choices: [
      { text: '', description: '' },
      { text: '', description: '' }
    ]
  }
}) => {
  const [poll, setPoll] = useState(JSON.parse(JSON.stringify(currentPoll)));
  const location = useLocation();

  useEffect(() => {
    const fetchPoll = async () => {
      if (location.state && location.state.currentPoll != null) {
        setPoll(location.state.currentPoll);
      }
    };

    fetchPoll();
  }, [location.state]);

  const addChoice = () => {
    poll.choices.push({ text: '', description: '' });
    setPoll(JSON.parse(JSON.stringify(poll)));
  };
  const deleteChoice = (i) => {
    poll.choices.splice(i, 1);
    setPoll(JSON.parse(JSON.stringify(poll)));
  };
  const updatePoll = () => {
    setPoll(JSON.parse(JSON.stringify(poll)));
  };

  const disableCreate = () => {
    let choicesAreEmpty = poll.choices.some(
      (c) => c.text === '' || c.text == null
    );

    let choiceNames = poll.choices.map((e) => e['text']);
    var set = new Set(choiceNames);
    let duplicateChoices = set.size !== choiceNames.length;

    return (
      !poll.name ||
      !poll.question ||
      choicesAreEmpty ||
      poll.choices.length < 2 ||
      duplicateChoices
    );
  };
  let user = localStorage.getItem('email');
  const history = useHistory();

  return (
    <div className="poll-form main-div">
      {user != null && user !== 'null' ? (
        <Card className="card-title-div">
          <Card.Title className="card-title">
            {poll.status ? 'Start editing!' : 'Start creating!'}
            <Link to="/">
              <Image src={homeicon} className="home-btn" />
            </Link>
          </Card.Title>
          <Card className="card-div-body">
            <Form className="form-style">
              <Form.Group
                as={Row}
                className="group-style"
                controlId="poll-title"
              >
                <Form.Label className="label-style" column="lg" lg={4}>
                  Title
                </Form.Label>
                <Col sm={8}>
                  <Form.Control
                    type="text"
                    value={poll.name}
                    className="text-box"
                    placeholder="Title"
                    onChange={(e) => {
                      poll.name = e.target.value;
                      setPoll(JSON.parse(JSON.stringify(poll)));
                    }}
                  />
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
                  <Form.Control
                    as="textarea"
                    className="text-box"
                    id="text-area-style"
                    value={poll.question}
                    onChange={(e) => {
                      poll.question = e.target.value;
                      setPoll(JSON.parse(JSON.stringify(poll)));
                    }}
                    rows={5}
                    placeholder="Question"
                  />
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
                  <Choice
                    key={i}
                    c={c}
                    i={i}
                    deleteChoice={deleteChoice}
                    updatePoll={updatePoll}
                  />
                ))}
              </fieldset>
              <Button id="add-style" onClick={() => addChoice()}>
                Add more +
              </Button>
              <Button
                type="submit"
                id="create-btn"
                disabled={disableCreate()}
                onClick={async (e) => {
                  e.preventDefault();
                  let pollId = await createPoll(poll);
                  if (pollId) {
                    history.push(`/details/${pollId}`);
                  }
                }}
              >
                {poll.status ? 'update' : 'create'}
              </Button>
            </Form>
          </Card>
        </Card>
      ) : (
        <UnauthorizedView />
      )}
    </div>
  );
};

export default PollForm;
