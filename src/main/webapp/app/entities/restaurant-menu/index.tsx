import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RestaurantMenu from './restaurant-menu';
import RestaurantMenuDetail from './restaurant-menu-detail';
import RestaurantMenuUpdate from './restaurant-menu-update';
import RestaurantMenuDeleteDialog from './restaurant-menu-delete-dialog';

const RestaurantMenuRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RestaurantMenu />} />
    <Route path="new" element={<RestaurantMenuUpdate />} />
    <Route path=":id">
      <Route index element={<RestaurantMenuDetail />} />
      <Route path="edit" element={<RestaurantMenuUpdate />} />
      <Route path="delete" element={<RestaurantMenuDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RestaurantMenuRoutes;
