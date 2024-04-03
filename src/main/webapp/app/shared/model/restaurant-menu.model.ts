import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IRestaurantMenu {
  id?: number;
  menuName?: string | null;
  menuDescription?: string | null;
  restaurant?: IRestaurant | null;
}

export const defaultValue: Readonly<IRestaurantMenu> = {};
