jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFat, Fat } from '../fat.model';
import { FatService } from '../service/fat.service';

import { FatRoutingResolveService } from './fat-routing-resolve.service';

describe('Service Tests', () => {
  describe('Fat routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FatRoutingResolveService;
    let service: FatService;
    let resultFat: IFat | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FatRoutingResolveService);
      service = TestBed.inject(FatService);
      resultFat = undefined;
    });

    describe('resolve', () => {
      it('should return IFat returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFat = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFat).toEqual({ id: 123 });
      });

      it('should return new IFat if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFat = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFat).toEqual(new Fat());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFat = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFat).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
