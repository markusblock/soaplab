jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFragrance, Fragrance } from '../fragrance.model';
import { FragranceService } from '../service/fragrance.service';

import { FragranceRoutingResolveService } from './fragrance-routing-resolve.service';

describe('Service Tests', () => {
  describe('Fragrance routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FragranceRoutingResolveService;
    let service: FragranceService;
    let resultFragrance: IFragrance | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FragranceRoutingResolveService);
      service = TestBed.inject(FragranceService);
      resultFragrance = undefined;
    });

    describe('resolve', () => {
      it('should return IFragrance returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFragrance = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFragrance).toEqual({ id: 123 });
      });

      it('should return new IFragrance if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFragrance = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFragrance).toEqual(new Fragrance());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFragrance = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFragrance).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
