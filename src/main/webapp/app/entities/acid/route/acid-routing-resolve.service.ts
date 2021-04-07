import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAcid, Acid } from '../acid.model';
import { AcidService } from '../service/acid.service';

@Injectable({ providedIn: 'root' })
export class AcidRoutingResolveService implements Resolve<IAcid> {
  constructor(protected service: AcidService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAcid> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((acid: HttpResponse<Acid>) => {
          if (acid.body) {
            return of(acid.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Acid());
  }
}
