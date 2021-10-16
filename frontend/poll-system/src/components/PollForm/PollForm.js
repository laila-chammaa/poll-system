import './PollForm.css';
import '../../Cards.css';
import { Button, Card, Col, Form, Row } from 'react-bootstrap';
import { useState } from 'react';
import Choice from './Choice/Choice';

const PollForm = ({
  currentPoll = {
    title: '',
    description: '',
    choices: [
      { text: '', description: '' },
      { text: '', description: '' }
    ]
  }
}) => {
  const [poll, setPoll] = useState(JSON.parse(JSON.stringify(currentPoll)));

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
    let choicesAreFilled = poll.choices.some(
      (c) => c.text === '' || c.text == null
    );
    return poll.title === '' || poll.title == null || choicesAreFilled;
  };

  return (
    <div className="poll-form main-div">
      <Card className="card-title-div">
        <Card.Title className="card-title">Start creating!</Card.Title>
        <Card className="card-div-body">
          <Form className="form-style">
            <Form.Group as={Row} className="group-style" controlId="poll-title">
              <Form.Label className="label-style" column="lg" lg={4}>
                Title
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="text"
                  value={poll.title}
                  className="text-box"
                  placeholder="Title"
                  onChange={(e) => {
                    poll.title = e.target.value;
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
                Description
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  as="textarea"
                  className="text-box"
                  id="text-area-style"
                  value={poll.description}
                  onChange={(e) => {
                    poll.description = e.target.value;
                    setPoll(JSON.parse(JSON.stringify(poll)));
                  }}
                  rows={5}
                  placeholder="Description"
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
            <Button type="submit" id="create-btn" disabled={disableCreate()}>
              create
            </Button>
          </Form>
        </Card>
      </Card>
    </div>
  );
};

export default PollForm;
