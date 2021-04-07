import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFat, Fat } from '../fat.model';
import { FatService } from '../service/fat.service';

@Injectable({ providedIn: 'root' })
export class FatRoutingResolveService implements Resolve<IFat> {
  constructor(protected service: FatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fat: HttpResponse<Fat>) => {
          if (fat.body) {
            return of(fat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fat());
  }
}
