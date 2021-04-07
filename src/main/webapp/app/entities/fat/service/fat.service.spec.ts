import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFat, Fat } from '../fat.model';

import { FatService } from './fat.service';

describe('Service Tests', () => {
  describe('Fat Service', () => {
    let service: FatService;
    let httpMock: HttpTestingController;
    let elemDefault: IFat;
    let expectedResult: IFat | IFat[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FatService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        inci: 'AAAAAAA',
        sapNaoh: 0,
        sapKoh: 0,
        lauric: 0,
        myristic: 0,
        palmitic: 0,
        stearic: 0,
        ricinoleic: 0,
        oleic: 0,
        linoleic: 0,
        linolenic: 0,
        iodine: 0,
        ins: 0,
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

      it('should create a Fat', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Fat()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Fat', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            inci: 'BBBBBB',
            sapNaoh: 1,
            sapKoh: 1,
            lauric: 1,
            myristic: 1,
            palmitic: 1,
            stearic: 1,
            ricinoleic: 1,
            oleic: 1,
            linoleic: 1,
            linolenic: 1,
            iodine: 1,
            ins: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Fat', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            ricinoleic: 1,
            linolenic: 1,
          },
          new Fat()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Fat', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            inci: 'BBBBBB',
            sapNaoh: 1,
            sapKoh: 1,
            lauric: 1,
            myristic: 1,
            palmitic: 1,
            stearic: 1,
            ricinoleic: 1,
            oleic: 1,
            linoleic: 1,
            linolenic: 1,
            iodine: 1,
            ins: 1,
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

      it('should delete a Fat', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFatToCollectionIfMissing', () => {
        it('should add a Fat to an empty array', () => {
          const fat: IFat = { id: 123 };
          expectedResult = service.addFatToCollectionIfMissing([], fat);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fat);
        });

        it('should not add a Fat to an array that contains it', () => {
          const fat: IFat = { id: 123 };
          const fatCollection: IFat[] = [
            {
              ...fat,
            },
            { id: 456 },
          ];
          expectedResult = service.addFatToCollectionIfMissing(fatCollection, fat);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Fat to an array that doesn't contain it", () => {
          const fat: IFat = { id: 123 };
          const fatCollection: IFat[] = [{ id: 456 }];
          expectedResult = service.addFatToCollectionIfMissing(fatCollection, fat);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fat);
        });

        it('should add only unique Fat to an array', () => {
          const fatArray: IFat[] = [{ id: 123 }, { id: 456 }, { id: 78896 }];
          const fatCollection: IFat[] = [{ id: 123 }];
          expectedResult = service.addFatToCollectionIfMissing(fatCollection, ...fatArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fat: IFat = { id: 123 };
          const fat2: IFat = { id: 456 };
          expectedResult = service.addFatToCollectionIfMissing([], fat, fat2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fat);
          expect(expectedResult).toContain(fat2);
        });

        it('should accept null and undefined values', () => {
          const fat: IFat = { id: 123 };
          expectedResult = service.addFatToCollectionIfMissing([], null, fat, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fat);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
