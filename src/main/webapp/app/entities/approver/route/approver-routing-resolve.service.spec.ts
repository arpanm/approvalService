jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IApprover, Approver } from '../approver.model';
import { ApproverService } from '../service/approver.service';

import { ApproverRoutingResolveService } from './approver-routing-resolve.service';

describe('Service Tests', () => {
  describe('Approver routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ApproverRoutingResolveService;
    let service: ApproverService;
    let resultApprover: IApprover | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ApproverRoutingResolveService);
      service = TestBed.inject(ApproverService);
      resultApprover = undefined;
    });

    describe('resolve', () => {
      it('should return IApprover returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApprover = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApprover).toEqual({ id: 123 });
      });

      it('should return new IApprover if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApprover = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultApprover).toEqual(new Approver());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApprover = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApprover).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
