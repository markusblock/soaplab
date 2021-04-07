import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SoapReceiptComponent } from './list/soap-receipt.component';
import { SoapReceiptDetailComponent } from './detail/soap-receipt-detail.component';
import { SoapReceiptUpdateComponent } from './update/soap-receipt-update.component';
import { SoapReceiptDeleteDialogComponent } from './delete/soap-receipt-delete-dialog.component';
import { SoapReceiptRoutingModule } from './route/soap-receipt-routing.module';

@NgModule({
  imports: [SharedModule, SoapReceiptRoutingModule],
  declarations: [SoapReceiptComponent, SoapReceiptDetailComponent, SoapReceiptUpdateComponent, SoapReceiptDeleteDialogComponent],
  entryComponents: [SoapReceiptDeleteDialogComponent],
})
export class SoapReceiptModule {}
