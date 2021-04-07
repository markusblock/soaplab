import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AcidDetailComponent } from './acid-detail.component';

describe('Component Tests', () => {
  describe('Acid Management Detail Component', () => {
    let comp: AcidDetailComponent;
    let fixture: ComponentFixture<AcidDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AcidDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ acid: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AcidDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AcidDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load acid on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.acid).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
