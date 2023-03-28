import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Victim from './victim';
import VictimDetail from './victim-detail';
import VictimUpdate from './victim-update';
import VictimDeleteDialog from './victim-delete-dialog';

const VictimRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Victim />} />
    <Route path="new" element={<VictimUpdate />} />
    <Route path=":id">
      <Route index element={<VictimDetail />} />
      <Route path="edit" element={<VictimUpdate />} />
      <Route path="delete" element={<VictimDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VictimRoutes;
