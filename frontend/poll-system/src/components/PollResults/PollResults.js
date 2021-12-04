import './PollResults.css';
import '../../Cards.css';
import homeicon from '../../homeicon.png';
import { Card, Button, Image, Modal } from 'react-bootstrap';
import Chart from 'react-google-charts';
import { useLocation, useHistory, Link, useParams } from 'react-router-dom';
import React, { useState, useEffect } from 'react';
import { fetchPoll, updatePollStatus, fetchResults } from '../../api';

const PollResults = () => {
  const [chartType, setChartType] = useState('PieChart');
  const supportedCharts = ['ColumnChart', 'PieChart'];
  const [poll, setPoll] = useState(null);
  const [results, setResults] = useState(null);
  const [downloadShow, setDownloadShow] = useState(false);
  const { pollId } = useParams();
  const baseDownloadURL = `http://localhost:8080/api/votes?pollId=${pollId}&download=true`;

  useEffect(() => {
    const getResults = async () => {
      setResults(await fetchResults(pollId));
    };
    getResults();
  }, []);

  useEffect(() => {
    const fetchCurrentPoll = async () => {
      setPoll(await fetchPoll(pollId));
    };
    fetchCurrentPoll();
  }, []);

  let admin = false;
  const location = useLocation();
  location.state && location.state.admin ? (admin = true) : (admin = false);

  const history = useHistory();

  return (
    <div className="main-div">
      <Card className="card-title-div">
        <Card.Title className="card-title">
          Poll Results
          <Link to="/">
            <Image src={homeicon} className="home-btn" />
          </Link>
        </Card.Title>
        <Card className="card-div-body">
          <div className="outer">
            {supportedCharts.map((c, i) => (
              <div key={i} className="inner">
                <Button
                  onClick={() => {
                    setChartType(c);
                  }}
                >
                  {c}
                </Button>
              </div>
            ))}
          </div>
          <div style={{ display: 'flex', maxWidth: 530 }}>
            <Chart
              width={510}
              height={400}
              chartType={chartType}
              loader={<div>Loading Chart</div>}
              data={results}
              options={{
                title: poll ? poll.name : 'No poll',
                chartArea: { width: '65%' },
                vAxis: {
                  title: 'Votes'
                },
                legend: 'none',
                colors: ['#1F6FFB', '#A6E5FF', '#DCC8FF', '#B083FF', '#8137FF']
              }}
            />
          </div>
          <Button
            onClick={() => setDownloadShow(true)}
            className="btn-1 download"
          >
            download
          </Button>
          <Modal
            animation={false}
            size="sm"
            show={downloadShow}
            onHide={() => setDownloadShow(false)}
            aria-labelledby="example-modal-sizes-title-sm"
          >
            <Modal.Header closeButton>
              <Modal.Title>Download Format</Modal.Title>
            </Modal.Header>
            <Modal.Body>Please choose the format for your file.</Modal.Body>
            <Modal.Footer>
              <a
                href={baseDownloadURL + '&format=text'}
                className="download-btn"
              >
                Text
              </a>
              <a
                href={baseDownloadURL + '&format=json'}
                className="download-btn"
              >
                Json
              </a>
              <a
                href={baseDownloadURL + '&format=xml'}
                className="download-btn"
              >
                Xml
              </a>
            </Modal.Footer>
          </Modal>
          {admin ? (
            <div>
              <Button
                className="btn-2"
                onClick={() => {
                  updatePollStatus('closed', pollId);
                  history.push('/userPolls');
                }}
              >
                close
              </Button>
              <Button
                className="btn-1 clear"
                onClick={() => {
                  updatePollStatus('cleared', pollId);
                  history.push(`/details/${pollId}`);
                }}
              >
                clear
              </Button>
              <Button
                className="btn-1 unrelease"
                onClick={() => {
                  updatePollStatus('unreleased', pollId);
                  history.push(`/details/${pollId}`);
                }}
              >
                unrelease
              </Button>
            </div>
          ) : null}
        </Card>
      </Card>
    </div>
  );
};

export default PollResults;
