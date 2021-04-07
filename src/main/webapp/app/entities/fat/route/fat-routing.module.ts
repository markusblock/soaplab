import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FatComponent } from '../list/fat.component';
import { FatDetailComponent } from '../detail/fat-detail.component';
import { FatUpdateComponent } from '../update/fat-update.component';
import { FatRoutingResolveService } from './fat-routing-resolve.service';

const fatRoute: Routes = [
  {
    path: '',
    component: FatComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FatDetailComponent,
    resolve: {
      fat: FatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FatUpdateComponent,
    resolve: {
      fat: FatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FatUpdateComponent,
    resolve: {
      fat: FatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fatRoute)],
  exports: [RouterModule],
})
export class FatRoutingModule {}
