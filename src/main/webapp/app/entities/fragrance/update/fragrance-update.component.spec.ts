jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FragranceService } from '../service/fragrance.service';
import { IFragrance, Fragrance } from '../fragrance.model';

import { FragranceUpdateComponent } from './fragrance-update.component';

describe('Component Tests', () => {
  describe('Fragrance Management Update Component', () => {
    let comp: FragranceUpdateComponent;
    let fixture: ComponentFixture<FragranceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fragranceService: FragranceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FragranceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FragranceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FragranceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fragranceService = TestBed.inject(FragranceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fragrance: IFragrance = { id: 456 };

        activatedRoute.data = of({ fragrance });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fragrance));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fragrance = { id: 123 };
        spyOn(fragranceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fragrance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fragrance }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fragranceService.update).toHaveBeenCalledWith(fragrance);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fragrance = new Fragrance();
        spyOn(fragranceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fragrance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fragrance }));
        saveSubject.complete();

        // THEN
        expect(fragranceService.create).toHaveBeenCalledWith(fragrance);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fragrance = { id: 123 };
        spyOn(fragranceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fragrance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fragranceService.update).toHaveBeenCalledWith(fragrance);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
