import './AdminLogin.css';
import '../../Cards.css';
import { Card, FormControl, InputGroup } from 'react-bootstrap';

const AdminLogin = () => {
  return (
    <div className="main-div">
      <Card className="card-title-div">
        <Card.Title className="card-title">Welcome Admin!</Card.Title>
        <Card className="card-div-body">
          <Card.Text id="login-description">
            If you are who you say you are, you would know the secret passcode.
          </Card.Text>
          <InputGroup>
            <InputGroup.Text>With textarea</InputGroup.Text>
            <FormControl as="textarea" aria-label="With textarea" />
          </InputGroup>
        </Card>
      </Card>
    </div>
  );
};

export default AdminLogin;
