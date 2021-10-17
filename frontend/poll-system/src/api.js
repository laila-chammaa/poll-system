//used for making api requests
import axios from 'axios';

export const updatePollStatus = async (pollStatus) => {
  try {
    const { data: status } = await axios.put('/api/poll', {
      status: pollStatus
    });
    if (status === 200) {
      return true;
    }
  } catch (error) {
    alert(error);
  }
};

//TODO: need to update when the backend can accept choices
export const createPoll = async (poll) => {
  try {
    const { data: status } = await axios.post('/api/poll', null, {
      params: {
        name: poll.title,
        question: poll.question
      }
    });
    if (status === 200) {
      return true;
    }
  } catch (error) {
    return error;
  }
};

//TODO: need to update when the backend can send current poll
export const fetchPoll = async () => {
  try {
    const { data } = await axios.get('/api/poll');
    console.log(data);
    return data;
  } catch (error) {
    console.log(error);
  }
};

//TODO: need to check that it works
export const fetchResults = async () => {
  try {
    const { data } = await axios.get('/api/votes');
    return data;
  } catch (error) {
    console.log(error);
  }
};

//TODO: need to check that it works
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
    const { status } = await axios.post('api/poll', {
      choice
    });
    if (status === 200) {
      return true;
    }
  } catch (error) {
    console.log(error);
  }
};
