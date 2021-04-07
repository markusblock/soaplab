import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SoapReceiptService } from '../service/soap-receipt.service';

import { SoapReceiptComponent } from './soap-receipt.component';

describe('Component Tests', () => {
  describe('SoapReceipt Management Component', () => {
    let comp: SoapReceiptComponent;
    let fixture: ComponentFixture<SoapReceiptComponent>;
    let service: SoapReceiptService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SoapReceiptComponent],
      })
        .overrideTemplate(SoapReceiptComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SoapReceiptComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SoapReceiptService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.soapReceipts?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
