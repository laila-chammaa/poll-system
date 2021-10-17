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

//TODO: need to update when the backend can accept choices
export const createPoll = async (poll) => {
  try {
    const { data: status } = await axios.post('/api/poll', null, {
      params: {
        name: poll.name,
        question: poll.question
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
    const { data } = await axios.get('/api/votes');
    return data;
  } catch (error) {
    console.log(error);
  }
};

export const downloadResults = async () => {
  try {
    const { data } = await axios.get('/api/votes');
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
