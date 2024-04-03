import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/restaurant">
        <Translate contentKey="global.menu.entities.restaurant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/restaurant-menu">
        <Translate contentKey="global.menu.entities.restaurantMenu" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/menu-category">
        <Translate contentKey="global.menu.entities.menuCategory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/menu-item">
        <Translate contentKey="global.menu.entities.menuItem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dine-in-order">
        <Translate contentKey="global.menu.entities.dineInOrder" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-item">
        <Translate contentKey="global.menu.entities.orderItem" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
