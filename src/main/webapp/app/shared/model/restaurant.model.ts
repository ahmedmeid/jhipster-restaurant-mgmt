export interface IRestaurant {
  id?: number;
  restaurantName?: string | null;
  restaurantDescription?: string | null;
  noOfTables?: number | null;
}

export const defaultValue: Readonly<IRestaurant> = {};
