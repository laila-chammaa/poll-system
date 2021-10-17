import './PollDetails.css';
import '../../Cards.css';
import { Button, Card } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const PollDetails = () => {
    return (
        <div className="main-div">
            <Card className="card-title-div">
                <Card.Title className="card-title">Your Open Poll</Card.Title>
                <Card className="card-div-body">
                    <Link id="edit-btn" to="/create">
                        edit
                    </Link>
                    <Button id="run-btn">
                        run
                    </Button>
                </Card>
            </Card>
        </div>
    );
};

export default PollDetails;