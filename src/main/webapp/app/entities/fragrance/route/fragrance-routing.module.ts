import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FragranceComponent } from '../list/fragrance.component';
import { FragranceDetailComponent } from '../detail/fragrance-detail.component';
import { FragranceUpdateComponent } from '../update/fragrance-update.component';
import { FragranceRoutingResolveService } from './fragrance-routing-resolve.service';

const fragranceRoute: Routes = [
  {
    path: '',
    component: FragranceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FragranceDetailComponent,
    resolve: {
      fragrance: FragranceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FragranceUpdateComponent,
    resolve: {
      fragrance: FragranceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FragranceUpdateComponent,
    resolve: {
      fragrance: FragranceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fragranceRoute)],
  exports: [RouterModule],
})
export class FragranceRoutingModule {}
