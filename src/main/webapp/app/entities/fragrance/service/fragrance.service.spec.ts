import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FragranceType } from 'app/entities/enumerations/fragrance-type.model';
import { IFragrance, Fragrance } from '../fragrance.model';

import { FragranceService } from './fragrance.service';

describe('Service Tests', () => {
  describe('Fragrance Service', () => {
    let service: FragranceService;
    let httpMock: HttpTestingController;
    let elemDefault: IFragrance;
    let expectedResult: IFragrance | IFragrance[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FragranceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        inci: 'AAAAAAA',
        typ: FragranceType.VOLATILE_OIL,
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

      it('should create a Fragrance', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Fragrance()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Fragrance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            inci: 'BBBBBB',
            typ: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Fragrance', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Fragrance()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Fragrance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            inci: 'BBBBBB',
            typ: 'BBBBBB',
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

      it('should delete a Fragrance', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFragranceToCollectionIfMissing', () => {
        it('should add a Fragrance to an empty array', () => {
          const fragrance: IFragrance = { id: 123 };
          expectedResult = service.addFragranceToCollectionIfMissing([], fragrance);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fragrance);
        });

        it('should not add a Fragrance to an array that contains it', () => {
          const fragrance: IFragrance = { id: 123 };
          const fragranceCollection: IFragrance[] = [
            {
              ...fragrance,
            },
            { id: 456 },
          ];
          expectedResult = service.addFragranceToCollectionIfMissing(fragranceCollection, fragrance);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Fragrance to an array that doesn't contain it", () => {
          const fragrance: IFragrance = { id: 123 };
          const fragranceCollection: IFragrance[] = [{ id: 456 }];
          expectedResult = service.addFragranceToCollectionIfMissing(fragranceCollection, fragrance);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fragrance);
        });

        it('should add only unique Fragrance to an array', () => {
          const fragranceArray: IFragrance[] = [{ id: 123 }, { id: 456 }, { id: 28401 }];
          const fragranceCollection: IFragrance[] = [{ id: 123 }];
          expectedResult = service.addFragranceToCollectionIfMissing(fragranceCollection, ...fragranceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fragrance: IFragrance = { id: 123 };
          const fragrance2: IFragrance = { id: 456 };
          expectedResult = service.addFragranceToCollectionIfMissing([], fragrance, fragrance2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fragrance);
          expect(expectedResult).toContain(fragrance2);
        });

        it('should accept null and undefined values', () => {
          const fragrance: IFragrance = { id: 123 };
          expectedResult = service.addFragranceToCollectionIfMissing([], null, fragrance, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fragrance);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
