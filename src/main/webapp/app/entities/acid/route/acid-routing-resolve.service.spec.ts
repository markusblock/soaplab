jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAcid, Acid } from '../acid.model';
import { AcidService } from '../service/acid.service';

import { AcidRoutingResolveService } from './acid-routing-resolve.service';

describe('Service Tests', () => {
  describe('Acid routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AcidRoutingResolveService;
    let service: AcidService;
    let resultAcid: IAcid | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AcidRoutingResolveService);
      service = TestBed.inject(AcidService);
      resultAcid = undefined;
    });

    describe('resolve', () => {
      it('should return IAcid returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAcid = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAcid).toEqual({ id: 123 });
      });

      it('should return new IAcid if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAcid = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAcid).toEqual(new Acid());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAcid = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAcid).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
