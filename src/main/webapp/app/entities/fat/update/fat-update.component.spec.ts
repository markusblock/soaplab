jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FatService } from '../service/fat.service';
import { IFat, Fat } from '../fat.model';

import { FatUpdateComponent } from './fat-update.component';

describe('Component Tests', () => {
  describe('Fat Management Update Component', () => {
    let comp: FatUpdateComponent;
    let fixture: ComponentFixture<FatUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fatService: FatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FatUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FatUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FatUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fatService = TestBed.inject(FatService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fat: IFat = { id: 456 };

        activatedRoute.data = of({ fat });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fat));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fat = { id: 123 };
        spyOn(fatService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fat }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fatService.update).toHaveBeenCalledWith(fat);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fat = new Fat();
        spyOn(fatService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fat }));
        saveSubject.complete();

        // THEN
        expect(fatService.create).toHaveBeenCalledWith(fat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fat = { id: 123 };
        spyOn(fatService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fatService.update).toHaveBeenCalledWith(fat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
