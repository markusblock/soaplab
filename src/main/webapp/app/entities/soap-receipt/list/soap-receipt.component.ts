import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoapReceipt } from '../soap-receipt.model';
import { SoapReceiptService } from '../service/soap-receipt.service';
import { SoapReceiptDeleteDialogComponent } from '../delete/soap-receipt-delete-dialog.component';

@Component({
  selector: 'jhi-soap-receipt',
  templateUrl: './soap-receipt.component.html',
})
export class SoapReceiptComponent implements OnInit {
  soapReceipts?: ISoapReceipt[];
  isLoading = false;

  constructor(protected soapReceiptService: SoapReceiptService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.soapReceiptService.query().subscribe(
      (res: HttpResponse<ISoapReceipt[]>) => {
        this.isLoading = false;
        this.soapReceipts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISoapReceipt): number {
    return item.id!;
  }

  delete(soapReceipt: ISoapReceipt): void {
    const modalRef = this.modalService.open(SoapReceiptDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.soapReceipt = soapReceipt;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
