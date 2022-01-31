import "reflect-metadata"
import LeadsDao from "@functions/leads/dao.service";
import DatabaseService from "@services/database.services";
import { container } from 'tsyringe';
import UserStatsDao from "@functions/user-stats/dao.service";
import AWS from "aws-sdk";
import IConfig from "./services/config.interface";

container.registerSingleton("LeadsDao", LeadsDao);
container.registerSingleton("UserStatsDao", UserStatsDao);
container.registerSingleton("DatabaseService", DatabaseService);


const config: IConfig = { region: "eu-west-1" };
if (process.env.STAGE === process.env.DYNAMODB_LOCAL_STAGE) {
    config.region = process.env.DYNAMODB_LOCAL_REGION;
    config.accessKeyId = process.env.DYNAMODB_LOCAL_ACCESS_KEY_ID; 
    config.secretAccessKey = process.env.DYNAMODB_LOCAL_SECRET_ACCESS_KEY; 
    config.endpoint = process.env.DYNAMODB_LOCAL_ENDPOINT;
}
AWS.config.update(config);

const documentClient = new AWS.DynamoDB.DocumentClient();
container.registerInstance("DocumentClient", documentClient);

export const diContainer = container;