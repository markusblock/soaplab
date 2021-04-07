import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FatDetailComponent } from './fat-detail.component';

describe('Component Tests', () => {
  describe('Fat Management Detail Component', () => {
    let comp: FatDetailComponent;
    let fixture: ComponentFixture<FatDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FatDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fat: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fat on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
