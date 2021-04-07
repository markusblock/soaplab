import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFragrance, Fragrance } from '../fragrance.model';
import { FragranceService } from '../service/fragrance.service';

@Injectable({ providedIn: 'root' })
export class FragranceRoutingResolveService implements Resolve<IFragrance> {
  constructor(protected service: FragranceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFragrance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fragrance: HttpResponse<Fragrance>) => {
          if (fragrance.body) {
            return of(fragrance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fragrance());
  }
}
