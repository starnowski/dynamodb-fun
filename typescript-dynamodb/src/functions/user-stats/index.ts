import { handlerPath } from '@libs/handlerResolver';

export default {
  handler: `${handlerPath(__dirname)}/handler.main`,
  events: [
    {
      http: {
        method: 'post',
        path: 'user_stats',
        request: {
        }
      }
    }
    ,
    {
      http: {
        method: 'post',
        path: 'user_stats/search',
        request: {
        }
      }
    }
  ]
}
