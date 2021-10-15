import './Roles.css';
import '../../Cards.css'
import { Card } from 'react-bootstrap';
import { Link } from 'react-router-dom'

const Roles = () => {
    return (
        <div className="main-div">
            <Card className="card-title-div">
                <Card.Title className="card-title">Welcome!</Card.Title>
                <Card className="card-div-body">
                    <Card.Text className="description">
                        Who do you think you are?
                    </Card.Text>
                    <div>
                        <Link className="role-btn" to="/VoteForm">voter</Link>
                        <Link className="role-btn" to="/AdminLogin">admin</Link>
                    </div>
                </Card>
            </Card>
        </div>
    );
}

export default Roles;


