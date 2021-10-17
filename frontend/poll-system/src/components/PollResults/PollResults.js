import './PollResults.css';
import '../../Cards.css';
import { Card, Button } from 'react-bootstrap';
import Chart from 'react-google-charts';
import { useState } from 'react';
import { downloadResults } from '../../api';

const PollResults = () => {
  const [chartType, setChartType] = useState('LineChart');
  const supportedCharts = ['ColumnChart', 'LineChart', 'PieChart'];
  let poll = {
    title: 'Favorite Movie',
    question: 'Let us know what we should watch before we die.',
    choices: [
      { text: 'Megamind', description: 'my giant blue head' },
      {
        text: 'Treasure Planet',
        description: 'best boi in the best haircut'
      },
      { text: 'The Prestige', description: 'my brain was on the floor' },
      { text: 'Tinkerbell', description: 'comfort movie' }
    ]
  };
  // poll = fetchPoll();

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
                title: poll ? poll.title : 'No poll',
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
            className="btn-1"
            onClick={() => {
              downloadResults();
            }}
          >
            download
          </Button>
        </Card>
      </Card>
    </div>
  );
};

export default PollResults;
