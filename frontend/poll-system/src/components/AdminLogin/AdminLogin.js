import './AdminLogin.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png'
import { Button, Card, FormControl, Image, InputGroup } from 'react-bootstrap';
import { Link, useHistory } from 'react-router-dom';
import React, { useState, useEffect } from 'react';
import config from '../../config.json';
import { fetchPoll } from '../../api';

const AdminLogin = () => {
  let passcodeInput = React.createRef();
  let passcode = config.user.password;

  const history = useHistory();
  const [displayIncorrect, setIncorrect] = useState(false);

  const checkPasscode = () => {
    if (passcodeInput.current.value === passcode) {
      setIncorrect(false);
      return true;
    }
    return false;
  };

  const [poll, setPoll] = useState(null);

  useEffect(() => {
    const fetchCurrentPoll = async () => {
      setPoll(await fetchPoll());
    };

    fetchCurrentPoll();
  }, []);

  return (
    <div className="main-div">
      <Card className="card-title-div">
        <Card.Title className="card-title">
          Welcome Admin!
          <Link to="/"><Image src={homeicon} className="home-btn"/></Link>
        </Card.Title>
        <Card className="card-div-body">
          <Card.Text id="login-description">
            If you are who you say you are, you would know the secret passcode.
          </Card.Text>
          <InputGroup id="input-group-style">
            <FormControl
              type="text"
              id="passcode-box"
              aria-label="passcode"
              placeholder="passcode"
              ref={passcodeInput}
            />
            <Button
              id="enter-btn"
              onClick={() => {
                if (checkPasscode()) {
                  if (poll == null) {
                    history.push('/create');
                  } else {
                    history.push('/details');
                  }
                } else {
                  setIncorrect(true);
                }
              }}
            >
              enter
            </Button>
          </InputGroup>
          {displayIncorrect ? (
            <Card.Text id="incorrectMessage">Incorrect Password!</Card.Text>
          ) : (
            <Card.Text></Card.Text>
          )}
        </Card>
      </Card>
    </div>
  );
};

export default AdminLogin;
