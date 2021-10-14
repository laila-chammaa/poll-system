//used for making api requests
import axios from 'axios';

const url = 'http://localhost:8080/PollSystem_war/api"';

export const fetchCurrentPoll = async () => {
  let changeableUrl = `${url}/currentPoll`;

  try {
    const { data: currentPoll } = await axios.get(changeableUrl); //assuming what's being returned is an object
    return { currentPoll };
  } catch (error) {
    console.log(error);
  }
};

export const vote = async () => {
  try {
    const { data } = await axios.get(`${url}/daily`);
    const modifiedData = data.map((dailyData) => ({
      confirmed: dailyData.confirmed.total,
      deaths: dailyData.deaths.total,
      date: dailyData.reportDate,
    }));
    return modifiedData;
  } catch (error) {
    console.log(error);
  }
};

//getting the countries from daily api, to create the country picker
export const fetchResults = async () => {
  try {
    const {
      data: { countries },
    } = await axios.get(`${url}/countries`);

    return countries.map((country) => country.name);
  } catch (error) {
    console.log(error);
  }
};
