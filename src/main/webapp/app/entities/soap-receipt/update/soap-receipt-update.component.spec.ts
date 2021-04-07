jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SoapReceiptService } from '../service/soap-receipt.service';
import { ISoapReceipt, SoapReceipt } from '../soap-receipt.model';
import { IFat } from 'app/entities/fat/fat.model';
import { FatService } from 'app/entities/fat/service/fat.service';

import { SoapReceiptUpdateComponent } from './soap-receipt-update.component';

describe('Component Tests', () => {
  describe('SoapReceipt Management Update Component', () => {
    let comp: SoapReceiptUpdateComponent;
    let fixture: ComponentFixture<SoapReceiptUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let soapReceiptService: SoapReceiptService;
    let fatService: FatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SoapReceiptUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SoapReceiptUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SoapReceiptUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      soapReceiptService = TestBed.inject(SoapReceiptService);
      fatService = TestBed.inject(FatService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Fat query and add missing value', () => {
        const soapReceipt: ISoapReceipt = { id: 456 };
        const fats: IFat[] = [{ id: 40386 }];
        soapReceipt.fats = fats;

        const fatCollection: IFat[] = [{ id: 32612 }];
        spyOn(fatService, 'query').and.returnValue(of(new HttpResponse({ body: fatCollection })));
        const additionalFats = [...fats];
        const expectedCollection: IFat[] = [...additionalFats, ...fatCollection];
        spyOn(fatService, 'addFatToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ soapReceipt });
        comp.ngOnInit();

        expect(fatService.query).toHaveBeenCalled();
        expect(fatService.addFatToCollectionIfMissing).toHaveBeenCalledWith(fatCollection, ...additionalFats);
        expect(comp.fatsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const soapReceipt: ISoapReceipt = { id: 456 };
        const fats: IFat = { id: 59807 };
        soapReceipt.fats = [fats];

        activatedRoute.data = of({ soapReceipt });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(soapReceipt));
        expect(comp.fatsSharedCollection).toContain(fats);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const soapReceipt = { id: 123 };
        spyOn(soapReceiptService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ soapReceipt });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: soapReceipt }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(soapReceiptService.update).toHaveBeenCalledWith(soapReceipt);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const soapReceipt = new SoapReceipt();
        spyOn(soapReceiptService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ soapReceipt });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: soapReceipt }));
        saveSubject.complete();

        // THEN
        expect(soapReceiptService.create).toHaveBeenCalledWith(soapReceipt);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const soapReceipt = { id: 123 };
        spyOn(soapReceiptService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ soapReceipt });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(soapReceiptService.update).toHaveBeenCalledWith(soapReceipt);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFatById', () => {
        it('Should return tracked Fat primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFatById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedFat', () => {
        it('Should return option if no Fat is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedFat(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Fat for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedFat(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Fat is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedFat(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
