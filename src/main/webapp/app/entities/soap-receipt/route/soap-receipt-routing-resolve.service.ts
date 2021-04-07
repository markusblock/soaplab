import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISoapReceipt, SoapReceipt } from '../soap-receipt.model';
import { SoapReceiptService } from '../service/soap-receipt.service';

@Injectable({ providedIn: 'root' })
export class SoapReceiptRoutingResolveService implements Resolve<ISoapReceipt> {
  constructor(protected service: SoapReceiptService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISoapReceipt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((soapReceipt: HttpResponse<SoapReceipt>) => {
          if (soapReceipt.body) {
            return of(soapReceipt.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SoapReceipt());
  }
}
