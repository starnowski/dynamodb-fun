import 'reflect-metadata';
import { IUserStatQueryRequest, UserStat } from "@models/response";
import DatabaseService, { QueryItemOutput } from "@services/database.services";
import { inject, injectable } from "tsyringe";
import { AttributeMap } from 'aws-sdk/clients/dynamodb';

@injectable()
export default class UserStatsDao {

    constructor(
        @inject("DatabaseService")
    public databaseService: DatabaseService) { }

    persist(userStat:UserStat):Promise<UserStat>  {
        console.log(this.databaseService);
        return this.databaseService.create({
            TableName: "user_stats",
            Item: {
                user_id: userStat.user_id,
                timestamp: userStat.timestamp,
                weight: userStat.weight,
                blood_pressure: userStat.blood_pressure
            }
        }).then((value) => {
            return {
                user_id: value.Attributes ? value.Attributes.user_id : userStat.user_id,
                timestamp: value.Attributes ? value.Attributes.timestamp : userStat.timestamp,
                weight: value.Attributes ? value.Attributes.weight : userStat.weight,
                blood_pressure: value.Attributes ? value.Attributes.blood_pressure : userStat.blood_pressure
            };
        } );
    }

    mapQueryItemOutputToUserStatArray(output:QueryItemOutput):UserStat[]
    {
        let results:UserStat[] = [];
        output.Items.forEach(item => {
            results.push(this.mapItemToUserStat(item));
        });
        return results;
    }

    mapItemToUserStat(map:AttributeMap):UserStat{
        return {
            user_id: map.user_id ? map.user_id.S : null,
            timestamp: map.timestamp ? parseInt(map.timestamp.N) : null,
            weight: map.weight ? parseInt(map.weight.N) : null,
            blood_pressure: map.blood_pressure ? parseInt(map.blood_pressure.N) : null,
        };
    }

    query(query:IUserStatQueryRequest):Promise<UserStat[]>  {
        console.log(this.databaseService);
        let expressionAttributeNames = {};
        let expressionAttributeValues = {
            val1: query.userId
        };
        return this.databaseService.query({
            TableName: "user_stats",
            Limit: query.limit,
            KeyConditionExpression: "user_id = :val1",
            ExpressionAttributeNames: expressionAttributeNames,
            ExpressionAttributeValues: expressionAttributeValues
        }).then((value) => {
            return this.mapQueryItemOutputToUserStatArray(value);
        } );
    }
}