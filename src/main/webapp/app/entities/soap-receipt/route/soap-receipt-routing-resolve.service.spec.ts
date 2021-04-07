jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISoapReceipt, SoapReceipt } from '../soap-receipt.model';
import { SoapReceiptService } from '../service/soap-receipt.service';

import { SoapReceiptRoutingResolveService } from './soap-receipt-routing-resolve.service';

describe('Service Tests', () => {
  describe('SoapReceipt routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SoapReceiptRoutingResolveService;
    let service: SoapReceiptService;
    let resultSoapReceipt: ISoapReceipt | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SoapReceiptRoutingResolveService);
      service = TestBed.inject(SoapReceiptService);
      resultSoapReceipt = undefined;
    });

    describe('resolve', () => {
      it('should return ISoapReceipt returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSoapReceipt = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSoapReceipt).toEqual({ id: 123 });
      });

      it('should return new ISoapReceipt if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSoapReceipt = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSoapReceipt).toEqual(new SoapReceipt());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSoapReceipt = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSoapReceipt).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
