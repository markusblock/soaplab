import { ISoapReceipt } from 'app/entities/soap-receipt/soap-receipt.model';

export interface IFat {
  id?: number;
  name?: string | null;
  inci?: string | null;
  sapNaoh?: number | null;
  sapKoh?: number | null;
  lauric?: number | null;
  myristic?: number | null;
  palmitic?: number | null;
  stearic?: number | null;
  ricinoleic?: number | null;
  oleic?: number | null;
  linoleic?: number | null;
  linolenic?: number | null;
  iodine?: number | null;
  ins?: number | null;
  receipts?: ISoapReceipt[] | null;
}

export class Fat implements IFat {
  constructor(
    public id?: number,
    public name?: string | null,
    public inci?: string | null,
    public sapNaoh?: number | null,
    public sapKoh?: number | null,
    public lauric?: number | null,
    public myristic?: number | null,
    public palmitic?: number | null,
    public stearic?: number | null,
    public ricinoleic?: number | null,
    public oleic?: number | null,
    public linoleic?: number | null,
    public linolenic?: number | null,
    public iodine?: number | null,
    public ins?: number | null,
    public receipts?: ISoapReceipt[] | null
  ) {}
}

export function getFatIdentifier(fat: IFat): number | undefined {
  return fat.id;
}
