import { IFat } from 'app/entities/fat/fat.model';

export interface ISoapReceipt {
  id?: number;
  name?: string | null;
  liquid?: number | null;
  superfat?: number | null;
  fats?: IFat[] | null;
}

export class SoapReceipt implements ISoapReceipt {
  constructor(
    public id?: number,
    public name?: string | null,
    public liquid?: number | null,
    public superfat?: number | null,
    public fats?: IFat[] | null
  ) {}
}

export function getSoapReceiptIdentifier(soapReceipt: ISoapReceipt): number | undefined {
  return soapReceipt.id;
}
