import LeadsDao from "@functions/leads/dao.service";
import DatabaseService from "@services/database.services";
import "reflect-metadata"
import { container } from 'tsyringe';

container.registerSingleton("LeadsDao", LeadsDao);
container.registerSingleton("DatabaseService", DatabaseService);

export const diContainer = container;