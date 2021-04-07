import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAcid, Acid } from '../acid.model';

import { AcidService } from './acid.service';

describe('Service Tests', () => {
  describe('Acid Service', () => {
    let service: AcidService;
    let httpMock: HttpTestingController;
    let elemDefault: IAcid;
    let expectedResult: IAcid | IAcid[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AcidService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        inci: 'AAAAAAA',
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

      it('should create a Acid', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Acid()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Acid', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            inci: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Acid', () => {
        const patchObject = Object.assign(
          {
            inci: 'BBBBBB',
          },
          new Acid()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Acid', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            inci: 'BBBBBB',
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

      it('should delete a Acid', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAcidToCollectionIfMissing', () => {
        it('should add a Acid to an empty array', () => {
          const acid: IAcid = { id: 123 };
          expectedResult = service.addAcidToCollectionIfMissing([], acid);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(acid);
        });

        it('should not add a Acid to an array that contains it', () => {
          const acid: IAcid = { id: 123 };
          const acidCollection: IAcid[] = [
            {
              ...acid,
            },
            { id: 456 },
          ];
          expectedResult = service.addAcidToCollectionIfMissing(acidCollection, acid);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Acid to an array that doesn't contain it", () => {
          const acid: IAcid = { id: 123 };
          const acidCollection: IAcid[] = [{ id: 456 }];
          expectedResult = service.addAcidToCollectionIfMissing(acidCollection, acid);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(acid);
        });

        it('should add only unique Acid to an array', () => {
          const acidArray: IAcid[] = [{ id: 123 }, { id: 456 }, { id: 43987 }];
          const acidCollection: IAcid[] = [{ id: 123 }];
          expectedResult = service.addAcidToCollectionIfMissing(acidCollection, ...acidArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const acid: IAcid = { id: 123 };
          const acid2: IAcid = { id: 456 };
          expectedResult = service.addAcidToCollectionIfMissing([], acid, acid2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(acid);
          expect(expectedResult).toContain(acid2);
        });

        it('should accept null and undefined values', () => {
          const acid: IAcid = { id: 123 };
          expectedResult = service.addAcidToCollectionIfMissing([], null, acid, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(acid);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
