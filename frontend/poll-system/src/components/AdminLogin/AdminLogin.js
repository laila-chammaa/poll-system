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
import {Link, useHistory} from 'react-router-dom';
import React, {useEffect, useState} from 'react';
import { login } from '../../api';

const AdminLogin = () => {
  let loginEmail = React.createRef();
  let loginPassword = React.createRef();
  let signUpEmail = React.createRef();
  let signUpPassword = React.createRef();
  let signUpName = React.createRef();
  let forgotPwEmail = React.createRef();
  let forgotPwNewPw = React.createRef();
  let changePwEmail = React.createRef();
  let changeOldPw = React.createRef();
  let changeNewPw = React.createRef();

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
  const [newPwShow, setNewPwShow] = useState(false);
  const [displayNewPwSuccess, setNewPwSuccess] = useState(false);
  const [displayNewPwFail, setNewPwFail] = useState(false);
  const [validateShow, setValidateShow] = useState(false);
  const [displayValidateSuccess, setValidateSuccess] = useState(false);
  const [emailQuery, setEmailQuery] = useState('');
  const [tokenQuery, setTokenQuery] = useState('');

  let type;

  useEffect(() => {
    let urlParams;
    (window.onpopstate = function () {
      let match,
          pl = /\+/g, // Regex for replacing addition symbol with a space
          search = /([^&=]+)=?([^&]*)/g,
          decode = function (s) {
            return decodeURIComponent(s.replace(pl, ' '));
          },
          query = window.location.search.substring(1);

      urlParams = {};
      while ((match = search.exec(query)))
        urlParams[decode(match[1])] = decode(match[2]);
    })();

    type = urlParams['type'];
    setEmailQuery(urlParams['email']);
    setTokenQuery(urlParams['token']);

    if (type === 'signup') {
      let result = login(urlParams['email'], null, null, null, urlParams['token']);
      if (result) {
        setValidateShow(true);
        setValidateSuccess(true);
      }
      setValidateSuccess(false);
    } else if (type === 'forgot_pass') {
      setNewPwShow(true);
    }
  }, []);

  const checkPassword = async () => {
    let email = loginEmail.current.value;
    let password = loginPassword.current.value;

    let result = await login(email, password, null, null, null);
    if (result) {
      localStorage.setItem('email', email);
      setIncorrect(false);
      return true;
    }
    localStorage.setItem('email', null);
    return false;
  };

  const checkForgotPwRequest = async () => {
    let email = forgotPwEmail.current.value;

    return await login(email, null, null, null, null);
  }

  const checkChangePwRequest = async () => {
    let email = changePwEmail.current.value;
    let password = changeOldPw.current.value;
    let newPassword = changeNewPw.current.value;

    return await login(email, password, null, newPassword, null);
  }

  const checkSignUpSuccess = async () => {
    let email = signUpEmail.current.value;
    let password = signUpPassword.current.value;
    let name = signUpName.current.value;

    return await login(email, name, password, null, null);
  }

  const checkNewPwRequest = async () => {
    let newPassword = forgotPwNewPw.current.value;

    return await login(emailQuery, null, null, newPassword, tokenQuery);
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
                ref={loginEmail}
              />
            </Form.Group>
            <Form.Group className="password-group">
              <Form.Label className="login-label">Password</Form.Label>
              <Form.Control
                type="password"
                id="pw-box"
                aria-label="password"
                placeholder="password"
                ref={loginPassword}
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
              onHide={() => {
                setForgotPwShow(false);
                setForgotPwFail(false);
                setForgotPwSuccess(false);
              }}
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
                    please enter your email
                  </p>
                  <Form.Group className="center-body">
                    <Form.Label className="login-label">email</Form.Label>
                    <Form.Control
                        type="email"
                        className="modal-input"
                        aria-label="email"
                        placeholder="email"
                        ref={forgotPwEmail}
                    />
                  </Form.Group>
                </Form>
              ) : (
                <p className="modal-description">
                  your forgot password request is successful. please check your email!
                </p>
              )}
              {displayForgotPwFail ? (
                  <p className="unsuccessful-message">
                    could not find an account with this email. please try again!
                  </p>
              ) : (
                  <p></p>
              )}
            </Modal.Body>
            <Modal.Footer>
              {!displayForgotPwSuccess ? (
                  <Button
                      variant="primary"
                      onClick={async () => {
                        if (await checkForgotPwRequest()) {
                          setForgotPwSuccess(true)
                          setForgotPwFail(false)
                        } else {
                          setForgotPwFail(true)
                        }
                      }}>
                    enter
                  </Button>
              ) : (
                  <Button
                      variant="primary"
                      onClick={() => setForgotPwShow(false)}>
                    exit
                  </Button>
              )}
            </Modal.Footer>
          </Modal>
          <Modal
              animation={false}
              className="medium-modal"
              show={newPwShow}
              onHide={() => {
                setNewPwShow(false)
                setNewPwFail(false);
                setNewPwSuccess(false);
              }}
              aria-labelledby="example-modal-sizes-title-sm"
              centered
          >
            <Modal.Header closeButton>
              <Modal.Title>new password!</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {!displayNewPwSuccess ? (
                  <Form>
                    <p className="modal-description">
                      please enter your new password
                    </p>
                    <Form.Group className="center-body">
                      <Form.Label className="login-label">new password</Form.Label>
                      <Form.Control
                          type="password"
                          className="modal-input"
                          aria-label="newPassword"
                          placeholder="password"
                          ref={forgotPwNewPw}
                      />
                    </Form.Group>
                  </Form>
              ) : (
                  <p className="modal-description">
                    your password change is successful. please try logging in!
                  </p>
              )}
              {displayNewPwFail ? (
                  <p className="unsuccessful-message">
                    your password change was unsuccessful!
                  </p>
              ) : (
                  <p></p>
              )}
            </Modal.Body>
            <Modal.Footer>
              {!displayNewPwSuccess ? (
                  <Button
                      variant="primary"
                      onClick={async () => {
                        if (await checkNewPwRequest()) {
                          setNewPwSuccess(true)
                          setNewPwFail(false)
                        } else {
                          setNewPwFail(true)
                        }
                      }}>
                    enter
                  </Button>
              ) : (
                  <Button
                      variant="primary"
                      onClick={() => setNewPwShow(false)}>
                    exit
                  </Button>
              )}
            </Modal.Footer>
          </Modal>
          <Button
              className="login-features"
              onClick={() => setChangePwShow(true)}>
            change your password?
          </Button>
          <Modal
              animation={false}
              className="medium-modal"
              show={changePwShow}
              onHide={() => {
                setChangePwShow(false)
                setChangePwFail(false)
                setChangePwSuccess(false)
              }}
              aria-labelledby="example-modal-sizes-title-sm"
              centered
          >
            <Modal.Header closeButton>
              <Modal.Title>change your password?</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {!displayChangePwSuccess ? (
                <Form>
                  <p className="modal-description">
                    please fill in the fields to change your current password
                  </p>
                  <Form.Group className="center-body">
                    <Form.Label className="login-label">email</Form.Label>
                    <Form.Control
                        type="text"
                        className="modal-input"
                        aria-label="email"
                        placeholder="email"
                        ref={changePwEmail}
                    />
                  </Form.Group>
                  <Form.Group className="center-body">
                    <Form.Label className="login-label">current password</Form.Label>
                    <Form.Control
                        type="password"
                        className="modal-input"
                        aria-label="password"
                        placeholder="password"
                        ref={changeOldPw}
                    />
                  </Form.Group>
                  <Form.Group className="center-body">
                    <Form.Label className="login-label">new password</Form.Label>
                    <Form.Control
                        type="password"
                        className="modal-input"
                        aria-label="newPassword"
                        placeholder="password"
                        ref={changeNewPw}
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
                  your email or password is incorrect. please try again!
                </p>
              ) : (
                <p></p>
              )}
            </Modal.Body>
            <Modal.Footer>
              {!displayChangePwSuccess ? (
                  <Button
                      variant="primary"
                      onClick={async () => {
                        if (await checkChangePwRequest()) {
                          setChangePwSuccess(true)
                          setChangePwFail(false)
                        } else {
                          setChangePwFail(true)
                        }
                      }}>
                    enter
                  </Button>
              ) : (
                  <Button
                      variant="primary"
                      onClick={() => setChangePwShow(false)}>
                    exit
                  </Button>
              ) }
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
              onHide={() => {
                setSignUpShow(false)
                setSignUpFail(false)
                setSignUpSuccess(false)
              }}
              aria-labelledby="example-modal-sizes-title-sm"
              centered
          >
            <Modal.Header closeButton>
              <Modal.Title>signing up!</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {!displaySignUpSuccess ? (
                <Form>
                  <p className="modal-description">
                    please fill in the fields to sign up
                  </p>
                  <Form.Group className="center-body">
                    <Form.Label>name</Form.Label>
                    <Form.Control
                        type="text"
                        className="modal-input"
                        aria-label="name"
                        placeholder="name"
                        ref={signUpName}
                    />
                  </Form.Group>
                  <Form.Group className="center-body">
                    <Form.Label>email</Form.Label>
                    <Form.Control
                        type="email"
                        className="modal-input"
                        aria-label="email"
                        placeholder="email"
                        ref={signUpEmail}
                    />
                  </Form.Group>
                  <Form.Group className="center-body">
                    <Form.Label>password</Form.Label>
                    <Form.Control
                        type="password"
                        className="modal-input"
                        aria-label="password"
                        placeholder="password"
                        ref={signUpPassword}
                    />
                  </Form.Group>
                </Form>
              ) : (
                <p className="modal-description">
                  your sign up was successful. please check your email!
                </p>
              )}
              {displaySignUpFail ? (
                <p className="unsuccessful-message">
                  this email already has an account. please try logging in!
                </p>
              ) : (
                <p></p>
              )}
            </Modal.Body>
            <Modal.Footer>
              {!displaySignUpSuccess ? (
                  <Button
                      variant="primary"
                      onClick={async () => {
                        if (await checkSignUpSuccess()) {
                          setSignUpSuccess(true)
                          setSignUpFail(false)
                        } else {
                          setSignUpFail(true)
                        }
                      }}>
                    enter
                  </Button>
              ) : (
                  <Button
                      variant="primary"
                      onClick={() => setSignUpShow(false)}>
                    exit
                  </Button>
              ) }
            </Modal.Footer>
          </Modal>
          <Modal
              animation={false}
              className="medium-modal"
              show={validateShow}
              onHide={() => {
                setValidateShow(false)
              }}
              aria-labelledby="example-modal-sizes-title-sm"
              centered
          >
            <Modal.Header closeButton>
              <Modal.Title>validation successful!</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {!displayValidateSuccess ? (
                  <p className="modal-description">
                    validation successful! please try logging in!
                  </p>
              ) : (
                  <p className="unsuccessful-message">
                    validation unsuccessful!
                  </p>
              )}
            </Modal.Body>
            <Modal.Footer>
              <Button
                  variant="primary"
                  onClick={() => setValidateShow(false)}>
                exit
              </Button>
            </Modal.Footer>
          </Modal>
        </Card>
      </Card>
    </div>
  );
};

export default AdminLogin;
