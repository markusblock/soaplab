import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFragrance, getFragranceIdentifier } from '../fragrance.model';

export type EntityResponseType = HttpResponse<IFragrance>;
export type EntityArrayResponseType = HttpResponse<IFragrance[]>;

@Injectable({ providedIn: 'root' })
export class FragranceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/fragrances');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(fragrance: IFragrance): Observable<EntityResponseType> {
    return this.http.post<IFragrance>(this.resourceUrl, fragrance, { observe: 'response' });
  }

  update(fragrance: IFragrance): Observable<EntityResponseType> {
    return this.http.put<IFragrance>(`${this.resourceUrl}/${getFragranceIdentifier(fragrance) as number}`, fragrance, {
      observe: 'response',
    });
  }

  partialUpdate(fragrance: IFragrance): Observable<EntityResponseType> {
    return this.http.patch<IFragrance>(`${this.resourceUrl}/${getFragranceIdentifier(fragrance) as number}`, fragrance, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFragrance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFragrance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFragranceToCollectionIfMissing(
    fragranceCollection: IFragrance[],
    ...fragrancesToCheck: (IFragrance | null | undefined)[]
  ): IFragrance[] {
    const fragrances: IFragrance[] = fragrancesToCheck.filter(isPresent);
    if (fragrances.length > 0) {
      const fragranceCollectionIdentifiers = fragranceCollection.map(fragranceItem => getFragranceIdentifier(fragranceItem)!);
      const fragrancesToAdd = fragrances.filter(fragranceItem => {
        const fragranceIdentifier = getFragranceIdentifier(fragranceItem);
        if (fragranceIdentifier == null || fragranceCollectionIdentifiers.includes(fragranceIdentifier)) {
          return false;
        }
        fragranceCollectionIdentifiers.push(fragranceIdentifier);
        return true;
      });
      return [...fragrancesToAdd, ...fragranceCollection];
    }
    return fragranceCollection;
  }
}
