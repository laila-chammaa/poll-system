import axios from 'axios';

export const updatePollStatus = async (pollStatus) => {
  try {
    const { status } = await axios.put('/api/poll', null, {
      params: {
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
  let jsonPoll = JSON.stringify(poll)
  jsonPoll = jsonPoll.replace("[", "$")
  jsonPoll = jsonPoll.replace("]", "&")

  try {
    const { data: status } = await axios.post('/api/poll', null, {
      params: {
        poll: jsonPoll,
        status: poll.status
      }
    });
    if (status === 200) {
      return true;
    }
  } catch (error) {
    console.log(error);
  }
};

export const fetchPoll = async () => {
  try {
    const { data } = await axios.get('/api/poll');
    return data;
  } catch (error) {
    console.log(error);
  }
};

export const fetchResults = async () => {
  try {
    const { data } = await axios.get('/api/votes', {
      params: {
        format: 'text',
        download: 'false'
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

export const downloadResults = async () => {
  try {
    const { data } = await axios.get('/api/votes', {
      params: {
        format: 'text',
        download: 'true'
      }
    });
    return data;
  } catch (error) {
    console.log(error);
  }
};

export const vote = async (choice) => {
  try {
    const { status } = await axios.post('/api/votes', null, {
      params: {
        choice: choice.text
      }
    });
    if (status === 200) {
      return true;
    }
  } catch (error) {
    console.log(error);
  }
};
