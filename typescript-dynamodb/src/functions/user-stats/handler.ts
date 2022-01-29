import 'reflect-metadata';
import { formatJSONResponse } from '@libs/apiGateway';
import { middyfy } from '@libs/lambda';
import { UserStat } from '@models/response';
import { diContainer } from '@src/DIRegister';
import UserStatsDao from './dao.service';
import { APIGatewayProxyEvent, APIGatewayProxyResult, Context, Handler } from 'aws-lambda';


export const user_stats: Handler = async (event: APIGatewayProxyEvent, context: Context): Promise<APIGatewayProxyResult> => {
  const userStatsDao:UserStatsDao = diContainer.resolve("UserStatsDao");
  if (event.path == "/user_stats") {
    let result;
    try {
      let ob = JSON.parse(event.body!);
      let userStat:UserStat = {
        user_id: ob.user_id,
        timestamp: ob.timestamp,
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
      let ob = JSON.parse(event.body!);
      let userStat:UserStat = {
        user_id: ob.user_id,
        timestamp: ob.timestamp,
        blood_pressure: ob.blood_pressure,
        weight: ob.weight
      };
      result = await userStatsDao.persist(userStat);
    } catch (error) {
      result = error.stack;
    }
    return formatJSONResponse(result);
  }
  return null;
}

export const main = middyfy(user_stats);
