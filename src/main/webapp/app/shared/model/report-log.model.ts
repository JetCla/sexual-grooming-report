import dayjs from 'dayjs';
import { IReport } from 'app/shared/model/report.model';
import { IUser } from 'app/shared/model/user.model';

export interface IReportLog {
  id?: number;
  date?: string;
  change?: string | null;
  reportLogs?: IReport | null;
  updatedBy?: IUser | null;
}

export const defaultValue: Readonly<IReportLog> = {};
