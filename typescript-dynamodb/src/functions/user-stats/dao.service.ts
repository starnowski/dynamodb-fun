import 'reflect-metadata';
import { UserStat } from "@models/response";
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
}