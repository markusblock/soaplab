export interface IAcid {
  id?: number;
  name?: string | null;
  inci?: string | null;
}

export class Acid implements IAcid {
  constructor(public id?: number, public name?: string | null, public inci?: string | null) {}
}

export function getAcidIdentifier(acid: IAcid): number | undefined {
  return acid.id;
}
