import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAcid, getAcidIdentifier } from '../acid.model';

export type EntityResponseType = HttpResponse<IAcid>;
export type EntityArrayResponseType = HttpResponse<IAcid[]>;

@Injectable({ providedIn: 'root' })
export class AcidService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/acids');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(acid: IAcid): Observable<EntityResponseType> {
    return this.http.post<IAcid>(this.resourceUrl, acid, { observe: 'response' });
  }

  update(acid: IAcid): Observable<EntityResponseType> {
    return this.http.put<IAcid>(`${this.resourceUrl}/${getAcidIdentifier(acid) as number}`, acid, { observe: 'response' });
  }

  partialUpdate(acid: IAcid): Observable<EntityResponseType> {
    return this.http.patch<IAcid>(`${this.resourceUrl}/${getAcidIdentifier(acid) as number}`, acid, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAcid>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAcid[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAcidToCollectionIfMissing(acidCollection: IAcid[], ...acidsToCheck: (IAcid | null | undefined)[]): IAcid[] {
    const acids: IAcid[] = acidsToCheck.filter(isPresent);
    if (acids.length > 0) {
      const acidCollectionIdentifiers = acidCollection.map(acidItem => getAcidIdentifier(acidItem)!);
      const acidsToAdd = acids.filter(acidItem => {
        const acidIdentifier = getAcidIdentifier(acidItem);
        if (acidIdentifier == null || acidCollectionIdentifiers.includes(acidIdentifier)) {
          return false;
        }
        acidCollectionIdentifiers.push(acidIdentifier);
        return true;
      });
      return [...acidsToAdd, ...acidCollection];
    }
    return acidCollection;
  }
}
