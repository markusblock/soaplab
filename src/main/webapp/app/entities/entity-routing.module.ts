import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'soap-receipt',
        data: { pageTitle: 'soaplabApp.soapReceipt.home.title' },
        loadChildren: () => import('./soap-receipt/soap-receipt.module').then(m => m.SoapReceiptModule),
      },
      {
        path: 'fat',
        data: { pageTitle: 'soaplabApp.fat.home.title' },
        loadChildren: () => import('./fat/fat.module').then(m => m.FatModule),
      },
      {
        path: 'acid',
        data: { pageTitle: 'soaplabApp.acid.home.title' },
        loadChildren: () => import('./acid/acid.module').then(m => m.AcidModule),
      },
      {
        path: 'fragrance',
        data: { pageTitle: 'soaplabApp.fragrance.home.title' },
        loadChildren: () => import('./fragrance/fragrance.module').then(m => m.FragranceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
