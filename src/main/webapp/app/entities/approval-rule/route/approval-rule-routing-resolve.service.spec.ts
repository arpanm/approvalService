jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IApprovalRule, ApprovalRule } from '../approval-rule.model';
import { ApprovalRuleService } from '../service/approval-rule.service';

import { ApprovalRuleRoutingResolveService } from './approval-rule-routing-resolve.service';

describe('Service Tests', () => {
  describe('ApprovalRule routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ApprovalRuleRoutingResolveService;
    let service: ApprovalRuleService;
    let resultApprovalRule: IApprovalRule | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ApprovalRuleRoutingResolveService);
      service = TestBed.inject(ApprovalRuleService);
      resultApprovalRule = undefined;
    });

    describe('resolve', () => {
      it('should return IApprovalRule returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApprovalRule = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApprovalRule).toEqual({ id: 123 });
      });

      it('should return new IApprovalRule if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApprovalRule = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultApprovalRule).toEqual(new ApprovalRule());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApprovalRule = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApprovalRule).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
