import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AcidService } from '../service/acid.service';

import { AcidComponent } from './acid.component';

describe('Component Tests', () => {
  describe('Acid Management Component', () => {
    let comp: AcidComponent;
    let fixture: ComponentFixture<AcidComponent>;
    let service: AcidService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AcidComponent],
      })
        .overrideTemplate(AcidComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AcidComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AcidService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.acids?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
