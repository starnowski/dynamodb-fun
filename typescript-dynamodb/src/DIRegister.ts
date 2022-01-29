import "reflect-metadata"
import LeadsDao from "@functions/leads/dao.service";
import DatabaseService from "@services/database.services";
import { container } from 'tsyringe';
import UserStatsDao from "@functions/user-stats/dao.service";

container.registerSingleton("LeadsDao", LeadsDao);
container.registerSingleton("UserStatsDao", UserStatsDao);
container.registerSingleton("DatabaseService", DatabaseService);

export const diContainer = container;