jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AcidService } from '../service/acid.service';
import { IAcid, Acid } from '../acid.model';

import { AcidUpdateComponent } from './acid-update.component';

describe('Component Tests', () => {
  describe('Acid Management Update Component', () => {
    let comp: AcidUpdateComponent;
    let fixture: ComponentFixture<AcidUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let acidService: AcidService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AcidUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AcidUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AcidUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      acidService = TestBed.inject(AcidService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const acid: IAcid = { id: 456 };

        activatedRoute.data = of({ acid });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(acid));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const acid = { id: 123 };
        spyOn(acidService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ acid });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: acid }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(acidService.update).toHaveBeenCalledWith(acid);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const acid = new Acid();
        spyOn(acidService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ acid });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: acid }));
        saveSubject.complete();

        // THEN
        expect(acidService.create).toHaveBeenCalledWith(acid);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const acid = { id: 123 };
        spyOn(acidService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ acid });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(acidService.update).toHaveBeenCalledWith(acid);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
