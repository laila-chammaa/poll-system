import './PollForm.css';
import '../../Cards.css';
import { Button, Card, Col, Form, Row } from 'react-bootstrap';

const PollForm = () => {
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
              <Col sm={9}>
                <Form.Control
                  type="text"
                  className="text-box"
                  placeholder="Title"
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
              <Col sm={9}>
                <Form.Control
                  as="textarea"
                  className="text-box"
                  id="text-area-style"
                  rows={5}
                  placeholder="Description"
                />
              </Col>
            </Form.Group>
            <fieldset>
              <Form.Group
                as={Row}
                className="group-style"
                controlId="poll-choice1"
              >
                {' '}
                {/*TO-DO: Do we need controlId??*/}
                <Form.Label className="label-style" column="lg" lg={3}>
                  Choice 1
                </Form.Label>
                <Col sm={9}>
                  <Form.Control
                    type="text"
                    className="text-box"
                    placeholder="Choice"
                  />
                </Col>
              </Form.Group>
              <Form.Group
                as={Row}
                className="group-style"
                controlId="poll-choice2"
              >
                <Form.Label className="label-style" column="lg" lg={3}>
                  Choice 2
                </Form.Label>
                <Col sm={9}>
                  <Form.Control
                    type="text"
                    className="text-box"
                    placeholder="Choice"
                  />
                </Col>
              </Form.Group>
              <Form.Group
                as={Row}
                className="group-style"
                controlId="poll-choice3"
              >
                <Form.Label className="label-style" column="lg" lg={3}>
                  Choice 3
                </Form.Label>
                <Col sm={9}>
                  <Form.Control
                    type="text"
                    className="text-box"
                    placeholder="Choice"
                  />
                </Col>
              </Form.Group>
            </fieldset>
            <Button id="add-style">Add more +</Button>
            <Form.Group as={Row} className="group-style">
              <Col sm={12}>
                <Button type="submit" id="create-btn">
                  create
                </Button>
              </Col>
            </Form.Group>
          </Form>
        </Card>
      </Card>
    </div>
  );
};

export default PollForm;
