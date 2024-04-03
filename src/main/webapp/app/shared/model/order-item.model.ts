import { IMenuItem } from 'app/shared/model/menu-item.model';
import { IDineInOrder } from 'app/shared/model/dine-in-order.model';

export interface IOrderItem {
  id?: number;
  quantity?: number | null;
  specialRequests?: string | null;
  menuItem?: IMenuItem | null;
  dineInOrder?: IDineInOrder | null;
}

export const defaultValue: Readonly<IOrderItem> = {};
