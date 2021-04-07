import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFat, getFatIdentifier } from '../fat.model';

export type EntityResponseType = HttpResponse<IFat>;
export type EntityArrayResponseType = HttpResponse<IFat[]>;

@Injectable({ providedIn: 'root' })
export class FatService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/fats');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(fat: IFat): Observable<EntityResponseType> {
    return this.http.post<IFat>(this.resourceUrl, fat, { observe: 'response' });
  }

  update(fat: IFat): Observable<EntityResponseType> {
    return this.http.put<IFat>(`${this.resourceUrl}/${getFatIdentifier(fat) as number}`, fat, { observe: 'response' });
  }

  partialUpdate(fat: IFat): Observable<EntityResponseType> {
    return this.http.patch<IFat>(`${this.resourceUrl}/${getFatIdentifier(fat) as number}`, fat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFatToCollectionIfMissing(fatCollection: IFat[], ...fatsToCheck: (IFat | null | undefined)[]): IFat[] {
    const fats: IFat[] = fatsToCheck.filter(isPresent);
    if (fats.length > 0) {
      const fatCollectionIdentifiers = fatCollection.map(fatItem => getFatIdentifier(fatItem)!);
      const fatsToAdd = fats.filter(fatItem => {
        const fatIdentifier = getFatIdentifier(fatItem);
        if (fatIdentifier == null || fatCollectionIdentifiers.includes(fatIdentifier)) {
          return false;
        }
        fatCollectionIdentifiers.push(fatIdentifier);
        return true;
      });
      return [...fatsToAdd, ...fatCollection];
    }
    return fatCollection;
  }
}
