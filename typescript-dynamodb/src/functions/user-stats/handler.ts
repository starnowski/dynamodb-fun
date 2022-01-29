import 'reflect-metadata';
import { formatJSONResponse } from '@libs/apiGateway';
import { IUserStatQueryRequest, UserStat } from '@models/response';
import { diContainer } from '@src/DIRegister';
import UserStatsDao from './dao.service';
import { APIGatewayProxyEvent, APIGatewayProxyResult, Context, Handler } from 'aws-lambda';
import middy from '@middy/core';


export const user_stats: Handler = async (event: APIGatewayProxyEvent, context: Context): Promise<APIGatewayProxyResult> => {
  const userStatsDao:UserStatsDao = diContainer.resolve("UserStatsDao");
  if (event.path == "/user_stats") {
    let result;
    try {
      console.log("event.body");
      console.log(event.body);
      let ob = JSON.parse(event.body);
      let userStat:UserStat = {
        user_id: ob.user_id,
        timestamp: (new Date(ob.timestamp).getTime()/1000),
        blood_pressure: ob.blood_pressure,
        weight: ob.weight
      };
      let result;
      result = await userStatsDao.persist(userStat);
    } catch (error) {
      result = error.stack;
    }
    return formatJSONResponse(result);
  }
  if (event.path == "/user_stats/search") {
    let result;
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
    statusCode: 404,
    body: "Invalid resource"
  };
}

export const main = middy(user_stats);
