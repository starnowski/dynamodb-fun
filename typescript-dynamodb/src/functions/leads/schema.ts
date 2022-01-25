export default {
  type: "object",
  properties: {
    name: { type: 'string' },
    type: { type: 'string' },
    url: { type: 'string' }
  },
  required: ['name', 'url']
} as const;
