import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReportLog from './report-log';
import ReportLogDetail from './report-log-detail';
import ReportLogUpdate from './report-log-update';
import ReportLogDeleteDialog from './report-log-delete-dialog';

const ReportLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReportLog />} />
    <Route path="new" element={<ReportLogUpdate />} />
    <Route path=":id">
      <Route index element={<ReportLogDetail />} />
      <Route path="edit" element={<ReportLogUpdate />} />
      <Route path="delete" element={<ReportLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReportLogRoutes;
