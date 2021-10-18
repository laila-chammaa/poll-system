import './PollResults.css';
import '../../Cards.css';
import { Card, Button } from 'react-bootstrap';
import Chart from 'react-google-charts';
import { useLocation, useHistory } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { downloadResults, fetchPoll, updatePollStatus } from '../../api';

const PollResults = () => {
  const [chartType, setChartType] = useState('LineChart');
  const supportedCharts = ['ColumnChart', 'LineChart', 'PieChart'];
  const [poll, setPoll] = useState(null);

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
              data={[
                ['City', 'Total Population'],
                ['New York City, NY', 8175000],
                ['Los Angeles, CA', 3792000],
                ['Chicago, IL', 2695000],
                ['Houston, TX', 2099000],
                ['Philadelphia, PA', 1526000]
              ]}
              options={{
                title: poll ? poll.name : 'No poll',
                chartArea: { width: '65%' },
                vAxis: {
                  title: 'Vote'
                },
                legend: 'none',
                colors: ['#1F6FFB', '#A6E5FF', '#DCC8FF', '#B083FF', '#8137FF']
              }}
            />
          </div>
          <Button
            className="btn-1 download"
            onClick={() => {
              downloadResults();
            }}
          >
            download
          </Button>
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
