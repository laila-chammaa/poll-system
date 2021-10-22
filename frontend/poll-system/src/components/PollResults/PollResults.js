import './PollResults.css';
import '../../Cards.css';
import { Card, Button } from 'react-bootstrap';
import Chart from 'react-google-charts';
import { useLocation, useHistory } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { fetchPoll, updatePollStatus, fetchResults } from '../../api';

const PollResults = () => {
  const [chartType, setChartType] = useState('PieChart');
  const supportedCharts = ['ColumnChart', 'PieChart'];
  const [poll, setPoll] = useState(null);
  const [results, setResults] = useState(null);
  const downloadURL =
    'http://localhost:8080/api/votes?format=text&download=true';

  useEffect(() => {
    const getResults = async () => {
      setResults(await fetchResults());
    };
    getResults();
  }, []);

  useEffect(() => {
    const fetchCurrentPoll = async () => {
      setPoll(await fetchPoll());
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
        <Card.Title className="card-title">Poll Results</Card.Title>
        <Card className="card-body">
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
          <a href={downloadURL} className="btn-1 download">
            download
          </a>
          {admin ? (
            <div>
              <Button
                className="btn-2"
                onClick={() => {
                  updatePollStatus('closed');
                  history.push('/create');
                }}
              >
                close
              </Button>
              <Button
                className="btn-1 clear"
                onClick={() => {
                  updatePollStatus('cleared');
                  history.push('/details');
                }}
              >
                clear
              </Button>
            </div>
          ) : null}
        </Card>
      </Card>
    </div>
  );
};

export default PollResults;
