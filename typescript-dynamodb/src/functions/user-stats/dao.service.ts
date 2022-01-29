import 'reflect-metadata';
import { IUserStatQueryRequest, UserStat } from "@models/response";
import DatabaseService from "@services/database.services";
import { inject, injectable } from "tsyringe";

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

    mapQueryItemOutputToUserStat(output:QueryItemOutput):UserStat[]
    {

        return {
            user_id: value.Attributes ? value.Attributes.user_id : userStat.user_id,
            timestamp: value.Attributes ? value.Attributes.timestamp : userStat.timestamp,
            weight: value.Attributes ? value.Attributes.weight : userStat.weight,
            blood_pressure: value.Attributes ? value.Attributes.blood_pressure : userStat.blood_pressure
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
            return this.mapQueryItemOutputToUserStat(value);
        } );
    }
}