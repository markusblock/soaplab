import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISoapReceipt, SoapReceipt } from '../soap-receipt.model';

import { SoapReceiptService } from './soap-receipt.service';

describe('Service Tests', () => {
  describe('SoapReceipt Service', () => {
    let service: SoapReceiptService;
    let httpMock: HttpTestingController;
    let elemDefault: ISoapReceipt;
    let expectedResult: ISoapReceipt | ISoapReceipt[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SoapReceiptService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        liquid: 0,
        superfat: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SoapReceipt', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SoapReceipt()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SoapReceipt', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            liquid: 1,
            superfat: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SoapReceipt', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            liquid: 1,
          },
          new SoapReceipt()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SoapReceipt', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            liquid: 1,
            superfat: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SoapReceipt', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSoapReceiptToCollectionIfMissing', () => {
        it('should add a SoapReceipt to an empty array', () => {
          const soapReceipt: ISoapReceipt = { id: 123 };
          expectedResult = service.addSoapReceiptToCollectionIfMissing([], soapReceipt);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(soapReceipt);
        });

        it('should not add a SoapReceipt to an array that contains it', () => {
          const soapReceipt: ISoapReceipt = { id: 123 };
          const soapReceiptCollection: ISoapReceipt[] = [
            {
              ...soapReceipt,
            },
            { id: 456 },
          ];
          expectedResult = service.addSoapReceiptToCollectionIfMissing(soapReceiptCollection, soapReceipt);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SoapReceipt to an array that doesn't contain it", () => {
          const soapReceipt: ISoapReceipt = { id: 123 };
          const soapReceiptCollection: ISoapReceipt[] = [{ id: 456 }];
          expectedResult = service.addSoapReceiptToCollectionIfMissing(soapReceiptCollection, soapReceipt);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(soapReceipt);
        });

        it('should add only unique SoapReceipt to an array', () => {
          const soapReceiptArray: ISoapReceipt[] = [{ id: 123 }, { id: 456 }, { id: 50810 }];
          const soapReceiptCollection: ISoapReceipt[] = [{ id: 123 }];
          expectedResult = service.addSoapReceiptToCollectionIfMissing(soapReceiptCollection, ...soapReceiptArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const soapReceipt: ISoapReceipt = { id: 123 };
          const soapReceipt2: ISoapReceipt = { id: 456 };
          expectedResult = service.addSoapReceiptToCollectionIfMissing([], soapReceipt, soapReceipt2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(soapReceipt);
          expect(expectedResult).toContain(soapReceipt2);
        });

        it('should accept null and undefined values', () => {
          const soapReceipt: ISoapReceipt = { id: 123 };
          expectedResult = service.addSoapReceiptToCollectionIfMissing([], null, soapReceipt, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(soapReceipt);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
