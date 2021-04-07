import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISoapReceipt } from '../soap-receipt.model';

@Component({
  selector: 'jhi-soap-receipt-detail',
  templateUrl: './soap-receipt-detail.component.html',
})
export class SoapReceiptDetailComponent implements OnInit {
  soapReceipt: ISoapReceipt | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soapReceipt }) => {
      this.soapReceipt = soapReceipt;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
