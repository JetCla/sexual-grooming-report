import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReportComments from './report-comments';
import ReportCommentsDetail from './report-comments-detail';
import ReportCommentsUpdate from './report-comments-update';
import ReportCommentsDeleteDialog from './report-comments-delete-dialog';

const ReportCommentsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReportComments />} />
    <Route path="new" element={<ReportCommentsUpdate />} />
    <Route path=":id">
      <Route index element={<ReportCommentsDetail />} />
      <Route path="edit" element={<ReportCommentsUpdate />} />
      <Route path="delete" element={<ReportCommentsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReportCommentsRoutes;
