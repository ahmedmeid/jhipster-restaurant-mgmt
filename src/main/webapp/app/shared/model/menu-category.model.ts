import { IRestaurantMenu } from 'app/shared/model/restaurant-menu.model';

export interface IMenuCategory {
  id?: number;
  categoryName?: string | null;
  categoryDescription?: string | null;
  menu?: IRestaurantMenu | null;
}

export const defaultValue: Readonly<IMenuCategory> = {};
