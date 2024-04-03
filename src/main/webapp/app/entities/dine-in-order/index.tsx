import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DineInOrder from './dine-in-order';
import DineInOrderDetail from './dine-in-order-detail';
import DineInOrderUpdate from './dine-in-order-update';
import DineInOrderDeleteDialog from './dine-in-order-delete-dialog';

const DineInOrderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DineInOrder />} />
    <Route path="new" element={<DineInOrderUpdate />} />
    <Route path=":id">
      <Route index element={<DineInOrderDetail />} />
      <Route path="edit" element={<DineInOrderUpdate />} />
      <Route path="delete" element={<DineInOrderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DineInOrderRoutes;
