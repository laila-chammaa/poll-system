import './Choice.css';
import '../../../Cards.css';
import { Button, Col, Form, Row } from 'react-bootstrap';

const Choice = ({ c, i, deleteChoice }) => {
  return (
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
  );
};

export default Choice;
