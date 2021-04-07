import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISoapReceipt, getSoapReceiptIdentifier } from '../soap-receipt.model';

export type EntityResponseType = HttpResponse<ISoapReceipt>;
export type EntityArrayResponseType = HttpResponse<ISoapReceipt[]>;

@Injectable({ providedIn: 'root' })
export class SoapReceiptService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/soap-receipts');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(soapReceipt: ISoapReceipt): Observable<EntityResponseType> {
    return this.http.post<ISoapReceipt>(this.resourceUrl, soapReceipt, { observe: 'response' });
  }

  update(soapReceipt: ISoapReceipt): Observable<EntityResponseType> {
    return this.http.put<ISoapReceipt>(`${this.resourceUrl}/${getSoapReceiptIdentifier(soapReceipt) as number}`, soapReceipt, {
      observe: 'response',
    });
  }

  partialUpdate(soapReceipt: ISoapReceipt): Observable<EntityResponseType> {
    return this.http.patch<ISoapReceipt>(`${this.resourceUrl}/${getSoapReceiptIdentifier(soapReceipt) as number}`, soapReceipt, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISoapReceipt>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISoapReceipt[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSoapReceiptToCollectionIfMissing(
    soapReceiptCollection: ISoapReceipt[],
    ...soapReceiptsToCheck: (ISoapReceipt | null | undefined)[]
  ): ISoapReceipt[] {
    const soapReceipts: ISoapReceipt[] = soapReceiptsToCheck.filter(isPresent);
    if (soapReceipts.length > 0) {
      const soapReceiptCollectionIdentifiers = soapReceiptCollection.map(soapReceiptItem => getSoapReceiptIdentifier(soapReceiptItem)!);
      const soapReceiptsToAdd = soapReceipts.filter(soapReceiptItem => {
        const soapReceiptIdentifier = getSoapReceiptIdentifier(soapReceiptItem);
        if (soapReceiptIdentifier == null || soapReceiptCollectionIdentifiers.includes(soapReceiptIdentifier)) {
          return false;
        }
        soapReceiptCollectionIdentifiers.push(soapReceiptIdentifier);
        return true;
      });
      return [...soapReceiptsToAdd, ...soapReceiptCollection];
    }
    return soapReceiptCollection;
  }
}
