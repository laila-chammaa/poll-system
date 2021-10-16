import './PollForm.css';
import '../../Cards.css';
import { Button, Card, Col, Form, Row } from 'react-bootstrap';
import { useState } from 'react';

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

  return (
    <div className="main-div">
      <Card className="card-title-div">
        <Card.Title className="card-title">Start creating!</Card.Title>
        <Card className="card-div-body">
          <Form className="form-style">
            <Form.Group as={Row} className="group-style" controlId="poll-title">
              <Form.Label className="label-style" column="lg" lg={3}>
                Title
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="text"
                  value={poll.title}
                  key={poll.title}
                  className="text-box"
                  placeholder="Title"
                  onChange={(e) => (poll.title = e.target.value)}
                />
              </Col>
            </Form.Group>
            <Form.Group
              as={Row}
              className="group-style"
              controlId="poll-description"
            >
              <Form.Label className="label-style" column="lg" lg={3}>
                Description
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  as="textarea"
                  className="text-box"
                  id="text-area-style"
                  value={poll.description}
                  rows={5}
                  placeholder="Description"
                />
              </Col>
            </Form.Group>
            <fieldset>
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
                  <Col lg={1} className="choice">
                    <Form.Control
                      type="text"
                      value={c.text}
                      className="text-box name inner"
                      placeholder="Name"
                    />
                    <Form.Control
                      type="text"
                      value={c.description}
                      className="text-box description inner"
                      placeholder="Description"
                    />
                    <Button
                      className="delete-btn inner"
                      onClick={() => {
                        deleteChoice(i);
                      }}
                    >
                      -
                    </Button>
                  </Col>
                </Form.Group>
              ))}
            </fieldset>
            <Button id="add-style" onClick={() => addChoice()}>
              Add more +
            </Button>
            <Button type="submit" id="create-btn">
              create
            </Button>
          </Form>
        </Card>
      </Card>
    </div>
  );
};

export default PollForm;
