import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Restaurant from './restaurant';
import RestaurantMenu from './restaurant-menu';
import MenuCategory from './menu-category';
import MenuItem from './menu-item';
import DineInOrder from './dine-in-order';
import OrderItem from './order-item';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="restaurant/*" element={<Restaurant />} />
        <Route path="restaurant-menu/*" element={<RestaurantMenu />} />
        <Route path="menu-category/*" element={<MenuCategory />} />
        <Route path="menu-item/*" element={<MenuItem />} />
        <Route path="dine-in-order/*" element={<DineInOrder />} />
        <Route path="order-item/*" element={<OrderItem />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
