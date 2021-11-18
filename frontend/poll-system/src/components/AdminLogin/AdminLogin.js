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
import React, { useState } from 'react';
import { login } from '../../api';

const AdminLogin = () => {
  let passcodeInput = React.createRef();
  let emailInput = React.createRef();

  const history = useHistory();
  const [displayIncorrect, setIncorrect] = useState(false);

  const checkPasscode = async () => {
    let email = emailInput.current.value;
    let password = passcodeInput.current.value;
    let result = await login(email, password);
    if (result) {
      localStorage.setItem('email', email);
      setIncorrect(false);
      return true;
    }
    localStorage.setItem('email', null);
    return false;
  };

  const popover = (
    <Popover id="popover-basic">
      <Popover.Header as="h3">Not yet implemented</Popover.Header>
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
            <Form.Group className="password-group">
              <Form.Label className="login-label">Password</Form.Label>
              <Form.Control
                type="password"
                id="pw-box"
                aria-label="password"
                placeholder="password"
                ref={passcodeInput}
              />
              <Button
                id="enter-btn"
                onClick={async () => {
                  if (await checkPasscode()) {
                    history.push('/userPolls');
                  } else {
                    setIncorrect(true);
                  }
                }}
              >
                enter
              </Button>
            </Form.Group>
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
