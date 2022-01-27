import 'reflect-metadata';
import type { ValidatedEventAPIGatewayProxyEvent } from '@libs/apiGateway';
import { formatJSONResponse } from '@libs/apiGateway';
import { middyfy } from '@libs/lambda';
import { Lead } from '@models/response';
import Container from 'typedi';
import LeadsDao from './dao.service';
import schema from './schema';
import { diContainer } from 'src/DIRegister';


const leads: ValidatedEventAPIGatewayProxyEvent<typeof schema> = async (event) => {
  // console.log(event);
  // const leadsDao = Container.get(LeadsDao);
  const leadsDao:LeadsDao = diContainer.resolve("LeadsDao");
  let lead:Lead = {
    name: event.body.name,
    type: event.body.type,
    url: event.body.url
  };
  const result = await leadsDao.persist(lead);
  return formatJSONResponse(result);
}

export const main = middyfy(leads);
