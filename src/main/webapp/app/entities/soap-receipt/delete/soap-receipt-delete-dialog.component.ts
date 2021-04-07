import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoapReceipt } from '../soap-receipt.model';
import { SoapReceiptService } from '../service/soap-receipt.service';

@Component({
  templateUrl: './soap-receipt-delete-dialog.component.html',
})
export class SoapReceiptDeleteDialogComponent {
  soapReceipt?: ISoapReceipt;

  constructor(protected soapReceiptService: SoapReceiptService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.soapReceiptService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
