import dayjs from 'dayjs';
import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IDineInOrder {
  id?: number;
  orderTime?: dayjs.Dayjs | null;
  tableNumber?: number | null;
  restaurant?: IRestaurant | null;
}

export const defaultValue: Readonly<IDineInOrder> = {};
