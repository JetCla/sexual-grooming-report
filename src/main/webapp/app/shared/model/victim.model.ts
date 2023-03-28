export interface IVictim {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  age?: number | null;
  city?: string | null;
  state?: string | null;
  country?: string | null;
  observations?: string | null;
}

export const defaultValue: Readonly<IVictim> = {};
