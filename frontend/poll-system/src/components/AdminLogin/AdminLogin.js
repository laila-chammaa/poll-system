import './AdminLogin.css';
import '../../Cards.css';
import {Button, Card, FormControl, InputGroup} from 'react-bootstrap';
import {useHistory} from "react-router-dom";
import React, {useState} from 'react';

const AdminLogin = () => {

  let passcodeInput = React.createRef();
  let passcode = "yes"

  const history = useHistory();
  const [displayIncorrect, setIncorrect] = useState(false);

  const checkPasscode = () => {
    if(passcodeInput.current.value === passcode){
      return true;
      setIncorrect(false);
    }
    return false;
  }

  return (
    <div className="main-div">
      <Card className="card-title-div">
        <Card.Title className="card-title">Welcome Admin!</Card.Title>
        <Card className="card-div-body">
          <Card.Text id ="login-description">
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
                if(checkPasscode()){
                  history.push("/create");
                }
                else{
                  setIncorrect(true);
                }
              }}
            >enter</Button>
          </InputGroup>
          {displayIncorrect ? (
            <Card.Text id="incorrectMessage">
              Incorrect Password!
            </Card.Text>
          ) : (
            <Card.Text></Card.Text>
          )}
        </Card>
      </Card>
    </div>
  );
};

export default AdminLogin;
