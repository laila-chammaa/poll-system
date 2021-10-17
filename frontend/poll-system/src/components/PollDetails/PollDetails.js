import './PollDetails.css';
import '../../Cards.css';
import { Button, Card, Spinner } from 'react-bootstrap';
import { Link, useHistory } from 'react-router-dom';
import { updatePollStatus, fetchPoll } from '../../api';
import { useState, useEffect } from 'react';

const PollDetails = () => {
  const [poll, setPoll] = useState(null);
  const [status, setStatus] = useState(null);

  useEffect(() => {
    const fetchCurrentPoll = async () => {
      setPoll(await fetchPoll());
    };

    fetchCurrentPoll();
  }, [status]);

  const history = useHistory();
  if (poll && poll.status === 'RELEASED') {
    history.push('/results');
  }

  return (
    <div className="main-div">
      {poll != null ? (
        <Card className="card-title-div">
          <Card.Title className="card-title">
            Your {poll.status.toLowerCase()} poll
          </Card.Title>
          <Card className="card-div-body">
            {poll.status === 'CREATED' ? (
              <div>
                <Link id="edit-btn" to="/create" props={{ currentPoll: poll }}>
                  edit
                </Link>
                <Button
                  id="run-btn"
                  onClick={() => {
                    updatePollStatus('running');
                    setStatus('running');
                  }}
                >
                  run
                </Button>
              </div>
            ) : poll.status === 'RUNNING' ? (
              <div>
                <Link
                  id="edit-btn"
                  to={{
                    pathname: '/create',
                    state: { currentPoll: poll }
                  }}
                >
                  edit
                </Link>
                <Button
                  id="run-btn"
                  onClick={() => {
                    updatePollStatus('released');
                    setStatus('released');
                  }}
                >
                  release
                </Button>
              </div>
            ) : (
              <h1></h1>
            )}
          </Card>
        </Card>
      ) : (
        <Card className="card-title-div">
          <Card.Title className="card-title">Loading your poll</Card.Title>
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
        </Card>
      )}
    </div>
  );
};

export default PollDetails;
