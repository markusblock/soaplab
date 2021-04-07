import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SoapReceiptComponent } from '../list/soap-receipt.component';
import { SoapReceiptDetailComponent } from '../detail/soap-receipt-detail.component';
import { SoapReceiptUpdateComponent } from '../update/soap-receipt-update.component';
import { SoapReceiptRoutingResolveService } from './soap-receipt-routing-resolve.service';

const soapReceiptRoute: Routes = [
  {
    path: '',
    component: SoapReceiptComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SoapReceiptDetailComponent,
    resolve: {
      soapReceipt: SoapReceiptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SoapReceiptUpdateComponent,
    resolve: {
      soapReceipt: SoapReceiptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SoapReceiptUpdateComponent,
    resolve: {
      soapReceipt: SoapReceiptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(soapReceiptRoute)],
  exports: [RouterModule],
})
export class SoapReceiptRoutingModule {}
