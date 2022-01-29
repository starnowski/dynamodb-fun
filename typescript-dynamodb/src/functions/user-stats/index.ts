import { handlerPath } from '@libs/handlerResolver';

export default {
  handler: `${handlerPath(__dirname)}/handler.user_stats`,
  events: [
    {
      http: {
        method: 'post',
        path: 'user_stats',
        request: {
          schemas: {
            'application/json': {
              type: "object",
              properties: {
                user_id: { type: 'string' },
                timestamp: { type: 'string' },
                blood_pressure: { type: 'number' },
                weight: { type: 'number' }
              },
              required: ['user_id', 'timestamp']
            }
          }
        }
      }
    }
    ,
    {
      http: {
        method: 'post',
        path: 'user_stats/search',
        request: {
          schemas: {
            'application/json': {
              type: "object",
              properties: {
                user_id: { type: 'string' },
                after_timestamp: { type: 'string' },
                limit: { type: 'number' }
              },
              required: ['user_id']
            }
          }
        }
      }
    }
  ]
}
