//used for making api requests
import axios from 'axios';

const url = 'http://localhost:8080/api"';

export const updatePollStatus = async (pollStatus) => {
  let changeableUrl = `${url}/poll`;

  try {
    const { data: status } = await axios.put(changeableUrl, {
      status: pollStatus
    });
    if (status === 200) {
      return true;
    }
  } catch (error) {
    console.log(error);
  }
};

//TODO: need to update when the backend can create
export const createPoll = async (poll) => {
  let changeableUrl = `${url}/poll`;
  try {
    const { data: status } = await axios.post(changeableUrl, {
      poll
    });
    if (status === 200) {
      return true;
    }
  } catch (error) {
    console.log(error);
  }
};

//TODO: need to update when the backend can send current poll
export const fetchPoll = async () => {
  let changeableUrl = `${url}/poll`;

  try {
    const { data: currentPoll } = await axios.get(changeableUrl);
    return { currentPoll };
  } catch (error) {
    console.log(error);
  }
};

//TODO: need to check that it works
export const fetchResults = async () => {
  let changeableUrl = `${url}/votes`;

  try {
    const { data } = await axios.get(changeableUrl);
    return data;
  } catch (error) {
    console.log(error);
  }
};

export const vote = async (choice) => {
  try {
    const { data: status } = await axios.post(`${url}/poll`, {
      choice
    });
    if (status === 200) {
      return true;
    }
  } catch (error) {
    console.log(error);
  }
};
