import 'reflect-metadata';
import { formatJSONResponse, ValidatedEventAPIGatewayProxyEvent } from '@libs/apiGateway';
import { IUserStatQueryRequest, UserStat } from '@models/response';
import { diContainer } from '@src/DIRegister';
import UserStatsDao from './dao.service';
import { middyfy } from '@src/libs/lambda';


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
    } catch (error) {
      result = error.stack;
    }
    return formatJSONResponse(result);
  }
  if (event.path == "/user_stats/search") {
    let result:any;
    try {
      let ob = JSON.parse(event.body);
      let userStatQuery:IUserStatQueryRequest = {
        userId: ob.user_id,
        after_timestamp: (new Date(ob.timestamp).getTime()/1000),
        limit: ob.limit
      };
      result = await userStatsDao.query(userStatQuery);
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
