import axios from 'axios';

export const updatePollStatus = async (pollStatus, pollId) => {
  try {
    const { status } = await axios.put('/api/poll', null, {
      params: {
        pollId: pollId,
        status: pollStatus
      }
    });
    if (status === 200) {
      return true;
    }
  } catch (error) {
    console.log(error);
  }
};

export const createPoll = async (poll) => {
  let jsonPoll = JSON.stringify(poll);
  jsonPoll = jsonPoll.replaceAll('[', '$');
  jsonPoll = jsonPoll.replaceAll(']', '&');

  try {
    const data = await axios.post('/api/poll', null, {
      params: {
        poll: jsonPoll,
        status: poll.status
      }
    });
    if (data.status === 200) {
      return data.data;
    }
  } catch (error) {
    console.log(error);
  }
};

export const fetchPoll = async (pollId) => {
  try {
    const { data } = await axios.get('/api/poll', {
      params: { pollId }
    });
    return data;
  } catch (error) {
    console.log(error);
  }
};

export const fetchPollsByCreator = async (creator) => {
  try {
    const { data } = await axios.get('/api/poll', {
      params: { creator }
    });
    return data;
  } catch (error) {
    console.log(error);
  }
};

export const fetchResults = async (pollId) => {
  try {
    const { data } = await axios.get('/api/votes', {
      params: {
        format: 'text',
        download: 'false',
        pollId
      }
    });
    for (var i = 1; i < data.length; i++) {
      data[i][1] = parseInt(data[i][1]); //changing vote count to int
    }
    return data;
  } catch (error) {
    console.log(error);
  }
};

// pin is optional, TODO: update the frontend to pass the pin to this method
export const vote = async (pollId, choice, pin = null) => {
  try {
    const data = await axios.post('/api/votes', null, {
      params: {
        choice: choice.text,
        pollId,
        pin
      }
    });
    if (data.status === 200) {
      return data.data; //same pin or newly generated one
    }
  } catch (error) {
    console.log(error);
  }
};

export const login = async (email, password) => {
  try {
    const data = await axios.post('/api/login', null, {
      params: {
        email: email,
        password: password
      }
    });
    return data.data;
  } catch (error) {
    console.log(error);
  }
};
