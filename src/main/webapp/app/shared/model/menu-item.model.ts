import { IMenuCategory } from 'app/shared/model/menu-category.model';

export interface IMenuItem {
  id?: number;
  itemName?: string | null;
  itemDescription?: string | null;
  ingredients?: string | null;
  price?: number | null;
  menuCategory?: IMenuCategory | null;
}

export const defaultValue: Readonly<IMenuItem> = {};
