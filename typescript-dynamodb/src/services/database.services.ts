import 'reflect-metadata';
import * as AWS from 'aws-sdk';
import ResponseModel from '@models/response';
import { injectable } from 'tsyringe';
import IConfig from './config.interface';

// Put
type PutItem = AWS.DynamoDB.DocumentClient.PutItemInput;
export type PutItemOutput = AWS.DynamoDB.DocumentClient.PutItemOutput;

// Batch write
type BatchWrite = AWS.DynamoDB.DocumentClient.BatchWriteItemInput;
type BatchWriteOutPut = AWS.DynamoDB.DocumentClient.BatchWriteItemOutput;

// Update
type UpdateItem = AWS.DynamoDB.DocumentClient.UpdateItemInput;
type UpdateItemOutPut = AWS.DynamoDB.DocumentClient.UpdateItemOutput;

// Query
type QueryItem = AWS.DynamoDB.DocumentClient.QueryInput;
export type QueryItemOutput = AWS.DynamoDB.DocumentClient.QueryOutput;

// Get
type GetItem = AWS.DynamoDB.DocumentClient.GetItemInput;
type GetItemOutput = AWS.DynamoDB.DocumentClient.GetItemOutput;

// Delete
type DeleteItem = AWS.DynamoDB.DocumentClient.DeleteItemInput;
type DeleteItemOutput = AWS.DynamoDB.DocumentClient.DeleteItemOutput;

const config: IConfig = { region: "eu-west-1" };
if (process.env.STAGE === process.env.DYNAMODB_LOCAL_STAGE) {
    config.region = process.env.DYNAMODB_LOCAL_REGION;
    config.accessKeyId = process.env.DYNAMODB_LOCAL_ACCESS_KEY_ID; 
    config.secretAccessKey = process.env.DYNAMODB_LOCAL_SECRET_ACCESS_KEY; 
    config.endpoint = process.env.DYNAMODB_LOCAL_ENDPOINT;
}
AWS.config.update(config);

const documentClient = new AWS.DynamoDB.DocumentClient();

@injectable()
export default class DatabaseService {

    create(params: PutItem): Promise<PutItemOutput> {
        try {
            return documentClient.put(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `create-error: ${error}`);
        }
    }

    batchCreate = async(params: BatchWrite): Promise<BatchWriteOutPut> => {
        try {
            return await documentClient.batchWrite(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `batch-write-error: ${error}`);
        }
    }

    update = async (params: UpdateItem): Promise<UpdateItemOutPut> => {
        try {
            return await documentClient.update(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `update-error: ${error}`);
        }
    }

    query(params: QueryItem): Promise<QueryItemOutput> {
        try {
            return documentClient.query(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `query-error: ${error}`);
        }
    }

    get = async (params: GetItem): Promise<GetItemOutput> => {
        try {
            return await documentClient.get(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `get-error: ${error}`);
        }
    }

    delete = async (params: DeleteItem): Promise<DeleteItemOutput> => {
        try {
            return await documentClient.delete(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `delete-error: ${error}`);
        }
    }
}