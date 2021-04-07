import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AcidComponent } from '../list/acid.component';
import { AcidDetailComponent } from '../detail/acid-detail.component';
import { AcidUpdateComponent } from '../update/acid-update.component';
import { AcidRoutingResolveService } from './acid-routing-resolve.service';

const acidRoute: Routes = [
  {
    path: '',
    component: AcidComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AcidDetailComponent,
    resolve: {
      acid: AcidRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AcidUpdateComponent,
    resolve: {
      acid: AcidRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AcidUpdateComponent,
    resolve: {
      acid: AcidRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(acidRoute)],
  exports: [RouterModule],
})
export class AcidRoutingModule {}
