import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Report from './report';
import ReportLog from './report-log';
import ReportComments from './report-comments';
import Victim from './victim';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="report/*" element={<Report />} />
        <Route path="report-log/*" element={<ReportLog />} />
        <Route path="report-comments/*" element={<ReportComments />} />
        <Route path="victim/*" element={<Victim />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
