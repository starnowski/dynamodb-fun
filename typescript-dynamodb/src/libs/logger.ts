import winston, { format, transports } from "winston";
import { v4 as uuidv4 } from 'uuid';

const logLevels = {

    fatal: 0,
  
    error: 1,
  
    warn: 2,
  
    info: 3,
  
    debug: 4,
  
    trace: 5,
  
  };

const logger = winston.createLogger({

    level: "info",
  
    levels: logLevels,
  
    transports: [new transports.Console({ level: "info" })],

    format: format.combine(format.timestamp(), format.json()),

    defaultMeta: {

        service: "dynamodb-lambda-fun",
    
    },
  
  });

let lambdaRequestUUID:string;

export function generatedLambdaRequestUUID() {
    lambdaRequestUUID = uuidv4();
}

export function  logInfo(message?: any): void {
    logger.child({ context: { lambdaRequrestId: lambdaRequestUUID } }).info(message);
}

export function  logError(message?: any): void {
    logger.child({ context: { lambdaRequrestId: lambdaRequestUUID } }).error(message);
}