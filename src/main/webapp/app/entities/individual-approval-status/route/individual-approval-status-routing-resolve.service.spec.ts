jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIndividualApprovalStatus, IndividualApprovalStatus } from '../individual-approval-status.model';
import { IndividualApprovalStatusService } from '../service/individual-approval-status.service';

import { IndividualApprovalStatusRoutingResolveService } from './individual-approval-status-routing-resolve.service';

describe('Service Tests', () => {
  describe('IndividualApprovalStatus routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IndividualApprovalStatusRoutingResolveService;
    let service: IndividualApprovalStatusService;
    let resultIndividualApprovalStatus: IIndividualApprovalStatus | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IndividualApprovalStatusRoutingResolveService);
      service = TestBed.inject(IndividualApprovalStatusService);
      resultIndividualApprovalStatus = undefined;
    });

    describe('resolve', () => {
      it('should return IIndividualApprovalStatus returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIndividualApprovalStatus = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIndividualApprovalStatus).toEqual({ id: 123 });
      });

      it('should return new IIndividualApprovalStatus if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIndividualApprovalStatus = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIndividualApprovalStatus).toEqual(new IndividualApprovalStatus());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIndividualApprovalStatus = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIndividualApprovalStatus).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
