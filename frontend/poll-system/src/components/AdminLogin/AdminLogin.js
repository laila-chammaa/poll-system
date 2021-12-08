import './AdminLogin.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png';
import {
  Button,
  Card,
  Form,
  Image,
  Modal,
} from 'react-bootstrap';
import { Link, useHistory } from 'react-router-dom';
import React, { useState } from 'react';
import { login } from '../../api';

const AdminLogin = () => {
  let passwordInput = React.createRef();
  let emailInput = React.createRef();
  let nameInput = React.createRef();
  let newPasswordInput = React.createRef();

  const history = useHistory();
  const [displayIncorrect, setIncorrect] = useState(false);
  const [forgotPwShow, setForgotPwShow] = useState(false);
  const [displayForgotPwSuccess, setForgotPwSuccess] = useState(false);
  const [displayForgotPwFail, setForgotPwFail] = useState(false);
  const [changePwShow, setChangePwShow] = useState(false);
  const [displayChangePwSuccess, setChangePwSuccess] = useState(false);
  const [displayChangePwFail, setChangePwFail] = useState(false);
  const [signUpShow, setSignUpShow] = useState(false);
  const [displaySignUpSuccess, setSignUpSuccess] = useState(false);
  const [displaySignUpFail, setSignUpFail] = useState(false);

  const checkPassword = async () => {
    let email = emailInput.current.value;
    let password = passwordInput.current.value;
    let result = await login(email, password);
    if (result) {
      localStorage.setItem('email', email);
      setIncorrect(false);
      return true;
    }
    localStorage.setItem('email', null);
    return false;
  };

  const checkForgotPwRequest = async () => {
    let email = emailInput.current.value;
    let result = await login(email)
    return !!result;
  }

  const checkSignUpSuccess = async () => {
    let email = emailInput.current.value;
    let password = passwordInput.current.value;
    let name = nameInput.current.value;
    let result = await login(email, name, password)
    return !!result;
  }

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
                ref={passwordInput}
              />
              <Button
                id="enter-btn"
                onClick={async () => {
                  if (await checkPassword()) {
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
          <Button
              className="login-features"
              onClick={() => setForgotPwShow(true)}>
            forgot your password?
          </Button>
          <Modal
              animation={false}
              className="medium-modal"
              show={forgotPwShow}
              onHide={() => setForgotPwShow(false)}
              aria-labelledby="example-modal-sizes-title-sm"
              centered
          >
            <Modal.Header closeButton>
              <Modal.Title>forgot your password?</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {!displayForgotPwSuccess ? (
                <Form>
                  <p className="modal-description">
                    please enter your email to receive it
                  </p>
                  <Form.Group className="center-body">
                    <Form.Label className="login-label">email</Form.Label>
                    <Form.Control
                        type="email"
                        className="modal-input"
                        aria-label="email"
                        placeholder="email"
                        ref={emailInput}
                    />
                  </Form.Group>
                </Form>
              ) : (
                <p className="modal-description">
                  your forgot password request is successful. please check your email!
                </p>
              )}
              {!displayForgotPwFail ? (
                  <p className="unsuccessful-message">
                    your forgot password request was unsuccessful. please try again!
                  </p>
              ) : (
                  <p></p>
              )}
            </Modal.Body>
            <Modal.Footer>
              <Button
                  variant="primary"
                  onClick={async () => {
                    if (await checkForgotPwRequest()) {
                      setForgotPwSuccess(true)
                    } else {
                      setForgotPwFail(true)
                    }
                  }}>
                enter
              </Button>
            </Modal.Footer>
          </Modal>
          <Modal
              animation={false}
              className="medium-modal"
              show={changePwShow}
              onHide={() => setChangePwShow(false)}
              aria-labelledby="example-modal-sizes-title-sm"
              centered
          >
            <Modal.Header closeButton>
              <Modal.Title>change password?</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {!displayChangePwSuccess ? (
                <Form>
                  <p className="modal-description">
                    if you would like to change your current password, please enter a new password
                  </p>
                  <Form.Group className="center-body">
                    <Form.Label className="login-label">new password</Form.Label>
                    <Form.Control
                        type="text"
                        className="modal-input"
                        aria-label="newPassword"
                        placeholder="password"
                        ref={newPasswordInput}
                    />
                  </Form.Group>
                </Form>
              ) : (
                <p className="modal-description">
                  your password has been changed. please log in with your new password now!
                </p>
              )}
              {displayChangePwFail ? (
                <p className="unsuccessful-message">
                  your password change was unsuccessful. please try again!
                </p>
              ) : (
                <p></p>
              )}
            </Modal.Body>
            <Modal.Footer>
              <Button
                  variant="primary"
                  onClick={() => {
                    setChangePwShow(false)
                  }}>
                exit
              </Button>
              <Button
                  variant="primary"
                  onClick={() => {
                    // to implement for change password after Transform View
                  }}>
                enter
              </Button>
            </Modal.Footer>
          </Modal>
          <Button
              className="login-features"
              onClick={() => setSignUpShow(true)}>
            sign up
          </Button>
          <Modal
              animation={false}
              className="medium-modal"
              show={signUpShow}
              onHide={() => setSignUpShow(false)}
              aria-labelledby="example-modal-sizes-title-sm"
              centered
          >
            <Modal.Header closeButton>
              <Modal.Title>signing up!</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {!displaySignUpSuccess ? (
                <Form>
                  <Form.Group className="center-body">
                    <Form.Label>name</Form.Label>
                    <Form.Control
                        type="text"
                        className="modal-input"
                        aria-label="name"
                        placeholder="name"
                        ref={nameInput}
                    />
                  </Form.Group>
                  <Form.Group className="center-body">
                    <Form.Label>email</Form.Label>
                    <Form.Control
                        type="email"
                        className="modal-input"
                        aria-label="email"
                        placeholder="email"
                        ref={emailInput}
                    />
                  </Form.Group>
                  <Form.Group className="center-body">
                    <Form.Label>password</Form.Label>
                    <Form.Control
                        type="text"
                        className="modal-input"
                        aria-label="password"
                        placeholder="password"
                        ref={passwordInput}
                    />
                  </Form.Group>
                </Form>
              ) : (
                <p className="modal-description">
                  your sign up was successful. you can log in now!
                </p>
              )}
              {displaySignUpFail ? (
                <p className="unsuccessful-message">
                  your sign up was unsuccessful. please try again!
                </p>
              ) : (
                <p></p>
              )}
            </Modal.Body>
            <Modal.Footer>
              <Button
                  variant="primary"
                  onClick={async () => {
                    if (await checkSignUpSuccess()) {
                      setSignUpSuccess(true)
                    } else {
                      setSignUpFail(true)
                    }
                  }}>
                enter
              </Button>
            </Modal.Footer>
          </Modal>
        </Card>
      </Card>
    </div>
  );
};

export default AdminLogin;
