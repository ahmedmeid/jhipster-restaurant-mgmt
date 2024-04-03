import restaurant from 'app/entities/restaurant/restaurant.reducer';
import restaurantMenu from 'app/entities/restaurant-menu/restaurant-menu.reducer';
import menuCategory from 'app/entities/menu-category/menu-category.reducer';
import menuItem from 'app/entities/menu-item/menu-item.reducer';
import dineInOrder from 'app/entities/dine-in-order/dine-in-order.reducer';
import orderItem from 'app/entities/order-item/order-item.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  restaurant,
  restaurantMenu,
  menuCategory,
  menuItem,
  dineInOrder,
  orderItem,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
