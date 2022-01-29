import 'reflect-metadata';
import { formatJSONResponse, ValidatedEventAPIGatewayProxyEvent } from '@libs/apiGateway';
import { IUserStatQueryRequest, UserStat } from '@models/response';
import { diContainer } from '@src/DIRegister';
import UserStatsDao from './dao.service';
import { middyfy } from '@src/libs/lambda';


function mapUserStatToDto(userStat:UserStat):any{
  return {
    user_id: userStat.user_id,
    timestamp: new Date(userStat.timestamp),
    weight: userStat.weight,
    blood_pressure: userStat.blood_pressure
  };
}

function mapUserStatsToDtos(userStats:UserStat[]):any{
  let results = [];
  userStats.forEach(userStat => {
    results.push(mapUserStatToDto(userStat));
  });
  return results;
}

const user_stats: ValidatedEventAPIGatewayProxyEvent<any> = async (event) => {
  const userStatsDao:UserStatsDao = diContainer.resolve("UserStatsDao");
  if (event.path == "/user_stats") {
    let result:any;
    try {
      console.log("event.body");
      console.log(event.body);
      let userStat:UserStat = {
        user_id: event.body.user_id,
        timestamp: (new Date(event.body.timestamp).getTime()/1000),
        blood_pressure: event.body.blood_pressure,
        weight: event.body.weight
      };
      result = await userStatsDao.persist(userStat);
      result = mapUserStatToDto(result);
    } catch (error) {
      result = error.stack;
    }
    return formatJSONResponse(result);
  }
  if (event.path == "/user_stats/search") {
    let result:any;
    try {
      let userStatQuery:IUserStatQueryRequest = {
        userId: event.body.user_id,
        after_timestamp: (new Date(event.body.timestamp).getTime()/1000),
        limit: event.body.limit
      };
      result = await userStatsDao.query(userStatQuery);
      result = mapUserStatsToDtos(result);
    } catch (error) {
      result = error.stack;
    }
    return formatJSONResponse(result);
  }
  return {
    statusCode: 400,
    body: "Invalid resource"
  };
}

export const main = middyfy(user_stats);
