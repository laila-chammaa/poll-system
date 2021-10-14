import './VoteForm.css';
import '../../Cards.css';
import { Link } from 'react-router-dom';
import { Card, Form, Button } from 'react-bootstrap';
import { useState } from 'react';

const VoteForm = () => {
  const [voted, setVoted] = useState(false);
  const [chosenAnswer, setChosenAnswer] = useState(null);
  //TODO: remove hardcoded
  const title = 'Favorite Movie';
  const description = 'Let us know what we should watch before we die.';
  const choices = [
    { text: 'Megamind', description: 'my giant blue head' },
    {
      text: 'Treasure Planet',
      description: 'best boi in the best haircut'
    },
    { text: 'The Prestige', description: 'my brain was on the floor' },
    { text: 'Tinkerbell', description: 'comfort movie' }
  ];
  return (
    <div className="main-div">
      <Card className="card-title-div">
        <Card.Title className="card-title">{title}</Card.Title>
        <Card.Title className="card-description">{description}</Card.Title>
        <Card className="card-body">
          {!voted ? (
            <div>
              <Form className="choice-list">
                {choices.map((c, i) => (
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
                Vote is counted. The results are not yet released.
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
              <Link className="btn-2" to="/results">
                see results
              </Link>
            </div>
          )}
        </Card>
      </Card>
    </div>
  );
};

export default VoteForm;
