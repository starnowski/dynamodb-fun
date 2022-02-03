import 'reflect-metadata';
import { IUserStatQueryRequest, UserStat } from "@models/response";
import { IDatabaseService, QueryItemOutput } from "@services/database.services";
import { inject, injectable } from "tsyringe";
import { logInfo } from '@src/libs/logger';

@injectable()
export default class UserStatsDao {

    constructor(
        @inject("DatabaseService")
    public databaseService: IDatabaseService) { }

    persist(userStat:UserStat):Promise<UserStat>  {
        logInfo(this.databaseService);
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

    mapItemToUserStat(map:any):UserStat{
        return {
            user_id: map.user_id ? map.user_id : null,
            timestamp: map.timestamp ? map.timestamp : null,
            weight: map.weight ? map.weight : null,
            blood_pressure: map.blood_pressure ? map.blood_pressure : null
        };
    }

    query(query:IUserStatQueryRequest):Promise<UserStat[]>  {
        logInfo(this.databaseService);
        let expressionAttributeNames = null;
        let expressionAttributeValues = {
            ":val1": query.userId
        };
        let keyConditionExpression:string = "user_id = :val1";
        if (query.after_timestamp) {
            expressionAttributeNames = {
                "#t_attribute": "timestamp"
            };
            expressionAttributeValues[":val2"] = query.after_timestamp;
            keyConditionExpression += " and #t_attribute >= :val2";
        }
        return this.databaseService.query({
            TableName: "user_stats",
            Limit: query.limit,
            KeyConditionExpression: keyConditionExpression,
            ExpressionAttributeNames: expressionAttributeNames,
            ExpressionAttributeValues: expressionAttributeValues
        }).then((value) => {
            return this.mapQueryItemOutputToUserStatArray(value);
        } );
    }
}