import '../../Cards.css';
import { Card } from 'react-bootstrap';
import React from 'react';

const UnauthorizedView = () => {
  return (
    <Card className="card-title-div unauthorized">
      <Card.Title className="card-title">Not logged in</Card.Title>
      <Card className="card-div-body"></Card>
    </Card>
  );
};

export default UnauthorizedView;
