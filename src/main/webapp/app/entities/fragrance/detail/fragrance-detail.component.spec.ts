import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FragranceDetailComponent } from './fragrance-detail.component';

describe('Component Tests', () => {
  describe('Fragrance Management Detail Component', () => {
    let comp: FragranceDetailComponent;
    let fixture: ComponentFixture<FragranceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FragranceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fragrance: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FragranceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FragranceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fragrance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fragrance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
