jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IApprovalRequestItem, ApprovalRequestItem } from '../approval-request-item.model';
import { ApprovalRequestItemService } from '../service/approval-request-item.service';

import { ApprovalRequestItemRoutingResolveService } from './approval-request-item-routing-resolve.service';

describe('Service Tests', () => {
  describe('ApprovalRequestItem routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ApprovalRequestItemRoutingResolveService;
    let service: ApprovalRequestItemService;
    let resultApprovalRequestItem: IApprovalRequestItem | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ApprovalRequestItemRoutingResolveService);
      service = TestBed.inject(ApprovalRequestItemService);
      resultApprovalRequestItem = undefined;
    });

    describe('resolve', () => {
      it('should return IApprovalRequestItem returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApprovalRequestItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApprovalRequestItem).toEqual({ id: 123 });
      });

      it('should return new IApprovalRequestItem if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApprovalRequestItem = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultApprovalRequestItem).toEqual(new ApprovalRequestItem());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApprovalRequestItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApprovalRequestItem).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
