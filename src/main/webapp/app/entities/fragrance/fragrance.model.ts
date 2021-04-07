import { FragranceType } from 'app/entities/enumerations/fragrance-type.model';

export interface IFragrance {
  id?: number;
  name?: string | null;
  inci?: string | null;
  typ?: FragranceType | null;
}

export class Fragrance implements IFragrance {
  constructor(public id?: number, public name?: string | null, public inci?: string | null, public typ?: FragranceType | null) {}
}

export function getFragranceIdentifier(fragrance: IFragrance): number | undefined {
  return fragrance.id;
}
