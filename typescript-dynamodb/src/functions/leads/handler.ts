import 'reflect-metadata';
import type { ValidatedEventAPIGatewayProxyEvent } from '@libs/apiGateway';
import { formatJSONResponse } from '@libs/apiGateway';
import { middyfy } from '@libs/lambda';
import { Lead } from '@models/response';
import LeadsDao from './dao.service';
import schema from './schema';
import { diContainer } from '@src/DIRegister';
import validator from '@middy/validator';
import { generatedLambdaRequestUUID } from '@src/libs/logger';


const leads: ValidatedEventAPIGatewayProxyEvent<typeof schema> = async (event) => {
  generatedLambdaRequestUUID();
  const leadsDao:LeadsDao = diContainer.resolve("LeadsDao");
  let lead:Lead = {
    name: event.body.name,
    type: event.body.type,
    url: event.body.url
  };
  let result;
  try {
    result = await leadsDao.persist(lead);
  } catch (error) {
    result = error.stack;
  }
  return formatJSONResponse(result);
}

const inputSchema = {
  type: 'object',
  properties: {
    body: {
      type: 'object',
      properties: {
        name: {
          type: 'string'
        }
        ,
        type: {
          type: 'string'
        }
        ,
        url: {
          type: 'string'
        }
      },
      // Insert here all required event properties
      required: ['name', 'type']
    }
  }
}

export const main = middyfy(leads).use(validator({inputSchema: inputSchema}));
