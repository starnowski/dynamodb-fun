import 'reflect-metadata';
import * as AWS from 'aws-sdk';
import ResponseModel from '@models/response';
import { inject, injectable } from 'tsyringe';

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

export interface IDatabaseService {

    create(params: PutItem): Promise<PutItemOutput>;
    query(params: QueryItem): Promise<QueryItemOutput>
}

@injectable()
export default class DatabaseService implements IDatabaseService{

    constructor(@inject("DocumentClient") private documentClient:AWS.DynamoDB.DocumentClient){}

    create(params: PutItem): Promise<PutItemOutput> {
        try {
            return this.documentClient.put(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `create-error: ${error}`);
        }
    }

    batchCreate = async(params: BatchWrite): Promise<BatchWriteOutPut> => {
        try {
            return await this.documentClient.batchWrite(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `batch-write-error: ${error}`);
        }
    }

    update = async (params: UpdateItem): Promise<UpdateItemOutPut> => {
        try {
            return await this.documentClient.update(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `update-error: ${error}`);
        }
    }

    query(params: QueryItem): Promise<QueryItemOutput> {
        try {
            return this.documentClient.query(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `query-error: ${error}`);
        }
    }

    get = async (params: GetItem): Promise<GetItemOutput> => {
        try {
            return await this.documentClient.get(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `get-error: ${error}`);
        }
    }

    delete = async (params: DeleteItem): Promise<DeleteItemOutput> => {
        try {
            return await this.documentClient.delete(params).promise();
        } catch (error) {
            throw new ResponseModel({}, 500, `delete-error: ${error}`);
        }
    }
}