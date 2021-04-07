import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoapReceiptDetailComponent } from './soap-receipt-detail.component';

describe('Component Tests', () => {
  describe('SoapReceipt Management Detail Component', () => {
    let comp: SoapReceiptDetailComponent;
    let fixture: ComponentFixture<SoapReceiptDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SoapReceiptDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ soapReceipt: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SoapReceiptDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SoapReceiptDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load soapReceipt on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.soapReceipt).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
