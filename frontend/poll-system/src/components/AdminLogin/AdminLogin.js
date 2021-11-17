import './AdminLogin.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png';
import {
  Button,
  Card,
  Form,
  Image,
  OverlayTrigger,
  Popover
} from 'react-bootstrap';
import { Link, useHistory } from 'react-router-dom';
import React, { useState, useEffect } from 'react';
import { fetchPoll, login } from '../../api';

const AdminLogin = () => {
  let passcodeInput = React.createRef();
  let emailInput = React.createRef();

  const history = useHistory();
  const [displayIncorrect, setIncorrect] = useState(false);

  const checkPasscode = () => {
    if (
      login(emailInput.current.value, passcodeInput.current.value) === 'true'
    ) {
      setIncorrect(false);
      return true;
    }
    return false;
  };

  const [poll, setPoll] = useState(null);

  useEffect(() => {
    const fetchCurrentPoll = async () => {
      setPoll(await fetchPoll('123'));
    };

    fetchCurrentPoll();
  }, []);

  const popover = (
    <Popover id="popover-basic">
      <Popover.Header as="h3">Popover right</Popover.Header>
      <Popover.Body>
        And here's some <strong>amazing</strong> content. It's very engaging.
        right?
      </Popover.Body>
    </Popover>
  );

  return (
    <div className="main-div">
      <Card className="card-title-div">
        <Card.Title className="card-title">
          Welcome Admin!
          <Link to="/">
            <Image src={homeicon} className="home-btn" />
          </Link>
        </Card.Title>
        <Card className="card-div-body">
          {/*<Card.Text id="login-description">*/}
          {/*  Please authenticate*/}
          {/*</Card.Text>*/}
          <Form id="form-group-style">
            <Form.Group>
              <Form.Label className="login-label">Email</Form.Label>
              <Form.Control
                type="email"
                id="email-box"
                aria-label="email"
                placeholder="email"
                ref={emailInput}
              />
            </Form.Group>
            <Form.Group>
              <Form.Label className="login-label">Password</Form.Label>
              <Form.Control
                type="password"
                id="pw-box"
                aria-label="password"
                placeholder="password"
                ref={passcodeInput}
              />
            </Form.Group>
            <Button
              id="enter-btn"
              onClick={() => {
                if (checkPasscode()) {
                  console.log('password correct');
                  // if (poll == null) {
                  //   history.push('/create');
                  // } else {
                  //   history.push('/details');
                  // }
                } else {
                  setIncorrect(true);
                }
              }}
            >
              enter
            </Button>
          </Form>
          {displayIncorrect ? (
            <Card.Text id="incorrectMessage">Incorrect Password!</Card.Text>
          ) : (
            <Card.Text></Card.Text>
          )}
          <OverlayTrigger trigger="focus" placement="bottom" overlay={popover}>
            <Card.Text>
              <Button id="sign-up">sign up</Button>
            </Card.Text>
          </OverlayTrigger>
        </Card>
      </Card>
    </div>
  );
};

export default AdminLogin;
