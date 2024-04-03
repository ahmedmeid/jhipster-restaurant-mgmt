import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MenuCategory from './menu-category';
import MenuCategoryDetail from './menu-category-detail';
import MenuCategoryUpdate from './menu-category-update';
import MenuCategoryDeleteDialog from './menu-category-delete-dialog';

const MenuCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MenuCategory />} />
    <Route path="new" element={<MenuCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<MenuCategoryDetail />} />
      <Route path="edit" element={<MenuCategoryUpdate />} />
      <Route path="delete" element={<MenuCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MenuCategoryRoutes;
