import './Login.css';
import React, { useState, useContext } from 'react';
import {
    Card,
    Button,
} from 'react-bootstrap';

const Login = () => {
    const handleClick = (e) => {

    }
    
    return (
        <div className="login">
            <Card className="main-div">
                <Card.Title className="card-title">Welcome!</Card.Title>
                <Card className="div-body">
                    <Card.Text className="description">
                        Who do you think you are?
                    </Card.Text>
                    <div>
                        <Button className="login-btn" value="voter">voter</Button>
                        <Button className="login-btn" value="admin">admin</Button>
                    </div>
                </Card>
            </Card>
        </div>
    );
}

export default Login;


