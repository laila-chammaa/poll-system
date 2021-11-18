import './PollView.css';
import { Button, Card, Col, Form, Image, Row, Spinner } from 'react-bootstrap';
import { Link, useHistory, useLocation } from 'react-router-dom';
import React, { useState, useEffect } from 'react';

const PollView = ({ poll }) => {
  return <div className="main-div">{poll.name}</div>;
};

export default PollView;
